package com.sundbybergsit.services;

import com.sundbybergsit.objects.PersonDataEntry;

import javax.transaction.SystemException;

public interface FatmanDataService {

    void save(PersonDataEntry personDataEntry) throws SystemException;
}
