package ru.alphadoub.voting.validation;

import org.springframework.util.Assert;
import ru.alphadoub.voting.model.Dish;
import ru.alphadoub.voting.validation.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;

public class ValidationUtil {
    private ValidationUtil() {
    }

    public static <T> T checkNotFound(T object, int id) {
        return checkNotFound(object, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String arg) {
        if (object == null) throw new NotFoundException("Not found entity with " + arg);
        return object;
    }

    public static void checkNotFound(int numberOfChanges, int id) {
        checkNotFound(numberOfChanges, "id=" + id);
    }

    public static void checkNotFound(int numberOfChanges, String msg) {
        if (numberOfChanges == 0) throw new NotFoundException("Not found entity with " + msg);
    }

    //эту проверку при создании блюда лучше разместить в контроллере,
    //так как нам не нужен запрос в базу для её осуществления.
    // Поэтому её можно осуществить, "не углубляясь" в приложение
    public static void checkWrongDateForCreate(Dish dish) {
        if (LocalTime.now().compareTo(LocalTime.of(11, 0)) < 0) {
            Assert.isTrue(dish.getDate().compareTo(LocalDate.now()) >= 0, dish + " must not be with date earlier than " + LocalDate.now());
        }
        else {
            Assert.isTrue(dish.getDate().compareTo(LocalDate.now()) > 0, dish + " must be with date later than " + LocalDate.now());
        }
    }

    public static void checkWrongDateForUpdate(Dish oldDish, Dish newDish) {
        LocalDate today = LocalDate.now();
        LocalDate oldDate = oldDish.getDate();
        LocalDate newDate = newDish.getDate();

        if (oldDate.compareTo(today) < 0) {
            Assert.isTrue(newDate.equals(oldDate), "You can not change date of old dish. " + newDish + " must be with date=" + oldDate);
        } else {
            if (LocalTime.now().compareTo(LocalTime.of(11, 0)) < 0) {
                Assert.isTrue(newDate.compareTo(today) >= 0, "You can not set old date to dish. " + newDish + " must not be with date earlier than " + today);
            } else {
                if (oldDate.equals(today)) {
                    Assert.isTrue(newDate.equals(oldDate), "After 11:00 you can not change date of current day's dish. " + newDish + " must be with date=" + oldDate);
                } else {
                    Assert.isTrue(newDate.compareTo(today) > 0, "You can not set to dish current date after 11:00 or old date" + newDish + " must be later than " + today);
                }
            }
        }
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
