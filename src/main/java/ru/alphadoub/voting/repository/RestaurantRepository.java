package ru.alphadoub.voting.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.alphadoub.voting.model.Restaurant;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Override
    @Transactional
    Restaurant save(Restaurant restaurant);

    @Override
    Optional<Restaurant> findById(Integer id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Restaurant r WHERE r.id=?1")
    int delete(int id);

    @Cacheable("restaurants")
    @Query("SELECT r FROM Restaurant r ORDER BY r.name ASC")
    List<Restaurant> getAll();
}
