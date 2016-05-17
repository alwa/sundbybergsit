package com.sundbybergsit.fatman.jsf;

import com.sundbybergsit.entities.FatmanDbUser;
import com.sundbybergsit.entities.PersonDataDbEntry;
import com.sundbybergsit.entities.UserDbSettings;
import com.sundbybergsit.fatman.PersonDataDbEntryBuilder;
import com.sundbybergsit.services.*;
import org.junit.gen5.api.BeforeEach;
import org.junit.gen5.api.Disabled;
import org.junit.gen5.api.Test;
import org.junit.gen5.junit4.runner.JUnit5;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import javax.faces.context.FacesContext;
import java.sql.Date;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(JUnit5.class)
public class MultiUserHistoryBeanTest {

    private MultiUserHistoryBean bean;

    @Mock
    private UserStatisticsService userStatisticsService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserSettingsRepository userSettingsRepository;
    @Mock
    private UserDbSettings dummySettings;
    @Mock
    private FatmanDbUser dummyUser;
    @Mock
    private LineChartModel linearModel;
    @Mock
    private FatmanDataService fatmanDataService;
    @Mock
    private PersonDataDbEntryRepository personDataDbEntryRepository;
    @Captor
    private ArgumentCaptor<LineChartSeries> lineChartSeriesCaptor;
    @Mock
    private FatmanLoginBean loginBean;

    private Date today = new Date(new java.util.Date().getTime());

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        bean = spy(new MultiUserHistoryBean());
        bean.setUserStatisticsService(userStatisticsService);
        bean.setPersonDataDbEntryRepository(personDataDbEntryRepository);
        bean.setService(fatmanDataService);
        bean.setUserRepository(userRepository);
        bean.setUserSettingsRepository(userSettingsRepository);
        bean.setLoginBean(loginBean);
        when(userRepository.findUserByUserName("some user")).thenReturn(new FatmanDbUser("some user", 120, today, "a", "a"));
        when(userSettingsRepository.findSettingsForUser(anyString())).thenReturn(dummySettings);
        when(loginBean.getUserId()).thenReturn("some user");

        bean.setUserId("some user");
        doReturn(mock(FacesContext.class)).when(bean).getFacesContext();
    }

    @Test
    @Disabled("Obsolete behaviour")
    public void twentyEntriesInResultWhenRetrievingAllDataThatContainTwentyOneEntries() throws Exception {
        when(personDataDbEntryRepository.findAllEntries(anyString(), any(Date.class), any(Date.class))).thenReturn(createFakeEntries(21));

        bean.init();
        bean.setLinearModel(linearModel);
        bean.load();

        verify(linearModel, times(3)).addSeries(lineChartSeriesCaptor.capture());
        List<LineChartSeries> allSeries = lineChartSeriesCaptor.getAllValues();
        assertThat(allSeries.size(), is(3));
        assertThat(allSeries.get(0).getData().size(), is(20));
    }

    @Test
    @Disabled("Obsolete behaviour")
    public void twentyEntriesInResultWhenRetrievingAllDataThatContainFortyEntries() throws Exception {
        when(personDataDbEntryRepository.findAllEntries(anyString(), any(Date.class), any(Date.class))).thenReturn(createFakeEntries(40));

        bean.init();
        bean.setLinearModel(linearModel);
        bean.load();

        verify(linearModel, times(3)).addSeries(lineChartSeriesCaptor.capture());
        List<LineChartSeries> allSeries = lineChartSeriesCaptor.getAllValues();
        assertThat(allSeries.size(), is(3));
        assertThat(allSeries.get(0).getData().size(), is(20));
    }

    @Test
    public void createLinearModelMultiUsersAddSeries() throws Exception {
        bean.setFromDate(oneDayBefore(oneDayBefore(oneDayBefore(today))));
        bean.setToDate(today);
        when(personDataDbEntryRepository.findAllEntries("bla", oneDayBefore(oneDayBefore(oneDayBefore(today))), today)).thenReturn(
                Arrays.asList(
                        new PersonDataDbEntryBuilder().withDate(oneDayBefore(oneDayBefore(today))).build(),
                        new PersonDataDbEntryBuilder().withDate(oneDayBefore(today)).build()));
        when(personDataDbEntryRepository.findAllEntries("bla2", oneDayBefore(oneDayBefore(oneDayBefore(today))), today)).thenReturn(
                Arrays.asList(
                        new PersonDataDbEntryBuilder().withDate(oneDayBefore(oneDayBefore(today))).build()));
        when(userRepository.findUserByUserName("bla")).thenReturn(new FatmanDbUser("bla", 120, new Date(0), "a", "a"));
        when(userRepository.findUserByUserName("bla2")).thenReturn(new FatmanDbUser("bla2", 120, new Date(0), "a", "a"));
        bean.setSelectedUsers(Arrays.asList("bla", "bla2"));

        bean.init();
        bean.setLinearModel(linearModel);
        bean.load();

        verify(linearModel, times(2)).addSeries(lineChartSeriesCaptor.capture());
        List<LineChartSeries> allSeries = lineChartSeriesCaptor.getAllValues();
        assertThat(allSeries.size(), is(2));
        assertThat(allSeries.get(0).getData().size(), is(allSeries.get(1).getData().size()));
        assertThat(allSeries.get(0).getData().containsKey(oneDayBefore(oneDayBefore(today)).toString()), is(true));
        assertThat(allSeries.get(0).getData().containsKey(oneDayBefore(today).toString()), is(true));
        assertThat(allSeries.get(1).getData().containsKey(oneDayBefore(oneDayBefore(oneDayBefore(today))).toString()), is(true));
    }

    @Test
    public void createLinearModelMultiUsersMakesFakeEntriesWhenDataIsMissing() throws Exception {
        when(personDataDbEntryRepository.findAllEntries(anyString(), any(Date.class), any(Date.class))).thenReturn(Collections.<PersonDataDbEntry>emptyList());
        when(userRepository.findUserByUserName("bla")).thenReturn(new FatmanDbUser("bla", 120, new Date(0), "a", "a"));
        when(userRepository.findUserByUserName("bla2")).thenReturn(new FatmanDbUser("bla2", 120, oneDayBefore(today), "a", "a"));
        bean.setSelectedUsers(Arrays.asList("bla", "bla2"));
        bean.setFromDate(oneDayBefore(oneDayBefore(today)));
        bean.setToDate(today);

        bean.init();
        bean.setLinearModel(linearModel);
        bean.load();

        verify(linearModel, times(2)).addSeries(lineChartSeriesCaptor.capture());
        List<LineChartSeries> allSeries = lineChartSeriesCaptor.getAllValues();
        assertThat(allSeries.size(), is(2));
        assertThat(allSeries.get(0).getData().size(), is(allSeries.get(1).getData().size()));
    }

    private Date oneDayBefore(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date(date.getTime()));
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return new Date(cal.getTime().getTime());
    }

    private List<PersonDataDbEntry> createFakeEntries(int numberOfEntries) {
        List<PersonDataDbEntry> result = new ArrayList<>();
        for (int i = 0; i < numberOfEntries; i++) {
            result.add(stubPersonDataDbEntry(i));
        }
        return result;
    }

    private PersonDataDbEntry stubPersonDataDbEntry(int count) {
        return new PersonDataDbEntry(new FatmanDbUser("bla", 120, today, "a", "a"), 80f, 80f, 80f, new Date(2013, 1, count), 3);
    }
}
