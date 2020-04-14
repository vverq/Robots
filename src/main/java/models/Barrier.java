package gui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Barrier
{
    private volatile int m_barrierPositionX;
    private volatile int m_barrierPositionY;
    private volatile Image m_barrierImage;
    private final HashMap<String, String> images = new HashMap<>();
    {
        images.put("wall", "wall.png");
        images.put("greenWall", "greenWall.png");
        images.put("iceWall", "iceWall.png");
    };

    public Barrier(int x, int y, String imageName)
    {
        m_barrierPositionX = x;
        m_barrierPositionY = y;
        try
        {
            m_barrierImage =  ImageIO.read(new File(images.get(imageName)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
