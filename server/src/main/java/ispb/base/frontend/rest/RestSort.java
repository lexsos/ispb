package ispb.base.frontend.rest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class RestSort implements Iterable {

    private List<RestSortItem> sortItems;

    private static Gson gson=new Gson();
    private static Type type = new TypeToken<List<RestSortItem>>(){}.getType();

    public RestSort(String jsonData){
        try {
            sortItems = gson.fromJson(jsonData, type);
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
