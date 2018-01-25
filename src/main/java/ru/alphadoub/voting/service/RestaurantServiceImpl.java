package ru.alphadoub.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.alphadoub.voting.model.Restaurant;
import ru.alphadoub.voting.model.User;
import ru.alphadoub.voting.model.Vote;
import ru.alphadoub.voting.repository.RestaurantRepository;
import ru.alphadoub.voting.repository.VoteRepository;

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
        return checkNotFound(restaurantRepository.findById(id).orElse(null), id);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Override
    @Transactional
    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNotFound(restaurantRepository.findById(restaurant.getId()).orElse(null), restaurant.getId());
        restaurantRepository.save(restaurant);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Override
    public void delete(int id) {
        checkNotFound(restaurantRepository.delete(id), id);
    }

    @Cacheable("restaurants")
    @Override
    public List<Restaurant> getAll() {
        return restaurantRepository.getAll();
    }

    @Override
    @Transactional
    public Vote vote(int id, User user) {
        Assert.notNull(user, "user must not be null");
        Restaurant restaurant = get(id);
        Vote vote = new Vote(user, restaurant);
        return voteRepository.save(vote);
    }
}

