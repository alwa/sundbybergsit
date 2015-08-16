package com.sundbybergsit.fatman.jsf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

@ApplicationScoped
@ManagedBean(name = "versionBean")
public class VersionBean implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(VersionBean.class);

    private String version;

    private String build;

    @PostConstruct
    public void init() {
        try {
            InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/META-INF/MANIFEST.MF");
            Manifest manifest = new Manifest();
            manifest.read(is);
            version = manifest.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION);
            build = manifest.getMainAttributes().getValue("Implementation-Build");
            LOGGER.info("Initializing Fatman version [#0], build: #1", version, build);
        } catch (IOException ioe) {
            LOGGER.error("Unable to read the Manifest file from classpath.", ioe);
        }
    }

    public String getVersion() {
        return version;
    }

    public String getBuild() {
        return build;
    }
}

