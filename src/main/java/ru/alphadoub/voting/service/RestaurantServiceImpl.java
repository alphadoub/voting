package ru.alphadoub.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.alphadoub.voting.model.Restaurant;
import ru.alphadoub.voting.repository.RestaurantRepository;

import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository repository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return repository.save(restaurant);
    }

    @Override
    public Restaurant get(int id) {
        /*
              * нужна будет проверка (с выбросом кастомного exception, если не будет пройдена),
              * что ресторан с переданными id существует
              */
        return repository.findOne(id);//обернуть в check-метод(реализация позже)
    }

    @Override
    @Transactional
    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        /*
              * нужна будет проверка (с выбросом кастомного exception, если не будет пройдена),
              * что ресторан, который мы хотим обновить, существует
              */
        get(restaurant.getId()); //обернуть в check-метод(реализация позже)
        repository.save(restaurant);
    }

    @Override
    public void delete(int id) {
        /*
              * нужна будет проверка (с выбросом кастомного exception, если не будет пройдена),
              * что ресторан, который мы хотим удалить, существует
              */

        repository.delete(id);//обернуть в check-метод(реализация позже)
    }

    @Override
    public List<Restaurant> getAll() {
        return repository.getAll();
    }
}

