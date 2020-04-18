package models;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Floor
{
    private volatile int m_floorPositionX;
    private volatile int m_floorPositionY;
    private volatile Image m_floorImage;
    private volatile int m_floorWidth = 40;
    private volatile int m_floorHeight = 40;

    private final HashMap<String, String> images = new HashMap<>();
    {
        images.put("wood", "images/wood.png");
        images.put("gravel", "images/gravel.png");
    };

    Floor(int x, int y, String imageName)
    {
        try
        {
            m_floorPositionX = x;
            m_floorPositionY = y;
            m_floorImage =  ImageIO.read(new File(images.get(imageName)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Image getM_floorImage()
    {
        return m_floorImage;
    }

    public int getM_floorPositionX()
    {
        return m_floorPositionX;
    }

    public int getM_floorPositionY()
    {
        return m_floorPositionY;
    }

    public int getM_floorWidth() { return  m_floorWidth;}

    public int getM_floorHeight() {return m_floorHeight;}
}

