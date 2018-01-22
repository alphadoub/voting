package ru.alphadoub.voting.util;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import ru.alphadoub.voting.model.User;
import ru.alphadoub.voting.to.UserTo;

public class UserUtil {
    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }

    public static User prepareToSave(User user, PasswordEncoder passwordEncoder) {
        String password = user.getPassword();
        user.setPassword(StringUtils.isEmpty(password) ? password : passwordEncoder.encode(password));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
