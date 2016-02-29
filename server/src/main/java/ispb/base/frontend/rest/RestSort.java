package ispb.base.frontend.rest;

import com.google.gson.reflect.TypeToken;
import ispb.base.utils.GsonGetter;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class RestSort implements Iterable {

    private List<RestSortItem> sortItems;

    private static Type type = new TypeToken<List<RestSortItem>>(){}.getType();

    public RestSort(String jsonData){
        try {
            sortItems = GsonGetter.get().fromJson(jsonData, type);
        }
        catch (Throwable e){
            sortItems = new LinkedList();
        }
    }

    public RestSort(){
        sortItems = new LinkedList();
    }

    public Iterator<RestSortItem> iterator(){
        return sortItems.iterator();
    }
}
