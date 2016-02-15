package ispb.base.db.filter;

public interface WhereBuilder {
    WhereStatement buildAnd(FieldSetDescriptor descriptor, DataSetFilter filter);
    WhereStatement buildAnd(FieldSetDescriptor descriptor, DataSetFilter filter1, DataSetFilter filter2);
}
