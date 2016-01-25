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

    public void add(String fieldName, CmpOperator operator, Object value){
        filterItems.add(new DataSetFilterItem(fieldName, operator, value));
    }

    public Iterator<DataSetFilterItem> iterator(){
        return filterItems.iterator();
    }
}
