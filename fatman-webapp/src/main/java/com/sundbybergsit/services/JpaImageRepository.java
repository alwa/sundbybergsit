package com.sundbybergsit.services;

import com.sundbybergsit.FatmanRepository;
import com.sundbybergsit.entities.FatmanDbUser;
import com.sundbybergsit.entities.UserDbSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

@Stateless
public class JpaImageRepository implements ImageRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaImageRepository.class);

    @Inject
    @FatmanRepository
    private EntityManager entityManager;

    @Inject
    private ImageRepository imageRepository;

    @Override
    @TransactionAttribute
    public void updatePhantomImage(String userId, byte[] phantomImage) {
        LOGGER.info("Updating phantom image for user: " + userId);
        FatmanDbUser user = findUserByUserName(userId);
        UserDbSettings settingsForUser = findSettingsForUser(user.getId());
        settingsForUser.setPhantomImage(phantomImage);
    }

    @Override
    @TransactionAttribute
    public void updateFatImage(String userId, byte[] fatImageContents) {
        LOGGER.info("Updating fat image for user: " + userId);
        FatmanDbUser user = findUserByUserName(userId);
        UserDbSettings settingsForUser = findSettingsForUser(user.getId());
        settingsForUser.setFatImage(fatImageContents);
    }

    private UserDbSettings findSettingsForUser(long userId) {
        try {
            return entityManager.createQuery("select settings from UserDbSettings settings where settings.fatmanUser.id = :id", UserDbSettings.class)
                    .setParameter("id", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private FatmanDbUser findUserByUserName(String username) {
        try {
            return entityManager.createQuery("select user from FatmanDbUser user where user.username = :userName", FatmanDbUser.class)
                    .setParameter("userName", username).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
