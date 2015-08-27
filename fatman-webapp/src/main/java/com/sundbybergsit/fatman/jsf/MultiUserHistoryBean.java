package com.sundbybergsit.fatman.jsf;

import com.sundbybergsit.entities.FatmanDbUser;
import com.sundbybergsit.entities.PersonDataDbEntry;
import com.sundbybergsit.entities.UserDbSettings;
import com.sundbybergsit.services.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ManagedBean(name = "multiUserHistoryBean")
public class MultiUserHistoryBean implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiUserHistoryBean.class);

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

    private List<SelectItem> users;

    private Date fromDate = lastWeek();

    private Date toDate = new Date();

    private LineChartModel linearModel = new LineChartModel();
    private String userId;
    private List<String> selectedUsers = new ArrayList<>();

    @PostConstruct
    public void init() {
        LOGGER.info("init()");

        Validate.notNull(loginBean, "loginBean must be set!");

        userId = loginBean.getUserId();
        Validate.notNull(userId, "userId must be set!");

        FatmanDbUser user = userRepository.findUserByUserName(userId);
        Validate.notNull(user, "user must be set!");

        displayName = user.getFirstName() + " " + user.getLastName();
    }

    public void load() {
        linearModel.clear();

        for (String selectedUser : selectedUsers) {
            FatmanDbUser fatmanUser = userRepository.findUserByUserName(selectedUser);
            LineChartSeries fatSeries = new LineChartSeries();
            fatSeries.setLabel(fatmanUser.getFirstName());
            fatSeries.setMarkerStyle("diamond");

            List<PersonDataDbEntry> entries = new ArrayList<>(getPersonDataDbEntries());

            final Calendar count = Calendar.getInstance();
            count.setTime(fromDate);

            while (count.before(toDate)) {
                PersonDataDbEntry entry = (PersonDataDbEntry) CollectionUtils.find(entries, new Predicate() {
                    @Override
                    public boolean evaluate(Object o) {
                        PersonDataDbEntry entry = (PersonDataDbEntry) o;
                        return entry.getDate().before(count.getTime());
                    }
                });
                LocalDate localDate = Instant.ofEpochMilli(new Date(count.getTime().getTime()).getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                float fatPercentage = entry == null ? 0 : entry.getFatPercentage();
                fatSeries.set(localDate.toString(), fatPercentage);
                count.add(Calendar.DAY_OF_MONTH, 1);
            }

            linearModel.addSeries(fatSeries);
        }

        linearModel.getAxes().put(AxisType.Y, new LinearAxis());

        DateAxis xAxis = new DateAxis("Datum");
        xAxis.setTickCount(daysBetween(fromDate, toDate));
        xAxis.setTickAngle(-50);

        xAxis.setMin(Instant.ofEpochMilli(fromDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate().toString());
        xAxis.setMax(Instant.ofEpochMilli(toDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate().toString());
        xAxis.setTickFormat("%F");
        linearModel.getAxes().put(AxisType.X, xAxis);
    }

    public String getFullName(String userId) {
        FatmanDbUser user = userRepository.findUserByUserName(userId);
        return user.getFirstName() + " " + user.getLastName();
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

    private int daysBetween(Date fromDate, Date toDate) {
        long diff = toDate.getTime() - fromDate.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
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

    public String getDisplayName() {
        return displayName;
    }

    public LineChartModel getLinearModel() {
        return linearModel;
    }

    public void setLinearModel(LineChartModel linearModel) {
        this.linearModel = linearModel;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        FatmanDbUser user = userRepository.findUserByUserName(userId);
        displayName = user.getFirstName() + " " + user.getLastName();
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
