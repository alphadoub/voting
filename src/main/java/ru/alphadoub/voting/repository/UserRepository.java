package ru.alphadoub.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.alphadoub.voting.model.User;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {
    @Override
    @Transactional
    User save(User user);

    @Override
    Optional<User> findById(Integer id);

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id=?1")
    int delete(int id);

    @Query("SELECT DISTINCT u from User u LEFT JOIN FETCH u.roles WHERE u.email=?1")
    User getByEmail(String email);

    @Query("SELECT u from User u ORDER BY u.name ASC, u.email ASC")
    List<User> getAll();
}
