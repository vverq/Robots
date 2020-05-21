package models;

import javax.swing.*;
import java.awt.*;


public class Fire
{
    private volatile Image fireImage = new ImageIcon("images/fire.gif").getImage();
    private volatile int height = fireImage.getHeight(null);
    private volatile int width = fireImage.getWidth(null);
    private volatile int x;
    private volatile int y;

    Fire(int y, int x)
    {
        this.y = y;
        this.x = x;
    }

    public Image getFireImage()
    {
        return fireImage;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getMiddleX() { return x + width / 2; }

    public int getMiddleY() { return y + height / 2; }
}
