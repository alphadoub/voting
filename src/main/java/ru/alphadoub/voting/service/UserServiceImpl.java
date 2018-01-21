package ru.alphadoub.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.alphadoub.voting.model.User;
import ru.alphadoub.voting.repository.UserRepository;
import ru.alphadoub.voting.to.UserTo;

import java.util.List;

import static ru.alphadoub.voting.util.UserUtil.updateFromTo;
import static ru.alphadoub.voting.util.ValidationUtil.checkNotFound;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return repository.save(user);
    }

    @Override
    public User get(int id) {
        return checkNotFound(repository.findOne(id), id);
    }

    @Override
    @Transactional
    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        checkNotFound(repository.findOne(user.getId()), user.getId());
        repository.save(user);
    }

    @Override
    @Transactional
    public void update(UserTo userTo) {
        User user = updateFromTo(get(userTo.getId()), userTo);
        repository.save(user);
    }

    @Override
    public void delete(int id) {
        checkNotFound(repository.delete(id), id);
    }

    @Override
    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    @Override
    public List<User> getAll() {
        return repository.getAll();
    }
}
