package map;

import models.Fire;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;

public class LevelMap
{
    private int width;
    private int height;
    private BlockMap[][] map;
    private ConcurrentLinkedDeque[][] fireMap;
    private volatile ConcurrentLinkedDeque<Fire> fires;
    public GraphFromMap graph;

    public LevelMap(String filename)
    {
        fires = new ConcurrentLinkedDeque<Fire>();
        try
        {
            Scanner sc = new Scanner(new File(filename));
            var strMapSize = sc.nextLine().split(" ");
            width = Integer.parseInt(strMapSize[0]);
            height = Integer.parseInt(strMapSize[1]);
            map = new BlockMap[height][width];
            fireMap = new ConcurrentLinkedDeque[height][width];
            graph = new GraphFromMap(this);
            while (sc.hasNext())
            {
                String str = sc.nextLine();
                String[] mapElement = str.split(" ");
                for (int x = Integer.parseInt(mapElement[2]); x <= Integer.parseInt(mapElement[3]); x++)
                {
                    for (int y = Integer.parseInt(mapElement[4]); y <= Integer.parseInt(mapElement[5]); y++)
                    {
                        if (mapElement[0].equals("b"))
                        {
                            map[y][x] = new Barrier(x, y, mapElement[1]);
                        }
                        else
                        {
                            map[y][x] = new Floor(x, y, mapElement[1]);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public void addFire(Fire fire) {
        var x = fire.getMiddleX() / BlockMap.getM_width();
        var y = fire.getMiddleY() / BlockMap.getM_height();
        if (fireMap[y][x] == null)
            fireMap[y][x] = new ConcurrentLinkedDeque<Fire>();
        fireMap[y][x].addLast(fire);
        fires.addLast(fire);
    }

    public BlockMap[][] getMap() { return map; }

    public ConcurrentLinkedDeque[][] getFireMap() { return fireMap; }

    public ConcurrentLinkedDeque<Fire> getFires() { return fires; }

//    public FireMap[][] getFireMap()
//    {
//        return fireMap;
//    }
}
