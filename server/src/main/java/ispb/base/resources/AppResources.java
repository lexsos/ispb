package ispb.base.resources;


import ispb.base.Application;

import java.util.Properties;

public interface AppResources {
    String getAsString(Class forClazz, String path);
    <T> T getJsonAsObject(Class forClazz, String path, Class<T> type);
    Properties getAsProperties(Class forClazz, String path);
    void loadSingletons(Application application);
}
