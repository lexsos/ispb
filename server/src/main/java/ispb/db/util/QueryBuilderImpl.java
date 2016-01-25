package ispb.db.util;

import ispb.base.db.filter.WhereStatement;
import ispb.base.db.utils.QueryBuilder;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Iterator;

public class QueryBuilderImpl implements QueryBuilder {

    public Query getQuery(String tmpl, WhereStatement whereStatement, Session session){
        String hql = tmpl.replaceAll("\\{where_statement\\}", whereStatement.getWhere());
        Query query = session.createQuery(hql);

        for (Iterator<String> i = whereStatement.getParameters().keySet().iterator(); i.hasNext();){
            String parameterName = i.next();
            Object value = whereStatement.getParameters().get(parameterName);
            query.setParameter(parameterName, value);
        }

        return query;
    }
}
