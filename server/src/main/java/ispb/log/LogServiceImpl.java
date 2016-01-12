package ispb.log;

import ispb.base.resources.Config;
import ispb.base.service.LogService;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LogServiceImpl implements LogService {

    private Logger logger = null;
    private String confName = "log_conf.properties";

    public LogServiceImpl(Config config){
        Properties conf = new Properties();
        InputStream in = LogServiceImpl.class.getResourceAsStream(confName);
        try {
            conf.load(in);
        }
        catch (IOException e){
            System.err.println("Can't load conf file " + confName);
        }

        conf.setProperty("log4j.appender.file.File", config.getAsStr("log.file"));
        conf.setProperty("log4j.appender.file.MaxFileSize", config.getAsStr("log.file_max_size"));
        conf.setProperty("log4j.appender.file.MaxBackupIndex", config.getAsStr("log.file_count"));
        conf.setProperty("log4j.rootLogger", config.getAsStr("log.level") + ", stdout, file");

        PropertyConfigurator.configure(conf);
        logger = Logger.getRootLogger();
    }

    public void debug(Object message){
        logger.debug(message);
    }
    public void debug(Object message, Throwable t){
        logger.debug(message, t);
    }

    public void info(Object message){
        logger.info(message);
    }
    public void info(Object message, Throwable t){
        logger.info(message, t);
    }

    public void warn(Object message){
        logger.warn(message);
    }
    public void warn(Object message, Throwable t){
        logger.warn(message, t);
    }

    public void error(Object message){
        logger.error(message);
    }
    public void error(Object message, Throwable t){
        logger.error(message, t);
    }
}
