package ru.alphadoub.voting.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.alphadoub.voting.model.Dish;
import ru.alphadoub.voting.validation.NotFoundException;

import javax.validation.ConstraintViolationException;

import static ru.alphadoub.voting.DishTestData.*;
import static ru.alphadoub.voting.DishTestData.assertMatch;
import static ru.alphadoub.voting.DishTestData.getCreated;
import static ru.alphadoub.voting.DishTestData.getUpdated;
import static ru.alphadoub.voting.RestaurantTestData.*;


public class DishServiceTest extends AbstractServiceTest {
    @Autowired
    DishService service;

    @Test
    public void testCreate() throws Exception {
        Dish newDish = getCreated();
        Dish created = service.create(newDish, RESTAURANT1_ID);
        newDish.setId(created.getId());
        assertMatch(service.getAllByRestaurantId(RESTAURANT1_ID), newDish, DISH3, DISH1, DISH2);
    }

    @Test
    public void testNullDishCreate() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("dish must not be null");
        service.create(null, RESTAURANT1_ID);
    }

    @Test
    public void testCreateNotFoundRestaurant() throws Exception {
        thrown.expect(NotFoundException.class);
        Dish newDish = getCreated();
        service.create(newDish, 111111);
    }

    @Test
    public void testDuplicatedNameCreate() throws Exception {
        thrown.expect(DataAccessException.class);
        Dish newDish = new Dish("salad1", 500);
        service.create(newDish, RESTAURANT1_ID);
    }

    @Test
    public void testGet() throws Exception {
        Dish dish = service.get(DISH1_ID, RESTAURANT1_ID);
        assertMatch(dish, DISH1);

    }
    @Test
    public void testGetNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.get(DISH1_ID, RESTAURANT3_ID);
    }

    @Test
    public void testUpdate() throws Exception {
        Dish updated = getUpdated(DISH1);
        service.update(updated, RESTAURANT1_ID);
        assertMatch(service.get(DISH1_ID, RESTAURANT1_ID), updated);
    }

    @Test
    public void testNullDishUpdate() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("dish must not be null");
        service.update(null, RESTAURANT1_ID);
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        Dish updated = getUpdated(DISH1);
        service.update(updated, RESTAURANT3_ID);
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(DISH1_ID, RESTAURANT1_ID);
        assertMatch(service.getAllByRestaurantId(RESTAURANT1_ID), DISH3, DISH2);
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.delete(DISH1_ID, RESTAURANT2_ID);
    }

    @Test
    public void testGetAllByRestaurantId() throws Exception {
        assertMatch(service.getAllByRestaurantId(RESTAURANT1_ID), RESTAURANT1_MENU);
        assertMatch(service.getAllByRestaurantId(RESTAURANT2_ID), RESTAURANT2_MENU);
        assertMatch(service.getAllByRestaurantId(RESTAURANT3_ID), RESTAURANT3_MENU);

    }

    @Test
    public void testGetAllByRestaurantIdNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.getAllByRestaurantId(1);
    }

    @Test
    public void testValidation() throws Exception {
        //@NotBlank and @Size(2-100) name checking
        validateRootCause(() -> service.create(new Dish("", 1000), RESTAURANT1_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Dish("D", 1000), RESTAURANT1_ID), ConstraintViolationException.class);

        //@NotNull and @Range(50-50000) price checking
        validateRootCause(() -> service.create(new Dish("newDish", null), RESTAURANT1_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Dish("newDish", 49), RESTAURANT1_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Dish("newDish", 50001), RESTAURANT1_ID), ConstraintViolationException.class);



    }
}