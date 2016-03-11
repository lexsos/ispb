package ispb.resources;


import ispb.base.Application;
import ispb.base.resources.AppResources;
import ispb.base.utils.GsonGetter;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
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

    public void loadSingletons(Application application){

        Properties singletons = new Properties();
        InputStream in = getClass().getResourceAsStream("singletons.properties");

        try {
            singletons.load(in);
        }
        catch (IOException e){
            System.out.print("Error occurs while reading singletons.properties: " + e.getMessage());
        }

        for (Iterator i=singletons.keySet().iterator(); i.hasNext(); ){
            String typeName = i.next().toString();
            String fileName = singletons.getProperty(typeName);
            try {
                Class type = Class.forName(typeName);
                Object obj = getJsonAsObject(AppResourcesImpl.class, fileName, type);
                application.addByType(type, obj);

            }
            catch (Throwable e){
                System.out.print("Error occurs while load type: " + e.getMessage());
            }
        }
    }
}
