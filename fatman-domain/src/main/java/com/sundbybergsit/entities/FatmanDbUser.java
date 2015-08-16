package com.sundbybergsit.entities;

import org.apache.bval.constraints.NotEmpty;
import org.apache.commons.lang.Validate;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

import static javax.persistence.TemporalType.DATE;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "Users", uniqueConstraints = @UniqueConstraint(name = "uq__users__username", columnNames = "username"))
public class FatmanDbUser extends PersistedEntity {

    @NotNull
    @NotEmpty
    @Column(name = "username", nullable = false)
    private String username;

    @Min(100)
    @Max(300)
    @Column(name = "heightInCentimetres", nullable = false)
    private int heightInCentimetres;

    @NotNull
    @Temporal(DATE)
    @Column(name = "birthday", nullable = false)
    private java.sql.Date birthday;

    @NotNull
    @NotEmpty
    @Column(name = "firstName", nullable = false)
    private String firstName;

    @NotNull
    @NotEmpty
    @Column(name = "lastName", nullable = false)
    private String lastName;

    public FatmanDbUser(String username, int heightInCentimetres, java.sql.Date birthday, String firstName, String lastName) {
        Validate.notNull(username);
        Validate.notNull(birthday);
        Validate.notNull(firstName);
        Validate.notNull(lastName);
        this.username = username;
        this.heightInCentimetres = heightInCentimetres;
        this.birthday = birthday;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public FatmanDbUser() {
    }

    public String getUsername() {
        return username;
    }

    public int getHeightInCentimetres() {
        return heightInCentimetres;
    }

    public java.sql.Date getBirthday() {
        return birthday;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setHeightInCentimetres(int heightInCentimetres) {
        this.heightInCentimetres = heightInCentimetres;
    }

    public void setBirthday(java.sql.Date birthday) {
        this.birthday = birthday;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof FatmanDbUser))
            return false;

        FatmanDbUser other = (FatmanDbUser) o;

        if (getId() == null) {
            return false;
        }

        // equivalence by id
        return getId().equals(other.getId());
    }

    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        } else {
            return super.hashCode();
        }
    }

    @Override
    public String toString() {
        return "FatmanDbUser{" +
                "username='" + username + '\'' +
                ", heightInCentimetres=" + heightInCentimetres +
                ", birthday=" + birthday +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
