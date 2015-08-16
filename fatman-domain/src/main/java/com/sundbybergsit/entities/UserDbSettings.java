package com.sundbybergsit.entities;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Arrays;

@Entity
@Table(name = "Settings", uniqueConstraints = @UniqueConstraint(name = "uq__settings__username", columnNames = "fatmanUser"))
public class UserDbSettings extends PersistedEntity {

    @OneToOne
    @Column(name = "fatmanUser", nullable = false)
    @JoinColumn(name = "fatmanUser")
    private FatmanDbUser fatmanUser;

    @Column(name = "scale", nullable = false)
    private Integer scale;

    @Min(0)
    @Max(100)
    @Column(name = "targetWeight", nullable = false)
    private Integer targetWeight;

    @Min(0)
    @Max(100)
    @Column(name = "targetFatPercentage", nullable = false)
    private Integer targetFatPercentage;

    @Min(0)
    @Max(100)
    @Column(name = "targetWaterPercentage", nullable = false)
    private Integer targetWaterPercentage;

    @Column(name = "showDataToEveryone", nullable = false)
    private Boolean showDataToEveryone;

    @Column(name = "fatImage",length = 8048, nullable = true)
    @Lob
    private byte[] fatImage;

    @Column(name = "phantomImage",length = 8048, nullable = true)
    @Lob
    private byte[] phantomImage;

    public UserDbSettings(FatmanDbUser fatmanUser) {
        this.fatmanUser = fatmanUser;
        this.scale = 0;
        this.targetWeight = 75;
        this.targetFatPercentage = 10;
        this.targetWaterPercentage = 70;
        this.showDataToEveryone = true;
    }

    public UserDbSettings() {
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public Integer getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(Integer targetWeight) {
        this.targetWeight = targetWeight;
    }

    public Integer getTargetFatPercentage() {
        return targetFatPercentage;
    }

    public void setTargetFatPercentage(Integer targetFatPercentage) {
        this.targetFatPercentage = targetFatPercentage;
    }

    public Integer getTargetWaterPercentage() {
        return targetWaterPercentage;
    }

    public void setTargetWaterPercentage(Integer targetWaterPercentage) {
        this.targetWaterPercentage = targetWaterPercentage;
    }

    public Boolean getShowDataToEveryone() {
        return showDataToEveryone;
    }

    public void setShowDataToEveryone(Boolean showDataToEveryone) {
        this.showDataToEveryone = showDataToEveryone;
    }

    public FatmanDbUser getFatmanUser() {
        return fatmanUser;
    }

    public void setFatmanUser(FatmanDbUser fatmanDbUser) {
        throw new UnsupportedOperationException("This operation is not supported!");
    }

    public void setFatImage(byte[] fatImage) {
        this.fatImage = Arrays.copyOf(fatImage, fatImage.length);
    }

    public void setPhantomImage(byte[] phantomImage) {
        this.phantomImage = Arrays.copyOf(phantomImage, phantomImage.length);
    }

    public byte[] getFatImage() {
        return fatImage;
    }

    public byte[] getPhantomImage() {
        return phantomImage;
    }
}
