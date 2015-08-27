package com.sundbybergsit.fatman.jsf;

import com.sundbybergsit.entities.FatmanDbUser;
import com.sundbybergsit.entities.PersonDataDbEntry;
import com.sundbybergsit.entities.UserDbSettings;
import com.sundbybergsit.services.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.faces.context.FacesContext;
import javax.transaction.SystemException;
import java.sql.Date;

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
    private FatmanDataService fatmanDataService;
    @Mock
    private PersonDataDbEntryRepository personDataDbEntryRepository;
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
        doReturn(mock(FacesContext.class)).when(handler).getFacesContext();
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
}
