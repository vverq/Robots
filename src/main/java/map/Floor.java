package map;

import map.BlockMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Floor extends BlockMap
{
    private volatile Image m_floorImage;

    private final HashMap<String, String> images = new HashMap<>();
    {
        images.put("wood", "src/main/resources/images/wood.png");
        images.put("gravel", "src/main/resources/images/gravel.png");
    };

    Floor(int x, int y, String imageName)
    {
        super(x, y, true);
        try
        {
            m_floorImage =  ImageIO.read(new File(images.get(imageName)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        setImage(m_floorImage);
    }

    public Image getM_floorImage()
    {
        return m_floorImage;
    }
}

