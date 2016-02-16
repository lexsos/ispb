package ispb.base.db.sort;

import ispb.base.db.field.FieldSetDescriptor;

public interface SortBuilder {
    String buildSort(FieldSetDescriptor descriptor, DataSetSort sort);
}
