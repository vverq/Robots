package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;

public class LevelMap
{
    private ConcurrentLinkedDeque<Barrier> barrierMap = new ConcurrentLinkedDeque<>();
    private ConcurrentLinkedDeque<Floor> floorMap = new ConcurrentLinkedDeque<>();

    public LevelMap(String filename)
    {
        try
        {
            Scanner sc = new Scanner(new File(filename));
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
                            barrierMap.push(new Barrier(x, y, mapElement[1]));
                        }
                        else
                        {
                            floorMap.push(new Floor(x, y, mapElement[1]));
                        }
                    }
                }
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public ConcurrentLinkedDeque<Barrier> getBarrierMap()
    {
        return barrierMap;
    }

    public ConcurrentLinkedDeque<Floor> getFloorMap()
    {
        return floorMap;
    }
}
