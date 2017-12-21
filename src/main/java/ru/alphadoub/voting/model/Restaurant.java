package ru.alphadoub.voting.model;

public class Restaurant extends BaseEntity {

    private Integer countOfVotes;

    public Restaurant() {
    }

    public Restaurant(Integer id, String name, Integer countOfVotes) {
        super(id, name);
        this.countOfVotes = countOfVotes;
    }

    public Integer getCountOfVotes() {
        return countOfVotes;
    }

    public void setCountOfVotes(Integer countOfVotes) {
        this.countOfVotes = countOfVotes;
    }
}
