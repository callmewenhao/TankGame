/**
 * @author zhaowenhao
 * @create 2022-07-03
 * @Description
 */

package com.wenhao.tankgame2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;


/**
 * panel class
 */
public class MyPanel extends JPanel implements KeyListener, Runnable {
    // some init setting
    Hero hero = null;

    // for enemies
    Vector<Enemy> enemyTanks = new Vector<>();
    int enemyNums = 6;

    // for bombs
    Vector<Bomb> bombs = new Vector<>();
    Image images1 = null;
    Image images2 = null;
    Image images3 = null;

    // for record
    Vector<Node> nodes = new Vector<>();

    public MyPanel() {
        // choose the mode!
        Scanner scanner = new Scanner(System.in);
        System.out.print("input choose: 1(new game), 2(last game): ");
        int choose = scanner.nextInt();
        if(choose == 2) {
            File file = new File(Recorder.getRecordFile());
            if(file.exists()) {
                nodes = Recorder.loadRecord();
            } else {
                System.out.println("file doesn't exist, start a new game!");
                choose = 1;
            }
        }

        // init hero tank
        this.hero = new Hero(500, 325, 0, TankType.MyTank); // init tank
        hero.setSpeed(3);

        // init enemy tanks according to the mode
        switch(choose) {
            case 1:
                // new game
                for (int i = 0; i < enemyNums; i++) {
                    Enemy enemy = new Enemy(100 * (i + 1), 100, i % 4, TankType.EnemyTank);
                    enemy.setSpeed(2);
                    enemy.setEnemyTanks(enemyTanks); // oop for avoiding overlap
                    new Thread(enemy).start();
                    enemyTanks.add(enemy);
                }
                break;
            case 2:
                // the last game
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    Enemy enemy = new Enemy(node.getX(), node.getY(), node.getD(), TankType.EnemyTank);
                    enemy.setSpeed(2);
                    enemy.setEnemyTanks(enemyTanks);
                    new Thread(enemy).start();
                    enemyTanks.add(enemy);
                }
                break;
            default:break;
        }

        // set Record
        Recorder.setEnemyTanks(enemyTanks);

        // init bombs
        images1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/big.png"));
        images2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/middle.png"));
        images3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/small.png"));

        // music
        PlayAudio playAudio = new PlayAudio("src\\5170.wav");
        playAudio.start();

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // draw background
        g.fillRect(0, 0, 1000, 750);

        // show info!
        showInfo(g);

        // draw the hero tanks
        if (hero != null && hero.isLive()) {
            drawTank(hero.getX(), hero.getY(), g, hero.getDirection(), hero.getTankType());
        }

        // draw  all the hero bullets
        if (hero.bullets != null) { // null pointer judge!
            for (int i = 0; i < hero.bullets.size(); i++) { // all bullets todo: another way?
                Bullet bullet = hero.bullets.get(i);
                if (bullet.isLive()) { // draw the live bullet
                    drawBullet(bullet, g);
                } else { // remove the dead bullet
                    hero.bullets.remove(bullet);
                }
            }
        }

        // draw the enemy tanks and bullets
        for (int i = 0; i < enemyTanks.size(); i++) {
            Enemy enemy = enemyTanks.get(i);
            if (enemy.isLive()) {
                // draw the enemy tank
                drawTank(enemy.getX(), enemy.getY(), g, enemy.getDirection(), enemy.getTankType());

                // draw the bullets of enemy tank
                if (enemy.bullets != null) {
                    for (int j = 0; j < enemy.bullets.size(); j++) { // all bullets todo: another way?
                        Bullet bullet = enemy.bullets.get(j);
                        if (bullet.isLive()) { // draw the live bullet
                            drawBullet(bullet, g);
                        } else { // remove the dead bullet
                            enemy.bullets.remove(bullet);
                        }
                    }
                }
            } else {
                enemyTanks.remove(enemy);
            }
        }

        // draw the bombs
        for (int i = 0; i < bombs.size(); i++) {
            Bomb bomb = bombs.get(i);
            if (bomb.getLife() > 6) {
                g.drawImage(images1, bomb.getX(), bomb.getY(), 60, 60, this);
            } else if (bomb.getLife() > 3) {
                g.drawImage(images2, bomb.getX(), bomb.getY(), 60, 60, this);
            } else {
                g.drawImage(images3, bomb.getX(), bomb.getY(), 60, 60, this);
            }
            // bomb.lifeDown(); // reduce the life value of bomb, i moved it to the run() in Bomb Thread class
            if (!bomb.isLive()) {
                bombs.remove(bomb);
            }
        }
    }

    //
    public void showInfo(Graphics g) {
        //
        g.setColor(Color.BLACK);
        Font font = new Font("宋体", Font.BOLD, 25);
        g.setFont(font);
        g.drawString("您累计击毁敌方坦克", 1020, 30);
        this.drawTank(1050, 60, g, 0, TankType.EnemyTank);
        g.setColor(Color.BLACK);
        g.drawString(Recorder.getDestroyedTankNums() + "", 1200, 100);
    }

    /**
     * judge hero crash enemy
     */
    public void heroCrashEnemy() {
        if (hero.isLive()) {
            for (int i = 0; i < enemyTanks.size(); i++) {
                Enemy enemy = enemyTanks.get(i);
                if (enemy.isLive() && hero.overlap(enemy)) { // overlap
                    // enemy is dead
                    enemy.setLive(false);

                    // add record
                    Recorder.addDestroyedTankNum();

                    // build a bomb
                    Bomb bomb = new Bomb(enemy.getX(), enemy.getY());
                    new Thread(bomb).start();
                    bombs.add(bomb);
                }
            }
        }
    }

    /**
     * judge the hero bullet hit enemy
     */
    public void hitEnemy() {
        for (int i = 0; i < hero.bullets.size(); i++) {
            Bullet bullet = hero.bullets.get(i);
            if (bullet.isLive()) { // bullet judge
                for (int j = 0; j < enemyTanks.size(); j++) {
                    hitTank(hero.bullets.get(i), enemyTanks.get(j));
                }
            }
        }
    }

    /**
     * judge the enemy bullet hit hero
     */
    public void hitHero() {
        for (int i = 0; i < enemyTanks.size(); i++) {
            Enemy enemy = enemyTanks.get(i);
            for (int j = 0; j < enemy.bullets.size(); j++) {
                Bullet bullet = enemy.bullets.get(j);
                if (hero.isLive() && bullet.isLive()) {
                    hitTank(bullet, hero);
                }
            }
        }

    }

    /**
     * @param bullet the bullet
     * @param tank   the tank
     */
    public void hitTank(Bullet bullet, Tank tank) {
        if (tank.getX() < bullet.getX() && bullet.getX() < tank.getX() + 60
                && tank.getY() < bullet.getY() && bullet.getY() < tank.getY() + 60) {

            // change the state of bullet and enemy tank
            bullet.setLive(false);
            tank.setLive(false);

            // add record
            if(tank instanceof Enemy) {
                Recorder.addDestroyedTankNum();
            }

            // build a bomb
            Bomb bomb = new Bomb(tank.getX(), tank.getY());
            new Thread(bomb).start();
            bombs.add(bomb);
        }
    }

    /**
     * drawBullet(Bullet bullet, Graphics g)
     *
     * @param bullet the bullet will be drawn
     * @param g      the panel
     */
    public void drawBullet(Bullet bullet, Graphics g) {
        g.setColor(Color.white);
        switch (bullet.getDirection()) {
            case 0:
                g.fill3DRect(bullet.getX() - 2, bullet.getY() - 7, 5, 5, false);
                break;
            case 1:
                g.fill3DRect(bullet.getX() + 5, bullet.getY() - 2, 5, 5, false);
                break;
            case 2:
                g.fill3DRect(bullet.getX() - 2, bullet.getY() + 5, 5, 5, false);
                break;
            case 3:
                g.fill3DRect(bullet.getX() - 7, bullet.getY() - 2, 5, 5, false);
                break;
            default:
                break;
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
                g.setColor(Color.yellow);
                break;
            case EnemyTank:
                g.setColor(Color.cyan);
                break;
        }

        // draw the tank according to the direction!
        switch (direction) {
            case 0: // up
                g.fill3DRect(x, y, 15, 60, false); // left wheel
                g.fill3DRect(x + 45, y, 15, 60, false); // right wheel
                g.fill3DRect(x + 15, y + 10, 30, 40, false); // tank body
                g.fillOval(x + 15, y + 15, 30, 30); // tank head
                g.drawLine(x + 30, y, x + 30, y + 30);
                break;
            case 1: // right
                g.fill3DRect(x, y, 60, 15, false); // up wheel
                g.fill3DRect(x, y + 45, 60, 15, false); // down wheel
                g.fill3DRect(x + 10, y + 15, 40, 30, false); // tank body
                g.fillOval(x + 15, y + 15, 30, 30); // tank head
                g.drawLine(x + 30, y + 30, x + 60, y + 30);
                break;
            case 2: // down
                g.fill3DRect(x, y, 15, 60, false); // left wheel
                g.fill3DRect(x + 45, y, 15, 60, false); // right wheel
                g.fill3DRect(x + 15, y + 10, 30, 40, false); // tank body
                g.fillOval(x + 15, y + 15, 30, 30); // tank head
                g.drawLine(x + 30, y + 30, x + 30, y + 60);
                break;
            case 3: // left
                g.fill3DRect(x, y, 60, 15, false); // up wheel
                g.fill3DRect(x, y + 45, 60, 15, false); // down wheel
                g.fill3DRect(x + 10, y + 15, 40, 30, false); // tank body
                g.fillOval(x + 15, y + 15, 30, 30); // tank head
                g.drawLine(x, y + 30, x + 30, y + 30);
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
            case KeyEvent.VK_W:
                hero.setDirection(0);
                if (hero.getY() > 0) {
                    hero.moveUp();
                }
                break;
            case KeyEvent.VK_D:
                hero.setDirection(1);
                if (hero.getX() + 60 < 1000) {
                    hero.moveRight();
                }
                break;
            case KeyEvent.VK_S:
                hero.setDirection(2);
                if (hero.getY() + 60 < 750) {
                    hero.moveDown();
                }
                break;
            case KeyEvent.VK_A:
                hero.setDirection(3);
                if (hero.getX() > 0) {
                    hero.moveLeft();
                }
                break;
            case KeyEvent.VK_J:
                System.out.println("hero shot the enemy");
                hero.shotOtherTank();
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

    @Override
    public void run() {
        while (true) {

            // each 50ms repaint the panel
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // refresh the state of living bullet and tank
            hitEnemy();

            // refresh the state of hero
            hitHero();

            // overlap detection for hero
            heroCrashEnemy();

            // repaint the panel
            this.repaint();
        }
    }
}
