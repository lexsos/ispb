package ispb.base.radius;


import ispb.base.db.dataset.RadiusUserDataSet;

public interface RadiusAttributeListBuilder {
    RadiusAttributeList getAttributeList(RadiusUserDataSet user);
}
