package ispb.base;

import ispb.base.db.utils.DaoFactory;
import ispb.base.resources.AppResources;
import ispb.base.resources.Config;

public interface Application {

    Config getConfig();
    void setConfig(Config config);

    AppResources getAppResources();
    void setAppResources(AppResources resources);

    DaoFactory getDaoFactory();
    void setDaoFactory(DaoFactory daoFactory);
}
