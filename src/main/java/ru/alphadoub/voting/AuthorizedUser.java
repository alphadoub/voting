package ru.alphadoub.voting;

import ru.alphadoub.voting.model.Role;
import ru.alphadoub.voting.model.User;

public class AuthorizedUser {
    /*
        * Временная заглушка. Пока имитируем аворизованного пользователя и возможность его редактирования
        * статическим полем и статическим методом. При введении spring security
        * и использовании @AuthenticationPrincipal потребность в статичсеких элементах отпадёт.
        */
    public static User user = new User(100001, "User", "user@gmail.com", "userPassword", Role.ROLE_USER);

    private static int id = 100001;

    public static int id() {
         return id;
    }

    public static void update(User updated) {
        user = updated;
    }
}
