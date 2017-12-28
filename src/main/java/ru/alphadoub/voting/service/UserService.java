package ru.alphadoub.voting.service;

import ru.alphadoub.voting.model.User;

public interface UserService {
    User create(User user);

    User get(int id);

    void update(User user);

    void delete(int id);
}
