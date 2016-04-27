package ispb.base.db.container;


import ispb.base.db.fieldtype.RadiusAttributeCondition;

public interface RadiusUserAttributeContainer {

    long getUserId();
    String getAttributeName();
    String getAttributeValue();
    RadiusAttributeCondition getCondition();
}
