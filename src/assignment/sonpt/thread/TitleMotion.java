/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.sonpt.thread;

import javax.swing.JLabel;

/**
 *
 * @author sonpt_ph19600
 */
public class TitleMotion extends Thread {

    public static JLabel jLabel;

    @Override
    public void run() {
        String title = jLabel.getText() + " ";
        while (true) {
            title = title.charAt(title.length() - 1) + title.substring(0, title.length() - 1);
            jLabel.setText(title);
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
            }
        }
    }

}
