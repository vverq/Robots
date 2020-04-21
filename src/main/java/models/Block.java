package models;

import java.awt.*;

public class Block
{
    private volatile int m_positionX;
    private volatile int m_positionY;
    private volatile boolean m_isAvailable;
    private static volatile int m_height = 40;
    private static volatile int m_width = 40;
    private Image m_image;

    Block(int x, int y, boolean isAvailable) {
        m_isAvailable = isAvailable;
        m_positionX = x;
        m_positionY = y;
        m_image = null;
    }

    public Image getM_image() { return m_image; }

    public int getM_positionX()
    {
        return m_positionX;
    }

    public int getM_positionY()
    {
        return m_positionY;
    }

    public static int getM_height() { return  m_height;}

    public static int getM_width() { return m_width; }

    public int getM_middlePositionX() { return m_width * m_positionX + m_width / 2; }

    public int getM_middlePositionY() { return m_height * m_positionY + m_height / 2; }

    public boolean isAvailableForRobot() { return m_isAvailable; }

    void setImage(Image image) { m_image = image; }

    public Image getImage() { return m_image; }
}
