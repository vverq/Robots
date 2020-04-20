package models;

import gui.GameVisualizer;

import java.util.concurrent.ConcurrentLinkedDeque;

public class RobotController
{
    private static final double maxVelocity = 5;
    private static final double maxAngularVelocity = 0.01;

    // нужно запретить котику бегать по барьерам и еще есть дублирвание кода в методах moveRobot и
    // autoMoveRobot, я боюсь туда залезать

    public void moveRobot(GameVisualizer.Direction direction, Robot robot,
                          ConcurrentLinkedDeque<Barrier> barrierMap, ConcurrentLinkedDeque<Target> targets)
    {
        double xValue = robot.getM_robotPositionX();
        double yValue = robot.getM_robotPositionY();
        switch(direction) {
            case UP:
                if (RobotController.isCorrect(xValue, yValue - maxVelocity, barrierMap))
                    yValue -= maxVelocity;
                break;
            case DOWN:
                if (RobotController.isCorrect(xValue,yValue + maxVelocity, barrierMap))
                    yValue += maxVelocity;
                break;
            case RIGHT:
                if (RobotController.isCorrect(xValue + maxVelocity, yValue, barrierMap))
                    xValue += maxVelocity;
                break;
            case LEFT:
                if (RobotController.isCorrect(xValue - maxVelocity, yValue, barrierMap))
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

    public void autoMoveRobot(double duration, Robot robot, ConcurrentLinkedDeque<Target> targets)
    {
        var velocity = maxVelocity;
        var target = targets.getFirst();
        double distance = RobotController.distance(target.getM_targetPositionX(), target.getM_targetPositionY(),
                robot.getM_robotPositionX(), robot.getM_robotPositionY());
        if (distance < 0.5)
        {
            return;
        }
        double angleToTarget = RobotController.angleTo(
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
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        angleToTarget = RobotController.angleTo(
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
                    400 - (Math.max(robot.getM_robotDiam1(), robot.getM_robotDiam2()) / 2))
            );

            var yValue = robot.getM_robotPositionY() + Math.sin(angleToTarget) * duration * velocity;
            robot.setM_robotPositionY(applyLimits(
                    yValue,
                    Math.max(robot.getM_robotDiam1(), robot.getM_robotDiam2()) / 2,
                    400 - (Math.max(robot.getM_robotDiam1(), robot.getM_robotDiam2()) / 2))
            );
        }
        else {
            robot.setM_robotDirection(RobotController.asNormalizedRadians(
                    robot.getM_robotDirection() + angularVelocity * duration)
            );
        }
        for (Target targetForEat: targets)
        {
            if (RobotController.isRobotNearToTarget(
                    targetForEat, robot.getM_robotPositionX(),
                    robot.getM_robotPositionY(), robot.getM_robotDiam1(), robot.getM_robotDiam2()))
            {
                targets.remove(targetForEat);
            }
        }
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

    public static boolean isCorrect(double xPos, double yPos, ConcurrentLinkedDeque<Barrier> barrierMap)
    {
        int x = round(xPos);
        int y = round(yPos);
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

    public void setRobotDirection(GameVisualizer.Direction direction, Robot robot) {
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
}
