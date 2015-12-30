package ispb.base.resources;


public interface Config {
    String getAsStr(String key);
    int getAsInt(String key);
    float getAsFloat(String key);
    boolean getAsBool(String key);
}
