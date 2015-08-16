package com.sundbybergsit.fatman.jsf;

import com.sundbybergsit.calculation.Calculator;
import com.sundbybergsit.entities.FatmanDbUser;
import com.sundbybergsit.entities.PersonDataDbEntry;
import com.sundbybergsit.objects.TimePeriodType;
import com.sundbybergsit.services.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.*;

@RequestScoped
@ManagedBean(name = "fatmanDataHandlerBean")
public class FatmanDataHandlerBean {
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
    private java.util.Date date = new java.util.Date();
    @Max(value = 635, message = "Världens fetaste man vägde 635kg. Du kan inte vara fetare än så!")
    @Min(value = 40, message = "Väger du mindre än 40kg så är det bara att springa till BK direkt...")
    private Float weightInKilograms;
    private Float fatPercentage;
    private Float waterPercentage;

    private Date fromDate = lastWeek();

    private Date toDate = new Date();

    @Min(0)
    @Max(3)
    private Integer activityLevel;

    private MeterGaugeChartModel meterGaugeModel;
    private CartesianChartModel linearModel = new CartesianChartModel();
    private String userId;
    private List<String> selectedUsers;

    public void create() throws SystemException {
        try {
            userId = loginBean.getUserId();
            FatmanDbUser user = userRepository.findUserByUserName(userId);
            displayName = user.getFirstName() + " " + user.getLastName();
            PersonDataDbEntry data = new PersonDataDbEntry(user, weightInKilograms, fatPercentage,
                    waterPercentage, new java.sql.Date(date.getTime()), activityLevel);
            personDataDbEntryRepository.save(data);
            LOGGER.info("Persisting new data entry: {}", data);
            createMeterGaugeModel();
            showInfoMessage("Hohoo, värdet sparades ned i databasen!");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            showErrorMessage(e);
        }
    }

    public void createLinearModel() {
        linearModel.clear();
        LineChartSeries waterSeries = new LineChartSeries();
        waterSeries.setLabel("Vatten");
        LineChartSeries fatSeries = new LineChartSeries();
        fatSeries.setLabel("Fett");
        fatSeries.setMarkerStyle("diamond");

        LineChartSeries weightSeries = new LineChartSeries();
        weightSeries.setLabel("Vikt");

        List<PersonDataDbEntry> entries = getPersonDataDbEntries();
        for (PersonDataDbEntry entry : entries) {
            weightSeries.set(entry.getDate(), entry.getWeight());
            fatSeries.set(entry.getDate(), entry.getFatPercentage());
            waterSeries.set(entry.getDate(), entry.getWaterPercentage());
        }
        linearModel.addSeries(weightSeries);
        linearModel.addSeries(fatSeries);
        linearModel.addSeries(waterSeries);

        if (!userSettingsRepository.findSettingsForUser(userId).getShowDataToEveryone()) {
            showWarnMessage("Obs: Dina värden syns inte för någon annan användare");
        }
        showInfoMessage(createFatmanComment());
    }

    // Multiple charts
    public void createLinearModelMultiUsers() {
        linearModel.clear();

        for (String user : selectedUsers) {
            userId = user;
            FatmanDbUser fatmanUser = userRepository.findUserByUserName(userId);
            LineChartSeries fatSeries = new LineChartSeries();
            fatSeries.setLabel(fatmanUser.getFirstName());
            fatSeries.setMarkerStyle("diamond");

            List<PersonDataDbEntry> entries = new ArrayList<>(getPersonDataDbEntries());

            final Calendar count = Calendar.getInstance();
            count.setTime(fromDate);

            Calendar now = Calendar.getInstance();

            while (count.before(now)) {
                PersonDataDbEntry entry = (PersonDataDbEntry) CollectionUtils.find(entries, new Predicate() {
                    @Override
                    public boolean evaluate(Object o) {
                        PersonDataDbEntry entry = (PersonDataDbEntry) o;
                        return entry.getDate().before(count.getTime());
                    }
                });
                if (entry == null) {
                    fatSeries.set(count.getTime(), 99.9);
                } else {
                    entries.remove(entry);
                    fatSeries.set(entry.getDate(), entry.getFatPercentage());
                }
                count.add(Calendar.DAY_OF_MONTH, 1);
            }

            linearModel.addSeries(fatSeries);
        }

    }

    private List<PersonDataDbEntry> getPersonDataDbEntries() {
        List<PersonDataDbEntry> allEntries = personDataDbEntryRepository.findAllEntries(userId, fromDate, toDate);
        if (allEntries.size() > 20) {
            List<PersonDataDbEntry> interpolatedEntries = new ArrayList<>();
            int size = allEntries.size();
            interpolatedEntries.add(allEntries.get(0));
            for (int i = 1; i < 19; i++) {
                interpolatedEntries.add(allEntries.get(i * size / 20));
            }
            interpolatedEntries.add(allEntries.get(size - 1));
            return interpolatedEntries;
        }
        return allEntries;
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

    private void showErrorMessage(Exception e) {
        FacesContext facesContext = getFacesContext();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ooof, någonting gick fel", e.getMessage()));
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

    public CartesianChartModel getLinearModel() {
        return linearModel;
    }

    public void setLinearModel(CartesianChartModel linearModel) {
        this.linearModel = linearModel;
    }

    public void createMeterGaugeModel() {
        List<Number> intervals = new ArrayList<Number>() {{
            add(18.5);
            add(24.9);
            add(29.9);
            add(50);
        }};
        FatmanDbUser user = userRepository.findUserByUserName(userId);

        Calculator<FatmanDbUser> bmiCalculator = new Calculator<FatmanDbUser>() {
            @Override
            public Number calculate(FatmanDbUser user, Number weight) {
                return (float) weight / (((double) user.getHeightInCentimetres() / 100) * ((double) user.getHeightInCentimetres() / 100));
            }
        };
//        Calculator<FatmanDbUser> bmiCalculator =
//                (FatmanDbUser u, Number w) -> {
//                    double heightInMetres = (double) u.getHeightInCentimetres() / 100;
//                    return (float) w /
//                            (heightInMetres * heightInMetres);
//                };
        Number bmi = bmiCalculator.calculate(user, weightInKilograms);
        meterGaugeModel = new MeterGaugeChartModel(bmi, intervals);
    }

    /* BMI = Kroppsvikt /( Längden * Längden) = kg/m*m */
    Number calculateBmi() {

        FatmanDbUser user = userRepository.findUserByUserName(userId);
        double heightInMeters = (double) user.getHeightInCentimetres() / 100;
        return weightInKilograms /
                (heightInMeters * heightInMeters);
    }

    public MeterGaugeChartModel getMeterGaugeModel() {
        return meterGaugeModel;
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

    public String getFatLevelComment() {
        FatmanDbUser user = userRepository.findUserByUserName(userId);
        Calculator<FatmanDbUser> bmiCalculator = new Calculator<FatmanDbUser>() {
            @Override
            public Number calculate(FatmanDbUser user, Number weight) {
                return (float) weight / (((double) user.getHeightInCentimetres() / 100) * ((double) user.getHeightInCentimetres() / 100));
            }
        };
//        Calculator<FatmanDbUser> bmiCalculator =
//                (FatmanDbUser u, Number w) -> {
//                    double heightInMetres = (double) u.getHeightInCentimetres() / 100;
//                    return (float) w /
//                            (heightInMetres * heightInMetres);
//                };
        Number bmi = bmiCalculator.calculate(user, weightInKilograms);
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

    public String createFatmanComment() {
        if (userId == null) {
            return "";
        }
        try {
            int fatPercentageDiff = getFatDiff();
            return fatPercentageDiff > 0 ? displayName + " är fetare än när hen började. SKAM!" : displayName + " har tappat " + Math.abs(fatPercentageDiff) + "% fett totalt!";
        } catch (NoFatmanDataForUserException e) {
            return "Det finns ingenting att säga ännu eftersom det inte finns tillräckligt med data";
        }
    }

    private int getFatDiff() throws NoFatmanDataForUserException {
        return userStatisticsService.fatDiff(userId, fromDate, toDate);
    }

    public Date getFromDate() {
        return fromDate;
    }

    private Date lastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        return cal.getTime();
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public List<String> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<String> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setUserSettingsRepository(UserSettingsRepository userSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
    }
}
