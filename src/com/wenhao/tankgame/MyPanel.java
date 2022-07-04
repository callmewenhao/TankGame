/**
 * @author zhaowenhao
 * @create 2022-07-03
 * @Description
 */

package com.wenhao.tankgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;


/**
 * panel class
 */
public class MyPanel extends JPanel implements KeyListener {
    // some init setting
    Hero hero = null;
    Vector<Enemy> enemyTanks = new Vector<>();
    int enemyNums = 6;

    public MyPanel() {
        // hero tank
        this.hero = new Hero(300, 400, 0, TankType.MyTank); // init tank
        hero.setSpeed(2);

        // add enemy tanks
        for (int i = 0; i < enemyNums; i++) {
            Enemy enemy = new Enemy(100 * (i + 1), 100, i % 4, TankType.EnemyTank);
            enemy.setSpeed(2);
            enemyTanks.add(enemy);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // draw background
        g.fillRect(0, 0, 1000, 750);

        // draw the hero tanks
        drawTank(hero.getX(), hero.getY(), g, hero.getDirection(), hero.getTankType());

        // draw the enemy tanks
        for (Enemy enemy : enemyTanks) {
            drawTank(enemy.getX(), enemy.getY(), g, enemy.getDirection(), enemy.getTankType());
        }

    }

    /**
     * @param x         x of left top point coordinate of tank
     * @param y         y of left top point coordinate of tank
     * @param g         draw pen
     * @param direction the direction of tank
     * @param type      the type of tank, A Enum var, such as MyTank, EnemyTank
     */
    public void drawTank(int x, int y, Graphics g, int direction, TankType type) {
        // set the color of the tank
        switch (type) {
            case MyTank:
                g.setColor(Color.cyan);
                break;
            case EnemyTank:
                g.setColor(Color.yellow);
                break;
        }

        // draw the tank according to the direction!
        switch (direction) {
            case 0: // up
                g.fill3DRect(x, y, 10, 60, false); // left wheel
                g.fill3DRect(x + 30, y, 10, 60, false); // right wheel
                g.fill3DRect(x + 10, y + 10, 20, 40, false); // tank body
                g.drawOval(x + 10, y + 20, 20, 20); // tank head
                g.drawLine(x + 20, y, x + 20, y + 30);
                break;
            case 1: // right
                g.fill3DRect(x, y, 60, 10, false); // left wheel
                g.fill3DRect(x, y + 30, 60, 10, false); // right wheel
                g.fill3DRect(x + 10, y + 10, 40, 20, false); // tank body
                g.drawOval(x + 20, y + 10, 20, 20); // tank head
                g.drawLine(x + 30, y + 20, x + 60, y + 20);
                break;
            case 2: // down
                g.fill3DRect(x, y, 10, 60, false); // left wheel
                g.fill3DRect(x + 30, y, 10, 60, false); // right wheel
                g.fill3DRect(x + 10, y + 10, 20, 40, false); // tank body
                g.drawOval(x + 10, y + 20, 20, 20); // tank head
                g.drawLine(x + 20, y + 30, x + 20, y + 60);
                break;
            case 3: // left
                g.fill3DRect(x, y, 60, 10, false); // left wheel
                g.fill3DRect(x, y + 30, 60, 10, false); // right wheel
                g.fill3DRect(x + 10, y + 10, 40, 20, false); // tank body
                g.drawOval(x + 20, y + 10, 20, 20); // tank head
                g.drawLine(x, y + 20, x + 30, y + 20);
                break;
            default:
                System.out.println("no tank be drawn!");
                break;
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    // deal with key Pressed!
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                hero.moveDown();
                break;
            case KeyEvent.VK_UP:
                hero.moveUp();
                break;
            case KeyEvent.VK_LEFT:
                hero.moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                hero.moveRight();
                break;
            case KeyEvent.VK_W:
                hero.setDirection(0);
                break;
            case KeyEvent.VK_D:
                hero.setDirection(1);
                break;
            case KeyEvent.VK_S:
                hero.setDirection(2);
                break;
            case KeyEvent.VK_A:
                hero.setDirection(3);
                break;

            default:
                break;
        }
        // repaint the panel
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
