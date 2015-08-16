/*
*
*
*/
package com.sundbybergsit.applet;

import com.sundbybergsit.objects.FatmanUser;
import com.sundbybergsit.objects.PersonDataEntry;
import com.sundbybergsit.objects.TimePeriodType;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * FatPanel - En panel som kan rita diagram.
 *
 * @author Alix Warnke - KTH Datateknik
 * @version 2.0.2 22/8/2007
 */
public class FatPanel extends JPanel {

    private String fatmanUser;

    static final long serialVersionUID = 2;
    private LinkedList<Integer> weight = new LinkedList<>();
    private LinkedList<Integer> fat = new LinkedList<>();
    private LinkedList<Integer> water = new LinkedList<>();
    private final BodyDataBean dataBean;
    private FatGuy fatGuy;
    private SundbybergsITService fatmanDbService;
    private final FatmanApplet applet;

    /**
     * En anslutning till databasen, information om vad f�r tabell som ska skapas och dess m�lv�rden
     *
     * @param fatmanDbService where to retrieve domain objects
     * @param fatGuy          Referens till Fatman-figuren
     * @param fatmanUser      Anv�ndaren
     */
    public FatPanel(FatmanApplet applet, SundbybergsITService fatmanDbService, BodyDataBean dataBean, FatGuy fatGuy, String fatmanUser) {
        this.applet = applet;
        this.fatmanDbService = fatmanDbService;
        this.dataBean = dataBean;
        this.fatGuy = fatGuy;
        this.setBackground(Color.white);
        this.fatmanUser = fatmanUser;
        this.updateUI();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // paints background
        applet.repaint();
        dataBean.setLast(0);
        dataBean.setLastx(0);
        dataBean.setLasty(-500);
        g.setColor(new Color(176, 48, 96));
        int xalpha = 5;
        int yalpha = 30;
        for (int i = 0; i < dataBean.getCounter() * dataBean.getScale() / 5; i++)
            g.drawLine(xalpha + i * 5, yalpha + 20, xalpha - 1 + i * 5, yalpha + 20);
        g.drawString(Translator.localise("level.max"), dataBean.getCounter() * dataBean.getScale() + 30, yalpha + 25);
        g.setColor(Color.pink);
        for (int i = 0; i < dataBean.getCounter() * dataBean.getScale() / 5; i++)
            g.drawLine(xalpha + i * 5, yalpha + 70, xalpha - 1 + i * 5, yalpha + 70);
        g.drawString(Translator.localise("level.high"), dataBean.getCounter() * dataBean.getScale() + 30, yalpha + 75);
        g.setColor(Color.white);
        for (int i = 0; i < dataBean.getCounter() * dataBean.getScale() / 5; i++) //Fantomlinjen
        {
            g.drawLine(xalpha + i * 5, yalpha + 120, xalpha + 1 + i * 5, yalpha + 120);
            g.drawLine(xalpha + i * 5, yalpha + 121, xalpha + 1 + i * 5, yalpha + 121);
        }
        g.drawString(Translator.localise("level.normal"), dataBean.getCounter() * dataBean.getScale() + 30, yalpha + 125);
        g.setColor(new Color(178, 223, 238));
        for (int i = 0; i < dataBean.getCounter() * dataBean.getScale() / 5; i++)
            g.drawLine(xalpha + i * 5, yalpha + 170, xalpha - 1 + i * 5, yalpha + 170);
        g.drawString(Translator.localise("level.low"), dataBean.getCounter() * dataBean.getScale() + 30, yalpha + 175);
        g.setColor(Color.black);
        if (dataBean.getTypeScale() == TimePeriodType.THIS_MONTH) {
            g.drawString(Translator.localise("time.thisMonth"), this.getWidth() / 4, 20);
        } else if (dataBean.getTypeScale() == TimePeriodType.THIS_WEEK) {
            g.drawString(Translator.localise("time.thisWeek"), this.getWidth() / 4, 20);
        } else if (dataBean.getTypeScale() == TimePeriodType.ALL) {
            g.drawString(Translator.localise("time.all"), this.getWidth() / 4, 20);
        }
        g.drawString(Translator.localise("weight.goal") + ": " + String.valueOf(dataBean.getWeightTarget() / 10) + "kg", this.getWidth() - 90, 30);
        g.drawString(Translator.localise("fat.goal") + ": " + String.valueOf((float) dataBean.getFatPercentageTarget() / 10) + "%", this.getWidth() - 90, 45);

        g.drawLine(xalpha, yalpha, xalpha, yalpha + 180);
        for (int i = 0; i < 19; i++) {
            g.drawLine(xalpha, yalpha + i * 10, xalpha + 2, yalpha + i * 10);
        }
        for (int i = 1; i < dataBean.getCounter(); i++) {
            g.drawLine(xalpha, yalpha + 180, xalpha + i * dataBean.getScale(), yalpha + 180);
            g.drawLine(xalpha + i * dataBean.getScale(), yalpha + 178, xalpha + i * dataBean.getScale(), yalpha + 182);
        }
        g.setColor(Color.red);
        dataBean.setWsize(weight.size());
        dataBean.setWcounter(0);
        while (dataBean.getWcounter() < dataBean.getWsize()) {
            dataBean.setLast((weight.get(dataBean.getWcounter())));
            dataBean.setLast(dataBean.getLast() - dataBean.getWeightTarget());
            dataBean.setLast(-dataBean.getLast());
            if (dataBean.getLasty() == -500)
                dataBean.setLasty(dataBean.getLast());
            g.drawLine(xalpha + dataBean.getLastx(), yalpha + 119 + dataBean.getLasty(), xalpha + dataBean.getLastx() + dataBean.getScale(), yalpha + 119 + dataBean.getLast());
            g.drawLine(xalpha + dataBean.getLastx(), yalpha + 120 + dataBean.getLasty(), xalpha + dataBean.getLastx() + dataBean.getScale(), yalpha + 120 + dataBean.getLast());
            g.drawLine(xalpha + dataBean.getLastx(), yalpha + 121 + dataBean.getLasty(), xalpha + dataBean.getLastx() + dataBean.getScale(), yalpha + 121 + dataBean.getLast());
            dataBean.setLasty(dataBean.getLast());
            dataBean.setLastx(dataBean.getLastx() + dataBean.getScale());
            dataBean.setWcounter(dataBean.getWcounter() + 1);
        }
        if (dataBean.getLast() > 50) {
            g.drawString(Translator.localise("weight") + ": " + Translator.localise("weight.min"), this.getWidth() - 90, this.getHeight() - 40);
            dataBean.setWeightlevel(0);
        } else if (dataBean.getLast() + 100 <= 0) {
            g.drawString(Translator.localise("weight") + " : " + Translator.localise("weight.max"), this.getWidth() - 90, this.getHeight() - 40);
            dataBean.setWeightlevel(4);
        } else if (dataBean.getLast() + 50 <= 0) {
            g.drawString(Translator.localise("weight") + ": " + Translator.localise("weight.veryhigh"), this.getWidth() - 90, this.getHeight() - 40);
            dataBean.setWeightlevel(3);
        } else if (dataBean.getLast() < 0) {
            g.drawString(Translator.localise("weight") + ": " + Translator.localise("weight.high"), this.getWidth() - 90, this.getHeight() - 40);
            dataBean.setWeightlevel(2);
        } else {
            g.drawString(Translator.localise("weight") + ": " + Translator.localise("weight.normal"), this.getWidth() - 90, this.getHeight() - 40);
            dataBean.setWeightlevel(1);
        }
        g.setColor(Color.yellow);
        dataBean.setLastx(0);
        dataBean.setLasty(-500);
        dataBean.setFsize(fat.size());
        dataBean.setFcounter(0);
        while (dataBean.getFcounter() < dataBean.getFsize()) {
            dataBean.setLast(fat.get(dataBean.getFcounter()));
            dataBean.setLast(dataBean.getLast() - dataBean.getFatPercentageTarget());
            dataBean.setLast(-dataBean.getLast());
            if (dataBean.getLasty() == -500) {
                dataBean.setLasty(dataBean.getLast());
            }
            g.drawLine(xalpha + dataBean.getLastx(), yalpha + 119 + dataBean.getLasty(), xalpha + dataBean.getLastx() + dataBean.getScale(), yalpha + 119 + dataBean.getLast());
            g.drawLine(xalpha + dataBean.getLastx(), yalpha + 120 + dataBean.getLasty(), xalpha + dataBean.getLastx() + dataBean.getScale(), yalpha + 120 + dataBean.getLast());
            g.drawLine(xalpha + dataBean.getLastx(), yalpha + 121 + dataBean.getLasty(), xalpha + dataBean.getLastx() + dataBean.getScale(), yalpha + 121 + dataBean.getLast());
            dataBean.setLasty(dataBean.getLast());
            dataBean.setLastx(dataBean.getLastx() + dataBean.getScale());
            dataBean.setFcounter(dataBean.getFcounter() + 1);
        }
        if (dataBean.getLast() > 50) {
            g.drawString(Translator.localise("fat") + ": " + Translator.localise("fat.min"), this.getWidth() - 90, this.getHeight() - 25);
            dataBean.setFatlevel(0);
        } else if (dataBean.getLast() + 100 <= 0) {
            g.drawString(Translator.localise("fat") + ": " + Translator.localise("fat.max"), this.getWidth() - 90, this.getHeight() - 25);
            dataBean.setFatlevel(4);
        } else if (dataBean.getLast() + 50 <= 0) {
            g.drawString(Translator.localise("fat") + ": " + Translator.localise("fat.veryhigh"), this.getWidth() - 90, this.getHeight() - 25);
            dataBean.setFatlevel(3);
        } else if (dataBean.getLast() < 0) {
            g.drawString(Translator.localise("fat") + ": " + Translator.localise("fat.high"), this.getWidth() - 90, this.getHeight() - 25);
            dataBean.setFatlevel(2);
        } else {
            g.drawString(Translator.localise("fat") + ": " + Translator.localise("fat.normal"), this.getWidth() - 90, this.getHeight() - 25);
            dataBean.setFatlevel(1);
        }
        g.setColor(Color.BLUE);
        dataBean.setLastx(0);
        dataBean.setLasty(-500);
        dataBean.setWasize(water.size());
        dataBean.setWacounter(0);
        while (dataBean.getWacounter() < dataBean.getWasize()) {
            dataBean.setLast(water.get(dataBean.getWacounter()));
            dataBean.setLast(dataBean.getLast() - dataBean.getWaterPercentageTarget());
            dataBean.setLast(-dataBean.getLast());
            if (dataBean.getLasty() == -500) {
                dataBean.setLasty(dataBean.getLast());
            }
            g.drawLine(xalpha + dataBean.getLastx(), yalpha + 119 + dataBean.getLasty(), xalpha + dataBean.getLastx() + dataBean.getScale(), yalpha + 119 + dataBean.getLast());
            g.drawLine(xalpha + dataBean.getLastx(), yalpha + 120 + dataBean.getLasty(), xalpha + dataBean.getLastx() + dataBean.getScale(), yalpha + 120 + dataBean.getLast());
            g.drawLine(xalpha + dataBean.getLastx(), yalpha + 121 + dataBean.getLasty(), xalpha + dataBean.getLastx() + dataBean.getScale(), yalpha + 121 + dataBean.getLast());
            dataBean.setLasty(dataBean.getLast());
            dataBean.setLastx(dataBean.getLastx() + dataBean.getScale());
            dataBean.setWacounter(dataBean.getWacounter() + 1);
        }
        if (dataBean.getLast() > 50) {
            g.drawString(Translator.localise("water") + ": " + Translator.localise("water.min"), this.getWidth() - 90, this.getHeight() - 10);
            dataBean.setWaterlevel(0);
        } else if (dataBean.getLast() + 50 <= 0) {
            g.drawString(Translator.localise("water") + ": " + Translator.localise("water.normal"), this.getWidth() - 90, this.getHeight() - 10);
            dataBean.setWaterlevel(3);
        } else if (dataBean.getLast() < 0) {
            g.drawString(Translator.localise("water") + ": " + Translator.localise("fat.low"), this.getWidth() - 90, this.getHeight() - 10);
            dataBean.setWaterlevel(2);
        } else {
            g.drawString(Translator.localise("water") + ": " + Translator.localise("fat.verylow"), this.getWidth() - 90, this.getHeight() - 10);
            dataBean.setWaterlevel(1);
        }
        fatGuy.drawFatman(this, dataBean.getWeightlevel(), dataBean.getFatlevel(), dataBean.getWaterlevel(), g);
    }

    /**
     * Uppdaterar inneh�llet p� tabellen som skall ritas ut av paintComponent()
     */
    public void update() {
        dataBean.setCounter(1);
        weight.clear();
        fat.clear();
        water.clear();

        java.util.List<PersonDataEntry> resultSet;

        resultSet = fatmanDbService.getPersonDataEntries(fatmanUser, dataBean.getTypeScale());
        for (PersonDataEntry entry : resultSet) {
            weight.addLast((int) entry.getWeight());
            fat.addLast((int) entry.getFatPercentage());
            water.addLast((int) entry.getWaterPercentage());
            dataBean.setCounter(dataBean.getCounter() + 1);
        }
        revalidate();
    }
}
