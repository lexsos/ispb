package ispb.base.db.filter;

import java.util.Map;

public class FieldSetDescriptor {

    private Map<String, FieldDescriptor> fieldDescriptors;

    public FieldDescriptor getFieldDescriptor(DataSetFilterItem field){
        return fieldDescriptors.get(field.getFieldName());
    }

    public Map<String, FieldDescriptor> getFieldDescriptors() {
        return fieldDescriptors;
    }

    public void setFieldDescriptors(Map<String, FieldDescriptor> fieldDescriptors) {
        this.fieldDescriptors = fieldDescriptors;
    }
}
