/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.sonpt.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;

/**
 *
 * @author sonpt_ph19600
 */
public class ClockThread extends Thread {

    public static JLabel jLabel;

    @Override
    public void run() {
        SimpleDateFormat spfDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat spfTime = new SimpleDateFormat("hh:mm:ss");
        while (true) {
            Date timeNow = new Date();
            String strDateNow = spfDate.format(timeNow);
            String strTimenow = spfTime.format(timeNow);
            jLabel.setText(strDateNow);
            jLabel.setText(strTimenow);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
    }

}
