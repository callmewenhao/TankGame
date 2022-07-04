/**
 * @author zhaowenhao
 * @create 2022-07-03
 * @Description
 */

package com.wenhao.tankgame;


/**
 * tank class
 */
public class Tank{
    private int x;
    private int y;
    // 0:up 1:right 2:down 3:left
    private int direction;
    private int speed = 1;

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
}
