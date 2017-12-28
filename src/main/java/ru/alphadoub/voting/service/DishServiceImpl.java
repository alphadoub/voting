package ru.alphadoub.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.alphadoub.voting.model.Dish;
import ru.alphadoub.voting.model.Restaurant;
import ru.alphadoub.voting.repository.DishRepository;
import ru.alphadoub.voting.repository.RestaurantRepository;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    private final DishRepository dishRepository;

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public DishServiceImpl(DishRepository dishRepository, RestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @Transactional
    public Dish create(Dish dish, int restaurantId) {
        /*
              * нужна будет проверка (с выбросом кастомного exception, если не будет пройдена),
              * что ресторан, которому мы хотим добавить блюдо существует
              */

        Restaurant restaurant = restaurantRepository.getOne(restaurantId); //обернуть в check-метод(реализация позже)
        Assert.notNull(dish, "dish must not be null");
        dish.setRestaurant(restaurant);
        return dishRepository.save(dish);
    }

    @Override
    public Dish get(int id, int restaurantId) {
        /*
              * нужна будет проверка (с выбросом кастомного exception, если не будет пройдена),
              * что блюдо с переданными id и restaurantId существует
              */
        return dishRepository.get(id, restaurantId); //обернуть в check-метод(реализация позже)
    }

    @Override
    @Transactional
    public void update(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");

        /*
              * нужна будет проверка (с выбросом кастомного exception, если не будет пройдена),
              * что блюдо, которое мы хотим обновить, существует
              */
        get(dish.getId(), restaurantId); //обернуть в check-метод(реализация позже)
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        dishRepository.save(dish);
    }

    @Override
    public void delete(int id, int restaurantId) {
        /*
              * нужна будет проверка (с выбросом кастомного exception, если не будет пройдена),
              * что блюдо, которое мы хотим удалить, существует
              */
        dishRepository.delete(id, restaurantId);//обернуть в check-метод(реализация позже)
    }

    @Override
    @Transactional
    public List<Dish> getAllByRestaurantId(int restaurantId) {
        /*
              * нужна будет проверка (с выбросом кастомного exception, если не будет пройдена),
              * что ресторан с переданным id существует
              */
        restaurantRepository.getOne(restaurantId);//обернуть в check-метод(реализация позже)
        return dishRepository.getAllByRestaurantId(restaurantId);
    }
}
