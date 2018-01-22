package ru.alphadoub.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.alphadoub.voting.model.Restaurant;
import ru.alphadoub.voting.model.User;
import ru.alphadoub.voting.model.Vote;
import ru.alphadoub.voting.repository.RestaurantRepository;
import ru.alphadoub.voting.repository.VoteRepository;
import ru.alphadoub.voting.to.RestaurantWithVotes;
import ru.alphadoub.voting.util.RestaurantUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static ru.alphadoub.voting.util.ValidationUtil.checkNotFound;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;

    private final VoteRepository voteRepository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, VoteRepository voteRepository) {
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Override
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant get(int id) {
        return checkNotFound(restaurantRepository.findOne(id), id);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Override
    @Transactional
    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNotFound(restaurantRepository.findOne(restaurant.getId()), restaurant.getId());
        restaurantRepository.save(restaurant);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Override
    public void delete(int id) {
        checkNotFound(restaurantRepository.delete(id), id);
    }

    /*
         * Метод вохвращает кэшируемвый результат, но аннотация кэширования
         * перенесена в слой репозитория для обеспечения возможности использования
         * результата кэширования внутри текущего класса. Как альтернатива создания
         * дополнительно проксиобъекта сервиса в классе сервиса
         */
    @Override
    public List<Restaurant> getAll() {
        return restaurantRepository.getAll();
    }

    @Override
    @Transactional
    public RestaurantWithVotes getWithCurrentDayVotes(int id) {
        Restaurant restaurant = get(id);
        long countOfVotes = voteRepository.countByRestaurantIdAndDate(id, LocalDate.now());
        return RestaurantUtil.getWithVotes(restaurant, countOfVotes);
    }

    @Override
    @Transactional
    public List<RestaurantWithVotes> getAllWithCurrentDayVotes() {
        List<Restaurant> restaurants = getAll();
        if (restaurants.isEmpty()) {
            return Collections.emptyList();
        }
        List<Vote> votes = voteRepository.getAllByDate(LocalDate.now());
        return RestaurantUtil.getWithVotes(restaurants, votes);
    }

    @Override
    @Transactional
    public void vote(int id, User user) {
        Assert.notNull(user, "user must not be null");
        Restaurant restaurant = get(id);
        Vote vote = new Vote(user, restaurant);
        voteRepository.save(vote);
    }
}

