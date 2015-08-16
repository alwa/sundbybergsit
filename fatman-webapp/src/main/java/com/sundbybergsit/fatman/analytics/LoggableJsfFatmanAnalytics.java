package com.sundbybergsit.fatman.analytics;

import com.sundbybergsit.entities.FatmanDbUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@RequestScoped
@ManagedBean(name = "loggableFatmanAnalyticsBean")
public class LoggableJsfFatmanAnalytics extends JsfFatmanAnalytics {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsfFatmanAnalytics.class);

    @Override
    public FatmanDbUser getMostPhantomManThisWeek() {
        LOGGER.info("getMostPhantomManThisWeek()");
        return super.getMostPhantomManThisWeek();
    }

    @Override
    public FatmanDbUser getFattestManThisWeek() {
        LOGGER.info("getFattestManThisWeek()");
        return super.getFattestManThisWeek();
    }
}