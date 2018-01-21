package ru.alphadoub.voting.util;


import ru.alphadoub.voting.model.User;
import ru.alphadoub.voting.to.UserTo;

public class UserUtil {
    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }
}
