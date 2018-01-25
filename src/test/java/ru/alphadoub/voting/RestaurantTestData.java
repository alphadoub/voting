package ru.alphadoub.voting;

import ru.alphadoub.voting.model.Restaurant;
import ru.alphadoub.voting.to.RestaurantWithMenu;
import ru.alphadoub.voting.to.RestaurantWithVotes;
import ru.alphadoub.voting.util.RestaurantUtil;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alphadoub.voting.DishTestData.*;

public class RestaurantTestData {
    public static final int RESTAURANT1_ID = 100004;
    public static final int RESTAURANT2_ID = 100005;
    public static final int RESTAURANT3_ID = 100006;

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "Restaurant1");
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT2_ID, "Restaurant2");
    public static final Restaurant RESTAURANT3 = new Restaurant(RESTAURANT3_ID, "Restaurant3");

    public static final RestaurantWithMenu RESTAURANT1_WITH_MENU = RestaurantUtil.createWithMenu(RESTAURANT1, RESTAURANT1_MENU);
    public static final RestaurantWithMenu RESTAURANT2_WITH_MENU = RestaurantUtil.createWithMenu(RESTAURANT2, RESTAURANT2_MENU);
    public static final RestaurantWithMenu RESTAURANT3_WITH_MENU = RestaurantUtil.createWithMenu(RESTAURANT3, RESTAURANT3_MENU);

    public static final RestaurantWithVotes RESTAURANT1_WITH_VOTES = new RestaurantWithVotes(RESTAURANT1_ID, RESTAURANT1.getName(), (long) 2);
    public static final RestaurantWithVotes RESTAURANT2_WITH_VOTES = new RestaurantWithVotes(RESTAURANT2_ID, RESTAURANT2.getName(), (long) 0);
    public static final RestaurantWithVotes RESTAURANT3_WITH_VOTES = new RestaurantWithVotes(RESTAURANT3_ID, RESTAURANT3.getName(), (long) 1);

    public static final List<Restaurant> RESTAURANTS = Arrays.asList(RESTAURANT1, RESTAURANT2, RESTAURANT3);
    public static final List<RestaurantWithVotes> RESTAURANTS_WITH_VOTES = Arrays.asList(RESTAURANT1_WITH_VOTES, RESTAURANT3_WITH_VOTES, RESTAURANT2_WITH_VOTES);
    public static final List<RestaurantWithMenu> RESTAURANTS_WITH_MENU = Arrays.asList(RESTAURANT1_WITH_MENU, RESTAURANT2_WITH_MENU, RESTAURANT3_WITH_MENU);

    public static final String RESTAURANT1_WITH_MENU_JSON = "{\"id\":100004,\"name\":\"Restaurant1\",\"menu\":[{\"id\":100009,\"name\":\"vine1\",\"price\":550},{\"id\":100007,\"name\":\"salad1\",\"price\":600},{\"id\":100008,\"name\":\"steak1\",\"price\":800}]}";
    public static final String RESTAURANTS_WITH_MENU_JSON = "[{\"id\":100004,\"name\":\"Restaurant1\",\"menu\":[{\"id\":100009,\"name\":\"vine1\",\"price\":550},{\"id\":100007,\"name\":\"salad1\",\"price\":600},{\"id\":100008,\"name\":\"steak1\",\"price\":800}]}," +
                                                            "{\"id\":100005,\"name\":\"Restaurant2\",\"menu\":[{\"id\":100018,\"name\":\"vine2\",\"price\":500},{\"id\":100016,\"name\":\"salad2\",\"price\":540},{\"id\":100017,\"name\":\"steak2\",\"price\":750}]}," +
                                                            "{\"id\":100006,\"name\":\"Restaurant3\",\"menu\":[{\"id\":100021,\"name\":\"vine3\",\"price\":530},{\"id\":100019,\"name\":\"salad3\",\"price\":570},{\"id\":100020,\"name\":\"steak3\",\"price\":770}]}]";

    public static void assertMatch(Restaurant actual, Restaurant expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Restaurant> actual, Restaurant...expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(Arrays.asList(expected));
    }

    public static Restaurant getCreated() {
        return new Restaurant("NewRestaurant");
    }

    public static Restaurant getUpdated(Restaurant restaurant) {
        return new Restaurant(restaurant.getId(), "UPDATED Restaurant");
    }
}
