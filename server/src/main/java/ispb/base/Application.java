package ispb.base;


public interface Application {

    <T> void addByType(Class<T> clazz, T obj);
    <T> T getByType(Class<T> clazz);
}
