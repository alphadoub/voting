package ru.alphadoub.voting.model;


import java.io.Serializable;
import java.time.LocalDate;

public class DateUserIdKey implements Serializable {
    private LocalDate date;

    private Integer user;

    public DateUserIdKey() {
    }

    public DateUserIdKey(LocalDate date, Integer user) {
        this.date = date;
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateUserIdKey that = (DateUserIdKey) o;

        if (getDate() != null ? !getDate().equals(that.getDate()) : that.getDate() != null) return false;
        return !(getUser() != null ? !getUser().equals(that.getUser()) : that.getUser() != null);

    }

    @Override
    public int hashCode() {
        int result = getDate() != null ? getDate().hashCode() : 0;
        result = 31 * result + (getUser() != null ? getUser().hashCode() : 0);
        return result;
    }
}
