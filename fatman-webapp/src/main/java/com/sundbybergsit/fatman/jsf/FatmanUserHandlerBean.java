package com.sundbybergsit.fatman.jsf;

import com.sundbybergsit.entities.FatmanDbUser;
import com.sundbybergsit.entities.UserDbSettings;
import com.sundbybergsit.services.UserRepository;
import com.sundbybergsit.services.UserSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.transaction.SystemException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RequestScoped
@ManagedBean(name = "fatmanUserHandlerBean")
public class FatmanUserHandlerBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(FatmanUserHandlerBean.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSettingsRepository userSettingsRepository;

    @ManagedProperty(value = "#{loginBean}")
    private FatmanLoginBean loginBean;

    private String username;
    private String firstname;
    private String lastname;
    private Date birthday = new Date(Calendar.getInstance().getTimeInMillis());
    private Integer heightInCentimetres;
    private List<SelectItem> users;

    public void create() throws SystemException {
        try {
            username = loginBean.getUserId();
            FatmanDbUser user = new FatmanDbUser(username, heightInCentimetres, birthday, firstname, lastname);
            userRepository.saveNew(user);
            LOGGER.info("Persisting new user: {}", user);
            showInfoMessage("Grattis! Ditt konto har skapats och du kan nu börja använda Fatman!");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            showErrorMessage(e);
        }
    }

    private void showInfoMessage(String message) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
    }

    private void showErrorMessage(Exception e) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ååå omöjligt. Någonting gick snett!", e.getMessage()));
    }

    public boolean hasAccount() {
        username = loginBean.getUserId();
        return userRepository.existsUser(username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Integer getHeightInCentimetres() {
        return heightInCentimetres;
    }

    public void setHeightInCentimetres(Integer heightInCentimetres) {
        this.heightInCentimetres = heightInCentimetres;
    }

    /* To make this injection successful, the inject property (loginBean) must provide the setter method. */
    public void setLoginBean(FatmanLoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public List<SelectItem> getUsers() {
        if (users == null) {
            List<FatmanDbUser> fatmanDbUsers = userRepository.findUsers();
            users = new ArrayList<>();
            for (FatmanDbUser user : fatmanDbUsers) {
                UserDbSettings settings = userSettingsRepository.findSettingsForUser(user.getId());
                if (loginBean.getUserId().equals(user.getUsername()) || settings.getShowDataToEveryone()) {
                    users.add(new SelectItem(user.getUsername(), user.getFirstName() + " " + user.getLastName()));
                }
            }
        }
        return users;
    }

    public String getFullName(String userId) {
        FatmanDbUser user = userRepository.findUserByUserName(userId);
        return user.getFirstName() + " " + user.getLastName();
    }
}
