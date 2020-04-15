package models;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Target
{
    private volatile int m_targetPositionX;
    private volatile int m_targetPositionY;
    private volatile Image m_targetImage;
    private volatile int m_targetWidth = 20;
    private volatile int m_targetHeight = 20;

    private final HashMap<Integer, String> images = new HashMap<>();
    {
        images.put(0,"images/cake.png");
        images.put(1, "images/cherry.png");
        images.put(2, "images/pancake.png");
        images.put(3, "images/ramen.png");
        images.put(4, "images/yogurt.png");
    }

    public Target(int x, int y, int num)
    {
        m_targetPositionX = x;
        m_targetPositionY = y;
        try
        {
            m_targetImage = ImageIO.read(new File(images.get(num)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public int getM_targetPositionX()
    {
        return m_targetPositionX;
    }

    public int getM_targetPositionY()
    {
        return m_targetPositionY;
    }

    public void setM_targetPositionX(int x)
    {
        m_targetPositionX = x;
    }

    public void setM_targetPositionY(int y)
    {
        m_targetPositionY = y;
    }

    public Image getM_targetImage()
    {
        return m_targetImage;
    }

    public int getM_targetWidth() { return m_targetWidth; }

    public int getM_targetHeight() { return m_targetHeight; }
}
