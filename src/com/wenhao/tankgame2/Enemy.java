/**
 * @author zhaowenhao
 * @create 2022-07-03
 * @Description
 */

package com.wenhao.tankgame2;

import java.util.Vector;

/**
 * enemy tank class
 */
public class Enemy extends Tank implements Runnable {
    private Vector<Enemy> enemyTanks = new Vector<>();

    public Enemy(int x, int y, int direction, TankType tankType) {
        super(x, y, direction, tankType);
    }

    //
    public boolean enemyOverlaps() {
        for(int i = 0; i < enemyTanks.size(); i++) {
            Enemy enemy = enemyTanks.get(i);
            if(this != enemy) {
                if(super.overlap(enemy)) return true;
            }
        }
        return false;
    }

    //
    @Override
    public void shotOtherTank() {
        // if enemy is living and enemy didn't shoot n(now i just hardcode it to 1) bullets
        if(super.isLive() && super.bullets.size() < 2) {
            super.shotOtherTank();
        }
    }

    @Override
    public void run() {
        boolean crash = false;
        while (true) {
            // enemy tank shots
            shotOtherTank();

            // move along the old direction
            switch (getDirection()) {
                case 0:
                    for (int i = 0; i < 30; i++) {
                        // move
                        if (!enemyOverlaps()) {
                            if(getY() > 0) {
                                moveUp();
                            }
                        } else {
                            super.changeDirection();
                            crash = true;
                            break;
                        }
                        // sleep for a while
                        try {
                            Thread.sleep(90);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1:
                    for (int i = 0; i < 30; i++) {
                        if (!enemyOverlaps()) {
                            if(getX() + 60 < 1000) {
                                moveRight();
                            }
                        } else {
                            super.changeDirection();
                            crash = true;
                            break;
                        }
                        try {
                            Thread.sleep(90);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 2:
                    for (int i = 0; i < 30; i++) {
                        if(!enemyOverlaps()) {
                            if(getX() + 60 < 750) {
                                moveDown();
                            }
                        } else {
                            super.changeDirection();
                            crash = true;
                            break;
                        }
                        try {
                            Thread.sleep(90);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 3:
                    for (int i = 0; i < 30; i++) {
                        if(!enemyOverlaps()) {
                            if(getX() > 0) {
                                moveLeft();
                            }
                        } else {
                            super.changeDirection();
                            crash = true;
                            break;
                        }
                        try {
                            Thread.sleep(90);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }

            // change the direction of enemy tank randomly
            if(crash) {
                switch (getDirection()) {
                    case 0:
                        moveUp();
                        break;
                    case 1:
                        moveRight();
                        break;
                    case 2:
                        moveDown();
                        break;
                    case 3:
                        moveLeft();
                        break;
                }
                crash = false;
            } else {
                setDirection((int) (Math.random() * 4)); // 0~3
            }

            // if enemy tank is dead
            if (!super.isLive()) {
                break;
            }
        }
    }

    public void setEnemyTanks(Vector<Enemy> enemyTanks) {
        this.enemyTanks = enemyTanks;
    }
}
