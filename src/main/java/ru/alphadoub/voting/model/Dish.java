package ru.alphadoub.voting.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;
import ru.alphadoub.voting.ValidationGroups;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "dishes")
public class Dish extends BaseEntity {
    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 50, max = 50000)
    private Integer price;

    @Column(name = "date", nullable = false)
    @NotNull
    /*
        * Как альтернатива @JsonFormat - кастомизировать json мэппинг с помощью jackson-datatype-hibernate5.
        * @JsonFormat не всегда корректно работает с часовыми поясами. На текущем этапе этого и не надо
        */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    /*
        * Как альтернатива @JsonIgnore - кастомизировать json мэппинг с помощью jackson-datatype-hibernate5.
        * Имеет смысл при большом кол-ве сущностей и необходимости централизованного управления
        * настройками мэппинга. Для текущей задачи (небольшое приложение) этого не нужно
        */
    @JsonIgnore
    @NotNull(groups = ValidationGroups.Persist.class)
    private Restaurant restaurant;

    public Dish() {
    }

    public Dish(String name, Integer price, LocalDate date) {
        this(null, name, price, date);
    }

    public Dish(Integer id, String name, Integer price, LocalDate date) {
        this(id, name, price, date, null);
    }

    public Dish(Integer id, String name, Integer price, LocalDate date, Restaurant restaurant) {
        super(id, name);
        this.price = price;
        this.date = date;
        this.restaurant = restaurant;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
                ", name=" + getName() +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}
