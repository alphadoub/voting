package ru.alphadoub.voting.model;

import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "votes")
@IdClass(DateUserIdKey.class)
public class Vote {
    @Id
    private LocalDate date = LocalDate.now();

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName="id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @BatchSize(size = 200)
    private Restaurant restaurant;

    public Vote() {
    }

    public Vote(User user, Restaurant restaurant) {
        this.user = user;
        this.restaurant = restaurant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "date=" + date +
                ", user=" + user.getId() +
                ", restaurant=" + restaurant.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vote vote = (Vote) o;

        if (getDate() != null ? !getDate().equals(vote.getDate()) : vote.getDate() != null) return false;
        return !(getUser() != null ? !getUser().equals(vote.getUser()) : vote.getUser() != null);

    }

    @Override
    public int hashCode() {
        int result = getDate() != null ? getDate().hashCode() : 0;
        result = 31 * result + (getUser() != null ? getUser().hashCode() : 0);
        return result;
    }
}
