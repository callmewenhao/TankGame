/**
 * @author zhaowenhao
 * @create 2022-07-03
 * @Description
 */

package com.wenhao.tankgame2;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;


/**
 * the entry of game
 */
public class TankGame extends JFrame {

    MyPanel mp = null;

    public static void main(String[] args) {
        TankGame tankGame = new TankGame();
    }

    public TankGame() {
        this.mp = new MyPanel();
        new Thread(mp).start(); // repaint thread
        this.add(mp);
        this.addKeyListener(mp); // listen to the Key pressed!
        this.setSize(1300, 900);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        // listen to close window
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    Recorder.saveRecord();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }
}
