package ru.alphadoub.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.alphadoub.voting.AuthorizedUser;
import ru.alphadoub.voting.ValidationGroups;
import ru.alphadoub.voting.model.Restaurant;
import ru.alphadoub.voting.model.Vote;
import ru.alphadoub.voting.repository.VoteRepository;
import ru.alphadoub.voting.service.DishService;
import ru.alphadoub.voting.service.RestaurantService;
import ru.alphadoub.voting.to.RestaurantWithMenu;
import ru.alphadoub.voting.to.RestaurantWithVotes;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.alphadoub.voting.util.RestaurantUtil.*;
import static ru.alphadoub.voting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = RestaurantRestController.URL)
public class RestaurantRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    static final String URL = "/restaurants";

    private final RestaurantService restaurantService;

    private final DishService dishService;

    private final VoteRepository voteRepository;

    @Autowired
    public RestaurantRestController(RestaurantService restaurantService, DishService dishService, VoteRepository voteRepository) {
        this.restaurantService = restaurantService;
        this.dishService = dishService;
        this.voteRepository = voteRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@Validated(ValidationGroups.Rest.class) @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkIsNew(restaurant);
        Restaurant created = restaurantService.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Transactional
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantWithMenu get(@PathVariable("id") int id) {
        log.info("get restaurant with id={}", id);
        return createWithMenu(restaurantService.get(id), dishService.getTodayRestaurantMenu(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Validated(ValidationGroups.Rest.class) @RequestBody Restaurant restaurant, @PathVariable("id") int id) {
        log.info("update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantService.update(restaurant);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id) {
        log.info("delete restaurant with id={}", id);
        restaurantService.delete(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return restaurantService.getAll();
    }
    
    @Transactional
    @GetMapping(value = "/with_menu", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RestaurantWithMenu> getAllWithMenu() {
        log.info("get all restaurants with menu");
        return getWithMenu(restaurantService.getAll(), dishService.getAllByDate(LocalDate.now()));
    }

    @PostMapping(value = "/{id}/vote", produces = MediaType.APPLICATION_JSON_VALUE)
    public Vote vote(@PathVariable("id") int id, @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("vote for restaurant with id={}", id);
        checkVotingTime(11);
        return restaurantService.vote(id, authorizedUser.getUser());
    }

    @Transactional
    @GetMapping(value = "/{id}/with_votes", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantWithVotes getWithCurrentDayVotes(@PathVariable("id") int id) {
        log.info("get restaurant {} with current day votes", id);
        return createWithVotes(restaurantService.get(id), voteRepository.countByRestaurantIdAndDate(id, LocalDate.now()));
    }

    @Transactional
    @GetMapping(value = "/with_votes", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RestaurantWithVotes> getAllWithCurrentDayVotes() {
        log.info("get all restaurants with current day votes");
        return getWithVotes(restaurantService.getAll(), voteRepository.getAllByDate(LocalDate.now()));
    }
}
