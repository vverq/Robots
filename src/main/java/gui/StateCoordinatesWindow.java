package gui;

import models.RobotController;
import models.Target;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;

public class StateCoordinatesWindow extends StateWindow
{
    private int[] coordinates;
    private RobotController robotController;

    StateCoordinatesWindow(String title, RobotController robotController)
    {
        super(title);
        this.robotController = robotController;
        robotController.addPropertyChangeListener(this);
    }

    @Override
    public void dispose()
    {
        robotController.removePropertyChangeListener(this);
        super.dispose();
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent)
    {
        if (propertyChangeEvent.getPropertyName().equals("newCoordinates"))
        {
            coordinates = (int[])propertyChangeEvent.getNewValue();
        }
        setText("X: " + coordinates[0] + " Y: " + coordinates[1]);
    }
}
