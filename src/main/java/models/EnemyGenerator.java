package models;

import map.LevelMap;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedDeque;

public class EnemyGenerator {
    private PropertyChangeSupport support;
    private Random rnd = new Random();
    private final Timer m_timer = initTimer();
    private static Timer initTimer()
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }
    private ConcurrentLinkedDeque<Enemy> enemies = new ConcurrentLinkedDeque<>();
    private HashMap<Enemy, EnemyController> enemyControllers = new HashMap();
    private LevelMap map;

    public EnemyGenerator (LevelMap map)
    {
        this.map = map;
        support = new PropertyChangeSupport(this);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onEnemyUpdateEvent();
            }
        }, 0, 10000);
    }

    private void onEnemyUpdateEvent()
    {
        int maxCountTargets = 3;
        int countNewTargets = rnd.nextInt(maxCountTargets - enemies.size() + 1);
        var blocksMap = map.getMap();
        for (var i = 0; i < countNewTargets; i++)
        {
            int x;
            int y;
            do {
                x = rnd.nextInt(map.getWidth());
                y = rnd.nextInt(map.getHeight());
            } while (!blocksMap[y][x].isAvailableForRobot());
            var newEnemy = new Enemy(blocksMap[y][x].getM_middlePositionX(),
                    blocksMap[y][x].getM_middlePositionY(), "images/virus.png");
            enemies.addLast(newEnemy);
            enemyControllers.put(newEnemy, new EnemyController(newEnemy));
        }
        updateEnemies();
    }

    public ConcurrentLinkedDeque<Enemy> getEnemies() { return enemies; }

    public void clear()
    {
        enemies = new ConcurrentLinkedDeque<Enemy>();
        enemyControllers = new HashMap<Enemy, EnemyController>();
    }

    public HashMap<Enemy, EnemyController> getEnemyControllers() { return enemyControllers; }

    public void setMap(LevelMap map) { this.map = map; }

    public void addPropertyChangeListener(PropertyChangeListener pcl)
    {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl)
    {
        support.removePropertyChangeListener(pcl);
    }

    private void updateEnemies()
    {
        support.firePropertyChange("newEnemies", null, enemies);
    }
}
