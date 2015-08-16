package com.sundbybergsit.services;

import com.sundbybergsit.FatmanRepository;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Calendar;
import java.util.Date;

@Stateless
public class JpaUserStatisticsService implements UserStatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaUserStatisticsService.class);

    @Inject
    @FatmanRepository
    private EntityManager entityManager;

    @Override
    public int totalFatDiff(String username) {
        Validate.notNull(username);
        try {
            Float startFatPercentage = entityManager.createQuery("select distinct firstEntry.fatPercentage from PersonDataDbEntry firstEntry where firstEntry.fatmanDbUser.username = :username and " +
                    "firstEntry.date = (select min(firstEntry2.date) from PersonDataDbEntry firstEntry2 where firstEntry2.fatmanDbUser.username = :username)", Float.class)
                    .setParameter("username", username).getSingleResult();
            Float endFatPercentage = entityManager.createQuery("select distinct lastEntry.fatPercentage from PersonDataDbEntry lastEntry where lastEntry.fatmanDbUser.username = :username and " +
                    "lastEntry.date = (select max(lastEntry2.date) from PersonDataDbEntry lastEntry2 where lastEntry2.fatmanDbUser.username = :username)", Float.class)
                    .setParameter("username", username).getSingleResult();
            return calculateDiffInPercentages(startFatPercentage, endFatPercentage);
        } catch (NoResultException e) {
            return 0;
        }
    }

    int calculateDiffInPercentages(Float startFatPercentage, Float endFatPercentage) {
        Validate.isTrue(startFatPercentage > 0 && endFatPercentage > 0);
        float diffPercentage = endFatPercentage - startFatPercentage;
        return (int) (diffPercentage / startFatPercentage * 100);
    }

    @Override
    public int thisWeekFatDiff(String userId) {
        Validate.notNull(userId);
        try {
            return fatDiffSince(userId, lastWeek());
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public int thisMonthFatDiff(String userId) {
        Validate.notNull(userId);
        try {
            return fatDiffSince(userId, lastMonth());
        } catch (NoResultException e) {
            return 0;
        }
    }


    private int fatDiffSince(String userId, Date date) {
        Float startFatPercentage = entityManager.createQuery("select distinct firstEntry.fatPercentage from PersonDataDbEntry firstEntry where firstEntry.fatmanDbUser.username = :username and " +
                "firstEntry.date = (select min(firstEntry2.date) from PersonDataDbEntry firstEntry2 where firstEntry2.fatmanDbUser.username = :username and firstEntry2.date >= :date)", Float.class)
                .setParameter("username", userId)
                .setParameter("date", date)
                .getSingleResult();
        Float endFatPercentage = entityManager.createQuery("select distinct lastEntry.fatPercentage from PersonDataDbEntry lastEntry where lastEntry.fatmanDbUser.username = :username and " +
                "lastEntry.date = (select max(lastEntry2.date) from PersonDataDbEntry lastEntry2 where lastEntry2.fatmanDbUser.username = :username and lastEntry2.date >= :date)", Float.class)
                .setParameter("username", userId)
                .setParameter("date", date)
                .getSingleResult();
        return calculateDiffInPercentages(startFatPercentage, endFatPercentage);
    }


    private java.sql.Date lastMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        return new java.sql.Date(cal.getTime().getTime());
    }

    private java.sql.Date lastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        return new java.sql.Date(cal.getTime().getTime());
    }
}
