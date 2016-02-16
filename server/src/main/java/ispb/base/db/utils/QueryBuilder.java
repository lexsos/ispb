package ispb.base.db.utils;

import ispb.base.db.field.FieldSetDescriptor;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import org.hibernate.Query;
import org.hibernate.Session;

public interface QueryBuilder {
    Query getQuery(String tmpl, Session session, FieldSetDescriptor fieldsDescriptor, DataSetFilter filter, DataSetSort sort);
}
