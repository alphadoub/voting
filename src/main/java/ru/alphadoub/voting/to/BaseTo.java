package ru.alphadoub.voting.to;

import ru.alphadoub.voting.HasId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public abstract class BaseTo implements HasId {
    private Integer id;

    @NotBlank
    @Size(min = 2, max = 100)
    //@SafeHtml(groups = {ValidationGroups.Rest.class})
    private String name;

    public BaseTo() {
    }

    public BaseTo(Integer id, String name) {
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
