package gui;

import java.beans.PropertyVetoException;
import java.util.HashMap;

public interface Restorable
{
    HashMap<String, Object> getProperties();
    void setProperties(HashMap<String, Object> properties);
}
