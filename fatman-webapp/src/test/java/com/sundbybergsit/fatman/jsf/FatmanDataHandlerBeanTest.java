package com.sundbybergsit.fatman.jsf;

import com.sundbybergsit.entities.FatmanDbUser;
import com.sundbybergsit.entities.PersonDataDbEntry;
import com.sundbybergsit.entities.UserDbSettings;
import com.sundbybergsit.services.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;

import javax.faces.context.FacesContext;
import javax.transaction.SystemException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FatmanDataHandlerBeanTest {

    private FatmanDataHandlerBean handler;

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
    private CartesianChartModel linearModel;
    @Mock
    private FatmanDataService fatmanDataService;
    @Mock
    private PersonDataDbEntryRepository personDataDbEntryRepository;
    @Captor
    private ArgumentCaptor<LineChartSeries> lineChartSeriesCaptor;
    @Mock
    private FatmanLoginBean loginBean;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        handler = spy(new FatmanDataHandlerBean());
        handler.setUserStatisticsService(userStatisticsService);
        handler.setPersonDataDbEntryRepository(personDataDbEntryRepository);
        handler.setService(fatmanDataService);
        handler.setUserRepository(userRepository);
        handler.setUserSettingsRepository(userSettingsRepository);
        handler.setLoginBean(loginBean);
        when(userRepository.findUserByUserName("some user")).thenReturn(new FatmanDbUser("some user", 120, new Date(0), "a", "a"));
        when(userSettingsRepository.findSettingsForUser(anyString())).thenReturn(dummySettings);
        when(loginBean.getUserId()).thenReturn("some user");

        handler.setUserId("some user");
        handler.setLinearModel(linearModel);
        doReturn(mock(FacesContext.class)).when(handler).getFacesContext();
    }

    @Test
    public void twentyEntriesInResultWhenRetrievingAllDataThatContainTwentyOneEntries() throws Exception {
        when(personDataDbEntryRepository.findAllEntries(anyString(), any(Date.class), any(Date.class))).thenReturn(createFakeEntries(21));

        handler.createLinearModel();

        verify(linearModel, times(3)).addSeries(lineChartSeriesCaptor.capture());
        List<LineChartSeries> allSeries = lineChartSeriesCaptor.getAllValues();
        assertThat(allSeries.size(), is(3));
        assertThat(allSeries.get(0).getData().size(), is(20));
    }

    @Test
    public void twentyEntriesInResultWhenRetrievingAllDataThatContainFortyEntries() throws Exception {
        when(personDataDbEntryRepository.findAllEntries(anyString(), any(Date.class), any(Date.class))).thenReturn(createFakeEntries(40));

        handler.createLinearModel();

        verify(linearModel, times(3)).addSeries(lineChartSeriesCaptor.capture());
        List<LineChartSeries> allSeries = lineChartSeriesCaptor.getAllValues();
        assertThat(allSeries.size(), is(3));
        assertThat(allSeries.get(0).getData().size(), is(20));
    }

    @Test
    public void createWithRequiredFieldsPersists() throws SystemException {
        handler.setFatPercentage(50.0f);
        handler.setWeightInKilograms(80.0f);

        handler.create();

        verify(personDataDbEntryRepository).save(isA(PersonDataDbEntry.class));
    }

    @Test
    public void createWithoutRequiredFieldsDoesNotPersist() throws SystemException {
        handler.setFatPercentage(50.0f);

        handler.create();

        verifyZeroInteractions(personDataDbEntryRepository);
    }

    @Test
    public void createLinearModelMultiUsersMakesFakeEntriesWhenDataIsMissing() throws Exception {
        when(personDataDbEntryRepository.findAllEntries(anyString(), any(Date.class), any(Date.class))).thenReturn(Collections.<PersonDataDbEntry>emptyList());
        when(userRepository.findUserByUserName("bla")).thenReturn(new FatmanDbUser("bla", 120, new Date(0), "a", "a"));
        when(userRepository.findUserByUserName("bla2")).thenReturn(new FatmanDbUser("bla2", 120, new Date(0), "a", "a"));
        handler.setSelectedUsers(Arrays.asList("bla", "bla2"));

        handler.createLinearModelMultiUsers();

        verify(linearModel, times(2)).addSeries(lineChartSeriesCaptor.capture());
        List<LineChartSeries> allSeries = lineChartSeriesCaptor.getAllValues();
        assertThat(allSeries.size(), is(2));
        assertThat(allSeries.get(0).getData().size(), is(allSeries.get(1).getData().size()));
    }

    private List<PersonDataDbEntry> createFakeEntries(int numberOfEntries) {
        List<PersonDataDbEntry> result = new ArrayList<>();
        for (int i = 0; i < numberOfEntries; i++) {
            result.add(stubPersonDataDbEntry(i));
        }
        return result;
    }

    private PersonDataDbEntry stubPersonDataDbEntry(int count) {
        return new PersonDataDbEntry(new FatmanDbUser("bla", 120, new Date(0), "a", "a"), 80f, 80f, 80f, new Date(2013, 1, count), 3);
    }
}
