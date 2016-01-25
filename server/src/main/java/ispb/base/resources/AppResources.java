package ispb.base.resources;


public interface AppResources {
    String getAsString(Class forClazz, String path);
    <T> T getJsonAsObject(Class forClazz, String path, Class<T> type);
}
