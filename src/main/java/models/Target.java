package models;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Target
{
    private volatile int m_targetPositionX;
    private volatile int m_targetPositionY;
    private volatile Image m_targetImage;
//    private volatile int m_targetWidth = 20;
//    private volatile int m_targetHeight = 20;
    private volatile int m_blockPositionX;
    private volatile int m_blockPositionY;

    public Target(int x, int y, String imageName)
    {
        m_targetPositionX = x;
        m_targetPositionY = y;
        m_blockPositionX = x / Block.getM_width();
        m_blockPositionY = y / Block.getM_height();
        try
        {
            m_targetImage = ImageIO.read(new File(imageName));
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

    public Image getM_targetImage()
    {
        return m_targetImage;
    }

    int getM_blockPositionX() { return m_blockPositionX; }

    int getM_blockPositionY() { return m_blockPositionY; }
}
