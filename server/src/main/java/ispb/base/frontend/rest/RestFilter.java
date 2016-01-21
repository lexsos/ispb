package ispb.base.frontend.rest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;


public class RestFilter {

    private List<RestFilterItem> filterItems;

    private static Gson gson=new Gson();
    private static Type type = new TypeToken<List<RestFilterItem>>(){}.getType();

    public RestFilter(String jsonData){
        try {
            filterItems = gson.fromJson(jsonData, type);
        }
        catch (Throwable e){
            filterItems = new LinkedList();
        }
    }

    public int getCount() {
        return filterItems.size();
    }

    public RestFilterItem getItem(int i) {
        return filterItems.get(i);
    }
}
