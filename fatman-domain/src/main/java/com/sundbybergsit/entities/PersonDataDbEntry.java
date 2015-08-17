package com.sundbybergsit.entities;

import org.apache.commons.lang.Validate;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static javax.persistence.TemporalType.DATE;

@Entity
@Table(name = "PersonDataEntries", uniqueConstraints = @UniqueConstraint(name = "uq__person_data_entries__username", columnNames = {"fatmanUser", "date"}))
public class PersonDataDbEntry extends PersistedEntity implements Comparable {

    public PersonDataDbEntry() {
    }

    @ManyToOne
    @Column(name = "fatmanUser", nullable = false)
    private FatmanDbUser fatmanDbUser;

    @Min(30)
    @Max(600)
    @Column(name = "weight", nullable = false)
    private Float weight;

    @Min(1)
    @Max(100)
    @Column(name = "fatPercentage", nullable = false)
    private Float fatPercentage;

    @Min(0)
    @Max(100)
    @Column(name = "waterPercentage", nullable = false)
    private Float waterPercentage;

    @NotNull
    @Temporal(DATE)
    @Column(name = "date", nullable = false)
    private java.sql.Date date;

    @Min(0)
    @Max(3)
    @Column(name = "activityLevel", nullable = false)
    private Integer activityLevel;

    public PersonDataDbEntry(FatmanDbUser fatmanDbUser, float weightInKilograms, float fatPercentage, float waterPercentage, java.sql.Date date, int activityLevel) {
        Validate.notNull(fatmanDbUser);
        this.fatmanDbUser = fatmanDbUser;
        this.weight = weightInKilograms;
        this.fatPercentage = fatPercentage;
        this.waterPercentage = waterPercentage;
        this.activityLevel = activityLevel;
        this.date = date;
    }

    public Float getWeight() {
        return weight;
    }

    public Float getFatPercentage() {
        return fatPercentage;
    }

    public Float getWaterPercentage() {
        return waterPercentage;
    }

    public FatmanDbUser getFatmanDbUser() {
        return fatmanDbUser;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public Integer getActivityLevel() {
        return activityLevel;
    }

    @Override
    public int compareTo(Object object) {
        PersonDataDbEntry otherPersonDateDbEntry = (PersonDataDbEntry) object;
        return this.date.compareTo(otherPersonDateDbEntry.getDate());
    }

    public void setFatmanDbUser(FatmanDbUser fatmanDbUser) {
        throw new UnsupportedOperationException("This operation is not supported!");
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public void setFatPercentage(Float fatPercentage) {
        this.fatPercentage = fatPercentage;
    }

    public void setWaterPercentage(Float waterPercentage) {
        this.waterPercentage = waterPercentage;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }

    public void setActivityLevel(Integer activityLevel) {
        this.activityLevel = activityLevel;
    }

    @Override
    public String toString() {
        return "PersonDataDbEntry{" +
                "fatmanDbUser=" + fatmanDbUser +
                ", weight=" + weight +
                ", fatPercentage=" + fatPercentage +
                ", waterPercentage=" + waterPercentage +
                ", date=" + date +
                ", activityLevel=" + activityLevel +
                '}';
    }
}
