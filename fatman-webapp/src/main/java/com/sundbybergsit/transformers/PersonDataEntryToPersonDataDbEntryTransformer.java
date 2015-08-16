package com.sundbybergsit.transformers;

import com.sundbybergsit.entities.FatmanDbUser;
import com.sundbybergsit.entities.PersonDataDbEntry;
import com.sundbybergsit.objects.PersonDataEntry;

import javax.persistence.EntityManager;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Z
 * Date: 2010-apr-22
 * Time: 22:36:51
 * To change this template use File | Settings | File Templates.
 */
public class PersonDataEntryToPersonDataDbEntryTransformer implements Transformer<PersonDataEntry, PersonDataDbEntry>{
    private final FatmanDbUser user;

    public PersonDataEntryToPersonDataDbEntryTransformer(FatmanDbUser user) {
        this.user = user;
    }

    public PersonDataDbEntry transform(PersonDataEntry objectToTransform) {
        java.sql.Date date = new java.sql.Date(objectToTransform.getDate().getTime());
        int activityLevel = objectToTransform.getActivityLevel();
        float fatPercentage = objectToTransform.getFatPercentage();
        float waterPercentage = objectToTransform.getWaterPercentage();
        float weight = objectToTransform.getWeight();
        return new PersonDataDbEntry(user, weight, fatPercentage, waterPercentage, date, activityLevel);
    }
}
