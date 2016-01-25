package ispb.base.db.filter;

public interface WhereBuilder {
    WhereStatement buildAnd(FieldSetDescriptor descriptor, DataSetFilter filter);
}
