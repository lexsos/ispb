package ispb.db.util;

import ispb.base.db.field.FieldDescriptor;
import ispb.base.db.field.FieldSetDescriptor;
import ispb.base.db.field.SortDirection;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.sort.DataSetSortItem;
import ispb.base.db.sort.SortBuilder;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SortBuilderImpl implements SortBuilder {

    public String buildSort(FieldSetDescriptor descriptor, DataSetSort sort){
        StringBuilder sortStatement = new StringBuilder();
        sortStatement.append("order by ");

        List<String> orderBy = new LinkedList<>();

        for (Iterator<DataSetSortItem> i = sort.iterator(); i.hasNext();){
            DataSetSortItem item = i.next();
            FieldDescriptor fieldDescriptor = descriptor.getFieldDescriptor(item);
            if (fieldDescriptor.isSortable() && item.getDirection() == SortDirection.ASC)
                orderBy.add(fieldDescriptor.getHqlFieldName());
            if (fieldDescriptor.isSortable() && item.getDirection() == SortDirection.DESC)
                orderBy.add(fieldDescriptor.getHqlFieldName() + " DESC");
        }

        if (orderBy.isEmpty() && descriptor.getDefaultSort() == null)
            return "";

        for (Iterator<String> i = orderBy.iterator(); i.hasNext();){
            sortStatement.append(i.next());
            if (i.hasNext())
                sortStatement.append(",");
            sortStatement.append(" ");
        }

        if (orderBy.isEmpty())
            sortStatement.append(descriptor.getDefaultSort());

        return sortStatement.toString();
    }
}