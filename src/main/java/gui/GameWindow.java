package gui;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import models.Robot;
import models.RobotController;
import models.LevelMap;
import models.TargetGenerator;

public class GameWindow extends RestorableJInternalFrame
{
    private final GameVisualizer m_visualizer;
    private final Robot robot;
    private final RobotController robotController;
    private final LevelMap map;
    private final TargetGenerator targetGenerator;
    private StatesKeeper m_keeper;

    private final java.util.Timer m_timer = initTimer();
    private static java.util.Timer initTimer()
    {
        java.util.Timer timer = new Timer("events generator", true);
        return timer;
    }

    GameWindow(String title, boolean autoMode, StatesKeeper keeper) throws IOException
    {
        super(title, true, true, false, true);
        robot = new Robot(80, 120, 0, "images/robot.png");
        map = new LevelMap("map1.txt");
        targetGenerator = new TargetGenerator(map);
        robotController = new RobotController(robot);
        m_visualizer = new GameVisualizer(autoMode, robot, map, robotController, targetGenerator);
        m_keeper = keeper;
        m_keeper.register(this, "GameWindow");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        setDefaultLocale(JInternalFrame.getDefaultLocale());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        pack();

        if (autoMode)
        {
            m_timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    robotController.autoMoveRobot(targetGenerator.getTargets(), map);
                }
            }, 0, 100);
        }

        var visualizer = this;
        if (!autoMode) {
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    visualizer.processKeyEvent(keyEvent);
                }
            });
        }
        setFocusable(true);
    }

    public enum Direction
    {
        UP,
        DOWN,
        RIGHT,
        LEFT
    }

    RobotController getRobotController() {
        return robotController;
    }

    TargetGenerator getTargetGenerator() {
        return targetGenerator;
    }

    public void dispose()
    {
        setVisible(false);
        m_keeper.unregister(this.getName());
        super.dispose();
    }

    void updateNames(Locale locale)
    {
        var bundle = ResourceBundle.getBundle("MainApplicationFrameBundle", locale);
        setLocale(locale);
        setTitle(bundle.getString("gameWindowTitle"));
    }

    protected void processKeyEvent(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                robotController.setRobotDirection(Direction.DOWN, robot);
                robotController.moveRobot(Direction.DOWN, map, targetGenerator.getTargets());
                break;
            case KeyEvent.VK_UP:
                robotController.setRobotDirection(Direction.UP, robot);
                robotController.moveRobot(Direction.UP, map, targetGenerator.getTargets());
                break;
            case KeyEvent.VK_RIGHT:
                robotController.setRobotDirection(Direction.RIGHT, robot);
                robotController.moveRobot(Direction.RIGHT, map, targetGenerator.getTargets());
                break;
            case KeyEvent.VK_LEFT:
                robotController.setRobotDirection(Direction.LEFT, robot);
                robotController.moveRobot(Direction.LEFT, map, targetGenerator.getTargets());
                break;
            default:
                break;
        }
    }
}
