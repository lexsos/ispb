package ispb.base.frontend.rest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class RestFilter implements Iterable {

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

    public RestFilter(){
        filterItems = new LinkedList();
    }

    public Iterator<RestFilterItem> iterator() {
        return filterItems.iterator();
    }

    public int size() {
        return filterItems.size();
    }

}
