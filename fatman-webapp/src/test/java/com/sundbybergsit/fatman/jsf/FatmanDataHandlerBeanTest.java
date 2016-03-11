package com.sundbybergsit.fatman.jsf;

import com.sundbybergsit.entities.FatmanDbUser;
import com.sundbybergsit.entities.PersonDataDbEntry;
import com.sundbybergsit.entities.UserDbSettings;
import com.sundbybergsit.services.*;
import org.junit.gen5.api.BeforeEach;
import org.junit.gen5.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.primefaces.model.chart.MeterGaugeChartModel;

import javax.faces.context.FacesContext;
import javax.transaction.SystemException;
import java.sql.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

class FatmanDataHandlerBeanTest {

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
    private FatmanDataService fatmanDataService;
    @Mock
    private PersonDataDbEntryRepository personDataDbEntryRepository;
    @Mock
    private FatmanLoginBean loginBean;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        handler = spy(new FatmanDataHandlerBean());
        handler.setUserStatisticsService(userStatisticsService);
        handler.setPersonDataDbEntryRepository(personDataDbEntryRepository);
        handler.setService(fatmanDataService);
        handler.setUserRepository(userRepository);
        handler.setUserSettingsRepository(userSettingsRepository);
        handler.setLoginBean(loginBean);

        when(userRepository.findUserByUserName("some user")).thenReturn(new FatmanDbUser("some user", 180, new Date(0), "a", "a"));
        when(userSettingsRepository.findSettingsForUser(anyString())).thenReturn(dummySettings);
        when(loginBean.getUserId()).thenReturn("some user");

        handler.setUserId("some user");
        doReturn(mock(FacesContext.class)).when(handler).getFacesContext();
    }

    @Test
    public void createWithRequiredFieldsInitializesMeterGaugeModelRight() throws SystemException {
        handler.setFatPercentage(50.0f);
        handler.setWeightInKilograms(80.0f);

        handler.create();

        MeterGaugeChartModel meterGaugeModel = handler.getMeterGaugeModel();
        assertThat(meterGaugeModel, notNullValue());
        assertThat(meterGaugeModel.getValue().intValue(), is(24));
        assertThat(meterGaugeModel.getGaugeLabel(), is("BMI"));
    }


    @Test
    public void createWithRequiredFieldsPersists() throws SystemException {
        handler.setFatPercentage(50.0f);
        handler.setWeightInKilograms(80.0f);

        handler.create();

        verify(personDataDbEntryRepository).save(isA(PersonDataDbEntry.class));
        verify(handler, times(0)).showErrorMessage(any(Exception.class));
    }

    @Test
    public void createWithoutRequiredFieldsDoesNotPersist() throws SystemException {
        handler.setFatPercentage(50.0f);

        handler.create();

        verifyZeroInteractions(personDataDbEntryRepository);
        verify(handler, times(1)).showErrorMessage(any(Exception.class));
    }

}
