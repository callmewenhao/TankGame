/**
 * @author zhaowenhao
 * @create 2022-07-03
 * @Description
 */

package com.wenhao.tankgame2;


import java.util.Vector;

/**
 * tank class
 */
public class Tank {
    private int x;
    private int y;
    // 0:up 1:right 2:down 3:left
    private int direction;
    private int speed = 1;
    private boolean isLive = true;


    // build a bullet obj
    Vector<Bullet> bullets = new Vector<>();

    TankType tankType;

    // move up down left right
    public void moveUp() {
        y -= speed;
    }

    public void moveDown() {
        y += speed;
    }

    public void moveLeft() {
        x -= speed;
    }

    public void moveRight() {
        x += speed;
    }

    // shot
    public void shotOtherTank() {
        Bullet bullet = null;
        switch(this.getDirection()) {
            case 0:
                bullet = new Bullet(this.getX() + 20, this.getY(), 0);
                break;
            case 1:
                bullet = new Bullet(this.getX() + 60, this.getY() + 20, 1);
                break;
            case 2:
                bullet = new Bullet(this.getX() + 20, this.getY() + 60, 2);
                break;
            case 3:
                bullet = new Bullet(this.getX(), this.getY() + 20, 3);
                break;
            default:
                System.out.println("bug!!!");
                break;
        }
        // start the thread
        new Thread(bullet).start();
        bullets.add(bullet);
    }

    // constructor
    public Tank(int x, int y, int direction, TankType tankType) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.tankType = tankType;
    }

    // getter and setter


    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public TankType getTankType() {
        return tankType;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }
}
