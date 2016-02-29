package ispb.base.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonGetter {

    private static final Gson GSON;

    static {
        GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    public static Gson get(){
        return GSON;
    }
}
