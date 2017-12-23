package ru.alphadoub.voting.model;

import org.hibernate.validator.constraints.Range;
import ru.alphadoub.voting.ValidationGroups;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "dishes")
public class Dish extends BaseEntity {
    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 50, max = 50000)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull(groups = ValidationGroups.Persist.class)
    private Restaurant restaurant;

    public Dish() {
    }

    public Dish(Integer id, String name, Integer price, Restaurant restaurant) {
        super(id, name);
        this.price = price;
        this.restaurant = restaurant;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + getId() +
                "name=" + getName() +
                "price=" + price +
                '}';
    }
}
