package com.sundbybergsit.transformers;

import com.sundbybergsit.entities.PersonDataDbEntry;
import com.sundbybergsit.objects.FatmanUser;
import com.sundbybergsit.objects.PersonDataEntry;

public class PersonDataDbEntryToPersonDataEntryTransformer implements Transformer<PersonDataDbEntry, PersonDataEntry> {

    public PersonDataEntry transform(PersonDataDbEntry personDataDbEntry) {
        FatmanUser user = new FatmanDbUserToFatmanUserTransformer().transform(personDataDbEntry.getFatmanDbUser());
        return new PersonDataEntry(user, personDataDbEntry.getWeight(),
                personDataDbEntry.getFatPercentage(), personDataDbEntry.getWaterPercentage(),
                personDataDbEntry.getDate(), personDataDbEntry.getActivityLevel());
    }
}
