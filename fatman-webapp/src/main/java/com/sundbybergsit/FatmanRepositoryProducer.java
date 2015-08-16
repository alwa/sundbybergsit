package com.sundbybergsit;


import javax.ejb.Stateless;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class FatmanRepositoryProducer {

    @PersistenceContext(unitName = "fatmanPersistence")
    EntityManager em;

    @Produces
    @FatmanRepository
    public EntityManager retrieveEntityManager() {
        return em;
    }

    public void disposeEntityManager(@Disposes @FatmanRepository EntityManager em) {
    }
}
