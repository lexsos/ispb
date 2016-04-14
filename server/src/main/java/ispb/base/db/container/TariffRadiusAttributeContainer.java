package ispb.base.db.container;


import ispb.base.db.dataset.TariffDataSet;
import ispb.base.db.fieldtype.RadiusAttributeCondition;

public interface TariffRadiusAttributeContainer {

    TariffDataSet getTariff();
    String getAttributeName();
    String getAttributeValue();
    RadiusAttributeCondition getCondition();
}
