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
import java.util.Collections;
import java.util.List;

@Stateless
public class JpaUserRepository implements UserRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(JpaUserRepository.class);

    @Inject
    @FatmanRepository
    private EntityManager entityManager;

    @Inject
    private UserStatisticsService userStatisticsService;

    @Override
    public List<FatmanDbUser> findUsers() {
        try {
            return entityManager.createQuery("select users from FatmanDbUser users", FatmanDbUser.class).getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Override
    @TransactionAttribute
    public void saveNew(FatmanDbUser fatmanDbUser) {
        entityManager.persist(fatmanDbUser);
        entityManager.persist(new UserDbSettings(fatmanDbUser));
    }

    @Override
    @TransactionAttribute
    public void update(FatmanDbUser existingUser) {
        entityManager.persist(existingUser);
    }

    /**
     * @param id ID
     * @return the user or null
     */
    @Override
    public FatmanDbUser findUser(long id) {
        try {
            return entityManager.createQuery("select distinct user from FatmanDbUser user where user.id = :id", FatmanDbUser.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * @param username User name
     * @return the user or null
     */
    @Override
    public FatmanDbUser findUserByUserName(String username) {
        try {
            return entityManager.createQuery("select user from FatmanDbUser user where user.username = :userName", FatmanDbUser.class)
                    .setParameter("userName", username).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean existsUser(String userName) {
        int count = entityManager.createQuery("select count(user) from FatmanDbUser user where user.username = :userName", Long.class)
                .setParameter("userName", userName).getSingleResult().intValue();
        return count == 1;
    }

    @Override
    @TransactionAttribute
    public boolean delete(long id) {
        FatmanDbUser user = findUser(id);
        if (user != null) {
            entityManager.remove(user);
            return true;
        }
        return false;
    }

    @Override
    public List<FatmanDbUser> findUsersWithPublicData() {
        try {
            return entityManager.createQuery("select users from FatmanDbUser users, UserDbSettings settings where users.id = settings.fatmanUser.id and settings.showDataToEveryone = true", FatmanDbUser.class).getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public FatmanDbUser findMostPhantomThisWeek() {
        List<FatmanDbUser> users = findUsersWithPublicData();
        int fatDiff = 100;
        FatmanDbUser mostPhantom = null;
        for (FatmanDbUser user : users) {
            int currentFatDiff = userStatisticsService.thisWeekFatDiff(user.getUsername());
            if (currentFatDiff < fatDiff) {
                fatDiff = currentFatDiff;
                mostPhantom = user;
            }
        }
        return mostPhantom;
    }

    @Override
    public FatmanDbUser findFattestThisWeek() {
        List<FatmanDbUser> users = findUsersWithPublicData();
        int fatDiff = -100;
        FatmanDbUser fattest = null;
        for (FatmanDbUser user : users) {
            int currentFatDiff = userStatisticsService.thisWeekFatDiff(user.getUsername());
            if (currentFatDiff > fatDiff) {
                fatDiff = currentFatDiff;
                fattest = user;
            }
        }
        return fattest;
    }

}
