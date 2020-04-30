package models;

import gui.GameWindow;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.ConcurrentLinkedDeque;

public class RobotController
{
    private static final double maxVelocity = 5;
    private Robot robot;
    private int[] newsCoordinates;
    private PropertyChangeSupport support;

    public RobotController(Robot CRobot)
    {
        robot = CRobot;
        support = new PropertyChangeSupport(this);
    }

    public void moveRobot(GameWindow.Direction direction,
                          LevelMap map, ConcurrentLinkedDeque<Target> targets)
    {
        double xValue = robot.getM_robotPositionX();
        double yValue = robot.getM_robotPositionY();
        switch(direction) {
            case UP:
                if (RobotController.isCorrect(xValue, yValue - maxVelocity, map))
                    yValue -= maxVelocity;
                break;
            case DOWN:
                if (RobotController.isCorrect(xValue,yValue + maxVelocity, map))
                    yValue += maxVelocity;
                break;
            case RIGHT:
                if (RobotController.isCorrect(xValue + maxVelocity, yValue, map))
                    xValue += maxVelocity;
                break;
            case LEFT:
                if (RobotController.isCorrect(xValue - maxVelocity, yValue, map))
                    xValue -= maxVelocity;
                break;
        }
        robot.setM_robotPositionX(applyLimits(
                xValue,
                Math.max(robot.getM_robotDiam1(), robot.getM_robotDiam2()) / 2,
                400 - (Math.max(robot.getM_robotDiam1(), robot.getM_robotDiam2()) / 2))
        );
        robot.setM_robotPositionY(applyLimits(
                yValue,
                Math.max(robot.getM_robotDiam1(), robot.getM_robotDiam2()) / 2,
                400 - (Math.max(robot.getM_robotDiam1(), robot.getM_robotDiam2()) / 2))
        );
        setNewsCoordinates(new int[]{(int) xValue, (int) yValue});
        for (Target targetForEat: targets)
        {
            if (RobotController.isRobotNearToTarget(
                    targetForEat, robot.getM_robotPositionX(), robot.getM_robotPositionY(),
                    robot.getM_robotDiam1(), robot.getM_robotDiam2()))
            {
                targets.remove(targetForEat);
            }
        }
    }

    public void autoMoveRobot(ConcurrentLinkedDeque<Target> targets, LevelMap map)
    {
        if (targets.size() == 0)
            return;
        var target = targets.getFirst();
        var x = round(robot.getM_robotPositionX()) / Block.getM_width();
        var y = round(robot.getM_robotPositionY()) / Block.getM_height();
        var currentBlock = map.getMap()[y][x];
        var targetBlock = map.getMap()[target.getM_blockPositionY()][target.getM_blockPositionX()];
        int nextBlockX;
        int nextBlockY;
        if (Math.abs(round(robot.getM_robotPositionX()) - currentBlock.getM_middlePositionX()) <=
                round(robot.getM_robotDiam1() / 2)
                && Math.abs(round(robot.getM_robotPositionY()) -
                currentBlock.getM_middlePositionY()) <= round(robot.getM_robotDiam2() / 2))  {
            var nextBlock = map.graph.getNextBlock(currentBlock, targetBlock);
            nextBlockX = nextBlock.getM_middlePositionX();
            nextBlockY = nextBlock.getM_middlePositionY();
        }
        else {
            nextBlockX = currentBlock.getM_middlePositionX() - round(robot.getM_robotDiam1() / 2);
            nextBlockY = currentBlock.getM_middlePositionY() - round(robot.getM_robotDiam2() / 2);
        }
        if (nextBlockX > robot.getM_robotPositionX()) {
            robot.setM_robotPositionX(Math.min(robot.getM_robotPositionX() + maxVelocity, nextBlockX));
            setRobotDirection(GameWindow.Direction.RIGHT, robot);
        }
        else if (nextBlockX < robot.getM_robotPositionX()) {
            robot.setM_robotPositionX(Math.max(robot.getM_robotPositionX() - maxVelocity, nextBlockX));
            setRobotDirection(GameWindow.Direction.LEFT, robot);
        }
        else if (nextBlockY > robot.getM_robotPositionY()) {
            robot.setM_robotPositionY(Math.min(robot.getM_robotPositionY() + maxVelocity, nextBlockY));
            setRobotDirection(GameWindow.Direction.DOWN, robot);
        }
        else {
            robot.setM_robotPositionY(Math.max(robot.getM_robotPositionY() - maxVelocity, nextBlockY));
            setRobotDirection(GameWindow.Direction.UP, robot);
        }
        setNewsCoordinates(new int[]{(int) robot.getM_robotPositionX(), (int) robot.getM_robotPositionY()});
        for (Target targetForEat: targets)
        {
            if (isRobotNearToTarget(
                    targetForEat, robot.getM_robotPositionX(),
                    robot.getM_robotPositionY(), robot.getM_robotDiam1(), robot.getM_robotDiam2()))
            {
                targets.remove(targetForEat);
            }
        }
    }

    private static boolean isRobotNearToTarget(
            Target targetForEat, double robotX, double robotY, double robotDiam1, double robotDiam2)
    {
        var targetX = targetForEat.getM_targetPositionX();
        var targetY = targetForEat.getM_targetPositionY();
        robotX = round(robotX);
        robotY = round(robotY);
        return targetX <= robotX + robotDiam1 / 2 && targetX >= robotX - robotDiam1 / 2
                && targetY <= robotY + robotDiam2 / 2 && targetY >= robotY - robotDiam2 / 2;
    }

    public static int round(double value)
    {
        return (int)(value + 0.5);
    }

    private static boolean isCorrect(double xPos, double yPos, LevelMap map)
    {
        int x = round(xPos) / Block.getM_width();
        int y = round(yPos) / Block.getM_height();
        return map.getMap()[y][x].isAvailableForRobot();
    }

    public void setRobotDirection(GameWindow.Direction direction, Robot robot) {
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

    private static double applyLimits(double value, double min, double max)
    {
        return Math.min(max, Math.max(value,min));
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl)
    {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl)
    {
        support.removePropertyChangeListener(pcl);
    }

    private void setNewsCoordinates(int[] coordinates)
    {
        support.firePropertyChange("newCoordinates", this.newsCoordinates, coordinates);
        this.newsCoordinates = coordinates;
    }

    public Robot getRobot()
    {
        return robot;
    }
}
