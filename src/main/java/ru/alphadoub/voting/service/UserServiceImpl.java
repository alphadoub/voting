package ru.alphadoub.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.alphadoub.voting.AuthorizedUser;
import ru.alphadoub.voting.model.User;
import ru.alphadoub.voting.repository.UserRepository;

import java.util.List;

import static ru.alphadoub.voting.util.UserUtil.prepareToSave;
import static ru.alphadoub.voting.util.ValidationUtil.checkNotFound;

@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return repository.save(prepareToSave(user, passwordEncoder));
    }

    @Override
    public User get(int id) {
        return checkNotFound(repository.findById(id).orElse(null), id);
    }

    @Override
    @Transactional
    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        checkNotFound(repository.findById(user.getId()).orElse(null), user.getId());
        repository.save(prepareToSave(user, passwordEncoder));
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}
