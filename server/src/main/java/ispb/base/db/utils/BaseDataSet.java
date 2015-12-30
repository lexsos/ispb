package ispb.base.db.utils;

import com.google.gson.Gson;

public class BaseDataSet {

    private static final Gson GSON = new Gson();

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
