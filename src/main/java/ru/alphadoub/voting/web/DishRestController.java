package ru.alphadoub.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.alphadoub.voting.ValidationGroups;
import ru.alphadoub.voting.model.Dish;
import ru.alphadoub.voting.service.DishService;

import java.net.URI;
import java.util.List;

import static ru.alphadoub.voting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = DishRestController.URL)
public class DishRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    static final String URL = RestaurantRestController.URL + "/{restaurant_id}/dishes";

    private final DishService service;

    @Autowired
    public DishRestController(DishService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> create(@Validated(ValidationGroups.Rest.class) @RequestBody Dish dish,
                       @PathVariable("restaurant_id") int restaurantId) {
        log.info("create {} in restaurant with id={}", dish, restaurantId);
        checkIsNew(dish);
        checkWrongDateForCreate(dish);
        Dish created = service.create(dish, restaurantId);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Dish get(@PathVariable("restaurant_id") int restaurantId, @PathVariable("id") int id) {
        log.info("get dish with id={} from restaurant with id={}", id, restaurantId);
        return service.get(id, restaurantId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Validated(ValidationGroups.Rest.class) @RequestBody Dish dish,
                       @PathVariable("restaurant_id") int restaurantId,
                       @PathVariable("id") int id) {
        log.info("update {} with id={} from restaurant with id={}", dish, id, restaurantId);
        assureIdConsistent(dish, id);
        service.update(dish, restaurantId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("restaurant_id") int restaurantId, @PathVariable("id") int id) {
        log.info("delete dish with id={} from restaurant with id={}", id, restaurantId);
        service.delete(id, restaurantId);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Dish> getTodayRestaurantMenu(@PathVariable("restaurant_id") int restaurantId) {
        log.info("get current day list of dishes from restaurant with id={}", restaurantId);
        return service.getTodayRestaurantMenu(restaurantId);
    }
}
