package gui;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


class StatesKeeper
{
    private HashMap<String, Restorable> restorableObjects;
    private JSONObject savedProperties;
    private File storageFile;

    StatesKeeper(File storageFile)
    {
        restorableObjects = new HashMap<>();
        this.storageFile = storageFile;
    }

    void register(Restorable restorableObject, String name)
    {
        restorableObjects.put(name, restorableObject);
    }

    void unregister(String name)
    {
        restorableObjects.remove(name);
    }

    void save() throws IOException
    {
        savedProperties = null;
        var fw = new FileWriter(storageFile);
        fw.write(getAllProperties().toJSONString());
        fw.close();
    }

    private JSONObject getAllProperties()
    {
        var properties = new JSONObject();
        for(ConcurrentMap.Entry<String, Restorable> entry : restorableObjects.entrySet())
        {
            var name = entry.getKey();
            var restorableObj = entry.getValue();
            properties.put(name, restorableObj.getProperties());
        }
        return properties;
    }

    private void setAllProperties(JSONObject properties)
    {
        for (Object obj : properties.entrySet())
        {
            var entry = (ConcurrentMap.Entry<String, HashMap<String, Object>>)obj;
            var name = entry.getKey();
            var objProperies = entry.getValue();
            var restorableObject = restorableObjects.get(name);
            restorableObject.setProperties(objProperies);
        }
    }

    JSONObject getPropertiesFromFile() throws IOException, ParseException {
        if (storageFile.length() == 0) {
            return null;
        }
        var parser = new JSONParser();
        var parsedFile = new Object();
        parsedFile = parser.parse(new FileReader(storageFile));
        JSONObject properties = (JSONObject) parsedFile;
        return properties;
    }

    boolean canLoad()
    {
        try {
            if (savedProperties == null) {
                savedProperties = getPropertiesFromFile();
                return savedProperties != null;
            }
            else
                return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    void load() throws ParseException, IOException
    {
        JSONObject properties;
        if (savedProperties == null) {
            savedProperties = getPropertiesFromFile();
        }
        properties = savedProperties;
        setAllProperties(properties);
    }
}
