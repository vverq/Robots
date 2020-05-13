package models;

import javax.swing.*;
import java.awt.*;


public class Fire
{
    private volatile Image fireImage = new ImageIcon("images/fire.gif").getImage();
    private volatile int x;
    private volatile int y;

    Fire(int x, int y)
    {
        this.x = x;
        this.y = y;
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
}
