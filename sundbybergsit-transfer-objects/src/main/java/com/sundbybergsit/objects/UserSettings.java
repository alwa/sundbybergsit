package com.sundbybergsit.objects;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserSettings", propOrder = {
        "fatmanUser",
        "scale",
        "targetWeight",
        "targetFatPercentage",
        "targetWaterPercentage"
})
public class UserSettings {
    @XmlElement
    private FatmanUser fatmanUser;
    @XmlAttribute
    private int scale;                  /* Scale of diagram on X-axis (lower value if diagram takes too much space) */
    @XmlAttribute
    private int targetWeight;            /* Target weight in kilograms (times 10) */
    @XmlAttribute
    private int targetFatPercentage;         /* Target fat percentage (times 10) */
    @XmlAttribute
    private int targetWaterPercentage;       /* Target water percentage (times 10) */

    @SuppressWarnings("unused") // Used by Jersey
    public UserSettings() {
    }

    public UserSettings(FatmanUser fatmanUser) {
        this.fatmanUser = fatmanUser;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(int targetWeight) {
        this.targetWeight = targetWeight;
    }

    public int getTargetFatPercentage() {
        return targetFatPercentage;
    }

    public void setTargetFatPercentage(int targetFatPercentage) {
        this.targetFatPercentage = targetFatPercentage;
    }

    public int getTargetWaterPercentage() {
        return targetWaterPercentage;
    }

    public void setTargetWaterPercentage(int targetWaterPercentage) {
        this.targetWaterPercentage = targetWaterPercentage;
    }

    public FatmanUser getFatmanUser() {
        return fatmanUser;
    }
}
