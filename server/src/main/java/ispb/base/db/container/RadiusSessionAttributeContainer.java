package ispb.base.db.container;


import java.util.Date;

public interface RadiusSessionAttributeContainer {
    Date getPacketAt();
    long getSessionId();
    long getPacketType();
    String getAttribute();
    String getValue();
}
