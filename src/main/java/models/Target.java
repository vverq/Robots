package models;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Target
{
    private volatile int m_targetPositionX;
    private volatile int m_targetPositionY;
    private volatile Image m_targetImage;

    public Target(int x, int y, String imageName) throws IOException
    {
        m_targetPositionX = x;
        m_targetPositionY = y;
        m_targetImage = ImageIO.read(new File(imageName));
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
}
