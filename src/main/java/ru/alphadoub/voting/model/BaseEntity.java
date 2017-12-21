package ru.alphadoub.voting.model;

public abstract class BaseEntity {
    private Integer id;

    private String name;

    protected BaseEntity() {
    }

    protected BaseEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
