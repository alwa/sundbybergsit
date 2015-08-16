package com.sundbybergsit.authentication.entities;

import org.apache.bval.constraints.Email;
import org.apache.bval.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static javax.persistence.TemporalType.DATE;

/**
 * DTO between AccountResource and AccountClient
 * TODO: Define the content of this DTO
 *
 * @author DrakPappa
 */
@Entity
@Table(name = "Users", uniqueConstraints = @UniqueConstraint(name = "uq__users__username", columnNames = "username"))
public class DbUser extends PersistedEntity {

    @NotNull
    @NotEmpty
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull
    @NotEmpty
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @NotEmpty
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @NotNull
    @NotEmpty
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Email
    @Column(name = "email", nullable = true)
    private String email;

    @Temporal(DATE)
    @Column(name = "birthday", nullable = false)
    private java.sql.Date birthday;

    public DbUser(String username, String password, String firstname, String lastname, java.sql.Date birthday) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public java.sql.Date getBirthday() {
        return birthday;
    }

    public void setBirthday(java.sql.Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
