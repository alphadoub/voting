package ru.alphadoub.voting;

import ru.alphadoub.voting.model.Dish;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alphadoub.voting.RestaurantTestData.*;

public class DishTestData {
    public static final int DISH1_ID = 100007;

    public static final Dish DISH1 = new Dish(DISH1_ID, "salad1", 600, RESTAURANT1);
    public static final Dish DISH2 = new Dish(DISH1_ID + 1, "steak1", 800, RESTAURANT1);
    public static final Dish DISH3 = new Dish(DISH1_ID + 2, "vine1", 550, RESTAURANT1);

    public static final Dish DISH4 = new Dish(DISH1_ID + 3, "salad2", 540, RESTAURANT2);
    public static final Dish DISH5 = new Dish(DISH1_ID + 4, "steak2", 750, RESTAURANT2);
    public static final Dish DISH6 = new Dish(DISH1_ID + 5, "vine2", 500, RESTAURANT2);

    public static final Dish DISH7 = new Dish(DISH1_ID + 6, "salad3", 570, RESTAURANT3);
    public static final Dish DISH8 = new Dish(DISH1_ID + 7, "steak3", 770, RESTAURANT3);
    public static final Dish DISH9 = new Dish(DISH1_ID + 8, "vine3", 530, RESTAURANT3);

    public static final List<Dish> RESTAURANT1_MENU = Arrays.asList(DISH3, DISH1, DISH2);
    public static final List<Dish> RESTAURANT2_MENU = Arrays.asList(DISH6, DISH4, DISH5);
    public static final List<Dish> RESTAURANT3_MENU = Arrays.asList(DISH9, DISH7, DISH8);

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
        return new Dish("new dish1", 300);
    }

    public static Dish getUpdated(Dish dish) {
        return new Dish(dish.getId(), "UPDATED dish", 650);
    }
}
