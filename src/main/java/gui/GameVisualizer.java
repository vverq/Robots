package gui;

import models.Target;
import models.Robot;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import javax.swing.JPanel;

public class GameVisualizer extends JPanel
{

    //TODO:  то, что осталось сделать:
    // 1) запретить роботу-коту бегать вниз головой, это крипово
    // 2) придумать что-то с countTargets, чтобы этот параметр постоянной варьировался в какой-то дельте
    // например, после съедания очередной цели рандомно определяется операция: либо новой цели не добавлять,
    // либо добавить одну, либо добавить несколько (но следить, чтобы кол-во несъеденных целей было разумно)
    // 3) добавить возможность взаимодействовать с роботом-котом кнопками
    // ps после кнопок робот-кот должен вроде нормально взаимодействовать с таргетами, тк в кач-ве координат первого
    // сейчас его правое ухо и поэтому он так криво ест, а еще он иногда все же убегает за границу и там прячется


    private Random rnd = new Random();
    private final Timer m_timer = initTimer();
    private static Timer initTimer()
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private Robot robot = new Robot(100, 100, 0, "images/robot.png");
    private ConcurrentLinkedDeque<Target> targets = new ConcurrentLinkedDeque<>();
    private int countTargets = 3;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.01;

    GameVisualizer()
    {
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onTargetUpdateEvent();
            }
        }, 0, 10);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onModelUpdateEvent();
            }
        }, 0, 10);
//        addMouseListener(new MouseAdapter()
//        {
//            @Override
//            public void mouseClicked(MouseEvent e)
//            {
//                setTargetPosition(e.getPoint());
//                repaint();
//            }
//        });
        setDoubleBuffered(true);
    }

    private void onTargetUpdateEvent()
    {
        while (targets.size() < countTargets)
        {
            targets.addLast(new Target(rnd.nextInt(350), rnd.nextInt(350), rnd.nextInt(5)));
        }
    }

    private void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private void onModelUpdateEvent()
    {
        var target = targets.getFirst();
        double distance = distance(target.getM_targetPositionX(), target.getM_targetPositionY(),
                robot.getM_robotPositionX(), robot.getM_robotPositionY());
        if (distance < 0.5)
        {
            return;
        }
        double angleToTarget = angleTo(
                robot.getM_robotPositionX(),
                robot.getM_robotPositionY(),
                target.getM_targetPositionX(),
                target.getM_targetPositionY()
        );
        double angularVelocity = 0;
        if (angleToTarget > robot.getM_robotDirection())
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < robot.getM_robotDirection())
        {
            angularVelocity = -maxAngularVelocity;
        }
        moveRobot(maxVelocity, angularVelocity, 10, target);
    }

    private static double applyLimits(double value, double min, double max)
    {
        return Math.min(max, Math.max(value,min));
    }

    private void moveRobot(double velocity, double angularVelocity, double duration, Target target)
    {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double angleToTarget = angleTo(
                robot.getM_robotPositionX(),
                robot.getM_robotPositionY(),
                target.getM_targetPositionX(),
                target.getM_targetPositionY()
        );
        if (Math.abs(angleToTarget - robot.getM_robotDirection()) < 0.1)
        {
            var xValue = robot.getM_robotPositionX() + Math.cos(angleToTarget) * duration * velocity;
            robot.setM_robotPositionX(applyLimits(
                    xValue,
                    Math.max(robot.getM_robotDiam1(), robot.getM_robotDiam2()) / 2,
                    this.getWidth() - (Math.max(robot.getM_robotDiam1(), robot.getM_robotDiam2()) / 2))
            );

            var yValue = robot.getM_robotPositionY() + Math.sin(angleToTarget) * duration * velocity;
            robot.setM_robotPositionY(applyLimits(
                    yValue,
                    Math.max(robot.getM_robotDiam1(), robot.getM_robotDiam2()) / 2,
                    this.getHeight() - (Math.max(robot.getM_robotDiam1(), robot.getM_robotDiam2()) / 2))
            );
        }
        else {
            robot.setM_robotDirection(asNormalizedRadians(
                    robot.getM_robotDirection() + angularVelocity * duration)
            );
        }
        for (Target targetForEat: targets)
        {
            if (round(robot.getM_robotPositionX()) == targetForEat.getM_targetPositionX()
                     && round(robot.getM_robotPositionY()) == targetForEat.getM_targetPositionY())
            {
                targets.remove(targetForEat);
            }
        }
    }

    private static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }

    private static int round(double value)
    {
        return (int)(value + 0.5);
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        drawRobot(
                g2d,
                round(robot.getM_robotPositionX()),
                round(robot.getM_robotPositionY()),
                robot.getM_robotDirection(),
                robot.getM_robotImage()
        );
        drawTarget(g2d, targets);
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction, Image robotImage)
    {
        int robotCenterX = round(robot.getM_robotPositionX());
        int robotCenterY = round(robot.getM_robotPositionY());
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.drawImage(robotImage, robotCenterX, robotCenterY, null);
    }

    private void drawTarget(Graphics2D g, ConcurrentLinkedDeque<Target> targets)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        for (Target target: targets)
        {
            g.drawImage(
                    target.getM_targetImage(),
                    target.getM_targetPositionX(),
                    target.getM_targetPositionY(),
                    null
            );
        }
    }
}