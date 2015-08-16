package com.sundbybergsit.services;

import com.sundbybergsit.entities.PersonDataDbEntry;

import java.util.Date;
import java.util.List;

public interface PersonDataDbEntryRepository {

    List<PersonDataDbEntry> findAllEntries(String username);

    List<PersonDataDbEntry> findAllEntries(String username, Date from, Date to);

    void save(PersonDataDbEntry data);
}
