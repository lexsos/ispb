package ispb.base.db.filter;


import ispb.base.db.field.CmpOperator;
import ispb.base.db.field.FieldItem;

import java.util.Objects;

public class DataSetFilterItem  implements FieldItem {
    private String fieldName;
    private CmpOperator operator;
    private Object value;

    public DataSetFilterItem(String fieldName, CmpOperator operator, Object value){
        setFieldName(fieldName);
        setOperator(operator);
        setValue(value);
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public CmpOperator getOperator() {
        return operator;
    }

    public void setOperator(CmpOperator operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int hashCode(){
        if (fieldName != null)
            return fieldName.hashCode();
        return 0;
    }

    @SuppressWarnings("RedundantIfStatement")
    public boolean equals(Object other){
        if (this == other)
            return true;
        if (other == null)
            return false;
        if(this.getClass() != other.getClass())
            return false;

        DataSetFilterItem otherObj = (DataSetFilterItem)other;
        if (!Objects.equals(this.getFieldName(), otherObj.getFieldName()))
            return false;
        if (this.getOperator() != otherObj.getOperator())
            return false;
        if (!Objects.equals(this.getValue(), otherObj.getValue()))
            return false;
        return true;
    }
}
