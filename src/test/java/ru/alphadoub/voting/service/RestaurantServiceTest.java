package ru.alphadoub.voting.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.alphadoub.voting.model.Restaurant;
import ru.alphadoub.voting.validation.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static ru.alphadoub.voting.RestaurantTestData.*;


public class RestaurantServiceTest extends AbstractServiceTest {
    @Autowired
    RestaurantService service;

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
        thrown.expect(NotFoundException.class);
        Restaurant restaurant = service.get(1);
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
        thrown.expect(NotFoundException.class);
        Restaurant updated = new Restaurant(1, "UPDATED Restaurant");
        service.update(updated);
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(RESTAURANT2_ID);
        assertMatch(service.getAll(), RESTAURANT1, RESTAURANT3);
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.delete(1);
    }

    @Test
    public void testGetAll() throws Exception {
        List<Restaurant> all = service.getAll();
        assertMatch(all, RESTAURANT1, RESTAURANT2, RESTAURANT3);
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