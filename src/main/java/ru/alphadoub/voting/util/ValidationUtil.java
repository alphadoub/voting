package ru.alphadoub.voting.util;

import ru.alphadoub.voting.HasId;
import ru.alphadoub.voting.model.BaseEntity;
import ru.alphadoub.voting.model.Dish;
import ru.alphadoub.voting.util.exception.IncomingDataException;
import ru.alphadoub.voting.util.exception.NotFoundException;
import ru.alphadoub.voting.util.exception.VotingTimeConstraintException;

import java.time.LocalDate;

import static java.time.LocalTime.now;
import static java.time.LocalTime.of;
import static ru.alphadoub.voting.Messages.*;

public class ValidationUtil {
    private ValidationUtil() {
    }

    public static <T> T checkNotFound(T object, int id) {
        return checkNotFound(object, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String arg) {
        if (object == null) throw new NotFoundException(arg);
        return object;
    }

    public static void checkNotFound(int numberOfChanges, int id) {
        checkNotFound(numberOfChanges, "id=" + id);
    }

    public static void checkNotFound(int numberOfChanges, String arg) {
        if (numberOfChanges == 0) throw new NotFoundException(arg);
    }

    public static void checkWrongDateForCreate(Dish dish) {
        if (now().compareTo(of(11, 0)) < 0) {
            if (dish.getDate().compareTo(LocalDate.now()) < 0) throw new IncomingDataException(String.format(OLD_DATE, dish, LocalDate.now()));
        }
        else {
            if (dish.getDate().compareTo(LocalDate.now()) <= 0) throw new IncomingDataException(String.format(OLD_DATE_AFTER_11, dish, LocalDate.now()));
        }
    }
    
    public static void checkWrongDateForUpdate(Dish oldDish, Dish newDish) {
        LocalDate today = LocalDate.now();
        LocalDate oldDate = oldDish.getDate();
        LocalDate newDate = newDish.getDate();

        if (oldDate.compareTo(today) < 0) {
            if (!newDate.equals(oldDate)) throw new IncomingDataException(String.format(OLD_DISH, newDish, oldDate));
        } else {
            if (now().compareTo(of(11, 0)) < 0) {
                if (newDate.compareTo(today) < 0) throw new IncomingDataException(String.format(OLD_DATE, newDish, today));
            } else {
                if (oldDate.equals(today)) {
                    if (!newDate.equals(oldDate)) throw new IncomingDataException(String.format(OLD_DISH_AFTER_11, newDish, oldDate));
                } else {
                    if (newDate.compareTo(today) <= 0) throw new IncomingDataException(String.format(OLD_DATE_AFTER_11, newDish, today));
                }
            }
        }
    }

    public static void checkIsNew(BaseEntity entity) {
        if(entity.getId() != null) throw new IncomingDataException(entity + "must be new (id=null)");
    }

    public static void assureIdConsistent(HasId entity, int id) {
        if (entity.getId() == null) {
            entity.setId(id);
        } else {
            if (entity.getId() != id) throw new IncomingDataException(entity + " must be with id=" + id);
        }
    }

    public static void checkVotingTime(int constaintHour) {
        if (now().compareTo(of(constaintHour, 0)) > 0) {
            throw new VotingTimeConstraintException(of(constaintHour, 0).toString());
        }
    }

    public static void checkRestaurantId(int actual, int idFromPath) {
        if (actual != idFromPath) throw new IncomingDataException(String.format(WRONG_RESTAURANT_ID, actual, idFromPath));
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
