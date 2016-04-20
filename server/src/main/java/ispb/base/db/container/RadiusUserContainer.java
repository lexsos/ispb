package ispb.base.db.container;


import java.util.Date;

public interface RadiusUserContainer {
    long getId();
    Date getDeleteAt();
    Date getCreateAt();
    String getUserName();
    String getPassword();
    String getIp4Address();
    Long getCustomerId();
}
