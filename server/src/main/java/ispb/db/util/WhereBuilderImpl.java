package ispb.db.util;

import ispb.base.db.field.FieldDescriptor;
import ispb.base.db.field.FieldSetDescriptor;
import ispb.base.db.filter.*;

import java.util.*;

public class WhereBuilderImpl implements WhereBuilder {

    private String prepareFieldCondition(FieldDescriptor descriptor, DataSetFilterItem item, String placeHolderName){
        StringBuilder hql = new StringBuilder();

        hql.append("(");
        hql.append(descriptor.getHqlFieldName());
        hql.append(" ");
        hql.append(item.getOperator().toHql());
        hql.append(" ");
        hql.append(":");
        hql.append(placeHolderName);
        hql.append(")");

        return hql.toString();
    }

    private String prepareFieldCondition(FieldDescriptor descriptor, DataSetFilterItem item){
        StringBuilder hql = new StringBuilder();
        hql.append("(");
        hql.append(descriptor.getHqlFieldName());
        hql.append(" ");
        hql.append(item.getOperator().toHql());
        hql.append(")");
        return hql.toString();
    }

    private String getPlaceHolderName(int number){
        return "filterValue" + Integer.toString(number);
    }

    public WhereStatement buildAnd(FieldSetDescriptor descriptors, DataSetFilter filter){

        if (filter == null)
            return new WhereStatement();

        List<String> conditions = new LinkedList<>();
        WhereStatement where = new WhereStatement();
        int count = 0;

        for (Iterator<DataSetFilterItem> i = filter.iterator(); i.hasNext();){
            DataSetFilterItem item = i.next();
            FieldDescriptor fieldDescriptor = descriptors.getFieldDescriptor(item);

            if (fieldDescriptor == null)
                continue;

            if (!fieldDescriptor.isValidOperator(item))
                continue;

            if (item.getOperator().hasArgument()) {

                if (!fieldDescriptor.isValidType(item))
                    continue;

                String placeHolderName = getPlaceHolderName(++count);
                where.getParameters().put(placeHolderName, item.getValue());
                conditions.add(prepareFieldCondition(fieldDescriptor, item, placeHolderName));
            }
            else
                conditions.add(prepareFieldCondition(fieldDescriptor, item));
        }

        if (conditions.isEmpty())
            return where;

        StringBuilder hql = new StringBuilder();
        hql.append("where ");
        for (Iterator<String> i=conditions.iterator(); i.hasNext();){
            hql.append(i.next());
            if (i.hasNext())
                hql.append(" and ");
        }
        where.setWhere(hql.toString());
        return where;
    }

}
