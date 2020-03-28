package gui;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class RestorableJInternalFrame extends JInternalFrame implements Restorable
{
    RestorableJInternalFrame(String title, boolean resizable,
                             boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }

    public HashMap<String, Object> getProperties() {
        var properties = new HashMap<String, Object>();
        properties.put("isMaximum", Boolean.toString(isMaximum));
        properties.put("Width", Integer.toString(getWidth()));
        properties.put("Height", Integer.toString(getHeight()));
        properties.put("isClosed", Boolean.toString(isClosed()));
        properties.put("LocationX", Integer.toString(getLocation().x));
        properties.put("LocationY", Integer.toString(getLocation().y));
        return properties;
    }

    public void setProperties(HashMap<String, Object> properties)
    {
        var width = getWidth();
        var height = getHeight();
        var location = getLocation();

        for (Map.Entry<String, Object> property : properties.entrySet()) {
            var propertyName = property.getKey();
            var propertyValue = property.getValue();
            switch (propertyName) {
                case "isMaximum":
                    isMaximum = Boolean.parseBoolean((String)propertyValue);
                    break;
                case "Width":
                    width = Integer.parseInt((String)propertyValue);
                    if (isMaximum)
                    {
                        setSize(this.getSize());
                    }
                    else
                    {
                        setSize(width, height);
                    }
                    break;
                case "Height":
                    height = Integer.parseInt((String)propertyValue);
                    break;
                case "isClosed":
                    isClosed = Boolean.parseBoolean((String)propertyValue);
                    break;
                case "LocationX":
                    location.x = Integer.parseInt((String)propertyValue);
                    if (isMaximum)
                        setLocation(location);
                    else
                        location = getLocation();
                    break;
                case "LocationY":
                    location.y = Integer.parseInt((String)propertyValue);
                    break;
                default:
                    break;
            }
        }
    }
}
