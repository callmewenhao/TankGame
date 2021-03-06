/**
 * @author zhaowenhao
 * @create 2022-07-05
 * @Description
 */

package com.wenhao.tankgame2;

import java.awt.*;

public class Bomb implements Runnable{

    private int x;
    private int y;
    private int life = 9;
    private boolean isLive = true;

    public void lifeDown() {
        if(life > 0) {
            life--;
        } else {
            isLive = false;
        }
    }

    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
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

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    // reduce the bomb life by a thread
    @Override
    public void run() {
        while(isLive) {
            try {
                Thread.sleep(50); // each 50ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lifeDown(); // reduce the life value of bomb
        }
    }
}
