/**
 * @author zhaowenhao
 * @create 2022-07-03
 * @Description
 */

package com.wenhao.tankgame2;

/**
 * hero tank class
 */
public class Hero extends Tank {

    public Hero(int x, int y, int direction, TankType tankType) {
        super(x, y, direction, tankType);
    }

    // Override the shotOtherTank()
    @Override
    public void shotOtherTank() {
        // our hero tank only shot 5 bullets at same moment
        if(super.bullets.size() == 5) {
            return;
        }
        super.shotOtherTank();
    }
}





