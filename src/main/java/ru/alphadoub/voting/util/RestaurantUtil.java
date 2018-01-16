package ru.alphadoub.voting.util;

import ru.alphadoub.voting.model.Restaurant;
import ru.alphadoub.voting.model.Vote;
import ru.alphadoub.voting.to.RestaurantWithVotes;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RestaurantUtil {
    private RestaurantUtil() {
    }

    public static List<RestaurantWithVotes> getWithVotes(List<Restaurant> restaurants, List<Vote> votes) {
        Map<Integer, Long> map = votes.stream().collect(Collectors.groupingBy(vote -> vote.getRestaurant().getId(), Collectors.counting()));
        return restaurants.stream()
                .map(restaurant -> getWithVotes(restaurant, map.containsKey(restaurant.getId()) ? map.get(restaurant.getId()) : 0))
                .sorted(Comparator.comparing(RestaurantWithVotes::getCountOfVotes).reversed())
                .collect(Collectors.toList());
    }


    public static RestaurantWithVotes getWithVotes(Restaurant restaurant, long countOfVotes) {
        return new RestaurantWithVotes(restaurant.getId(), restaurant.getName(), countOfVotes);
    }

}
