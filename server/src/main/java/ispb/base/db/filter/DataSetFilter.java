package ispb.base.db.filter;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class DataSetFilter implements Iterable {

    private List<DataSetFilterItem> filterItems;

    public DataSetFilter(){
        filterItems = new LinkedList();
    }

    public DataSetFilter(List<DataSetFilter> filters) {
        filterItems = new LinkedList();
        for(Iterator<DataSetFilter> i=filters.iterator();i.hasNext();)
            for(Iterator<DataSetFilterItem> j = i.next().iterator();j.hasNext();)
                add(j.next());
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
}
