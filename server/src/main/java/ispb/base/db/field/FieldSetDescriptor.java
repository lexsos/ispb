package ispb.base.db.field;

import ispb.base.db.field.FieldDescriptor;
import ispb.base.db.filter.DataSetFilterItem;
import ispb.base.db.sort.DataSetSortItem;

import java.util.Map;

public class FieldSetDescriptor {

    private Map<String, FieldDescriptor> fieldDescriptors;
    private String defaultSort;

    public FieldDescriptor getFieldDescriptor(DataSetFilterItem field){
        return fieldDescriptors.get(field.getFieldName());
    }

    public FieldDescriptor getFieldDescriptor(DataSetSortItem field){
        return fieldDescriptors.get(field.getFieldName());
    }

    public Map<String, FieldDescriptor> getFieldDescriptors() {
        return fieldDescriptors;
    }

    public void setFieldDescriptors(Map<String, FieldDescriptor> fieldDescriptors) {
        this.fieldDescriptors = fieldDescriptors;
    }

    public String getDefaultSort() {
        return defaultSort;
    }

    public void setDefaultSort(String defaultSort) {
        this.defaultSort = defaultSort;
    }
}
