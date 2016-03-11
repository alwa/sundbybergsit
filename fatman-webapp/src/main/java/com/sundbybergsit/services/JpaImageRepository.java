package com.sundbybergsit.services;

import com.sundbybergsit.FatmanRepository;
import com.sundbybergsit.entities.FatmanDbUser;
import com.sundbybergsit.entities.PersonDataDbEntry;
import com.sundbybergsit.entities.UserDbSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Arrays;

@Stateless
public class JpaImageRepository implements ImageRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaImageRepository.class);

    @Inject
    @FatmanRepository
    private EntityManager entityManager;

    @Override
    @TransactionAttribute
    public void updatePhantomImage(String userId, byte[] imageContents) {
        LOGGER.info(String.format("updatePhantomImage(userId=[%s], imageContents=[%s])", userId, Arrays.toString(imageContents)));

        UserDbSettings settingsForUser = findSettingsForUser(userId);
        settingsForUser.setPhantomImage(imageContents);
        entityManager.persist(settingsForUser);
    }

    @Override
    @TransactionAttribute
    public void updateFatImage(String userId, byte[] imageContents) {
        LOGGER.info(String.format("updatePhantomImage(userId=[%s], imageContents=[%s])", userId, Arrays.toString(imageContents)));

        UserDbSettings settingsForUser = findSettingsForUser(userId);
        settingsForUser.setFatImage(imageContents);
        entityManager.persist(settingsForUser);
    }

    public UserDbSettings findSettingsForUser(String userId) {
        LOGGER.info("findSettingsForUser()");

        try {
            return entityManager.createQuery("select settings from UserDbSettings settings where settings.fatmanUser.username = :userId", UserDbSettings.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new IllegalStateException(String.format("No user settings for user: %s", userId));
        }
    }

}