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
import ru.alphadoub.voting.to.UserTo;

import static ru.alphadoub.voting.util.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = ProfileRestController.URL)
public class ProfileRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    static final String URL = "/profile";

    public final UserService service;

    @Autowired
    public ProfileRestController(UserService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    //когда подключим security, аннотировать параметр authorizedUser @AuthenticationPrincipal
    public User get(/*AuthorizedUser authorizedUser*/) {
        log.info("get user with id={}", AuthorizedUser.id());
        return service.get(AuthorizedUser.id());
        //return AuthorizedUser.user;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    //когда подключим security, аннотировать параметр authorizedUser @AuthenticationPrincipal
    public void update(@Validated(ValidationGroups.Rest.class) @RequestBody UserTo userTo/*, AuthorizedUser authorizedUser*/) {
        log.info("update {} with id={}", userTo, AuthorizedUser.id());
        assureIdConsistent(userTo, AuthorizedUser.id());
        service.update(userTo);
        //Надо ли обновлять еще и юзера в AuthorizedUser? Проверить при введении security

    }

    @DeleteMapping
    //когда подключим security, аннотировать параметр authorizedUser @AuthenticationPrincipal
    public void delete(/*AuthorizedUser authorizedUser*/) {
        log.info("delete user with id={}", AuthorizedUser.id());
        service.delete(AuthorizedUser.id());
    }
}
