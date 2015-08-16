package com.sundbybergsit.authentication;

import javax.ejb.Stateless;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless (name = "entityRepositoryProducer")
public class EntityRepositoryProducer {

    @PersistenceContext(unitName = "authenticationPersistence")
    @EntityRepository
    private EntityManager em;

    @Produces
    @EntityRepository
    public EntityManager retrieveEntityManager() {
        return em;
    }

    public void disposeEntityManager(@Disposes @EntityRepository EntityManager em) {
    }
}