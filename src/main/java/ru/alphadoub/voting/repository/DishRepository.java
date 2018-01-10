package ru.alphadoub.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.alphadoub.voting.model.Dish;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {
    @Override
    @Transactional
    Dish save(Dish dish);

    @Query("SELECT d from Dish d WHERE d.id=?1 AND d.restaurant.id=?2")
    Dish get(int id, int restaurantId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Dish d WHERE d.id=?1 AND d.restaurant.id=?2")
    int delete(int id, int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=?1 ORDER BY d.price ASC")
    List<Dish> getAll(int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=?1 AND d.date=?2 ORDER BY d.price ASC")
    List<Dish> getAllByDate(int restaurantId, LocalDate date);
}
