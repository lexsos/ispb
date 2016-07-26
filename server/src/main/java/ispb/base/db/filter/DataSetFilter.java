package ispb.base.db.filter;


import ispb.base.db.field.CmpOperator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class DataSetFilter implements Iterable<DataSetFilterItem> {

    private final List<DataSetFilterItem> filterItems;

    public DataSetFilter(){
        filterItems = new LinkedList<>();
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
        return new DataSetFilter(new LinkedList<>(filterItems));
    }

    public DataSetFilterItem getFirst(String fieldName){
        for (DataSetFilterItem item: filterItems){
            if (Objects.equals(item.getFieldName(), fieldName))
                return item;
        }
        return null;
    }
}
