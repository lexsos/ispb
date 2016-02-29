package ispb.resources;


import ispb.base.resources.AppResources;
import ispb.base.utils.GsonGetter;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AppResourcesImpl implements AppResources {

    private ConcurrentMap<String, String> stringResourcesCache;
    private ConcurrentMap<String, Object> objectResourcesCache;
    private static AppResources instance = null;

    public static AppResources getInstance(){
        if (instance == null)
            instance = new AppResourcesImpl();
        return instance;
    }

    private AppResourcesImpl(){
        stringResourcesCache = new ConcurrentHashMap<>();
        objectResourcesCache = new ConcurrentHashMap<>();
    }

    private String getStringKey(Class clazz, String path){
        return clazz.getName() + "__" + path;
    }

    private String getObjectKey(Class clazz, String path, Class type){
        StringBuilder str = new StringBuilder();
        str.append(clazz.getName());
        str.append("__");
        str.append(path);
        str.append("__");
        str.append(type.getTypeName());
        return str.toString();
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

    public String getAsString(Class forClazz, String path){
        String key = getStringKey(forClazz, path);

        if (stringResourcesCache.containsKey(key)){
            return stringResourcesCache.get(key);
        }

        String result = loadAsString(forClazz, path);
        stringResourcesCache.put(key, result);
        return result;
    }

    public <T> T getJsonAsObject(Class forClazz, String path, Class<T> type){
        String key = getObjectKey(forClazz, path, type);
        if (objectResourcesCache.containsKey(key)){
            return (T)objectResourcesCache.get(key);
        }

        String json = getAsString(forClazz, path);
        T obj = GsonGetter.get().fromJson(json, type);
        objectResourcesCache.put(key, obj);
        return obj;
    }
}
