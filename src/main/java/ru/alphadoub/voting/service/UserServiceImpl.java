package ru.alphadoub.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.alphadoub.voting.model.User;
import ru.alphadoub.voting.repository.UserRepository;

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
        /*
              * нужна будет проверка (с выбросом кастомного exception, если не будет пройдена),
              * что пользователь с переданными id существует
              */
        return repository.findOne(id);//обернуть в check-метод(реализация позже)
    }

    @Override
    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        /*
              * нужна будет проверка (с выбросом кастомного exception, если не будет пройдена),
              * что пользователь, которого мы хотим обновить, существует
              */
        get(user.getId()); //обернуть в check-метод(реализация позже)
        repository.save(user);
    }

    @Override
    public void delete(int id) {
        /*
              * нужна будет проверка (с выбросом кастомного exception, если не будет пройдена),
              * что пользователь, которого мы хотим удалить, существует
              */
        repository.delete(id);//обернуть в check-метод(реализация позже)
    }
}
