package ru.alphadoub.voting.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "restaurants")
public class Restaurant extends BaseEntity {
    @Column(name = "count_of_votes", nullable = false)
    private int countOfVotes = 0;

    public Restaurant() {
    }

    public Restaurant(String name) {
        this(null, name);
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    public Integer getCountOfVotes() {
        return countOfVotes;
    }

    public void setCountOfVotes(Integer countOfVotes) {
        this.countOfVotes = countOfVotes;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", countOfVotes=" + countOfVotes +
                '}';
    }
}
