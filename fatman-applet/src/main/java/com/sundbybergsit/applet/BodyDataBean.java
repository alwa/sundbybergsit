package com.sundbybergsit.applet;

import com.sundbybergsit.objects.TimePeriodType;

import java.util.List;


/**
 * JavaBean holding user body data in runtime
 *
 * @author alwa
 */
public class BodyDataBean {
    private TimePeriodType timePeriodType;
    private int fatlevel, weightlevel, waterlevel, activitylevel;
    private int counter;
    private int last, lastx, lasty;
    private int wcounter, fcounter, wacounter;
    private int wsize, fsize, wasize;
    private int weightTarget, fatPercentageTarget, waterPercentageTarget;
    private int scale;

    public BodyDataBean() {
    }

    public int getFatlevel() {
        return fatlevel;
    }

    public void setFatlevel(int fatlevel) {
        this.fatlevel = fatlevel;
    }

    public int getWeightlevel() {
        return weightlevel;
    }

    public void setWeightlevel(int weightlevel) {
        this.weightlevel = weightlevel;
    }

    public int getWaterlevel() {
        return waterlevel;
    }

    public void setWaterlevel(int waterlevel) {
        this.waterlevel = waterlevel;
    }

    public TimePeriodType getTypeScale() {
        return timePeriodType;
    }

    public void setTypeScale(TimePeriodType timePeriodType) {
        this.timePeriodType = timePeriodType;
    }


    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public int getLastx() {
        return lastx;
    }

    public void setLastx(int lastx) {
        this.lastx = lastx;
    }

    public int getLasty() {
        return lasty;
    }

    public void setLasty(int lasty) {
        this.lasty = lasty;
    }

    public int getWcounter() {
        return wcounter;
    }

    public void setWcounter(int wcounter) {
        this.wcounter = wcounter;
    }

    public int getFcounter() {
        return fcounter;
    }

    public void setFcounter(int fcounter) {
        this.fcounter = fcounter;
    }

    public int getWacounter() {
        return wacounter;
    }

    public void setWacounter(int wacounter) {
        this.wacounter = wacounter;
    }

    public int getWsize() {
        return wsize;
    }

    public void setWsize(int wsize) {
        this.wsize = wsize;
    }

    public int getFsize() {
        return fsize;
    }

    public void setFsize(int fsize) {
        this.fsize = fsize;
    }

    public int getWasize() {
        return wasize;
    }

    public void setWasize(int wasize) {
        this.wasize = wasize;
    }

    public int getActivitylevel() {
        return activitylevel;
    }

    public void setActivitylevel(int activitylevel) {
        this.activitylevel = activitylevel;
    }

    public void setWeightTarget(int weightTarget) {
        this.weightTarget = weightTarget;
    }

    public void setFatPercentageTarget(int fatPercentageTarget) {
        this.fatPercentageTarget = fatPercentageTarget;
    }

    public void setWaterPercentageTarget(int waterPercentageTarget) {
        this.waterPercentageTarget = waterPercentageTarget;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getScale() {
        return scale;
    }

    public int getWeightTarget() {
        return weightTarget;
    }

    public int getFatPercentageTarget() {
        return fatPercentageTarget;
    }

    public int getWaterPercentageTarget() {
        return waterPercentageTarget;
    }
}
