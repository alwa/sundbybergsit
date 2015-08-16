package com.sundbybergsit.fatman.jsf;

import com.sundbybergsit.entities.UserDbSettings;
import com.sundbybergsit.services.UserSettingsRepository;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;

@ViewScoped
@ManagedBean(name = "fatmanUserSettingsBean")
public class FatmanUserSettingsHandlerBean implements Serializable {

    @Inject
    private UserSettingsRepository repository;

    @ManagedProperty(value = "#{loginBean}")
    private FatmanLoginBean loginBean;

    private boolean showMyValuesToEveryone;

    @PostConstruct
    public void init() {
        String userId = loginBean.getUserId();
        UserDbSettings settings = repository.findSettingsForUser(userId);
        showMyValuesToEveryone = settings.getShowDataToEveryone();
    }

    public void updateSettings() {
        try {
            String userId = loginBean.getUserId();
            repository.showMyValuesToEveryone(userId, showMyValuesToEveryone);
            showInfoMessage("Inställningarna är uppdaterade!");
        } catch (Exception e) {
            showErrorMessage(e);
        }
    }

    public void cancel() {
        init();
    }

    private void showErrorMessage(Exception e) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ååå omöjligt. Någonting gick snett!", e.getMessage()));
    }

    private void showInfoMessage(String message) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
    }

    public boolean isShowMyValuesToEveryone() {
        return showMyValuesToEveryone;
    }

    public void setShowMyValuesToEveryone(boolean showMyValuesToEveryone) {
        this.showMyValuesToEveryone = showMyValuesToEveryone;
    }

    /* To make this injection successful, the inject property (loginBean) must provide the setter method. */
    public void setLoginBean(FatmanLoginBean loginBean) {
        this.loginBean = loginBean;
    }
}
