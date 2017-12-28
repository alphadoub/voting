package ru.alphadoub.voting.service;

import ru.alphadoub.voting.model.Restaurant;

import java.util.List;

public interface RestaurantService {
    Restaurant create(Restaurant restaurant);

    Restaurant get(int id);

    void update(Restaurant restaurant);

    void delete(int id);

    List<Restaurant> getAll();
}
