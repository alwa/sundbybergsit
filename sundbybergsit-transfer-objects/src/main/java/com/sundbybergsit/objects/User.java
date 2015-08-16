/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sundbybergsit.objects;

import org.apache.bval.constraints.Email;

import javax.persistence.Entity;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "User", propOrder = {
        "username",
        "password",
        "firstname",
        "lastname",
        "email",
        "birthday"
})
public class User implements Serializable {

    @XmlAttribute
    private String username;
    @XmlAttribute
    private String password;
    @XmlAttribute
    private String firstname;
    @XmlAttribute
    private String lastname;
    @Email
    @XmlAttribute
    private String email;
    @XmlAttribute
    private Date birthday;

    public User() {
        // Xml rules require public constructor
    }

    public User(String username, String password, String firstname, String lastname, Date birthday) {
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
