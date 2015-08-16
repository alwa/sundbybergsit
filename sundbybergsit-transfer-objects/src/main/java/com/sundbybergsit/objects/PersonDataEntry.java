package com.sundbybergsit.objects;

import javax.xml.bind.annotation.*;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
/**
 * A biometric entry for a person on a specific date
 */
@XmlType(name = "FatmanUser", propOrder = {
        "user",
        "weight",
        "fatPercentage",
        "waterPercentage",
        "date",
        "activityLevel"
})
public class PersonDataEntry {
    @XmlElement
    private FatmanUser user;
    @XmlAttribute
    private float weight;
    @XmlAttribute
    private float fatPercentage;
    @XmlAttribute
    private float waterPercentage;
    @XmlAttribute
    private Date date;
    @XmlAttribute
    private int activityLevel;

    @SuppressWarnings("unused") // Used by Jersey
    public PersonDataEntry() {
    }

    public PersonDataEntry(FatmanUser fatmanUser, float weightInKilograms, float fatPercentage, float waterPercentage, Date date, int activityLevel) {
        this.user = fatmanUser;
        this.weight = weightInKilograms;
        this.fatPercentage = fatPercentage;
        this.waterPercentage = waterPercentage;
        this.activityLevel = activityLevel;
        this.date = date;
    }

    public float getWeight() {
        return weight;
    }

    public float getFatPercentage() {
        return fatPercentage;
    }

    public float getWaterPercentage() {
        return waterPercentage;
    }

    public FatmanUser getFatmanUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }

    public int getActivityLevel() {
        return activityLevel;
    }

    @Override
    public String toString() {
        return "PersonDataEntry{" +
                "user=" + user +
                ", weight=" + weight +
                ", fatPercentage=" + fatPercentage +
                ", waterPercentage=" + waterPercentage +
                ", date=" + date +
                ", activityLevel=" + activityLevel +
                '}';
    }
}
