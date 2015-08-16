package com.sundbybergsit.applet;

import com.sundbybergsit.objects.PersonDataEntry;

import java.applet.Applet;
import java.awt.*;
import java.util.List;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Z
 * Date: 2013-03-25
 * Time: 20:47
 * To change this template use File | Settings | File Templates.
 */
public class FatmanApplet extends Applet {

    private SundbybergsITService service;
    private FatGuy fatGuy;
    private FatPanel fatPanel;
    private String userId;

    public void init() {
        userId = getParameter("userId");
        service = new SundbybergsITService();
    }

    public void start() {

        List<PersonDataEntry> listOfEntries;// = service.getPersonDataEntries(loginBean.getLoggedInUser(), dataBean.getTypeScale());

        Vector<Vector> convertedDataCollection = new Vector<>();
//
//        Transformer<PersonDataEntry, Vector> transformer = new PersonDataEntryToVectorTransformer();
//        for (PersonDataEntry entry : listOfEntries) {
//            convertedDataCollection.add(transformer.transform(entry));
//        }
        fatGuy = new FatGuy();
        BodyDataBean dataBean = new BodyDataBean();
        fatPanel = new FatPanel(this, service, dataBean, fatGuy, userId);
        add(fatPanel);
    }

    @Override
    public void paint(Graphics g) {
        fatGuy.drawFatman(fatPanel, 75, 17, 60, g);
        super.paint(g);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
