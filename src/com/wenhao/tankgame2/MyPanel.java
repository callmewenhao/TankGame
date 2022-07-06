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

    public MyPanel() {
        // init hero tank
        this.hero = new Hero(300, 400, 0, TankType.MyTank); // init tank
        hero.setSpeed(3);

        // init enemy tanks
        for (int i = 0; i < enemyNums; i++) {
            Enemy enemy = new Enemy(100 * (i + 1), 100, i % 4, TankType.EnemyTank);
            enemy.setSpeed(3);
            new Thread(enemy).start();
            enemyTanks.add(enemy);
        }

        // init bombs
        images1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/big.png"));
        images2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/middle.png"));
        images3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/small.png"));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // draw background
        g.fillRect(0, 0, 1000, 750);

        // draw the hero tanks
        if(hero != null && hero.isLive()) {
            drawTank(hero.getX(), hero.getY(), g, hero.getDirection(), hero.getTankType());
        }

        // draw  all the hero bullets
        if(hero.bullets != null) { // null pointer judge!
            for (int i = 0; i < hero.bullets.size(); i++) { // all bullets todo: another way?
                Bullet bullet = hero.bullets.get(i);
                if(bullet.isLive()) { // draw the live bullet
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
                if(enemy.bullets != null) {
                    for (int j = 0; j < enemy.bullets.size(); j++) { // all bullets todo: another way?
                        Bullet bullet = enemy.bullets.get(j);
                        if(bullet.isLive()) { // draw the live bullet
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
            if(bomb.getLife() > 6) {
                g.drawImage(images1, bomb.getX(), bomb.getY(), 60, 60, this);
            } else if(bomb.getLife() > 3) {
                g.drawImage(images2, bomb.getX(), bomb.getY(), 60, 60, this);
            } else {
                g.drawImage(images3, bomb.getX(), bomb.getY(), 60, 60, this);
            }
            // bomb.lifeDown(); // reduce the life value of bomb, i moved it to the run() in Bomb Thread class
            if(!bomb.isLive() ) {
                bombs.remove(bomb);
            }
        }
    }

    /**
     * judge the hero bullet hit enemy
     */
    public void hitEnemy() {
        for (int i = 0; i < hero.bullets.size(); i++) {
            Bullet bullet = hero.bullets.get(i);
            if(bullet.isLive()) { // bullet judge
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
                if(hero.isLive() && bullet.isLive()) {
                    hitTank(bullet, hero);
                }
            }
        }

    }

    /**
     * @param bullet the bullet
     * @param tank the tank
     */
    public void hitTank(Bullet bullet, Tank tank) {
        switch (tank.getDirection()) {
            // up and down
            case 0:
            case 2:
                if(tank.getX() < bullet.getX() && bullet.getX() < tank.getX() + 40
                        && tank.getY() < bullet.getY() && bullet.getY() < tank.getY() + 60) {

                    // change the state of bullet and enemy tank
                    bullet.setLive(false);
                    tank.setLive(false);

                    // build a bomb
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    new Thread(bomb).start();
                    bombs.add(bomb);
                }
                break;
            // right and left
            case 1:
            case 3:
                if(tank.getX() < bullet.getX() && bullet.getX() < tank.getX() + 60
                        && tank.getY() < bullet.getY() && bullet.getY() < tank.getY() + 40) {

                    // change the state of bullet and enemy tank
                    bullet.setLive(false);
                    tank.setLive(false);

                    // build a bomb
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    new Thread(bomb).start();
                    bombs.add(bomb);
                }
                break;
            default:break;
        }
    }

    /**
     * drawBullet(Bullet bullet, Graphics g)
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
            default:break;
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
            case KeyEvent.VK_W:
                hero.setDirection(0);
                if(hero.getY() > 0) {
                    hero.moveUp();
                }
                break;
            case KeyEvent.VK_D:
                hero.setDirection(1);
                if(hero.getX() + 60 < 1000) {
                    hero.moveRight();
                }
                break;
            case KeyEvent.VK_S:
                hero.setDirection(2);
                if(hero.getY() + 60 < 750) {
                    hero.moveDown();
                }
                break;
            case KeyEvent.VK_A:
                hero.setDirection(3);
                if(hero.getX() > 0) {
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
        while(true) {

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

            //
            this.repaint();
        }
    }
}
