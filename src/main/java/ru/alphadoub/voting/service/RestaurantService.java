package ru.alphadoub.voting.service;

import ru.alphadoub.voting.model.Restaurant;
import ru.alphadoub.voting.model.User;
import ru.alphadoub.voting.to.RestaurantWithVotes;

import java.util.List;

public interface RestaurantService {
    Restaurant create(Restaurant restaurant);

    Restaurant get(int id);

    void update(Restaurant restaurant);

    void delete(int id);

    List<Restaurant> getAll();

    void vote(int restaurantId, User user);

    RestaurantWithVotes getWithCurrentDayVotes(int id);

    List<RestaurantWithVotes> getAllWithCurrentDayVotes();
}
