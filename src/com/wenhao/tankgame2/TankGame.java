/**
 * @author zhaowenhao
 * @create 2022-07-03
 * @Description
 */

package com.wenhao.tankgame2;

import javax.swing.*;


/**
 * the entry of game
 */
public class TankGame extends JFrame {

    MyPanel mp = null;

    public static void main(String[] args) {
        TankGame tankGame = new TankGame();
    }

    public TankGame(){
        this.mp = new MyPanel();
        new Thread(mp).start(); // repaint thread
        this.add(mp);
        this.addKeyListener(mp); // listen to the Key pressed!
        this.setSize(1200, 900);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
