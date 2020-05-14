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
    private GameVisualizer m_visualizer;
    private final Robot robot;
    private final RobotController robotController;
    private final TargetGenerator targetGenerator;
    private final EnemyGenerator enemyGenerator;
    private LevelMap map;
    private StatesKeeper m_keeper;

    private final java.util.Timer m_timer = initTimer();
    private final java.util.Timer m_timerMapsUpdate = initTimer();

    private static java.util.Timer initTimer() {
        java.util.Timer timer = new Timer("events generator", true);
        return timer;
    }

    private final java.util.Timer timer1 = initTimer();

    public GameWindow(String title, boolean autoMode, StatesKeeper keeper) throws IOException {
        super(title, true, true, false, true);
        LevelMap[] maps = new LevelMap[]{new LevelMap("map1.txt"), new LevelMap("map2.txt")};
        robot = new Robot(80, 120, 0, "images/robot.png");
        // map = new LevelMap("map1.txt");
        map = maps[0];
        enemyGenerator = new EnemyGenerator(map);
        targetGenerator = new TargetGenerator(map);
        robotController = new RobotController(robot);
        m_visualizer = new GameVisualizer(autoMode, robot, robotController, targetGenerator, enemyGenerator, map);
        m_keeper = keeper;
        m_keeper.register(this, "GameWindow");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        setDefaultLocale(JInternalFrame.getDefaultLocale());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        pack();


        // ЕСЛИ РАСКОММЕНТИТЬ, ТО ОГОНЬ НЕ РАБОТАЕЕЕЕЕЕЕЕЕТ
        m_timerMapsUpdate.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        if (robotController.getRobot().getM_robotPositionY() >= 300)
                        {
                            System.out.println("hehhe");
                            map = maps[1];
                            m_visualizer.setMap(map);
                            enemyGenerator.setMap(map);
                            targetGenerator.setMap(map);
                        }
                    }
                }, 0 ,100);

        if (autoMode) {
            m_timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    robotController.autoMoveRobot(targetGenerator.getTargets(), getLevelMap());
                }
            }, 0, 100);
        }

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (EnemyController enemyController : enemyGenerator.getEnemyControllers().values())
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
                robotController.moveRobot(Direction.DOWN, getLevelMap(), targetGenerator.getTargets());
                break;
            case KeyEvent.VK_UP:
                if (robotController.getRobot().getAttackStatus())
                {
                    robotController.getRobot().setAttackStatus(false);
                }
                robotController.setRobotDirection(Direction.UP, robot);
                robotController.moveRobot(Direction.UP, getLevelMap(), targetGenerator.getTargets());
                break;
            case KeyEvent.VK_RIGHT:
                if (robotController.getRobot().getAttackStatus())
                {
                    robotController.getRobot().setAttackStatus(false);
                }
                robotController.setRobotDirection(Direction.RIGHT, robot);
                robotController.moveRobot(Direction.RIGHT, getLevelMap(), targetGenerator.getTargets());
                break;
            case KeyEvent.VK_LEFT:
                if (robotController.getRobot().getAttackStatus())
                {
                    robotController.getRobot().setAttackStatus(false);
                }
                robotController.setRobotDirection(Direction.LEFT, robot);
                robotController.moveRobot(Direction.LEFT, getLevelMap(), targetGenerator.getTargets());
                break;
            case KeyEvent.VK_SPACE:
                robotController.attack(getLevelMap());
                break;
            default:
                if (robotController.getRobot().getAttackStatus())
                {
                    robotController.getRobot().setAttackStatus(false);
                }
                break;
        }
    }

    public LevelMap getLevelMap()
    {
        return map;
    }
}
