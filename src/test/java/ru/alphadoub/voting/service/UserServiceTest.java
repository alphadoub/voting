package ru.alphadoub.voting.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.alphadoub.voting.model.User;
import ru.alphadoub.voting.validation.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static ru.alphadoub.voting.UserTestData.*;

public class UserServiceTest extends AbstractServiceTest {
    @Autowired
    UserService service;

    @Test
    public void testCreate() throws Exception {
        User newUser = getCreated();
        User created = service.create(newUser);
        newUser.setId(created.getId());
        assertMatch(service.getAll(), ADMIN, newUser, USER1, USER2, USER3);
    }

    @Test
    public void testNullUserCreate() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("user must not be null");
        service.create(null);
    }

    @Test
    public void testDuplicatedEmailCreate() throws Exception {
        thrown.expect(DataAccessException.class);
        User newUser = new User("newUser", "user@gmail.com", "password");
        service.create(newUser);
    }


    @Test
    public void testGet() throws Exception {
        User user = service.get(ADMIN_ID);
        assertMatch(user, ADMIN);
    }

    @Test
    public void testGetNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.get(1);
    }

    @Test
    public void testUpdate() throws Exception {
        User updated = getUpdated(USER3);
        service.update(updated);
        assertMatch(service.get(USER3_ID), updated);
    }

    @Test
    public void testNullUserUpdate() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("user must not be");
        service.update(null);
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        User updated = new User(1, "UPDATED user", "updatedEmail@gmail.com", "updatedPassword");
        service.update(updated);
    }


    @Test
    public void testDelete() throws Exception {
        service.delete(USER2_ID);
        assertMatch(service.getAll(), ADMIN, USER1, USER3);
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.delete(1);
    }


    @Test
    public void testGetByEmail() throws Exception {
        User user = service.getByEmail("admin@gmail.com");
        assertMatch(user, ADMIN);
    }

    @Test
    public void testGetByNullEmail() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("email must not be null");
        service.getByEmail(null);
    }

    @Test
    public void testGetByEmailNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.getByEmail("notfound@gmail.com");
    }

    @Test
    public void testGetAll() throws Exception {
        List<User> all = service.getAll();
        assertMatch(all, ADMIN, USER1, USER2, USER3);
    }

    @Test
    public void testValidation() throws Exception {
        //@NotBlank and @Size name checking
        validateRootCause(() -> service.create(new User(" ", "newUser@gmail.com", "password")), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User("U", "newUser@gmail.com", "password")), ConstraintViolationException.class);

        //@Email and @NotBlank email checking
        validateRootCause(() -> service.create(new User("newUser", "wrongFormat@@@gmail.com", "password")), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User("newUser", "", "password")), ConstraintViolationException.class);

        //@NotBlank and @Size password checking
        validateRootCause(() -> service.create(new User("newUser", "newUser@gmail.com", "  ")), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User("newUser", "newUser@gmail.com", "123")), ConstraintViolationException.class);

    }
}