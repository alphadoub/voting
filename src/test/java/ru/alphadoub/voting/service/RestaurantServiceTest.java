package ru.alphadoub.voting.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.alphadoub.voting.model.Restaurant;
import ru.alphadoub.voting.to.RestaurantWithVotes;
import ru.alphadoub.voting.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static ru.alphadoub.voting.RestaurantTestData.*;
import static ru.alphadoub.voting.UserTestData.USER2;
import static ru.alphadoub.voting.UserTestData.USER3;
import static ru.alphadoub.voting.util.ValidationUtil.NOT_FOUND_MESSAGE;


public class RestaurantServiceTest extends AbstractServiceTest {
    @Autowired
    RestaurantService service;

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
        thrown.expectMessage(String.format(NOT_FOUND_MESSAGE, "id=" + wrongId));
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
        thrown.expectMessage(String.format(NOT_FOUND_MESSAGE, "id=" + wrongId));
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
        thrown.expectMessage(String.format(NOT_FOUND_MESSAGE, "id=" + wrongId));
        service.delete(wrongId);
    }

    @Test
    public void testGetAll() throws Exception {
        List<Restaurant> all = service.getAll();
        assertMatch(all, RESTAURANT1, RESTAURANT2, RESTAURANT3);
    }

    @Test
    public void testGetWithCurrentDayVotes() throws Exception {
        RestaurantWithVotes restaurant2WithVotes = service.getWithCurrentDayVotes(RESTAURANT2_ID);
        assertMatchWithVotes(restaurant2WithVotes, RESTAURANT2_WITH_VOTES);

        RestaurantWithVotes restaurant1WithVotes = service.getWithCurrentDayVotes(RESTAURANT1_ID);
        assertMatchWithVotes(restaurant1WithVotes, RESTAURANT1_WITH_VOTES);
    }

    @Test
    public void testGetWithCurrentDayVotesNotFound() throws Exception {
        int wrongId = 1;
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format(NOT_FOUND_MESSAGE, "id=" + wrongId));
        service.getWithCurrentDayVotes(wrongId);
    }

    @Test
    public void testGetAllWithCurrentDayVotes() throws Exception {
        List<RestaurantWithVotes> all = service.getAllWithCurrentDayVotes();
        assertMatchWithVotes(all, RESTAURANT1_WITH_VOTES, RESTAURANT3_WITH_VOTES, RESTAURANT2_WITH_VOTES);
    }

    @Test
    public void testVote() throws Exception {
        service.vote(RESTAURANT2_ID, USER2);
        assertMatchWithVotes(service.getWithCurrentDayVotes(RESTAURANT2_ID), getPlusOneVote(RESTAURANT2_WITH_VOTES));

    }

    @Test
    public void testRevote() throws Exception {
        service.vote(RESTAURANT3_ID, USER3);
        assertMatchWithVotes(service.getWithCurrentDayVotes(RESTAURANT3_ID), getPlusOneVote(RESTAURANT3_WITH_VOTES));
        assertMatchWithVotes(service.getWithCurrentDayVotes(RESTAURANT1_ID), getMinusOneVote(RESTAURANT1_WITH_VOTES));

    }

    @Test
    public void testVoteNotFound() throws Exception {
        int wrongId = 1;
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format(NOT_FOUND_MESSAGE, "id=" + wrongId));
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