package ru.alphadoub.voting;

import ru.alphadoub.voting.model.Restaurant;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantTestData {
    public static final int RESTAURANT1_ID = 100004;
    public static final int RESTAURANT2_ID = 100005;
    public static final int RESTAURANT3_ID = 100006;

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "Restaurant1");
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT2_ID, "Restaurant2");
    public static final Restaurant RESTAURANT3 = new Restaurant(RESTAURANT3_ID, "Restaurant3");

    public static void assertMatch(Restaurant actual, Restaurant expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Restaurant> actual, Restaurant... expected) {
        assertThat(actual).usingDefaultElementComparator().isEqualTo(Arrays.asList(expected));
    }

    public static Restaurant getCreated() {
        return new Restaurant("NewRestaurant");
    }

    public static Restaurant getUpdated(Restaurant restaurant) {
        return new Restaurant(restaurant.getId(), "UPDATED Restaurant");
    }
}
