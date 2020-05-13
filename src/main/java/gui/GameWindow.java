package gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

import models.*;
import map.LevelMap;
import models.Robot;

public class GameWindow extends RestorableJInternalFrame {
    private final GameVisualizer m_visualizer;
    private final Robot robot;
    private final RobotController robotController;
    private final LevelMap map;
    private final TargetGenerator targetGenerator;
    private final Enemy enemy;
    private final EnemyController enemyController;
    private StatesKeeper m_keeper;

    private final java.util.Timer m_timer = initTimer();

    private static java.util.Timer initTimer() {
        java.util.Timer timer = new Timer("events generator", true);
        return timer;
    }

    private final java.util.Timer timer1 = initTimer();

    GameWindow(String title, boolean autoMode, StatesKeeper keeper) throws IOException {
        super(title, true, true, false, true);
        robot = new Robot(80, 120, 0, "images/robot.png");
        map = new LevelMap("map1.txt");
        enemy = new Enemy(360, 80, "images/virus.png");
        targetGenerator = new TargetGenerator(map);
        robotController = new RobotController(robot);
        enemyController = new EnemyController(enemy);
        m_visualizer = new GameVisualizer(autoMode, robot, map, robotController, targetGenerator, enemyController, enemy);
        m_keeper = keeper;
        m_keeper.register(this, "GameWindow");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        setDefaultLocale(JInternalFrame.getDefaultLocale());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        pack();

        if (autoMode) {
            m_timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    robotController.autoMoveRobot(targetGenerator.getTargets(), map);
                }
            }, 0, 100);
        }

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                enemyController.moveEnemy(robot, map);
            }
        }, 0, 100);

        var visualizer = this;
//        if (!autoMode) {
//            this.addKeyListener(new KeyAdapter() {
//                @Override
//                public void keyPressed(KeyEvent keyEvent) {
//                    if (!isMoving.get()) {
//                        isMoving.set(true);
//                        while (true) {
////                            visualizer.processKeyEvent(keyEvent);
//                        }
//                    }
//                }
//
//                @Override
//                public void keyReleased(KeyEvent e) {
//                    isMoving.set(false);
//                }
//            });
//    }

//        timer1.schedule(new TimerTask() {
//                             @Override
//                             public void run() {
//                                 enemyController.moveEnemy(robot, map);
//                             }
//                         }, 0, 1000);
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

    @Override
    protected void processKeyEvent(KeyEvent keyEvent) {
        // todo навести красоту в этом методе, эти ифы в каждом кейсе не оч
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                if (robotController.getRobot().getAttackStatus())
                {
                    robotController.getRobot().setAttackStatus(false);
                }
                robotController.setRobotDirection(Direction.DOWN, robot);
                robotController.moveRobot(Direction.DOWN, map, targetGenerator.getTargets());
                break;
            case KeyEvent.VK_UP:
                if (robotController.getRobot().getAttackStatus())
                {
                    robotController.getRobot().setAttackStatus(false);
                }
                robotController.setRobotDirection(Direction.UP, robot);
                robotController.moveRobot(Direction.UP, map, targetGenerator.getTargets());
                break;
            case KeyEvent.VK_RIGHT:
                if (robotController.getRobot().getAttackStatus())
                {
                    robotController.getRobot().setAttackStatus(false);
                }
                robotController.setRobotDirection(Direction.RIGHT, robot);
                robotController.moveRobot(Direction.RIGHT, map, targetGenerator.getTargets());
                break;
            case KeyEvent.VK_LEFT:
                if (robotController.getRobot().getAttackStatus())
                {
                    robotController.getRobot().setAttackStatus(false);
                }
                robotController.setRobotDirection(Direction.LEFT, robot);
                robotController.moveRobot(Direction.LEFT, map, targetGenerator.getTargets());
                break;
            case KeyEvent.VK_SPACE:
                robotController.attack(map);
                break;
            default:
                if (robotController.getRobot().getAttackStatus())
                {
                    robotController.getRobot().setAttackStatus(false);
                }
                break;
        }
    }
}
