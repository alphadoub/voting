package ru.alphadoub.voting.to;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserTo extends BaseTo {
    @Email
    @NotBlank
    @Size(max = 100)
    /*@SafeHtml(groups = ValidationGroups.Rest.class)*/
    private String email;

    @NotBlank
    @Size(min = 5, max = 20)
    private String password;

    public UserTo() {
    }

    public UserTo(Integer id, String name, String email, String password) {
        super(id, name);
        this.email = email;
        this.password = password;
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

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + getId() +
                ", name='" + getName() + "\'" +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                "}";
    }
}
