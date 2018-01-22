package ru.alphadoub.voting;

import ru.alphadoub.voting.model.User;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = 1L;

    private User user;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user =user;
    }

    public User getUser() {
        return user;
    }

    public void update(User updatedUser) {
        user = updatedUser;
    }

    public int getId() {
        return user.getId();
    }

    @Override
    public String toString() {
        return user.toString();
    }
}
