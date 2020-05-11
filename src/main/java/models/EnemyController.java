package models;

import map.BlockMap;
import map.LevelMap;

import java.util.concurrent.ConcurrentLinkedDeque;

public class EnemyController
{
    private static final double maxVelocity = 5;
    private ConcurrentLinkedDeque<Enemy> enemies;
    private Enemy enemy;

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
        var robotBlock = map.getMap()[(int) robot.getM_robotPositionY() / BlockMap.getM_width()][(int) robot.getM_robotPositionX() / BlockMap.getM_height()];
        int nextBlockX;
        int nextBlockY;
        if (Math.abs(round(enemy.getEnemyPositionX()) - currentBlock.getM_middlePositionX()) <=
                round(enemy.getEnemyWidth() / 2)
                && Math.abs(round(enemy.getEnemyPositionY()) -
                currentBlock.getM_middlePositionY()) <= round(enemy.getEnemyHeight() / 2))  {
            var nextBlock = map.graph.getNextBlock(currentBlock, robotBlock);
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
                //todo умереть
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
