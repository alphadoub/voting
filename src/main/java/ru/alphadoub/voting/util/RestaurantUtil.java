package ru.alphadoub.voting.util;

import ru.alphadoub.voting.model.Dish;
import ru.alphadoub.voting.model.Restaurant;
import ru.alphadoub.voting.model.Vote;
import ru.alphadoub.voting.to.RestaurantWithMenu;
import ru.alphadoub.voting.to.RestaurantWithVotes;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RestaurantUtil {
    private RestaurantUtil() {
    }

    public static RestaurantWithVotes createWithVotes(Restaurant restaurant, long countOfVotes) {
        return new RestaurantWithVotes(restaurant.getId(), restaurant.getName(), countOfVotes);
    }

    public static List<RestaurantWithVotes> getWithVotes(List<Restaurant> restaurants, List<Vote> votes) {
        Map<Integer, Long> map = votes.stream().collect(Collectors.groupingBy(v -> v.getRestaurant().getId(), Collectors.counting()));
        return restaurants.stream()
                .map(r -> createWithVotes(r, map.getOrDefault(r.getId(), 0L)))
                .sorted(Comparator.comparing(RestaurantWithVotes::getCountOfVotes).reversed().thenComparing(RestaurantWithVotes::getName))
                .collect(Collectors.toList());
    }

    public static RestaurantWithMenu createWithMenu(Restaurant restaurant, List<Dish> menu) {
        return new RestaurantWithMenu(restaurant.getId(), restaurant.getName(), menu);
    }

    public static List<RestaurantWithMenu> getWithMenu(List<Restaurant> restaurants, List<Dish> dishes) {
        Map<Integer, List<Dish>> map = dishes.stream().collect(Collectors.groupingBy(d -> d.getRestaurant().getId()));
        return restaurants.stream()
                .map(r -> createWithMenu(r, map.getOrDefault(r.getId(), Collections.emptyList())))
                .sorted(Comparator.comparing(RestaurantWithMenu::getName))
                .collect(Collectors.toList());
    }
}
