/**
 * @author zhaowenhao
 * @create 2022-07-11
 * @Description
 */

package com.wenhao.tankgame2;

import java.io.*;
import java.util.Vector;

public class Recorder {
    private static int destroyedTankNums = 0;
    private static Vector<Enemy> enemyTanks = null;
    private static final String recordFile = "src\\myRecord.txt";

    // load the record file, then new a node vector for game
    public static Vector<Node> loadRecord() {

        String readData = null;
        Vector<Node> nodes = new Vector<>();
        //
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(recordFile));
            //
            destroyedTankNums = Integer.parseInt(bufferedReader.readLine());
            //
            while((readData = bufferedReader.readLine()) != null) {
                String[] xyd = readData.split(" ");
                int x = Integer.parseInt(xyd[0]);
                int y = Integer.parseInt(xyd[1]);
                int d = Integer.parseInt(xyd[2]);
                nodes.add(new Node(x, y, d));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return nodes;
    }

    // save the destroyed Tank Nums and the enemy position
    public static void saveRecord() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(recordFile));
        bufferedWriter.write(destroyedTankNums + "\r\n");

        for (int i = 0; i < enemyTanks.size(); i++) {
            Enemy enemy = enemyTanks.get(i);
            if(enemy.isLive()) {
                // save
                String record = enemy.getX() + " " + enemy.getY() + " " + enemy.getDirection();
                bufferedWriter.write(record);
                bufferedWriter.newLine();
            }
        }

        bufferedWriter.close();
    }

    public static String getRecordFile() {
        return recordFile;
    }

    public static void addDestroyedTankNum() {
        Recorder.destroyedTankNums++;
    }

    public static int getDestroyedTankNums() {
        return destroyedTankNums;
    }

    public static void setDestroyedTankNums(int destroyedTankNums) {
        Recorder.destroyedTankNums = destroyedTankNums;
    }

    public static Vector<Enemy> getEnemyTanks() {
        return enemyTanks;
    }

    public static void setEnemyTanks(Vector<Enemy> enemyTanks) {
        Recorder.enemyTanks = enemyTanks;
    }
}
