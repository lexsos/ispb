package ispb.base.frontend.utils;

import com.google.gson.Gson;
import ispb.base.frontend.utils.Jsonable;

public class RestBase implements Jsonable {

    private static final Gson GSON = new Gson();

    public String toJson(){
        return GSON.toJson(this);
    }
}
