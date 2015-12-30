package ispb.resources;


import ispb.base.resources.AppResources;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AppResourcesImpl implements AppResources {

    private ConcurrentMap<String, String> stringResourcesCache;
    private static AppResources instance = null;

    public static AppResources getInstance(){
        if (instance == null)
            instance = new AppResourcesImpl();
        return instance;
    }

    private AppResourcesImpl(){
        stringResourcesCache = new ConcurrentHashMap<String, String>();
    }

    private String getKey(Class clazz, String path){
        return clazz.getName() + "__" + path;
    }

    private String loadAsString(Class clazz, String path){
        InputStream stream = clazz.getResourceAsStream(path);
        try {
            return IOUtils.toString(stream);
        }
        catch (IOException e) {
            return null;
        }
    }

    public String getAsString(Class clazz, String path){
        String key = getKey(clazz, path);

        if (stringResourcesCache.containsKey(key)){
            return stringResourcesCache.get(key);
        }

        String result = loadAsString(clazz, path);
        stringResourcesCache.put(key, result);
        return result;
    }
}
