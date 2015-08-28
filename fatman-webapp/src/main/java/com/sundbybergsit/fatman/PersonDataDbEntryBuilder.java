package com.sundbybergsit.fatman;

import com.sundbybergsit.entities.FatmanDbUser;
import com.sundbybergsit.entities.PersonDataDbEntry;

import java.sql.Date;

public class PersonDataDbEntryBuilder {

    private FatmanDbUser user;
    private Float weight;
    private Float fat;
    private Float water;
    private Date date;
    private Integer activity;

    public PersonDataDbEntry build() {
        if (user == null) {
            user = new FatmanDbUser("bla", 180, new Date(0), "Mustaffa", "Ahmed");
        }
        if (weight == null) {
            weight = 80f;
        }
        if (fat == null) {
            fat = 17f;
        }
        if (water == null) {
            water = 65f;
        }
        if (date == null) {
            date = new Date(new java.util.Date().getTime());
        }
        if (activity == null) {
            activity = 2;
        }
        return new PersonDataDbEntry(user, weight, fat, water, date, activity);
    }

    public PersonDataDbEntryBuilder withUser(FatmanDbUser user) {
        this.user = user;
        return this;
    }

    public PersonDataDbEntryBuilder withWeight(float weight) {
        this.weight = weight;
        return this;
    }

    public PersonDataDbEntryBuilder withFat(float fat) {
        this.fat = fat;
        return this;
    }

    public PersonDataDbEntryBuilder withDate(Date date) {
        this.date = date;
        return this;
    }

    public PersonDataDbEntryBuilder withActivity(int activityLevel) {
        this.activity = activityLevel;
        return this;
    }

}
