package ispb.base.db.utils;

import java.util.Date;


public interface CreatedTimestampable {
    Date getCreateAt();
    void setCreateAt(Date createAt);
}
