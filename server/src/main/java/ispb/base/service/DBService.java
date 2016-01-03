package ispb.base.service;

import ispb.base.db.utils.DaoFactory;

public interface DBService {

    DaoFactory getDaoFactory();
    void clearDB();
    void migrate();
}
