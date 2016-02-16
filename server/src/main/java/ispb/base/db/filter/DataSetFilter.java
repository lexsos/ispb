package ispb.base.db.filter;


import ispb.base.db.field.CmpOperator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DataSetFilter implements Iterable {

    private List<DataSetFilterItem> filterItems;

    public DataSetFilter(){
        filterItems = new LinkedList();
    }

    private DataSetFilter(List<DataSetFilterItem> filterItems){
        this.filterItems = filterItems;
    }

    public void add(DataSetFilterItem item){
        if (!filterItems.contains(item))
            filterItems.add(item);
    }

    public void add(String fieldName, CmpOperator operator, Object value){
        add(new DataSetFilterItem(fieldName, operator, value));
    }

    public Iterator<DataSetFilterItem> iterator(){
        return filterItems.iterator();
    }

    public DataSetFilter getCopy(){
        DataSetFilter copy = new DataSetFilter(new LinkedList<>(filterItems));
        return copy;
    }
}
