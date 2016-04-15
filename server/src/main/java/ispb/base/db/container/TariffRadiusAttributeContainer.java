package ispb.base.db.container;


import ispb.base.db.fieldtype.RadiusAttributeCondition;

public interface TariffRadiusAttributeContainer {

    long getTariffId();
    String getAttributeName();
    String getAttributeValue();
    RadiusAttributeCondition getCondition();
}
