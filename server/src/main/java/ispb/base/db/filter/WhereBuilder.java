package ispb.base.db.filter;

import ispb.base.db.field.FieldSetDescriptor;

public interface WhereBuilder {
    WhereStatement buildAnd(FieldSetDescriptor descriptor, DataSetFilter filter);
}
