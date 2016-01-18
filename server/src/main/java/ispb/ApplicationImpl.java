package ispb;

import ispb.base.Application;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationImpl implements Application {

    private static Application app = null;

    private Map<String, Object> objByName = null;
    private Map<String, Object> objByType = null;

    private ApplicationImpl(){
        objByName = new ConcurrentHashMap<String, Object>();
        objByType = new ConcurrentHashMap<String, Object>();
    }

    public static Application getApplication(){
        if (app == null)
            app = new ApplicationImpl();
        return app;
    }

    public <T> void addByType(Class<T> clazz, T obj){
        objByType.put(clazz.getTypeName(), obj);
    }

    public <T> T getByType(Class<T> clazz){
        Object obj = objByType.get(clazz.getTypeName());
        if (clazz.isInstance(obj))
            return (T)obj;
        return null;
    }

    public void addByName(String name, Object obj){
        objByName.put(name, obj);
    }

    public <T> T getByName(String name, Class<T> clazz){
        Object obj = objByName.get(name);
        if (clazz.isInstance(obj))
            return (T)obj;
        return null;
    }

}
