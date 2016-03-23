package ispb.main;

import ispb.ApplicationImpl;
import ispb.base.Application;
import ispb.base.db.filter.WhereBuilder;
import ispb.base.db.sort.SortBuilder;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.resources.AppResources;
import ispb.base.resources.Config;
import ispb.base.service.DBService;
import ispb.base.service.LogService;
import ispb.db.DBServiceImpl;
import ispb.db.util.QueryBuilderImpl;
import ispb.db.util.SortBuilderImpl;
import ispb.db.util.WhereBuilderImpl;
import ispb.log.LogServiceImpl;
import ispb.resources.AppResourcesImpl;
import ispb.resources.ConfigImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


class ClearDb {

    public static void run (String configFile) {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Do you really clear data base (yes/no): ");
            String s = bufferRead.readLine();
            if (!s.equals("yes"))
                return;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return;
        }


        Application application = ApplicationImpl.getApplication();

        Config conf = new ConfigImpl(configFile );
        application.addByType(Config.class, conf);

        AppResources resources = AppResourcesImpl.getInstance();
        resources.loadSingletons(application);
        application.addByType(AppResources.class, resources);

        LogService logService  = new LogServiceImpl(conf);
        application.addByType(LogService.class, logService);

        WhereBuilder whereBuilder = new WhereBuilderImpl();
        SortBuilder sortBuilder = new SortBuilderImpl();
        QueryBuilder queryBuilder = new QueryBuilderImpl(whereBuilder, sortBuilder);

        DBService dbService = new DBServiceImpl(conf, resources, logService, queryBuilder);
        application.addByType(DBService.class, dbService);

        dbService.clearDB();
        dbService.migrate();

        System.out.println("Data base was successfully cleared.");
    }
}
