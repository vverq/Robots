package models;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.util.HashMap;

public class Barrier extends Block
{
    private volatile Image m_barrierImage;
    private final HashMap<String, String> images = new HashMap<>();
    {
        images.put("wall", "images/wall.png");
        images.put("greenWall", "images/greenWall.png");
        images.put("iceWall", "images/iceWall.png");
    };

    Barrier(int x, int y, String imageName)
    {
        super(x, y, false);
        try
        {
            m_barrierImage =  ImageIO.read(new File(images.get(imageName)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        setImage(m_barrierImage);
    }

    public Image getM_barrierImage()
    {
        return m_barrierImage;
    }
}
