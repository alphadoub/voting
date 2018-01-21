package ru.alphadoub.voting;

import ru.alphadoub.voting.model.Restaurant;
import ru.alphadoub.voting.to.RestaurantWithVotes;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantTestData {
    public static final int RESTAURANT1_ID = 100004;
    public static final int RESTAURANT2_ID = 100005;
    public static final int RESTAURANT3_ID = 100006;

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "Restaurant1");
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT2_ID, "Restaurant2");
    public static final Restaurant RESTAURANT3 = new Restaurant(RESTAURANT3_ID, "Restaurant3");

    public static final RestaurantWithVotes RESTAURANT1_WITH_VOTES = new RestaurantWithVotes(RESTAURANT1_ID, RESTAURANT1.getName(), (long) 2);
    public static final RestaurantWithVotes RESTAURANT2_WITH_VOTES = new RestaurantWithVotes(RESTAURANT2_ID, RESTAURANT2.getName(), (long) 0);
    public static final RestaurantWithVotes RESTAURANT3_WITH_VOTES = new RestaurantWithVotes(RESTAURANT3_ID, RESTAURANT3.getName(), (long) 1);

    public static final List<Restaurant> RESTAURANTS = Arrays.asList(RESTAURANT1, RESTAURANT2, RESTAURANT3);
    public static final List<RestaurantWithVotes> RESTAURANTS_WITH_VOTES = Arrays.asList(RESTAURANT1_WITH_VOTES, RESTAURANT3_WITH_VOTES, RESTAURANT2_WITH_VOTES);

    public static void assertMatch(Restaurant actual, Restaurant expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Restaurant> actual, Restaurant...expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(Arrays.asList(expected));
    }

    public static void assertMatchWithVotes(RestaurantWithVotes actual, RestaurantWithVotes expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatchWithVotes(Iterable<RestaurantWithVotes> actual, RestaurantWithVotes...expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(Arrays.asList(expected));
    }

    public static Restaurant getCreated() {
        return new Restaurant("NewRestaurant");
    }

    public static Restaurant getUpdated(Restaurant restaurant) {
        return new Restaurant(restaurant.getId(), "UPDATED Restaurant");
    }

    public static RestaurantWithVotes getPlusOneVote(RestaurantWithVotes restaurantWithVotes) {
        return new RestaurantWithVotes(restaurantWithVotes.getId(), restaurantWithVotes.getName(), restaurantWithVotes.getCountOfVotes() + 1);
    }

    public static RestaurantWithVotes getMinusOneVote(RestaurantWithVotes restaurantWithVotes) {
        return new RestaurantWithVotes(restaurantWithVotes.getId(), restaurantWithVotes.getName(), restaurantWithVotes.getCountOfVotes() - 1);
    }
}
