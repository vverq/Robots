package models;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Robot
{
    private volatile double m_robotPositionX;
    private volatile double m_robotPositionY;
    private volatile double m_robotDirection;
    private volatile double m_robotDiam1 = 30;
    private volatile double m_robotDiam2 = 30;
    private volatile Image m_robotImage;

    public Robot(double x, double y, double direcion, String imageName)
    {
        try
        {
            m_robotPositionX = x;
            m_robotPositionY = y;
            m_robotDirection = direcion;
            m_robotImage = ImageIO.read(new File(imageName));
        }
        catch (IOException e)
        {
                e.printStackTrace();
        }
    }

    public double getM_robotPositionX()
    {
        return m_robotPositionX;
    }

    public void setM_robotPositionX(double x)
    {
        m_robotPositionX = x;
    }

    public double getM_robotPositionY()
    {
        return m_robotPositionY;
    }

    public void setM_robotPositionY(double y)
    {
        m_robotPositionY = y;
    }

    public double getM_robotDirection()
    {
        return m_robotDirection;
    }

    public void setM_robotDirection(double direction)
    {
        m_robotDirection = direction;
    }

    public Image getM_robotImage()
    {
        return m_robotImage;
    }

    public double getM_robotDiam1()
    {
        return m_robotDiam1;
    }

    public double getM_robotDiam2()
    {
        return m_robotDiam2;
    }
}
