package ispb.base.db.sort;

import ispb.base.db.field.SortDirection;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DataSetSort implements Iterable {

    private List<DataSetSortItem> sortItems;

    public DataSetSort(){
        sortItems = new LinkedList<>();
    }

    public void add(DataSetSortItem item){
        sortItems.add(item);
    }

    public void add(String fieldName, SortDirection direction){
        add(new DataSetSortItem(fieldName, direction));
    }

    public Iterator<DataSetSortItem> iterator(){
        return sortItems.iterator();
    }
}
