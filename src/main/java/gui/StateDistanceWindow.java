package gui;

import models.RobotController;
import models.Target;
import models.TargetGenerator;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class StateDistanceWindow extends StateWindow {
    private ConcurrentLinkedDeque<Target> targets = new ConcurrentLinkedDeque<>();
    private int[] coordinates = new int[]{0, 0};
    private RobotController robotController;
    private TargetGenerator targetGenerator;

    StateDistanceWindow(String title, RobotController robotController, TargetGenerator targetGenerator) {
        super(title);
        this.robotController = robotController;
        this.targetGenerator = targetGenerator;
        targetGenerator.addPropertyChangeListener(this);
        robotController.addPropertyChangeListener(this);
    }

    @Override
    public void dispose() {
        targetGenerator.removePropertyChangeListener(this);
        robotController.removePropertyChangeListener(this);
        super.dispose();
    }

    private int getDistance(Target target) {
        return (int) Math.sqrt(Math.pow(coordinates[0] - target.getM_targetPositionX(), 2)
                + Math.pow(coordinates[1] - target.getM_targetPositionY(), 2));
    }

    private int getShortestDistance() {
        var shortestDistance = Integer.MAX_VALUE;
        for (Target target : targets) {
            var distance = getDistance(target);
            shortestDistance = Math.min(shortestDistance, distance);
        }
        return shortestDistance;
    }

    private void setDistance() {
        if (!targets.isEmpty()) {
            setText(Integer.toString(getShortestDistance() - (int) (robotController.getRobot().getM_robotDiam1() / 2)));
        } else {
            setText("N/A");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName().equals("newTargets")) {
            targets = (ConcurrentLinkedDeque<Target>) propertyChangeEvent.getNewValue();
        } else if (propertyChangeEvent.getPropertyName().equals("newCoordinates")) {
            coordinates = (int[]) propertyChangeEvent.getNewValue();
        }
        setDistance();
    }
}
