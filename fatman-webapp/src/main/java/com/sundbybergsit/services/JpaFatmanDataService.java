package com.sundbybergsit.services;

import com.sundbybergsit.FatmanRepository;
import com.sundbybergsit.entities.FatmanDbUser;
import com.sundbybergsit.entities.PersonDataDbEntry;
import com.sundbybergsit.objects.PersonDataEntry;
import com.sundbybergsit.transformers.PersonDataEntryToPersonDataDbEntryTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.SystemException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Stateless
public class JpaFatmanDataService implements FatmanDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaFatmanDataService.class);

    @Inject
    @FatmanRepository
    private EntityManager entityManager;

    @Override
    @TransactionAttribute
    public void save(PersonDataEntry personDataEntry) throws SystemException {
        PersonDataDbEntry entry = findEntry(personDataEntry.getFatmanUser().getUsername(), personDataEntry.getDate());
        if (entry != null) {
            entityManager.remove(entry);
        }
        try {
            FatmanDbUser fatmanUser = (FatmanDbUser) entityManager.createQuery("select distinct user from FatmanDbUser user where user.username = :userName")
                    .setParameter("userName", personDataEntry.getFatmanUser().getUsername()).getSingleResult();
            PersonDataDbEntry dbEntry = new PersonDataEntryToPersonDataDbEntryTransformer(fatmanUser).transform(personDataEntry);

            entityManager.persist(dbEntry);
        } catch (NoResultException e) {
            throw new IllegalArgumentException("The user: " + personDataEntry.getFatmanUser() + " does not exist!");
        }
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
}
