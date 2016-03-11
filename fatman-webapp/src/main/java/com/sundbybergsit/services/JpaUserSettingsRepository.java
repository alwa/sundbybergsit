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
import javax.persistence.NoResultException;

@Stateless
public class JpaUserSettingsRepository implements UserSettingsRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaUserSettingsRepository.class);

    @Inject
    @FatmanRepository
    private EntityManager entityManager;

    @Override
    public UserDbSettings findSettingsForUser(String userId) {
        LOGGER.info("findSettingsForUser()");

        try {
            return entityManager.createQuery("select settings from UserDbSettings settings where settings.fatmanUser.username = :userId", UserDbSettings.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public UserDbSettings findSettingsForUser(long userId) {
        LOGGER.info("findSettingsForUser()");

        try {
            return entityManager.createQuery("select settings from UserDbSettings settings where settings.fatmanUser.id = :id", UserDbSettings.class)
                    .setParameter("id", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    @Override
    @TransactionAttribute
    public void update(UserDbSettings settings) {
        LOGGER.info(String.format("update(settings=[%s])", settings));

        entityManager.persist(settings);
    }

    @Override
    @TransactionAttribute
    public void showMyValuesToEveryone(String userId, boolean hide) {
        FatmanDbUser user = findUserByUserName(userId);
        UserDbSettings settings = findSettingsForUser(user.getId());
        settings.setShowDataToEveryone(hide);
        entityManager.persist(settings);
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
