package gui;

import models.RobotController;
import models.Target;

import java.awt.*;
import java.beans.PropertyChangeEvent;

public class StateCoordinatesWindow extends StateWindow {
    private double[] coordinates;
    private RobotController robotController;

    public StateCoordinatesWindow(String title, RobotController robotController) {
        super(title);
        this.robotController = robotController;
        robotController.addPropertyChangeListener(this);
    }

    @Override
    public void dispose() {
        robotController.removePropertyChangeListener(this);
        super.dispose();
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent)
    {
        if (propertyChangeEvent.getPropertyName().equals("newCoordinates")) {
            coordinates = (double[])propertyChangeEvent.getNewValue();
        }
        setText("X: " + coordinates[0] + " Y: " + coordinates[1]);
    }
}
