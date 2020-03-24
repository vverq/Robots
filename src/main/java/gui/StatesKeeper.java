package gui;

import java.io.IOException;
import java.util.HashMap;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

public class StatesKeeper {
    //нет проверки на существование файла, могут происходить самые разные ошибки, но все, что сделает программа
    //это выведет сообщение об ошибке и продолжит свою работу
    //это не хорошо, раз уж у нас есть logWindow
    //ошибки лучше обрабатывать повыше уровнем, в MainApplication, и там же при ошибке в load, например,
    //сообщать об этом в лог
    //А еще код не потокобезопасный
    HashMap<Restorable, String> namesByRestorableObjects;
    HashMap<String, Restorable> restorableObjectsByNames;
    File storageFile;

    StatesKeeper(File storageFile)
    {
        namesByRestorableObjects = new HashMap<Restorable, String>();
        restorableObjectsByNames = new HashMap<String, Restorable>();
        this.storageFile = storageFile;
    }

    void register(Restorable restorableObject, String name)
    {
        namesByRestorableObjects.put(restorableObject, name);
        restorableObjectsByNames.put(name, restorableObject);
    }

    void unregister(Restorable restorableObject)
    {
        var name = namesByRestorableObjects.get(restorableObject);
        namesByRestorableObjects.remove(restorableObject);
        restorableObjectsByNames.remove(name);
    }

    void save() throws IOException
    {
        var fw = new FileWriter(storageFile);
        fw.write(getAllProperties().toJSONString());
        fw.close();
    }

    JSONObject getAllProperties()
    {
        var properties = new JSONObject();
        for(Map.Entry<String, Restorable> entry : restorableObjectsByNames.entrySet())
        {
            var name = entry.getKey();
            var restorableObj = entry.getValue();
            properties.put(name, restorableObj.getProperties());
        }
        return properties;
    }

    void setAllProperties(JSONObject properties)
    {
        for(Object obj : properties.entrySet()) {
            var entry = (Map.Entry<String, HashMap<String, Object>>)obj;
            var name = entry.getKey();
            var objProperies = entry.getValue();
            var restorableObj = restorableObjectsByNames.get(name);
            restorableObj.setproperties(objProperies);
        }
    }

    void load() {
        if (storageFile.length() == 0) {
            return;
        }
        var parser = new JSONParser();
        var parsedFile = new Object();
        try {
            parsedFile = parser.parse(new FileReader(storageFile));
        }
        catch(Exception e){
            System.out.println(e.toString());
            return;
        }
        JSONObject properties = (JSONObject) parsedFile;
        setAllProperties(properties);
    }
}
