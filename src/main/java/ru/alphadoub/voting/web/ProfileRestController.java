package ru.alphadoub.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.alphadoub.voting.AuthorizedUser;
import ru.alphadoub.voting.ValidationGroups;
import ru.alphadoub.voting.model.User;
import ru.alphadoub.voting.service.UserService;

import static ru.alphadoub.voting.AuthorizedUser.user;
import static ru.alphadoub.voting.util.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = ProfileRestController.URL)
public class ProfileRestController {
    private static final Logger log = LoggerFactory.getLogger(ProfileRestController.class);

    static final String URL = "/profile";

    public final UserService service;

    @Autowired
    public ProfileRestController(UserService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    //когда подключим security, аннотировать параметр authorizedUser @AuthenticationPrincipal
    public User get(/*AuthorizedUser authorizedUser*/) {
        return user;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    //когда подключим security, аннотировать параметр authorizedUser @AuthenticationPrincipal
    public void update(@Validated(ValidationGroups.Rest.class) @RequestBody User user/*, AuthorizedUser authorizedUser*/) {
        assureIdConsistent(user, AuthorizedUser.user.getId());
        service.update(user);
        AuthorizedUser.update(user);
    }

    @DeleteMapping
    //когда подключим security, аннотировать параметр authorizedUser @AuthenticationPrincipal
    public void delete(/*AuthorizedUser authorizedUser*/) {
        service.delete(AuthorizedUser.user.getId());
    }
}
