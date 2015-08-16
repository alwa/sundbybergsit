package com.sundbybergsit.authentication.services;

import com.sundbybergsit.authentication.EntityRepository;
import com.sundbybergsit.authentication.entities.DbUser;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.*;
import java.util.logging.Logger;

@Stateless
public class AuthenticationService {

    @Inject
    @EntityRepository
    private EntityManager entityRepository;

    Logger logger = Logger.getLogger(AuthenticationService.class.getSimpleName());

    /**
     * @param username Username
     * @param password Password
     * @return the user or null
     */
    public DbUser findUser(String username, String password) {
        try {
            return (DbUser) entityRepository
                    .createQuery("select user from DbUser user where user.username = :username and user.password = :password")
                    .setParameter("username", username)
                    .setParameter("password", password).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @TransactionAttribute
    public void save(DbUser user) throws SystemException {
        logger.info("Persisting user: " + user.getUsername());
        entityRepository.persist(user);
    }

}
