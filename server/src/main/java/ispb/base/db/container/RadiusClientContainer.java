package ispb.base.db.container;


import ispb.base.db.fieldtype.RadiusClientType;

public interface RadiusClientContainer {

    long getId();
    String getIp4Address();
    String getSecret();
    boolean isRejectInactive();
    RadiusClientType getClientType();
}
