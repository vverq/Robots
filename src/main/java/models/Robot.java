package models;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Robot
{
    private volatile double m_robotPositionX;
    private volatile double m_robotPositionY;
    private volatile double m_robotDirection;
    private volatile double m_robotDiam1;
    private volatile double m_robotDiam2;
    private volatile Image m_robotImage;
    private volatile boolean isAlive;
    private volatile boolean isAttack;

    public Robot(double x, double y, double direction, String imageName)
    {
        try
        {
            isAttack = false;
            isAlive = true;
            m_robotPositionX = x;
            m_robotPositionY = y;
            m_robotDirection = direction;
            m_robotImage = ImageIO.read(new File(imageName));
            m_robotDiam1 = m_robotImage.getWidth(null);
            m_robotDiam2 = m_robotImage.getHeight(null);
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

    void setM_robotPositionX(double x)
    {
        m_robotPositionX = x;
    }

    public double getM_robotPositionY()
    {
        return m_robotPositionY;
    }

    void setM_robotPositionY(double y)
    {
        m_robotPositionY = y;
    }

    public double getM_robotDirection()
    {
        return m_robotDirection;
    }

    void setM_robotDirection(double direction)
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

    double getM_robotDiam2()
    {
        return m_robotDiam2;
    }

    private void setM_robotImage(String imageName)
    {
        try {
            m_robotImage = ImageIO.read(new File(imageName));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    void killRobot()
    {
        setM_robotImage("src/main/resources/images/deadCat.png");
        isAlive = false;
    }

    boolean isAlive()
    {
        return isAlive;
    }

    public void setAttackStatus(boolean isAttack)
    {
        this.isAttack = isAttack;
    }

    public boolean getAttackStatus()
    {
        return isAttack;
    }
}
