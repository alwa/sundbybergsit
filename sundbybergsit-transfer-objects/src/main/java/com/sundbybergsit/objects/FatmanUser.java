package com.sundbybergsit.objects;

import javax.xml.bind.annotation.*;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlType(name = "fatmanUser", propOrder = {
        "username",
        "firstName",
        "lastName",
        "heightInCentimetres",
        "birthday"
})
public class FatmanUser {

    @XmlAttribute
    private String username;

    @XmlAttribute
    private String firstName;

    @XmlAttribute
    private String lastName;

    @XmlAttribute
    private int heightInCentimetres;

    @XmlAttribute
    private Date birthday;

    @SuppressWarnings("unused") // Used by Jersey
    public FatmanUser() {
    }

    public FatmanUser(String username, int heightInCentimetres, String firstName, String lastName, Date birthday) {
        this.username = username;
        this.heightInCentimetres = heightInCentimetres;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public int getHeightInCentimetres() {
        return heightInCentimetres;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setHeightInCentimetres(int heightInCentimetres) {
        this.heightInCentimetres = heightInCentimetres;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
