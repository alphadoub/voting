package ru.alphadoub.voting.to;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.alphadoub.voting.model.Dish;

import java.util.List;

public class RestaurantWithMenu extends BaseTo {
    @JsonSerialize
    private List<Dish> menu;

    public RestaurantWithMenu(Integer id, String name, List<Dish> menu) {
        super(id, name);
        this.menu = menu;
    }

    public List<Dish> getMenu() {
        return menu;
    }

    public void setMenu(List<Dish> menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "RestaurantWithVotes{" +
                "id=" + getId() +
                ", name='" + getName() + "\'" +
                ", menu=" + menu +
                '}';
    }
}
