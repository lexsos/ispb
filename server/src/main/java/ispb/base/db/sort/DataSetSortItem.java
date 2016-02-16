package ispb.base.db.sort;


import ispb.base.db.field.FieldItem;
import ispb.base.db.field.SortDirection;

public class DataSetSortItem implements FieldItem {

    private String fieldName;
    private SortDirection direction;

    public DataSetSortItem(String fieldName, SortDirection direction){
        this.fieldName = fieldName;
        this.direction = direction;
    }

    public String getFieldName() {
        return fieldName;
    }

    public SortDirection getDirection() {
        return direction;
    }
}
