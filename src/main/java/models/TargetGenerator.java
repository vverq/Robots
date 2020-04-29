package models;

import gui.StateDistanceWindow;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class TargetGenerator {
    private PropertyChangeSupport support;
    private Random rnd = new Random();
    private final Timer m_timer = initTimer();
    private static Timer initTimer()
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }
    private ConcurrentLinkedDeque<Target> targets = new ConcurrentLinkedDeque<>();
    private LevelMap map;
    private final HashMap<Integer, String> targetImages = new HashMap<>();
    {
        targetImages.put(0,"images/cake.png");
        targetImages.put(1, "images/cherry.png");
        targetImages.put(2, "images/pancake.png");
        targetImages.put(3, "images/ramen.png");
        targetImages.put(4, "images/yogurt.png");
    }
    public TargetGenerator(LevelMap map) {
        this.map = map;
        support = new PropertyChangeSupport(this);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onTargetUpdateEvent();
            }
        }, 0, 1000);
    }

    private void onTargetUpdateEvent()
    {
        int maxCountTargets = 5;
        int countNewTargets = rnd.nextInt(maxCountTargets - targets.size() + 1);
        var blocksMap = map.getMap();
        for (var i = 0; i < countNewTargets; i++) {
            int x;
            int y;
            do {
                x = rnd.nextInt(map.getWidth());
                y = rnd.nextInt(map.getHeight());
            } while (!blocksMap[y][x].isAvailableForRobot());
            targets.addLast(new Target(blocksMap[y][x].getM_middlePositionX() - 10,
                    blocksMap[y][x].getM_middlePositionY() - 10, targetImages.get(rnd.nextInt(5))));
        }
        updateTargets();
    }

    public ConcurrentLinkedDeque<Target> getTargets() {
        return targets;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl)
    {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl)
    {
        support.removePropertyChangeListener(pcl);
    }

    private void updateTargets()
    {
        support.firePropertyChange("newTargets", null, targets);
    }
}
