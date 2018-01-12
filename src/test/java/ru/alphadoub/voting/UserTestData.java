package ru.alphadoub.voting;

import ru.alphadoub.voting.model.Role;
import ru.alphadoub.voting.model.User;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTestData {
    public static final int ADMIN_ID = 100000;
    public static final int USER1_ID = 100001;
    public static final int USER2_ID = 100002;
    public static final int USER3_ID = 100003;

    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "adminPassword", Role.ROLE_ADMIN, Role.ROLE_USER);
    public static final User USER1 = new User(USER1_ID, "User", "user@gmail.com", "userPassword", Role.ROLE_USER);
    public static final User USER2 = new User(USER2_ID, "User2", "user2@gmail.com", "userPassword2", Role.ROLE_USER);
    public static final User USER3 = new User(USER3_ID, "User3", "user3@gmail.com", "userPassword3", Role.ROLE_USER);

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingDefaultElementComparator().isEqualTo(expected);
    }

    public static User getCreated() {
        return new User("User", "newUser@gmail.com", "password");
    }

    public static User getUpdated(User user) {
        return new User(user.getId(), "UPDATED user", user.getEmail(), "updatedPassword", Role.ROLE_ADMIN);
    }
}
