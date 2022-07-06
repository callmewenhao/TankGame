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

    public Enemy(int x, int y, int direction, TankType tankType) {
        super(x, y, direction, tankType);
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
        while (true) {
            // enemy tank shots
            shotOtherTank();

            // move along the old direction
            switch (getDirection()) {
                case 0:
                    for (int i = 0; i < 30; i++) {
                        // move
                        if (getY() > 0) {
                            moveUp();
                        }
                        // sleep for a while
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1:
                    for (int i = 0; i < 30; i++) {
                        if (getX() + 60 < 1000) {
                            moveRight();
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 2:
                    for (int i = 0; i < 30; i++) {
                        if(getX() + 60 < 750) {
                            moveDown();
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 3:
                    for (int i = 0; i < 30; i++) {
                        if(getX() > 0) {
                            moveLeft();
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }

            // change the direction of enemy tank randomly
            setDirection((int) (Math.random() * 4)); // 0~3

            // if enemy tank is dead
            if (!super.isLive()) {
                break;
            }
        }
    }
}
