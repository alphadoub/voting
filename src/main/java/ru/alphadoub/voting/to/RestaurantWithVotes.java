package ru.alphadoub.voting.to;

public class RestaurantWithVotes {
    private int id;

    private String name;

    private long countOfVotes;

    public RestaurantWithVotes(Integer id, String name, long countOfVotes) {
        this.id = id;
        this.name = name;
        this.countOfVotes = countOfVotes;
    }

    @Override
    public String toString() {
        return "RestaurantWithVotes{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", countOfVotes=" + countOfVotes +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCountOfVotes() {
        return countOfVotes;
    }

    public void setCountOfVotes(long countOfVotes) {
        this.countOfVotes = countOfVotes;
    }
}
