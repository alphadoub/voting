package ru.alphadoub.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.alphadoub.voting.AuthorizedUser;
import ru.alphadoub.voting.ValidationGroups;
import ru.alphadoub.voting.model.User;
import ru.alphadoub.voting.service.UserService;
import ru.alphadoub.voting.to.UserTo;
import ru.alphadoub.voting.util.UserUtil;

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
    public User get(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("get user with id={}", authorizedUser.getId());
        return authorizedUser.getUser();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Validated(ValidationGroups.Rest.class) @RequestBody UserTo userTo, @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("update {} with id={}", userTo, authorizedUser.getId());
        assureIdConsistent(userTo, authorizedUser.getId());
        service.update(UserUtil.updateFromTo(authorizedUser.getUser(), userTo));
    }

    @DeleteMapping
    public void delete(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("delete user with id={}", authorizedUser.getId());
        service.delete(authorizedUser.getId());
    }
}
