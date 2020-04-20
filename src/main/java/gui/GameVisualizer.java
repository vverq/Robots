package gui;

import models.*;
import models.Robot;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import javax.swing.JPanel;


public class GameVisualizer extends JPanel
{
    private Random rnd = new Random();
    private final Timer m_timer = initTimer();
    private static Timer initTimer()
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private Robot robot = new Robot(200, 200, 0, "images/robot.png");
    private LevelMap map = new LevelMap("map1.txt");
    private ConcurrentLinkedDeque<Barrier> barrierMap = map.getBarrierMap();
    private ConcurrentLinkedDeque<Floor> floorMap = map.getFloorMap();
    private ConcurrentLinkedDeque<Target> targets = new ConcurrentLinkedDeque<>();
    private RobotController robotController = new RobotController();

    private final HashMap<Integer, String> targetImages = new HashMap<>();
    {
        targetImages.put(0,"images/cake.png");
        targetImages.put(1, "images/cherry.png");
        targetImages.put(2, "images/pancake.png");
        targetImages.put(3, "images/ramen.png");
        targetImages.put(4, "images/yogurt.png");
    }

    GameVisualizer(boolean autoMode)
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
        }, 0, 1000);

        if (autoMode)
        {
            m_timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    robotController.autoMoveRobot(10, robot, targets);
                }
            }, 0, 200);
        }

        var visualizer = this;
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                visualizer.processKeyEvent(keyEvent);
            }
        });
        setFocusable(true);
        setDoubleBuffered(true);
    }

    private void onTargetUpdateEvent()
    {
        int maxCountTargets = 5;
        int countNewTargets = rnd.nextInt(maxCountTargets - targets.size() + 1);
        for (var i = 0; i < countNewTargets; i++) {
            var x = rnd.nextInt(350);
            var y = rnd.nextInt(350);
            while (!RobotController.isCorrect(x + 25, y + 25, barrierMap))
            {
                x = rnd.nextInt(350);
                y = rnd.nextInt(350);
            }
            targets.addLast(new Target(x, y, targetImages.get(rnd.nextInt(5))));
        }
    }

    public enum Direction {
        UP,
        DOWN,
        RIGHT,
        LEFT
    }

    protected void processKeyEvent(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                robotController.setRobotDirection(Direction.DOWN, robot);
                robotController.moveRobot(Direction.DOWN, robot, barrierMap, targets);
                break;
            case KeyEvent.VK_UP:
                robotController.setRobotDirection(Direction.UP, robot);
                robotController.moveRobot(Direction.UP, robot, barrierMap, targets);
                break;
            case KeyEvent.VK_RIGHT:
                robotController.setRobotDirection(Direction.RIGHT, robot);
                robotController.moveRobot(Direction.RIGHT, robot, barrierMap, targets);
                break;
            case KeyEvent.VK_LEFT:
                robotController.setRobotDirection(Direction.LEFT, robot);
                robotController.moveRobot(Direction.LEFT, robot, barrierMap, targets);
                break;
            default:
                break;
        }
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
    }

    private void drawMap(Graphics2D g)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 15, 15);
        g.setTransform(t);
        for (Barrier barrier: barrierMap)
        {
            g.drawImage(
                    barrier.getM_barrierImage(),
                    barrier.getM_barrierPositionX() * barrier.getM_berrierWidth(),
                    barrier.getM_barrierPositionY() * barrier.getM_barrierHeight(),
                    null
            );
        }
        for (Floor floor: floorMap)
        {
            g.drawImage(
                    floor.getM_floorImage(),
                    floor.getM_floorPositionX() * floor.getM_floorWidth(),
                    floor.getM_floorPositionY() * floor.getM_floorHeight(),
                    null
            );
        }
    }

    private void drawRobot(Graphics2D g)
    {
        int robotCenterX = RobotController.round(robot.getM_robotPositionX());
        int robotCenterY = RobotController.round(robot.getM_robotPositionY());
        AffineTransform t = AffineTransform.getRotateInstance(robot.getM_robotDirection(), robotCenterX, robotCenterY);
        g.setTransform(t);
        g.drawImage(
                robot.getM_robotImage(),
                robotCenterX - robot.getM_robotImage().getHeight(null) / 2,
                robotCenterY - robot.getM_robotImage().getWidth(null) / 2,
                null
        );
    }

    private void drawTarget(Graphics2D g)
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