package ispb;

import ispb.base.Application;
import ispb.base.db.utils.DaoFactory;
import ispb.base.resources.AppResources;
import ispb.base.resources.Config;

public class ApplicationImpl implements Application {

    private static Application app = null;
    private Config config = null;
    private AppResources resources = null;
    private DaoFactory daoFactory = null;

    private ApplicationImpl(){}

    public static Application getApplication(){
        if (app == null)
            app = new ApplicationImpl();
        return app;
    }

    public Config getConfig(){
        return config;
    }
    public void setConfig(Config config){
        this.config = config;
    }

    public AppResources getAppResources(){
        return resources;
    }
    public void setAppResources(AppResources resources){
        this.resources = resources;
    }

    public DaoFactory getDaoFactory(){
        return daoFactory;
    }
    public void setDaoFactory(DaoFactory daoFactory){
        this.daoFactory =daoFactory;
    }
}
