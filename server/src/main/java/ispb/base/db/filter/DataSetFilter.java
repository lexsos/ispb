package ispb.base.db.filter;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DataSetFilter implements Iterable {

    private List<DataSetFilterItem> filterItems;

    public DataSetFilter(){
        filterItems = new LinkedList();
    }

    public void add(DataSetFilterItem item){
        filterItems.add(item);
    }

    public Iterator<DataSetFilterItem> iterator(){
        return filterItems.iterator();
    }
}
