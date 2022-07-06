/**
 * @author zhaowenhao
 * @create 2022-07-05
 * @Description: bullet thread class
 */

package com.wenhao.tankgame2;

public class Bullet implements Runnable {
    private int x;
    private int y;
    private int direction;
    private int speed = 8;

    private boolean isLive = true;

    public Bullet(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    @Override
    public void run() {
        while (true) {
            // bullets sleep for 100ms
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // bullets move by speed
            switch (direction) {
                case 0:
                    y -= speed;
                    break;
                case 1:
                    x += speed;
                    break;
                case 2:
                    y += speed;
                    break;
                case 3:
                    x -= speed;
                    break;
                default:
                    break;
            }
            //
            System.out.println("bullet x:" + x + " y:" + y);

            // bullet die when out of range
            if (!(0 <= x && x <= 1000 && 0 <= y && y <= 750 && isLive)) {
                System.out.println("bullet thread exit!");
                isLive = false;
                break;
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

}
