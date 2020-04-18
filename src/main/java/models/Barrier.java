package models;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.util.HashMap;

public class Barrier
{
    private volatile int m_barrierPositionX;
    private volatile int m_barrierPositionY;
    private volatile Image m_barrierImage;
    private volatile int m_barrierHeight = 40;
    private volatile int m_berrierWidth = 40;
    private final HashMap<String, String> images = new HashMap<>();
    {
        images.put("wall", "images/wall.png");
        images.put("greenWall", "images/greenWall.png");
        images.put("iceWall", "images/iceWall.png");
    };

    Barrier(int x, int y, String imageName)
    {
        m_barrierPositionX = x;
        m_barrierPositionY = y;
        try
        {
            m_barrierImage =  ImageIO.read(new File(images.get(imageName)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Image getM_barrierImage()
    {
        return m_barrierImage;
    }

    public int getM_barrierPositionX()
    {
        return m_barrierPositionX;
    }

    public int getM_barrierPositionY()
    {
        return m_barrierPositionY;
    }

    public int getM_barrierHeight() { return  m_barrierHeight;}

    public int getM_berrierWidth() {return m_berrierWidth;}
}
