package ispb.base.db.utils;

import ispb.base.db.filter.WhereStatement;
import org.hibernate.Query;
import org.hibernate.Session;

public interface QueryBuilder {
    Query getQuery(String tmpl, WhereStatement whereStatement, Session session);
}
