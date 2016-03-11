package com.sundbybergsit.fatman.jsf;

import com.sundbybergsit.entities.UserDbSettings;
import com.sundbybergsit.services.UserSettingsRepository;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@ManagedBean(name = "imageBean")
@RequestScoped
public class ImageBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageBean.class);

    @Inject
    private UserSettingsRepository service;

    public StreamedContent getFatImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String id = context.getExternalContext().getRequestParameterMap().get("userId");
            UserDbSettings settings = service.findSettingsForUser(id);
            byte[] fatImage = settings.getFatImage();
            if (fatImage == null) {
                LOGGER.info("No fat image found for user: {}", id);
                return null;
            }
            return new DefaultStreamedContent(new ByteArrayInputStream(fatImage));
        }
    }

    public StreamedContent getPhantomImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String id = context.getExternalContext().getRequestParameterMap().get("userId");
            byte[] phantomImage = service.findSettingsForUser(id).getPhantomImage();
            if (phantomImage == null) {
                LOGGER.info("No phantom image found for user: {}", id);
                return null;
            }
            return new DefaultStreamedContent(new ByteArrayInputStream(phantomImage));
        }
    }

}
