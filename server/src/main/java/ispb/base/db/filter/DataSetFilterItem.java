package ispb.base.db.filter;


public class DataSetFilterItem {
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
}
