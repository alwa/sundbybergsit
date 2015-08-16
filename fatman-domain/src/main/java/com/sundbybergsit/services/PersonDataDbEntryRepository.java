package com.sundbybergsit.services;

import com.sundbybergsit.entities.PersonDataDbEntry;

import java.util.List;

public interface PersonDataDbEntryRepository {

    List<PersonDataDbEntry> findAllEntries(String username);

    List<PersonDataDbEntry> findAllEntriesThisMonth(String username);

    List<PersonDataDbEntry> findAllEntriesThisWeek(String username);

    void save(PersonDataDbEntry data);
}
