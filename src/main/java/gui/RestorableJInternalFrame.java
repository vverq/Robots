package gui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RestorableJInternalFrame extends JInternalFrame implements Restorable {
    //здесь несколько проблем:
    //1)Очень много кастов (но если сохранять в json не строки, будет ошибка)
    //Я так и не нашла способа аккуратно это написать
    //2)Попробуй развернуть любое окно, закрыть приложение, потом открыть и попробовать нажать на среднюю кнопку
    //Ты должна сама увидеть, что не так
    //3)Мб есть еще какие-то св-ва, к-ые надо сохранить?
    //4)Надо ли сохранять состояние mainApplicationFrame? Если да, то для него надо написать подобный класс,
    //но наследующий не от от JInternalFrame, а от JFrame
    public RestorableJInternalFrame(String title, boolean resizable,
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

    public void setproperties(HashMap<String, Object> properties)
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
                    setSize(width, height);
                    break;
                case "Height":
                    height = Integer.parseInt((String)propertyValue);
                    setSize(width, height);
                case "isClosed":
                    isClosed = Boolean.parseBoolean((String)propertyValue);
                    break;
                case "LocationX":
                    location.x = Integer.parseInt((String)propertyValue);
                    setLocation(location);
                    break;
                case "LocationY":
                    location.y = Integer.parseInt((String)propertyValue);
                    setLocation(location);
                    break;
                default:
                    break;
            }
        }
    }
}
