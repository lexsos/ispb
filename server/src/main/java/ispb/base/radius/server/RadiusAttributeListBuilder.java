package ispb.base.radius.server;


import ispb.base.db.dataset.RadiusUserDataSet;
import ispb.base.radius.packet.RadiusAttributeList;

public interface RadiusAttributeListBuilder {
    RadiusAttributeList getAttributeList(RadiusUserDataSet user);
}
