package models;

import map.BlockMap;
import map.LevelMap;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

//todo мб так же подписывать на события, а не передавать самого робота?

public class EnemyController
{
    private static final double maxVelocity = 2;
    private ConcurrentLinkedDeque<Enemy> enemies;
    private Enemy enemy;
    private BlockMap cashTarget;
    private ArrayList<BlockMap> cashPath;

    public EnemyController(ConcurrentLinkedDeque<Enemy> enemies)
    {
        this.enemies = enemies;
    }

    public EnemyController(Enemy enemy)
    {
        this.enemy = enemy;
    }

    public void moveEnemy(Robot robot, LevelMap map)
    {
        var x = round(enemy.getEnemyPositionX()) / BlockMap.getM_width();
        var y = round(enemy.getEnemyPositionY()) / BlockMap.getM_height();
        var currentBlock = map.getMap()[y][x];
        BlockMap robotBlock;
        synchronized (robot) {
            robotBlock = map.getMap()[(int) robot.getM_robotPositionY() / BlockMap.getM_width()][(int) robot.getM_robotPositionX() / BlockMap.getM_height()];
        }
        int nextBlockX;
        int nextBlockY;
        if (Math.abs(round(enemy.getEnemyPositionX()) - currentBlock.getM_middlePositionX()) <=
                round(enemy.getEnemyWidth() / 2)
                && Math.abs(round(enemy.getEnemyPositionY()) -
                currentBlock.getM_middlePositionY()) <= round(enemy.getEnemyHeight() / 2))  {
            BlockMap nextBlock = null;
            if (robotBlock == cashTarget) {
                for (var i = 0; i < cashPath.size(); i++) {
                    if (cashPath.get(i) == currentBlock) {
                        if (i + 1 < cashPath.size())
                            nextBlock = cashPath.get(i + 1);
                        else
                            nextBlock = currentBlock;
                    }
                }
            }
            if (nextBlock == null) {
                cashTarget = robotBlock;
                cashPath = map.graph.getPathTo(currentBlock, robotBlock, false);
                nextBlock = cashPath.get(1);
            }
            nextBlockX = nextBlock.getM_middlePositionX();
            nextBlockY = nextBlock.getM_middlePositionY();
        }
        else {
            nextBlockX = currentBlock.getM_middlePositionX() - round(enemy.getEnemyWidth() / 2);
            nextBlockY = currentBlock.getM_middlePositionY() - round(enemy.getEnemyHeight() / 2);
        }
        if (nextBlockX > enemy.getEnemyPositionX()) {
            enemy.setEnemyPositionX((int) Math.min(enemy.getEnemyPositionX() + maxVelocity, nextBlockX));
        }
        else if (nextBlockX < enemy.getEnemyPositionX()) {
            enemy.setEnemyPositionX((int) Math.max(enemy.getEnemyPositionX() - maxVelocity, nextBlockX));
        }
        else if (nextBlockY > enemy.getEnemyPositionY()) {
            enemy.setEnemyPositionY((int) Math.min(enemy.getEnemyPositionY() + maxVelocity, nextBlockY));
        }
        else {
            enemy.setEnemyPositionY((int) Math.max(enemy.getEnemyPositionY() - maxVelocity, nextBlockY));
        }
        if (isEnemyNearToRobot(
                    robot, enemy.getEnemyPositionX(),
                    enemy.getEnemyPositionY(), enemy.getEnemyWidth(), enemy.getEnemyHeight()))
            {
                robot.killRobot();
                System.out.println("мертв");
            }
    }

    public static int round(double value)
    {
        return (int)(value + 0.5);
    }

    private static boolean isEnemyNearToRobot(
            Robot robot, double enemyX, double enemyY, double robotDiam1, double robotDiam2)
    {
        var robotX = robot.getM_robotPositionX();
        var robotY = robot.getM_robotPositionY();
        enemyX = round(enemyX);
        enemyY = round(enemyY);
        return robotX <= enemyX + robotDiam1 / 2 && robotX >= enemyX - robotDiam1 / 2
                && robotY <= enemyY + robotDiam2 / 2 && robotY >= enemyY - robotDiam2 / 2;
    }
}
