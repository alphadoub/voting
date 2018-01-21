package ru.alphadoub.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.alphadoub.voting.AuthorizedUser;
import ru.alphadoub.voting.ValidationGroups;
import ru.alphadoub.voting.model.Restaurant;
import ru.alphadoub.voting.service.RestaurantService;
import ru.alphadoub.voting.to.RestaurantWithVotes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ru.alphadoub.voting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = RestaurantRestController.URL)
public class RestaurantRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    static final String URL = "/restaurants";

    private final RestaurantService service;

    @Autowired
    public RestaurantRestController(RestaurantService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant create(@Validated(ValidationGroups.Rest.class) @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkIsNew(restaurant);
        return service.create(restaurant);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant get(@PathVariable("id") int id) {
        log.info("get restaurant with id={}", id);
        return service.get(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Validated(ValidationGroups.Rest.class) @RequestBody Restaurant restaurant, @PathVariable("id") int id) {
        log.info("update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        service.update(restaurant);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id) {
        log.info("delete restaurant with id={}", id);
        service.delete(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return service.getAll();
    }

    @PostMapping(value = "/{id}/vote")
    public void vote(@PathVariable("id") int id, HttpServletRequest req) {
        log.info("vote for restaurant with id={}", id);
        checkVotingTime();
        service.vote(id, AuthorizedUser.user);
    }

    @GetMapping(value = "/{id}/with_votes", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantWithVotes getWithCurrentDayVotes(@PathVariable("id") int id) {
        log.info("get restaurant {} with current day votes", id);
        return service.getWithCurrentDayVotes(id);
    }

    @GetMapping(value = "/with_votes", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RestaurantWithVotes> getAllWithCurrentDayVotes() {
        log.info("get all restaurants with current day votes");
        return service.getAllWithCurrentDayVotes();
    }
}
