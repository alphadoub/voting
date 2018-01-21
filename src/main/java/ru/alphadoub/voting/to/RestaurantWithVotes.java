package ru.alphadoub.voting.to;

public class RestaurantWithVotes extends BaseTo {
    private long countOfVotes;

    public RestaurantWithVotes(Integer id, String name, long countOfVotes) {
        super(id, name);
        this.countOfVotes = countOfVotes;
    }

    @Override
    public String toString() {
        return "RestaurantWithVotes{" +
                "id=" + getId() +
                ", name='" + getName() + "\'" +
                ", countOfVotes=" + countOfVotes +
                '}';
    }

    public long getCountOfVotes() {
        return countOfVotes;
    }

    public void setCountOfVotes(long countOfVotes) {
        this.countOfVotes = countOfVotes;
    }
}
