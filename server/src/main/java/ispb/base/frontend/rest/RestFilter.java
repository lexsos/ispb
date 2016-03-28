package ispb.base.frontend.rest;

import com.google.gson.reflect.TypeToken;
import ispb.base.utils.GsonGetter;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class RestFilter implements Iterable<RestFilterItem> {

    private List<RestFilterItem> filterItems;

    private static final Type type = new TypeToken<List<RestFilterItem>>(){}.getType();

    public RestFilter(String jsonData){
        try {
            filterItems = GsonGetter.get().fromJson(jsonData, type);
        }
        catch (Throwable e){
            filterItems = new LinkedList<>();
        }
    }

    public RestFilter(){
        filterItems = new LinkedList<>();
    }

    public Iterator<RestFilterItem> iterator() {
        return filterItems.iterator();
    }
}
