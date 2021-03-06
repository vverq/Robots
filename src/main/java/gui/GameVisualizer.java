package gui;

import map.LevelMap;
import models.*;
import models.Robot;
import map.BlockMap;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.*;
import java.util.Timer;
import javax.swing.*;


public class GameVisualizer extends JPanel
{
    private final Timer m_timer = initTimer();
    private static Timer initTimer()
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }
    private Robot robot;
    private EnemyGenerator enemyGenerator;
    private RobotController robotController;
    private TargetGenerator targetGenerator;
    private LevelMap map;

    GameVisualizer(boolean autoMode, Robot robot, RobotController robotController,
                   TargetGenerator targetGenerator, EnemyGenerator enemyGenerator, LevelMap map)
    {
        this.map = map;
        this.robot = robot;
        this.robotController = robotController;
        this.targetGenerator = targetGenerator;
        this.enemyGenerator = enemyGenerator;
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 1);
        setDoubleBuffered(true);
    }

    private void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        drawMap(g2d);
        drawRobot(g2d);
        drawTarget(g2d);
        drawEnemy(g2d);
    }

    protected void setMap(LevelMap map) {
        this.map = map;
    }

    private void drawMap(Graphics2D g)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 15, 15);
        g.setTransform(t);
        for (var i = 0; i < map.getHeight(); i++) {
            for (var j = 0; j < map.getWidth(); j++) {
                var block = map.getMap()[i][j];
                g.drawImage(
                        block.getImage(),
                        block.getM_positionX() * BlockMap.getM_width(),
                        block.getM_positionY() * BlockMap.getM_height(),
                        null);
            }
        }
    }

    private void drawRobot(Graphics2D g)
    {
        int robotCenterX = RobotController.round(robot.getM_robotPositionX());
        int robotCenterY = RobotController.round(robot.getM_robotPositionY());
//        AffineTransform t = AffineTransform.getRotateInstance(robot.getM_robotDirection(), robotCenterX, robotCenterY);
//        g.setTransform(t);
        g.drawImage(
                robot.getM_robotImage(),
                robotCenterX - robot.getM_robotImage().getHeight(null) / 2,
                robotCenterY - robot.getM_robotImage().getWidth(null) / 2,
                null
        );
        if (map.getFires().size() > 0)
        {
            for (Fire fire: map.getFires())
            {
                g.drawImage(fire.getFireImage(), fire.getX(), fire.getY(), null);
            }
        }
    }


    private void drawTarget(Graphics2D g)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        for (Target target: targetGenerator.getTargets())
        {
            g.drawImage(
                    target.getM_targetImage(),
                    target.getM_targetPositionX(),
                    target.getM_targetPositionY(),
                    null
            );
        }
    }

    private void drawEnemy(Graphics2D g)
    {
        for (Enemy enemy : enemyGenerator.getEnemies()) {
            int enemyCenterX = EnemyController.round(enemy.getEnemyPositionX());
            int enemyCenterY = EnemyController.round(enemy.getEnemyPositionY());
            g.drawImage(
                    enemy.getEnemyImage(),
                    enemyCenterX - enemy.getEnemyImage().getHeight(null) / 2,
                    enemyCenterY - enemy.getEnemyImage().getWidth(null) / 2,
                    null
            );
        }
    }
}