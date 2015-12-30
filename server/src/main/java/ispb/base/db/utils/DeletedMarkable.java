package ispb.base.db.utils;

import java.util.Date;


public interface DeletedMarkable {

    Date getDeleteAt();
    void setDeleteAt(Date deleteAt);
}
