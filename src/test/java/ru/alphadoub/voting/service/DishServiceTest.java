package ru.alphadoub.voting.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.alphadoub.voting.model.Dish;
import ru.alphadoub.voting.util.exception.IncomingDataException;
import ru.alphadoub.voting.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;
import static ru.alphadoub.voting.DishTestData.*;
import static ru.alphadoub.voting.DishTestData.assertMatch;
import static ru.alphadoub.voting.DishTestData.getCreated;
import static ru.alphadoub.voting.DishTestData.getUpdated;
import static ru.alphadoub.voting.Messages.*;
import static ru.alphadoub.voting.RestaurantTestData.*;


public class DishServiceTest extends AbstractServiceTest {
    @Autowired
    DishService service;

    @Before
    public void clearSpringCache() throws Exception {
        cacheManager.getCache("dishes").clear();
    }


    @Test
    public void testCreate() throws Exception {
        Dish newDish = getCreated();
        Dish created = service.create(newDish, RESTAURANT1_ID);
        newDish.setId(created.getId());
        assertMatch(service.getTodayRestaurantMenu(RESTAURANT1_ID), newDish, DISH3, DISH1, DISH2);
    }

    @Test
    public void testNullDishCreate() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("dish must not be null");
        service.create(null, RESTAURANT1_ID);
    }

    @Test
    public void testCreateNotFoundRestaurant() throws Exception {
        int wrongId = 1;
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format(NOT_FOUND, "id=" + wrongId));
        Dish newDish = getCreated();
        service.create(newDish, wrongId);
    }

    @Test
    public void testDuplicatedNameDateCreate() throws Exception {
        thrown.expect(DataAccessException.class);
        Dish newDish = new Dish("salad1", 500, now());
        service.create(newDish, RESTAURANT1_ID);
    }

    @Test
    public void testGet() throws Exception {
        Dish dish = service.get(DISH1_ID, RESTAURANT1_ID);
        assertMatch(dish, DISH1);
    }

    @Test
    public void testGetNotFound() throws Exception {
        int wrongId = 1;
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format(NOT_FOUND, "id=" + wrongId));
        service.get(wrongId, RESTAURANT1_ID);
    }

    @Test
    public void testGetWrongRestaurantId() throws Exception {
        thrown.expect(IncomingDataException.class);
        thrown.expectMessage(String.format(WRONG_RESTAURANT_ID, RESTAURANT1_ID, RESTAURANT3_ID));
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
        int wrongId = 1;
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format(NOT_FOUND, "id=" + wrongId));
        Dish updated = getUpdated(DISH1);
        updated.setId(wrongId);
        service.update(updated, RESTAURANT1_ID);
    }

    @Test
    public void testUpdateWrongRestaurantId() throws Exception {
        thrown.expect(IncomingDataException.class);
        thrown.expectMessage(String.format(WRONG_RESTAURANT_ID, RESTAURANT1_ID, RESTAURANT3_ID));
        Dish updated = getUpdated(DISH1);
        service.update(updated, RESTAURANT3_ID);
    }

    @Test
    public void testUpdateDateOfOldDish() throws Exception {
        thrown.expect(IncomingDataException.class);
        Dish updated = getUpdated(DISH4);
        updated.setDate(now());
        thrown.expectMessage(String.format(OLD_DISH, updated, DISH4.getDate()));
        service.update(updated, RESTAURANT1_ID);
    }

    @Test
    public void testUpdateTodayDishWithWrongDate() throws Exception {
        thrown.expect(IncomingDataException.class);
        Dish updated = getUpdated(DISH1);

        LocalTime now = LocalTime.now();
        if (now.compareTo(LocalTime.of(11,0)) < 0) {
            updated.setDate(of(2017, Month.DECEMBER, 31));
            thrown.expectMessage(String.format(OLD_DATE, updated, LocalDate.now()));
        } else {
            updated.setDate(of(2111, Month.DECEMBER, 31));
            thrown.expectMessage(String.format(OLD_DISH_AFTER_11, updated, DISH1.getDate()));
        }
        service.update(updated, RESTAURANT1_ID);
    }

    @Test
    public void testUpdateFutureDishWithWrongDate() throws Exception {
        thrown.expect(IncomingDataException.class);
        Dish updated = getUpdated(DISH7);

        LocalTime now = LocalTime.now();
        if (now.compareTo(LocalTime.of(11,0)) < 0) {
            updated.setDate(of(2017, Month.DECEMBER, 31));
            thrown.expectMessage(String.format(OLD_DATE, updated, LocalDate.now()));
        } else {
            updated.setDate(LocalDate.now());
            thrown.expectMessage(String.format(OLD_DATE_AFTER_11, updated, LocalDate.now()));
        }
        service.update(updated, RESTAURANT1_ID);
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(DISH1_ID, RESTAURANT1_ID);
        assertMatch(service.getTodayRestaurantMenu(RESTAURANT1_ID), DISH3, DISH2);
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format(NOT_FOUND, "id=" + DISH1_ID + " in restaurant with id=" + RESTAURANT2_ID));
        service.delete(DISH1_ID, RESTAURANT2_ID);
    }

    @Test
    public void testGetTodayRestaurantMenu() throws Exception {
        assertMatch(service.getTodayRestaurantMenu(RESTAURANT1_ID), RESTAURANT1_MENU);
        assertMatch(service.getTodayRestaurantMenu(RESTAURANT2_ID), RESTAURANT2_MENU);
        assertMatch(service.getTodayRestaurantMenu(RESTAURANT3_ID), RESTAURANT3_MENU);
    }

    @Test
    public void testGetRestaurantMenuByDate() throws Exception {
        assertMatch(service.getRestaurantMenuByDate(RESTAURANT1_ID, of(2118, Month.DECEMBER, 31)), DISH9, DISH7, DISH8);
    }

    @Test
    public void testGetTodayRestaurantMenuNotFound() throws Exception {
        int wrongId = 1;
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format(NOT_FOUND, "id=" + wrongId));
        service.getTodayRestaurantMenu(wrongId);
    }

    @Test
    public void testGetAllByDate() throws Exception {
        assertMatch(service.getAllByDate(LocalDate.now()), DISH3, DISH1, DISH2, DISH12, DISH10, DISH11, DISH15, DISH13, DISH14);
    }

    @Test
    public void testValidation() throws Exception {
        //@NotBlank and @Size(2-100) name checking
        validateRootCause(() -> service.create(new Dish("", 1000, now()), RESTAURANT1_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Dish("D", 1000, now()), RESTAURANT1_ID), ConstraintViolationException.class);

        //@NotNull and @Range(50-50000) price checking
        validateRootCause(() -> service.create(new Dish("newDish", null, now()), RESTAURANT1_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Dish("newDish", 49, now()), RESTAURANT1_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Dish("newDish", 50001, now()), RESTAURANT1_ID), ConstraintViolationException.class);

        //@NotNull date checking
        validateRootCause(() -> service.create(new Dish("newDish", 50001, null), RESTAURANT1_ID), ConstraintViolationException.class);
    }
}