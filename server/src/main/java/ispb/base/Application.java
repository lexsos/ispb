package ispb.base;

import ispb.base.db.utils.DaoFactory;
import ispb.base.resources.AppResources;
import ispb.base.resources.Config;
import ispb.base.service.LogService;
import ispb.base.service.UserAccountService;

public interface Application {

    <T> void addByType(Class<T> clazz, T obj);
    <T> T getByType(Class<T> clazz);

    void addByName(String name, Object obj);
    <T> T getByName(String name, Class<T> clazz);

    Config getConfig();
    void setConfig(Config config);

    AppResources getAppResources();
    void setAppResources(AppResources resources);

    DaoFactory getDaoFactory();
    void setDaoFactory(DaoFactory daoFactory);

    UserAccountService getUserAccountService();
    void setUserAccountService(UserAccountService userAccountService);

    LogService getLogService();
    void setLogService(LogService logService);
}
