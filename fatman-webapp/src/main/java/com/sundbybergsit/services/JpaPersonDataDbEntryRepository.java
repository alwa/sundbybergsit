package com.sundbybergsit.services;

import com.sundbybergsit.FatmanRepository;
import com.sundbybergsit.entities.PersonDataDbEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

@Stateless
public class JpaPersonDataDbEntryRepository implements PersonDataDbEntryRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaPersonDataDbEntryRepository.class);

    @Inject
    @FatmanRepository
    private EntityManager entityManager;

    @Override
    public List<PersonDataDbEntry> findAllEntries(String username) {
        return entityManager.createQuery("select dataEntry from PersonDataDbEntry dataEntry where " +
                "dataEntry.fatmanDbUser.username = :user order by dataEntry.date asc ", PersonDataDbEntry.class)
                .setParameter("user", username)
                .getResultList();
    }

    @Override
    public List<PersonDataDbEntry> findAllEntries(String username, Date from, Date to) {
        return entityManager.createQuery("select dataEntry from PersonDataDbEntry dataEntry where " +
                "dataEntry.fatmanDbUser.username = :user and dataEntry.date >= :from and dataEntry.date <= :to order by dataEntry.date asc ", PersonDataDbEntry.class)
                .setParameter("user", username)
                .setParameter("from", new java.sql.Date(from.getTime()))
                .setParameter("to", new java.sql.Date(to.getTime()))
                .getResultList();
    }

    private PersonDataDbEntry findEntry(String username, Date date) {
        try {
            return entityManager.createQuery("select distinct dataEntry from PersonDataDbEntry dataEntry where " +
                    "dataEntry.fatmanDbUser.username = :username and dataEntry.date = :myDate", PersonDataDbEntry.class)
                    .setParameter("username", username)
                    .setParameter("myDate", new java.sql.Date(date.getTime()))
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @TransactionAttribute
    public void save(PersonDataDbEntry data) {
        PersonDataDbEntry entry = findEntry(data.getFatmanDbUser().getUsername(), data.getDate());
        if (entry != null) {
            LOGGER.info("Replacing existing entry for user: {} and date: {}", data.getFatmanDbUser().getUsername(), data.getDate());
            entry.setWeight(data.getWeight());
            entry.setFatPercentage(data.getFatPercentage());
            entry.setWaterPercentage(data.getWaterPercentage());
            entry.setActivityLevel(data.getActivityLevel());
            entityManager.persist(entry);
        } else {
            LOGGER.info("Persisting new entry for user: {} and date: {}", data.getFatmanDbUser().getUsername(), data.getDate());
            entityManager.persist(data);
        }
    }

}
