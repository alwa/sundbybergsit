package com.sundbybergsit.fatman.jsf;

import com.sundbybergsit.services.ImageRepository;
import org.primefaces.model.UploadedFile;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.logging.Logger;

@RequestScoped
@ManagedBean(name = "fileUploadController")
public class FileUploadController {

    private Logger logger = Logger.getLogger(FileUploadController.class.getName());

    @Inject
    private ImageRepository repository;

    @ManagedProperty(value = "#{loginBean}")
    private FatmanLoginBean loginBean;

    private UploadedFile phantomImage;
    private UploadedFile fatImage;

    public void uploadPhantomImage() {
        if (phantomImage != null) {
            FacesMessage msg = new FacesMessage("Bilden laddades upp", phantomImage.getFileName() + " is uploaded.");
            repository.updatePhantomImage(loginBean.getUserId(), phantomImage.getContents());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void uploadFatImage() {
        if (fatImage != null) {
            FacesMessage msg = new FacesMessage("Bilden laddades upp", fatImage.getFileName() + " is uploaded.");
            repository.updateFatImage(loginBean.getUserId(), fatImage.getContents());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public UploadedFile getPhantomImage() {
        return phantomImage;
    }

    public void setPhantomImage(UploadedFile phantomImage) {
        this.phantomImage = phantomImage;
    }

    public UploadedFile getFatImage() {
        return fatImage;
    }

    public void setFatImage(UploadedFile fatImage) {
        this.fatImage = fatImage;
    }

    /* To make this injection successful, the inject property (loginBean) must provide the setter method. */
    public void setLoginBean(FatmanLoginBean loginBean) {
        this.loginBean = loginBean;
    }
}
