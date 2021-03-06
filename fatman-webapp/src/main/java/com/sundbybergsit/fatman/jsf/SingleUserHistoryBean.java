package com.sundbybergsit.fatman.jsf;

import com.sundbybergsit.calculation.Calculator;
import com.sundbybergsit.entities.FatmanDbUser;
import com.sundbybergsit.entities.PersonDataDbEntry;
import com.sundbybergsit.entities.UserDbSettings;
import com.sundbybergsit.services.*;
import org.apache.commons.lang.Validate;
import org.primefaces.model.chart.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "singleUserHistoryBean")
public class SingleUserHistoryBean implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleUserHistoryBean.class);

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

    private Date fromDate = lastWeek();
    private Date toDate = new Date();

    private List<SelectItem> users;

    private LineChartModel linearModel;

    private String userId;

    @PostConstruct
    public void init() {
        LOGGER.info("init()");

        userId = loginBean.getUserId();
        Validate.notNull(userId, "userId must be set!");

        FatmanDbUser user = userRepository.findUserByUserName(userId);
        Validate.notNull(user, "user must be set!");

        displayName = user.getFirstName() + " " + user.getLastName();
        linearModel = getNewFatmanLineChartModel();
    }

    public void load() {
        LOGGER.info("load()");

        linearModel.clear();
        linearModel.getAxes().put(AxisType.Y, new LinearAxis());
        linearModel.getAxes().put(AxisType.Y2, getWeightAxis());
        linearModel.getAxes().put(AxisType.Y3, new LinearAxis());
        linearModel.getAxes().put(AxisType.Y4, new LinearAxis());

        LineChartSeries weakShameSeries = getLineChartSeries(AxisType.Y);
        LineChartSeries phantomShameSeries = getLineChartSeries(AxisType.Y);
        LineChartSeries lessShameSeries = getLineChartSeries(AxisType.Y);
        LineChartSeries muchShameSeries = getLineChartSeries(AxisType.Y);
        LineChartSeries weightSeries = getLineChartSeries(AxisType.Y2, "Vikt", "filledCircle");
        LineChartSeries fatSeries = getLineChartSeries(AxisType.Y3, "Fett", "filledDiamond");
        LineChartSeries waterSeries = getLineChartSeries(AxisType.Y4, "Vatten", "filledSquare");

        List<PersonDataDbEntry> entries = getPersonDataDbEntries();

        final LocalDate startDate = LocalDate.from(Instant.ofEpochMilli(fromDate.getTime()).atZone(ZoneId.systemDefault()));
        LocalDate endDate = LocalDate.from(Instant.ofEpochMilli(toDate.getTime()).atZone(ZoneId.systemDefault()));
        int count = 0;

        while (startDate.plusDays(count).isBefore(endDate.plusDays(1))) {
            PersonDataDbEntry dailyEntry = null;
            for (PersonDataDbEntry entry : entries) {
                LocalDate entryDate = entry.getDate().toLocalDate();

                if (ChronoUnit.DAYS.between(entryDate, startDate.plusDays(count)) < 1) {
                    dailyEntry = entry;
                    break;
                }
            }
            String thisDay = startDate.plusDays(count).toString();

            if (dailyEntry != null) {
                fatSeries.set(thisDay, dailyEntry.getFatPercentage());
                weightSeries.set(thisDay, dailyEntry.getWeight());
                waterSeries.set(thisDay, -dailyEntry.getWaterPercentage());
            }

            weakShameSeries.set(thisDay, 1);
            muchShameSeries.set(thisDay, 4);
            phantomShameSeries.set(thisDay, 2);
            lessShameSeries.set(thisDay, 3);
            count++;
        }

        addSeries(muchShameSeries, lessShameSeries, phantomShameSeries, weakShameSeries, weightSeries, fatSeries, waterSeries);

        linearModel.getAxes().put(AxisType.X, getXaxis());

        if (!userSettingsRepository.findSettingsForUser(userId).getShowDataToEveryone()) {
            showWarnMessage("Obs: Dina värden syns inte för någon annan användare");
        }
        showInfoMessage(createFatmanComment());
    }

    private LineChartSeries getLineChartSeries(AxisType axisType, String label, String markerStyle) {
        LineChartSeries series = new LineChartSeries();
        series.setYaxis(axisType);
        series.setLabel(label);
        series.setMarkerStyle(markerStyle);
        return series;
    }

    private LineChartSeries getLineChartSeries(AxisType axisType) {
        LineChartSeries series = new LineChartSeries();
        series.setYaxis(axisType);
        return series;
    }

    public List<SelectItem> getUsers() {
        LOGGER.info("getUsers()");

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

    public LineChartModel getLinearModel() {
        return linearModel;
    }

    public void setLinearModel(LineChartModel linearModel) {
        this.linearModel = linearModel;
    }

    /* To make this injection successful, the inject property (loginBean) must provide the setter method. */
    public void setLoginBean(FatmanLoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        FatmanDbUser user = userRepository.findUserByUserName(userId);
        displayName = user.getFirstName() + " " + user.getLastName();
        if (linearModel != null) {
            linearModel.setTitle(displayName);
        }
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

    private LineChartModel getNewFatmanLineChartModel() {
        LineChartModel model = new LineChartModel();
        model.setTitle(displayName);
        model.setSeriesColors(getSeriesColors());
        model.setExtender("customExtender");
        model.setAnimate(true);
        model.setShadow(true);
        model.setZoom(true);
        model.setShowDatatip(true);
        model.setLegendPosition("e");

        return model;
    }

    private void addSeries(LineChartSeries... allSeries) {
        for (LineChartSeries series : allSeries) {
            linearModel.addSeries(series);
        }
    }

    private DateAxis getXaxis() {
        DateAxis axis = new DateAxis("Datum");
        axis.setTickAngle(-50);

        axis.setMin(Instant.ofEpochMilli(fromDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate().toString());
        axis.setMax(Instant.ofEpochMilli(toDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate().toString());
        axis.setTickFormat("%F");
        axis.setTickInterval("1 day");
        return axis;
    }

    private LinearAxis getWeightAxis() {
        LinearAxis yAxis = new LinearAxis();
        yAxis.setLabel("Vikt (kg)");
        yAxis.setTickFormat("%d");
        FatmanDbUser user = userRepository.findUserByUserName(userId);
        Calculator<FatmanDbUser> weightFromBmiCalculator = (user1, bmi) ->
                bmi.intValue() * (((double) user1.getHeightInCentimetres() / 100) * ((double) user1.getHeightInCentimetres() / 100));
        Number minWeight = weightFromBmiCalculator.calculate(user, 15);
        Number maxWeight = weightFromBmiCalculator.calculate(user, 40);

        yAxis.setMin(minWeight);
        yAxis.setMax(maxWeight);
        yAxis.setTickCount(10);
        return yAxis;
    }

    private String getSeriesColors() {
        String lightBlue = "add8e6";
        String red = "FF0000";
        String yellow = "FFFF00";
        String blue = "0000FF";
        String white = "FFFFFF";
        String pink = "ffe4e1";
        String maroon = "b03060";
        return maroon + ", " + pink + ", " + white + ", " + lightBlue + ", " + red + "," + yellow + "," + blue;
    }

    private List<PersonDataDbEntry> getPersonDataDbEntries() {
        return personDataDbEntryRepository.findAllEntries(userId, fromDate, toDate);
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

    public String getDisplayName() {
        return displayName;
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

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setUserSettingsRepository(UserSettingsRepository userSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
    }

    public void setPersonDataDbEntryRepository(PersonDataDbEntryRepository personDataDbEntryRepository) {
        this.personDataDbEntryRepository = personDataDbEntryRepository;
    }

    public void setUserStatisticsService(UserStatisticsService userStatisticsService) {
        this.userStatisticsService = userStatisticsService;
    }
}
