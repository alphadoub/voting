package ru.alphadoub.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.alphadoub.voting.model.Dish;

import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {
    @Override
    Dish save(Dish dish);

    @Override
    Dish findOne(Integer integer);

    @Modifying
    @Transactional
    @Query("DELETE FROM Dish d WHERE d.id=?1")
    int delete(int id);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=?1 ORDER BY d.price ASC")
    List<Dish> getAllByRestaurantId(int restaurantId);
}
