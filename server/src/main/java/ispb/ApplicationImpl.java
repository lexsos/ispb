package ispb;

import ispb.base.Application;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationImpl implements Application {

    private static Application app = null;
    private final Map<String, Object> objByType;

    private ApplicationImpl(){
        objByType = new ConcurrentHashMap<>();
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
}
