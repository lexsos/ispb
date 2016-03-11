package ispb.base.resources;


import ispb.base.Application;

public interface AppResources {
    String getAsString(Class forClazz, String path);
    <T> T getJsonAsObject(Class forClazz, String path, Class<T> type);
    void loadSingletons(Application application);
}
