package ru.alphadoub.voting.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.alphadoub.voting.model.Restaurant;
import ru.alphadoub.voting.repository.VoteRepository;
import ru.alphadoub.voting.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

import static ru.alphadoub.voting.Messages.NOT_FOUND;
import static ru.alphadoub.voting.RestaurantTestData.*;
import static ru.alphadoub.voting.RestaurantTestData.assertMatch;
import static ru.alphadoub.voting.RestaurantTestData.getCreated;
import static ru.alphadoub.voting.RestaurantTestData.getUpdated;
import static ru.alphadoub.voting.UserTestData.*;


public class RestaurantServiceTest extends AbstractServiceTest {
    @Autowired
    RestaurantService service;

    @Autowired
    VoteRepository voteRepository;

    @Before
    public void clearSpringCache() throws Exception {
        cacheManager.getCache("restaurants").clear();
    }

    @Test
    public void testCreate() throws Exception {
        Restaurant newRestaurant = getCreated();
        Restaurant created = service.create(newRestaurant);
        newRestaurant.setId(created.getId());
        assertMatch(service.getAll(), newRestaurant, RESTAURANT1, RESTAURANT2, RESTAURANT3);
    }

    @Test
    public void testDuplicatedNameCreate() throws Exception {
        thrown.expect(DataAccessException.class);
        service.create(new Restaurant("Restaurant1"));
    }

    @Test
    public void testNullRestaurantCreate() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("restaurant must not be null");
        service.create(null);
    }

    @Test
    public void testGet() throws Exception {
        Restaurant restaurant = service.get(RESTAURANT2_ID);
        assertMatch(restaurant, RESTAURANT2);
    }

    @Test
    public void testGetNotFound() throws Exception {
        int wrongId = 1;
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format(NOT_FOUND, "id=" + wrongId));
        service.get(wrongId);
    }

    @Test
    public void testUpdate() throws Exception {
        Restaurant updated = getUpdated(RESTAURANT1);
        service.update(updated);
        assertMatch(service.get(RESTAURANT1_ID), updated);
    }

    @Test
    public void testNullRestaurantUpdate() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("restaurant must not be null");
        service.update(null);
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        int wrongId = 1;
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format(NOT_FOUND, "id=" + wrongId));
        Restaurant updated = new Restaurant(wrongId, "UPDATED Restaurant");
        service.update(updated);
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(RESTAURANT2_ID);
        assertMatch(service.getAll(), RESTAURANT1, RESTAURANT3);
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        int wrongId = 1;
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format(NOT_FOUND, "id=" + wrongId));
        service.delete(wrongId);
    }

    @Test
    public void testGetAll() throws Exception {
        List<Restaurant> all = service.getAll();
        assertMatch(all, RESTAURANT1, RESTAURANT2, RESTAURANT3);
    }

    @Test
    public void testVote() throws Exception {
        long countOfVotes = voteRepository.countByRestaurantIdAndDate(RESTAURANT2_ID, LocalDate.now());
        service.vote(RESTAURANT2_ID, USER2);
        Assert.assertEquals(countOfVotes + 1, voteRepository.countByRestaurantIdAndDate(RESTAURANT2_ID, LocalDate.now()));;

    }

    @Test
    public void testRepeatVote() throws Exception {
        long countOfVotes = voteRepository.countByRestaurantIdAndDate(RESTAURANT1_ID, LocalDate.now());
        service.vote(RESTAURANT1_ID, ADMIN);
        service.vote(RESTAURANT1_ID, ADMIN);
        Assert.assertEquals(countOfVotes, voteRepository.countByRestaurantIdAndDate(RESTAURANT1_ID, LocalDate.now()));

    }

    @Test
    public void testChangeVote() throws Exception {
        long countOfVotesOfR1 = voteRepository.countByRestaurantIdAndDate(RESTAURANT1_ID, LocalDate.now());
        long countOfVotesOfR3 = voteRepository.countByRestaurantIdAndDate(RESTAURANT3_ID, LocalDate.now());
        service.vote(RESTAURANT3_ID, USER3);
        Assert.assertEquals(countOfVotesOfR1 - 1, voteRepository.countByRestaurantIdAndDate(RESTAURANT1_ID, LocalDate.now()));
        Assert.assertEquals(countOfVotesOfR3 + 1, voteRepository.countByRestaurantIdAndDate(RESTAURANT3_ID, LocalDate.now()));
    }

    @Test
    public void testVoteNotFound() throws Exception {
        int wrongId = 1;
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format(NOT_FOUND, "id=" + wrongId));
        service.vote(wrongId, USER2);
    }

    @Test
    public void testNullUserVote() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("user must not be null");
        service.vote(RESTAURANT2_ID, null);
    }


    @Test
    public void testValidation() throws Exception {
        //@NotBlank name checking
        validateRootCause(() -> service.create(new Restaurant("")), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Restaurant(" ")), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Restaurant("R")), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Restaurant(null)), ConstraintViolationException.class);
    }
}