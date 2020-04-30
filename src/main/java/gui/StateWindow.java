package gui;

import models.RobotController;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class StateWindow extends JInternalFrame implements PropertyChangeListener {
    private JLabel label;
    private int[] news;

    StateWindow(String title)
    {
        super(title, false, false, false, true);
        label = new JLabel();
        label.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        label.setVerticalAlignment((int) CENTER_ALIGNMENT);
        label.setFont(new Font("Courier New", Font.BOLD, 14));
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.lightGray);
        panel.add(label);
        getContentPane().add(panel);
        pack();
    }

    void setText(String text)
    {
        label.setText(text);
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent)
    {
        this.setText(propertyChangeEvent.getNewValue().toString());
    }
}
