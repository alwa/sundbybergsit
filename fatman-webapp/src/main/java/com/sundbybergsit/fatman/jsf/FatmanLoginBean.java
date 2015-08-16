package com.sundbybergsit.fatman.jsf;

import com.google.api.client.auth.oauth2.Credential;
import com.sundbybergsit.authentication.Authentication;
import com.sundbybergsit.servlets.SessionCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.sundbybergsit.google.Util;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;

@SessionScoped
@ManagedBean(name = "loginBean")
public class FatmanLoginBean implements Serializable, Authentication {

    private static final Logger LOGGER = LoggerFactory.getLogger(FatmanLoginBean.class);

    private String accessToken;
    private boolean loggedIn;
    private String username;
    private String userId;
    private String sessionId;
    private String imageUrl;
    private Date birthday;

    @Override
    public void login() {
        try {
            Credential credential = Util.getFlow().loadCredential(sessionId);
            Plus.Builder builder = new Plus.Builder(Util.TRANSPORT, Util.JSON_FACTORY, credential);
            Plus plus = builder.build();
            Person person = plus.people().get("me").execute();
            userId = person.getId();
            imageUrl = person.getImage().getUrl();
            username = person.getDisplayName();
            try {
                birthday = Date.valueOf(person.getBirthday());
            } catch (Exception e) {
                // Ignore if birthday is incorrectly formatted
            }
            LOGGER.info("Person data parsed: ");
            LOGGER.info("Display name: {}", username);
            LOGGER.info("User ID: {}", userId);
            LOGGER.info("Image URL: {}", imageUrl);
            LOGGER.info("Birthday: {}", birthday);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public String getUsername() {
        return username;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setLoggedIn(boolean loggedIn) {
        if (loggedIn) {
            if (!this.loggedIn) {
                LOGGER.info("User is logging in.");
                login();
                LOGGER.info("User {} is logged in", username);
            }
        } else {
            accessToken = null;
            username = null;
            userId = null;
        }

        this.loggedIn = loggedIn;
    }

    @Override
    public void logout() throws IOException {
        setLoggedIn(false);
        Util.resetFlow();

        // Experimental
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

    public int getSessionCount() {
        return SessionCounter.getCount();
    }

    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setSessionId(String id) {
        sessionId = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Date getBirthday() {
        return birthday;
    }
}
