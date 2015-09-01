package com.sundbybergsit.fatman.jsf;

import com.sundbybergsit.calculation.Calculator;
import com.sundbybergsit.entities.FatmanDbUser;
import com.sundbybergsit.entities.PersonDataDbEntry;
import com.sundbybergsit.services.*;
import org.apache.commons.lang.Validate;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.*;

@ManagedBean(name = "fatmanDataHandlerBean")
public class FatmanDataHandlerBean implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(FatmanDataHandlerBean.class);

    @Inject
    private PersonDataDbEntryRepository personDataDbEntryRepository;

    @Inject
    private FatmanDataService service;

    @Inject
    private UserStatisticsService userStatisticsService;

    @Inject
    private UserSettingsRepository userSettingsRepository;

    @Inject
    private UserRepository userRepository;

    @ManagedProperty(value = "#{loginBean}")
    private FatmanLoginBean loginBean;

    private String displayName;
    @Max(value = 635, message = "Världens fetaste man vägde 635kg. Du kan inte vara fetare än så!")
    @Min(value = 40, message = "Väger du mindre än 40kg så är det bara att springa till BK direkt...")
    private Float weightInKilograms;
    private Float fatPercentage;
    private Float waterPercentage;

    private Date date = new Date();

    @Min(0)
    @Max(3)
    private Integer activityLevel = 2;

    private MeterGaugeChartModel meterGaugeModel;

    private String userId;

    @PostConstruct
    public void init() {
        try {
            Validate.notNull(loginBean, "loginBean must be set!");

            userId = loginBean.getUserId();
            Validate.notNull(userId, "userId must be set!");

            FatmanDbUser user = userRepository.findUserByUserName(userId);
            Validate.notNull(user, "user must be set!");

            displayName = user.getFirstName() + " " + user.getLastName();
        } catch (Exception e) {
            LOGGER.error(String.format("Error: %s", e.getMessage()), e);
            showErrorMessage(e);
        }
    }

    public void create() throws SystemException {
        try {
            Validate.notNull(weightInKilograms, "Weight must be set!");
            Validate.notNull(fatPercentage, "Fat percentage must be set!");

            FatmanDbUser user = userRepository.findUserByUserName(userId);
            Validate.notNull(user, "user must be set!");

            PersonDataDbEntry data = new PersonDataDbEntry(user, weightInKilograms, fatPercentage,
                    waterPercentage == null ? 0f : waterPercentage, new java.sql.Date(date.getTime()), activityLevel);
            personDataDbEntryRepository.save(data);
            LOGGER.info("Persisting new data entry: {}", data);
            createMeterGaugeModel(user);
            showInfoMessage("Hohoo, värdet sparades ned i databasen!");
        } catch (Exception e) {
            LOGGER.error(String.format("Error: %s", e.getMessage()), e);
            showErrorMessage(e);
        }
    }

    private void showInfoMessage(String message) {
        FacesContext facesContext = getFacesContext();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
    }

    private void showWarnMessage(String message) {
        FacesContext facesContext = getFacesContext();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, message, message));
    }

    FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    void showErrorMessage(Exception e) {
        FacesContext facesContext = getFacesContext();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, String.format("Ooof, någonting gick fel (%s)", e.getMessage()), e.getMessage()));
    }

    public String getDisplayName() {
        return displayName;
    }

    public Float getWeightInKilograms() {
        return weightInKilograms;
    }

    public void setWeightInKilograms(Float weightInKilograms) {
        this.weightInKilograms = weightInKilograms;
    }

    public Float getFatPercentage() {
        return fatPercentage;
    }

    public void setFatPercentage(Float fatPercentage) {
        this.fatPercentage = fatPercentage;
    }

    public Float getWaterPercentage() {
        return waterPercentage;
    }

    public void setWaterPercentage(Float waterPercentage) {
        this.waterPercentage = waterPercentage;
    }

    public Integer getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(Integer activityLevel) {
        this.activityLevel = activityLevel;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    private void createMeterGaugeModel(FatmanDbUser user) {
        List<Number> intervals = new ArrayList<Number>() {{
            add(18.5);
            add(24.9);
            add(29.9);
            add(39.9);
        }};

        Calculator<FatmanDbUser> bmiCalculator = (user1, weight) -> (float) weight / (((double) user1.getHeightInCentimetres() / 100) * ((double) user1.getHeightInCentimetres() / 100));

        Number bmi = bmiCalculator.calculate(user, weightInKilograms);
        meterGaugeModel = new MeterGaugeChartModel(bmi, intervals);
        meterGaugeModel.setShowTickLabels(false);
        meterGaugeModel.setIntervalOuterRadius(130);
        meterGaugeModel.setLabelHeightAdjust(110);
        meterGaugeModel.setSeriesColors("93b75f, 66cc66, E7E658, cc6666");
        meterGaugeModel.setTitle(getFatLevelComment(bmi));
        meterGaugeModel.setGaugeLabel("BMI");
    }

    public MeterGaugeChartModel getMeterGaugeModel() {
        return meterGaugeModel;
    }

    public void setMeterGaugeModel(MeterGaugeChartModel meterGaugeModel) {
        this.meterGaugeModel = meterGaugeModel;
    }

    public void setPersonDataDbEntryRepository(PersonDataDbEntryRepository personDataDbEntryRepository) {
        this.personDataDbEntryRepository = personDataDbEntryRepository;
    }

    public void setService(FatmanDataService service) {
        this.service = service;
    }

    public void setUserStatisticsService(UserStatisticsService userStatisticsService) {
        this.userStatisticsService = userStatisticsService;
    }

    /* To make this injection successful, the inject property (loginBean) must provide the setter method. */
    public void setLoginBean(FatmanLoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public String getFatLevelComment(Number bmi) {

        if (bmi.intValue() < 19) {
            return "Klen liten pojke";
        } else if (bmi.intValue() < 25) {
            return "Ooo, du verkar vara normal. Vafan behöver du fatman för?";
        } else if (bmi.intValue() < 26) {
            return "Du är nästan normalviktig, skippa nästa pizza så har du klarat viktmålet!";
        } else if (bmi.intValue() < 27) {
            return "Du är småtjock, några kilon kan du gott tappa.";
        } else if (bmi.intValue() < 28) {
            return "Du är överviktig. Ned i vikt om du inte vill vara en valross på stranden!";
        } else if (bmi.intValue() < 29) {
            return "Du är förmodligen tjock.";
        } else if (bmi.intValue() < 30) {
            return "Feta jävel. Dags att gå ned i vikt!";
        } else {
            return "Oooof, tjoooock!";
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        FatmanDbUser user = userRepository.findUserByUserName(userId);
        displayName = user.getFirstName() + " " + user.getLastName();
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setUserSettingsRepository(UserSettingsRepository userSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
    }

}
