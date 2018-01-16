package ru.alphadoub.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.alphadoub.voting.model.DateUserIdKey;
import ru.alphadoub.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, DateUserIdKey> {
    @Override
    @Transactional
    Vote save(Vote vote);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.restaurant.id=?1 AND v.date=?2")
    long countByRestaurantIdAndDate(int restaurantId, LocalDate date);

    @Query("SELECT v FROM Vote v WHERE v.date=?1")
    List<Vote> getAllByDate(LocalDate date);



}
