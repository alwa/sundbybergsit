package com.sundbybergsit.applet;

import javax.swing.*;
import java.awt.*;

/**
 * En grafisk representation av anv�ndaren
 */
public class FatGuy {

    /**
     * Ritar upp en figur baserad p� bifogade v�rden
     *
     * @param panelToDrawAt - panelen d�r figuren ritas
     * @param weightlevel   - viktniv�n
     * @param fatlevel      - fettniv�n
     * @param waterlevel    - vattenniv�n
     * @param g             - objektet som m�jligg�r utritningen
     */
    public void drawFatman(JPanel panelToDrawAt, float weightlevel, float fatlevel, float waterlevel, Graphics g) {
        //Figur som representerar anv�ndaren
        g.setColor(Color.black);
        float fatness = 5 * fatlevel - weightlevel * waterlevel + 5;
        g.fillOval(panelToDrawAt.getWidth() - 65, panelToDrawAt.getHeight() - 165, 16, 20); //huvud
        g.drawLine(panelToDrawAt.getWidth() - 57, panelToDrawAt.getHeight() - 147, panelToDrawAt.getWidth() - 57, panelToDrawAt.getHeight() - 135); //hals
        g.fillOval(panelToDrawAt.getWidth() - 80, panelToDrawAt.getHeight() - 139, 23, (int) (2 * waterlevel + 3)); //v�nster �verarm

        if (waterlevel < 3) {
            g.drawLine(panelToDrawAt.getWidth() - 80, panelToDrawAt.getHeight() - 137, panelToDrawAt.getWidth() - 76, panelToDrawAt.getHeight() - 125); //v�nster underarm
            g.fillOval(panelToDrawAt.getWidth() - 77, panelToDrawAt.getHeight() - 125, 7, 7); //v�nster hand
            g.drawLine(panelToDrawAt.getWidth() - 34, panelToDrawAt.getHeight() - 137, panelToDrawAt.getWidth() - 38, panelToDrawAt.getHeight() - 125); //h�ger underarm
            g.fillOval(panelToDrawAt.getWidth() - 43, panelToDrawAt.getHeight() - 125, 7, 7); //h�ger hand
        } else {
            g.drawLine(panelToDrawAt.getWidth() - 80, panelToDrawAt.getHeight() - 137, panelToDrawAt.getWidth() - 76, panelToDrawAt.getHeight() - 149);
            g.fillOval(panelToDrawAt.getWidth() - 77, panelToDrawAt.getHeight() - 151, 7, 7); //v�nster hand
            g.drawLine(panelToDrawAt.getWidth() - 34, panelToDrawAt.getHeight() - 137, panelToDrawAt.getWidth() - 38, panelToDrawAt.getHeight() - 149); //h�ger underarm
            g.fillOval(panelToDrawAt.getWidth() - 43, panelToDrawAt.getHeight() - 151, 7, 7); //h�ger hand
            g.fillOval(panelToDrawAt.getWidth() - 67, panelToDrawAt.getHeight() - 136, 20, 20); //�verkroppsmuskler
        }
        g.fillOval((panelToDrawAt.getWidth() - 57), panelToDrawAt.getHeight() - 139, 23, (int) (2 * waterlevel + 3)); //h�ger �verarm
        g.fillOval(panelToDrawAt.getWidth() - 61, panelToDrawAt.getHeight() - 135, 8, 40); //�verkropp
        g.fillOval((int) (panelToDrawAt.getWidth() - 57 - fatness / 2), panelToDrawAt.getHeight() - 115, (int) fatness, 18); //mage
        g.drawLine(panelToDrawAt.getWidth() - 58, panelToDrawAt.getHeight() - 97, panelToDrawAt.getWidth() - 66, panelToDrawAt.getHeight() - 80); //v�nster ben
        g.drawLine(panelToDrawAt.getWidth() - 55, panelToDrawAt.getHeight() - 97, panelToDrawAt.getWidth() - 47, panelToDrawAt.getHeight() - 80); //h�ger ben
        g.fillOval(panelToDrawAt.getWidth() - 68, panelToDrawAt.getHeight() - 82, 4, 4); //v�nster kn�sk�l
        g.fillOval(panelToDrawAt.getWidth() - 47, panelToDrawAt.getHeight() - 82, 4, 4); //h�ger kn�sk�l
        g.drawLine(panelToDrawAt.getWidth() - 66, panelToDrawAt.getHeight() - 81, panelToDrawAt.getWidth() - 66, panelToDrawAt.getHeight() - 65); //h�ger underben
        g.drawLine(panelToDrawAt.getWidth() - 46, panelToDrawAt.getHeight() - 81, panelToDrawAt.getWidth() - 46, panelToDrawAt.getHeight() - 65); //h�ger underben
        g.drawLine(panelToDrawAt.getWidth() - 74, panelToDrawAt.getHeight() - 65, panelToDrawAt.getWidth() - 65, panelToDrawAt.getHeight() - 65); //v�nster fot
        g.drawLine(panelToDrawAt.getWidth() - 46, panelToDrawAt.getHeight() - 65, panelToDrawAt.getWidth() - 38, panelToDrawAt.getHeight() - 65); //h�ger fot

        //Slut figur
    }
}
