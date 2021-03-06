package ispb.db.util;

import ispb.base.db.field.FieldSetDescriptor;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.filter.WhereBuilder;
import ispb.base.db.filter.WhereStatement;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.sort.SortBuilder;
import ispb.base.db.utils.QueryBuilder;
import org.hibernate.Query;
import org.hibernate.Session;

public class QueryBuilderImpl implements QueryBuilder {

    private final WhereBuilder whereBuilder;
    private final SortBuilder sortBuilder;

    public QueryBuilderImpl(WhereBuilder whereBuilder, SortBuilder sortBuilder){
        this.whereBuilder = whereBuilder;
        this.sortBuilder = sortBuilder;
    }

    public Query getQuery(String tmpl, Session session, FieldSetDescriptor fieldsDescriptor, DataSetFilter filter, DataSetSort sort){

        WhereStatement whereStatement = whereBuilder.buildAnd(fieldsDescriptor, filter);
        String sortStatement = sortBuilder.buildSort(fieldsDescriptor, sort);

        String hql = tmpl.replaceAll("\\{where_statement\\}", whereStatement.getWhere());
        hql = hql.replaceAll("\\{sort_statement\\}", sortStatement);
        Query query = session.createQuery(hql);

        for (String parameterName : whereStatement.getParameters().keySet()) {
            Object value = whereStatement.getParameters().get(parameterName);
            query.setParameter(parameterName, value);
        }

        return query;
    }
}
