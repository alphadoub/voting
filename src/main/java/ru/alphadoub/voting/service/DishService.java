package ru.alphadoub.voting.service;

import ru.alphadoub.voting.model.Dish;

import java.time.LocalDate;
import java.util.List;

public interface DishService {
    Dish create(Dish dish, int restaurantId);

    Dish get(int id, int restaurantId);

    void update(Dish dish, int restaurantId);

    void delete(int id, int restaurantId);

    List<Dish> getCurrentDayList(int restaurantId);

    List<Dish> getListByDate(int restaurantId, LocalDate date);
}
