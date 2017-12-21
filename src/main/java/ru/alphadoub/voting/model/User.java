package ru.alphadoub.voting.model;

import java.util.Set;

public class User extends BaseEntity {
    private String email;

    private String password;

    private Restaurant restaurant;

    private Set<Role> roles;

    public User() {
    }

    public User(Integer id, String name, String email, String password, Restaurant restaurant, Set<Role> roles) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.restaurant = restaurant;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
