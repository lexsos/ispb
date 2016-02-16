package ispb.base.db.field;


import com.google.gson.annotations.Expose;
import ispb.base.db.filter.DataSetFilterItem;

import java.util.Set;

public class FieldDescriptor {

    private Set<CmpOperator> validOperators;
    private String hqlFieldName;
    private String valueTypeName;
    private boolean sortable;

    @Expose
    private Class valueType;

    public Set<CmpOperator> getValidOperators() {
        return validOperators;
    }

    public void setValidOperators(Set<CmpOperator> validOperators) {
        this.validOperators = validOperators;
    }

    public String getHqlFieldName() {
        return hqlFieldName;
    }

    public void setHqlFieldName(String hqlFieldName) {
        this.hqlFieldName = hqlFieldName;
    }

    public String getValueTypeName() {
        return valueTypeName;
    }

    public void setValueTypeName(String valueTypeName) {
        this.valueTypeName = valueTypeName;
    }

    public boolean isValidOperator(CmpOperator operator){
        return validOperators.contains(operator);
    }

    public boolean isValidOperator(DataSetFilterItem item){
        return isValidOperator(item.getOperator());
    }

    public boolean isValidType(DataSetFilterItem item){

        if (valueTypeName == null){
            return true;
        }

        if (valueType == null ){
            synchronized (valueTypeName){
                try {
                    valueType = Class.forName(valueTypeName);
                }
                catch (Throwable e){
                    return false;
                }
            }
        }

        return valueType.isInstance(item.getValue());
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }
}
