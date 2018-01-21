package ru.alphadoub.voting.service;

import ru.alphadoub.voting.model.User;
import ru.alphadoub.voting.to.UserTo;

import java.util.List;

public interface UserService {
    User create(User user);

    User get(int id);

    void update(User user);

    void update(UserTo user);

    void delete(int id);

    User getByEmail(String email);

    List<User> getAll();
}
