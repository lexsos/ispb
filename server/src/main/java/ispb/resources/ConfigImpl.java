package ispb.resources;

import ispb.base.resources.Config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ConfigImpl implements Config {

    private final Properties defaults;
    private final Properties confFile;

    public ConfigImpl(String fileName){

        defaults = new Properties();
        confFile = new Properties();

        InputStream in = getClass().getResourceAsStream("defaults.properties");
        try {
            defaults.load(in);
        }
        catch (IOException e){
            System.out.print("Error occurs while reading default config: " + e.getMessage());
        }

        FileInputStream fin;
        try {
            fin = new FileInputStream(fileName);
            confFile.load(fin);
            fin.close();
        }
        catch (FileNotFoundException e){
            System.out.println("Config file not found: " + fileName);
        }
        catch (IOException e){
            System.out.print("Error occurs while reading config file: " + fileName);
            System.out.println(" Msg:" + e.getMessage());
        }
    }

    public String getAsStr(String key){
        String value = confFile.getProperty(key);
        if (value != null)
            return value;
        return defaults.getProperty(key);
    }

    public int getAsInt(String key){
        return Integer.parseInt(getAsStr(key));
    }

    public float getAsFloat(String key){
        return Float.parseFloat(getAsStr(key));
    }

    public boolean getAsBool(String key){
        String value = getAsStr(key);
        if (value == null || value.equals("0") || value.equals("no") || value.equals("false") )
            return false;
        return true;
    }

}
