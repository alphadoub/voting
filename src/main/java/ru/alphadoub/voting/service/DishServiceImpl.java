package ru.alphadoub.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.alphadoub.voting.model.Dish;
import ru.alphadoub.voting.model.Restaurant;
import ru.alphadoub.voting.repository.DishRepository;
import ru.alphadoub.voting.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.alphadoub.voting.util.ValidationUtil.checkNotFound;
import static ru.alphadoub.voting.util.ValidationUtil.checkWrongDateForUpdate;

@Service
public class DishServiceImpl implements DishService {
    private final DishRepository dishRepository;

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public DishServiceImpl(DishRepository dishRepository, RestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @Override
    @Transactional
    public Dish create(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        Restaurant restaurant = checkNotFound(restaurantRepository.findOne(restaurantId), restaurantId);
        dish.setRestaurant(restaurant);
        return dishRepository.save(dish);
    }

    @Cacheable("dishes")
    @Override
    public Dish get(int id, int restaurantId) {
        return checkNotFound(dishRepository.get(id, restaurantId), "id=" + id + " restaurantId=" + restaurantId);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @Override
    @Transactional
    public void update(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        Dish oldDish = get(dish.getId(), restaurantId);
        checkWrongDateForUpdate(oldDish, dish);
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        dishRepository.save(dish);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @Override
    public void delete(int id, int restaurantId) {
        checkNotFound(dishRepository.delete(id, restaurantId), "id=" + id + " restaurantId=" + restaurantId );
    }

    @Cacheable("dishes")
    @Override
    @Transactional
    public List<Dish> getCurrentDayList(int restaurantId) {
        checkNotFound(restaurantRepository.findOne(restaurantId), restaurantId);
        return dishRepository.getAllByDate(restaurantId, LocalDate.now());
    }
}
