package ru.alphadoub.voting.util;

import org.springframework.util.Assert;
import ru.alphadoub.voting.HasId;
import ru.alphadoub.voting.model.BaseEntity;
import ru.alphadoub.voting.model.Dish;
import ru.alphadoub.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;

import static java.lang.String.format;
import static ru.alphadoub.voting.Messages.*;

public class ValidationUtil {
    private ValidationUtil() {
    }

    public static <T> T checkNotFound(T object, int id) {
        return checkNotFound(object, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String arg) {
        if (object == null) throw new NotFoundException(format(NOT_FOUND, arg));
        return object;
    }

    public static void checkNotFound(int numberOfChanges, int id) {
        checkNotFound(numberOfChanges, "id=" + id);
    }

    public static void checkNotFound(int numberOfChanges, String arg) {
        if (numberOfChanges == 0) throw new NotFoundException(format(NOT_FOUND, arg));
    }

    public static void checkWrongDateForCreate(Dish dish) {
        if (LocalTime.now().compareTo(LocalTime.of(11, 0)) < 0) {
            Assert.isTrue(dish.getDate().compareTo(LocalDate.now()) >= 0, String.format(OLD_DATE, dish, LocalDate.now()));
        }
        else {
            Assert.isTrue(dish.getDate().compareTo(LocalDate.now()) > 0, String.format(OLD_DATE_AFTER_11, dish, LocalDate.now()));
        }
    }
    
    public static void checkWrongDateForUpdate(Dish oldDish, Dish newDish) {
        LocalDate today = LocalDate.now();
        LocalDate oldDate = oldDish.getDate();
        LocalDate newDate = newDish.getDate();

        if (oldDate.compareTo(today) < 0) {
            Assert.isTrue(newDate.equals(oldDate), String.format(OLD_DISH, newDish, oldDate));
        } else {
            if (LocalTime.now().compareTo(LocalTime.of(11, 0)) < 0) {
                Assert.isTrue(newDate.compareTo(today) >= 0, String.format(OLD_DATE, newDish, today));
            } else {
                if (oldDate.equals(today)) {
                    Assert.isTrue(newDate.equals(oldDate), String.format(OLD_DISH_AFTER_11, newDish, oldDate));
                } else {
                    Assert.isTrue(newDate.compareTo(today) > 0, String.format(OLD_DATE_AFTER_11, newDish, today));
                }
            }
        }
    }

    public static void checkIsNew(BaseEntity entity) {
        Assert.isTrue(entity.getId() == null, entity + "must be new (id=null)");
    }

    public static void assureIdConsistent(HasId entity, int id) {
        if (entity.getId() == null) {
            entity.setId(id);
        } else {
            Assert.isTrue(entity.getId() == id, entity + " must be with id=" + id);
        }
    }

    public static void checkVotingTime() {
        if (LocalTime.now().compareTo(LocalTime.of(11, 0)) > 0) {
            throw new UnsupportedOperationException("You can not vote after 11:00");
        }
    }

    public static void checkRestaurantId(int actual, int idFromPath) {
        Assert.isTrue(actual == idFromPath, String.format(WRONG_RESTAURANT_ID, actual, idFromPath));

    }

    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }
}
