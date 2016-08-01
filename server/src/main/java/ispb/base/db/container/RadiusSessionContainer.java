package ispb.base.db.container;


import java.util.Date;

public interface RadiusSessionContainer {
    long getCustomerId();
    long getRadiusUserId();
    long getRadiusClientId();
    Date getStartAt();
    Date getStopAt();
    Date getExpireAt();
}
