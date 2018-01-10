package ru.alphadoub.voting;

import ru.alphadoub.voting.model.Dish;

import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alphadoub.voting.RestaurantTestData.*;

public class DishTestData {
    public static final int DISH1_ID = 100007;

    public static final Dish DISH1 = new Dish(DISH1_ID, "salad1", 600, now(), RESTAURANT1);
    public static final Dish DISH2 = new Dish(DISH1_ID + 1, "steak1", 800, now(), RESTAURANT1);
    public static final Dish DISH3 = new Dish(DISH1_ID + 2, "vine1", 550, now(), RESTAURANT1);
    public static final Dish DISH4 = new Dish(DISH1_ID + 3, "salad1", 500, of(2017, Month.DECEMBER, 31), RESTAURANT1);
    public static final Dish DISH5 = new Dish(DISH1_ID + 4, "steak1", 700, of(2017, Month.DECEMBER, 31), RESTAURANT1);
    public static final Dish DISH6 = new Dish(DISH1_ID + 5, "vine1", 450, of(2017, Month.DECEMBER, 31), RESTAURANT1);
    public static final Dish DISH7 = new Dish(DISH1_ID + 6, "salad1", 700, of(2118, Month.DECEMBER, 31), RESTAURANT1);
    public static final Dish DISH8 = new Dish(DISH1_ID + 7, "steak1", 900, of(2118, Month.DECEMBER, 31), RESTAURANT1);
    public static final Dish DISH9 = new Dish(DISH1_ID + 8, "vine1", 650, of(2118, Month.DECEMBER, 31), RESTAURANT1);

    public static final Dish DISH10 = new Dish(DISH1_ID + 9, "salad2", 540, now(), RESTAURANT2);
    public static final Dish DISH11 = new Dish(DISH1_ID + 10, "steak2", 750, now(), RESTAURANT2);
    public static final Dish DISH12 = new Dish(DISH1_ID + 11, "vine2", 500, now(), RESTAURANT2);

    public static final Dish DISH13 = new Dish(DISH1_ID + 12, "salad3", 570, now(), RESTAURANT3);
    public static final Dish DISH14 = new Dish(DISH1_ID + 13, "steak3", 770, now(), RESTAURANT3);
    public static final Dish DISH15 = new Dish(DISH1_ID + 14, "vine3", 530, now(), RESTAURANT3);

    public static final List<Dish> RESTAURANT1_MENU = Arrays.asList(DISH3, DISH1, DISH2);
    public static final List<Dish> RESTAURANT2_MENU = Arrays.asList(DISH12, DISH10, DISH11);
    public static final List<Dish> RESTAURANT3_MENU = Arrays.asList(DISH15, DISH13, DISH14);

    public static void assertMatch(Dish actual, Dish expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Dish> actual, Dish... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Dish> actual, Iterable<Dish> expected) {
        assertThat(actual).usingDefaultElementComparator().isEqualTo(expected);
    }

    public static Dish getCreated() {
        return new Dish("new dish1", 300, now());
    }

    public static Dish getUpdated(Dish dish) {
        return new Dish(dish.getId(), "UPDATED dish", 650, dish.getDate());
    }
}
