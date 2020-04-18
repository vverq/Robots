package gui;

import models.Target;
import models.Robot;
import models.Barrier;
import models.Floor;
import models.LevelMap;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import javax.swing.JPanel;


// TODO: разделить логику и отрисовку
// TODO: вернуть прежний функционал тоже (например, можно создать такое же окошко, как диалоговое окно, которое
//  которое спрашивает, хочет ли пользователь восстановить состояние, только это новое окошко будет спрашивать
//  включить ли автоматический режим у котика или нет, если да, то пусть работает прежний функционал, иначе --
//  -- управление кнопочками


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

    private final HashMap<Integer, String> targetImages = new HashMap<>();
    {
        targetImages.put(0,"images/cake.png");
        targetImages.put(1, "images/cherry.png");
        targetImages.put(2, "images/pancake.png");
        targetImages.put(3, "images/ramen.png");
        targetImages.put(4, "images/yogurt.png");
    }

    private static final double maxVelocity = 5;
//    private static final double maxAngularVelocity = 0.01;

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
        }, 0, 1000);
//        m_timer.schedule(new TimerTask()
//        {
//            @Override
//            public void run()
//            {
//                onModelUpdateEvent();
//            }
//        }, 0, 10);
//        addMouseListener(new MouseAdapter()
//        {
//            @Override
//            public void mouseClicked(MouseEvent e)
//            {
//                setTargetPosition(e.getPoint());
//                repaint();
//            }
//        });
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
            while (!isCorrect(x + 25, y + 25))
            {
                x = rnd.nextInt(350);
                y = rnd.nextInt(350);
            }
            targets.addLast(new Target(x, y, targetImages.get(rnd.nextInt(5))));
        }
    }

    enum Direction {
        UP,
        DOWN,
        RIGHT,
        LEFT
    }

    protected void processKeyEvent(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                setRobotDirection(Direction.DOWN);
                moveRobot(maxVelocity, Direction.DOWN);
                break;
            case KeyEvent.VK_UP:
                setRobotDirection(Direction.UP);
                moveRobot(maxVelocity, Direction.UP);
                break;
            case KeyEvent.VK_RIGHT:
                setRobotDirection(Direction.RIGHT);
                moveRobot(maxVelocity, Direction.RIGHT);
                break;
            case KeyEvent.VK_LEFT:
                setRobotDirection(Direction.LEFT);
                moveRobot(maxVelocity, Direction.LEFT);
                break;
            default:
                break;
        }
    }

    private void setRobotDirection(Direction direction) {
        switch (direction) {
            case DOWN:
                robot.setM_robotDirection(Math.toRadians(180));
                break;
            case UP:
            case RIGHT:
            case LEFT:
                robot.setM_robotDirection(Math.toRadians(0));
                break;
            default:
                break;
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

//    private void onModelUpdateEvent()
//    {
//        var target = targets.getFirst();
//        double distance = distance(target.getM_targetPositionX(), target.getM_targetPositionY(),
//                robot.getM_robotPositionX(), robot.getM_robotPositionY());
//        if (distance < 0.5)
//        {
//            return;
//        }
//        double angleToTarget = angleTo(
//                robot.getM_robotPositionX(),
//                robot.getM_robotPositionY(),
//                target.getM_targetPositionX(),
//                target.getM_targetPositionY()
//        );
//        double angularVelocity = 0;
//        if (angleToTarget > robot.getM_robotDirection())
//        {
//            angularVelocity = maxAngularVelocity;
//        }
//        if (angleToTarget < robot.getM_robotDirection())
//        {
//            angularVelocity = -maxAngularVelocity;
//        }
//        moveRobot(maxVelocity, angularVelocity, 10, target);
//    }

    private static double applyLimits(double value, double min, double max)
    {
        return Math.min(max, Math.max(value,min));
    }

    private boolean isCorrect(int x, int y)
    {
        for (Barrier barrier: barrierMap)
        {
            int barrierX = barrier.getM_barrierPositionX() * barrier.getM_berrierWidth();
            int barrierY = barrier.getM_barrierPositionY() * barrier.getM_barrierHeight();

            if (barrierX <= x
                    && barrierX + barrier.getM_berrierWidth() >= x
                    && barrierY <=  y
                    && barrierY + barrier.getM_barrierHeight() >= y)
            {
                return false;
            }
        }
        return true;
    }

    private void moveRobot(double velocity, Direction direction) {
        double xValue = robot.getM_robotPositionX();
        double yValue = robot.getM_robotPositionY();
        switch(direction) {
            case UP:
                if (isCorrect(round(xValue), round(yValue - velocity)))
                    yValue -= velocity;
                break;
            case DOWN:
                if (isCorrect(round(xValue), round(yValue + velocity)))
                    yValue += velocity;
                break;
            case RIGHT:
                if (isCorrect(round(xValue + velocity), round(yValue)))
                    xValue += velocity;
                break;
            case LEFT:
                if (isCorrect(round(xValue - velocity), round(yValue)))
                    xValue -= velocity;
                break;
        }
        robot.setM_robotPositionX(applyLimits(
                xValue,
                Math.max(robot.getM_robotDiam1(), robot.getM_robotDiam2()) / 2,
                this.getWidth() - (Math.max(robot.getM_robotDiam1(), robot.getM_robotDiam2()) / 2))
        );
        robot.setM_robotPositionY(applyLimits(
                yValue,
                Math.max(robot.getM_robotDiam1(), robot.getM_robotDiam2()) / 2,
                this.getHeight() - (Math.max(robot.getM_robotDiam1(), robot.getM_robotDiam2()) / 2))
        );
        for (Target targetForEat: targets)
        {
            if (isRobotNearToTarget(targetForEat))
            {
                targets.remove(targetForEat);
            }
        }
    }

    /*private void moveRobot(double velocity, double angularVelocity, double duration, Target target)
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
            if (isRobotNearToTarget(targetForEat))
            {
                targets.remove(targetForEat);
            }
        }
    }*/

    private boolean isRobotNearToTarget(Target targetForEat) {
        var targetX = targetForEat.getM_targetPositionX();
        var targetY = targetForEat.getM_targetPositionY();
        var robotX = round(robot.getM_robotPositionX());
        var robotY = round(robot.getM_robotPositionY());
        var robotDiam1 = robot.getM_robotDiam1();
        var robotDiam2 = robot.getM_robotDiam2();
        return targetX <= robotX + robotDiam1 / 2 && targetX >= robotX - robotDiam1 / 2
                && targetY <= robotY + robotDiam2 / 2 && targetY >= robotY - robotDiam2 / 2;
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
        int robotCenterX = round(robot.getM_robotPositionX());
        int robotCenterY = round(robot.getM_robotPositionY());
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