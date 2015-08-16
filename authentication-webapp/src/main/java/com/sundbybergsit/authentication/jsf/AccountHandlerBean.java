package com.sundbybergsit.authentication.jsf;

import com.sundbybergsit.authentication.entities.DbUser;
import com.sundbybergsit.authentication.services.AuthenticationService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import java.sql.Date;
import java.util.Calendar;

@RequestScoped
@ManagedBean(name = "accountHandlerBean")
public class AccountHandlerBean {

    @Inject
    private AuthenticationService service;

    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private Date birthday = new Date(Calendar.getInstance().getTimeInMillis());

    public void create() throws SystemException {
        try {
            DbUser user = new DbUser(username, password, firstname, lastname, birthday);
            service.save(user);
            showInfoMessage("Worked!");
        } catch (Exception e) {
            showErrorMessage(e);
        }
    }

    private void showInfoMessage(String message) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
    }

    private void showErrorMessage(Exception e) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Some error", e.getMessage()));
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
}
