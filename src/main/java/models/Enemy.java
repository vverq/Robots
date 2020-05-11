package models;


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Enemy
{
    private volatile int enemyPositionX;
    private volatile int enemyPositionY;
    private volatile Image enemyImage;
    private volatile int enemyWidth;
    private volatile int enemyHeight;

    public Enemy(int x, int y, String imageName)
    {
        try
        {
            enemyPositionX = x;
            enemyPositionY = y;
            enemyImage = ImageIO.read(new File(imageName));
            enemyWidth = enemyImage.getWidth(null);
            enemyHeight = enemyImage.getHeight(null);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setEnemyPositionX(int x)
    {
        enemyPositionX = x;
    }

    public void setEnemyPositionY(int y)
    {
        enemyPositionY = y;
    }

    public int getEnemyPositionX()
    {
        return enemyPositionX;
    }

    public int getEnemyPositionY()
    {
        return enemyPositionY;
    }

    public Image getEnemyImage()
    {
        return enemyImage;
    }

    public int getEnemyWidth()
    {
        return enemyWidth;
    }

    public int getEnemyHeight()
    {
        return enemyHeight;
    }
}
