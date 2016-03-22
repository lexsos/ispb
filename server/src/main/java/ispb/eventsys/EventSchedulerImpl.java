package ispb.eventsys;

import ispb.base.Application;
import ispb.base.eventsys.EventScheduler;
import ispb.base.resources.AppResources;
import ispb.base.service.LogService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Properties;

public class EventSchedulerImpl implements EventScheduler {

    private Scheduler scheduler;
    private final LogService logService;
    private final Application application;

    public EventSchedulerImpl(LogService logService, Application application){
        this.logService = logService;
        this.application = application;
    }

    public void start(){
        if (scheduler != null)
            return;

        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.getContext().put("application", application);
        }
        catch (SchedulerException e){
            logService.warn("Error while creating scheduler", e);
        }

        loadJobs();

        try {
            scheduler.start();
        }
        catch (SchedulerException e){
            logService.warn("Error while staring scheduler", e);
        }
    }

    public void shutdown(){
        try {
            scheduler.shutdown();
        }
        catch (SchedulerException e){
            logService.warn("Error while shutdown scheduler", e);
        }
    }

    private void loadJobs(){
        AppResources resources = getAppResources();
        Properties jobs = resources.getAsProperties(getClass(), "jobs.properties");

        for (Object key : jobs.keySet()){
            String className = key.toString();
            String cron = jobs.getProperty(className);
            Class jobClass;

            try {
                jobClass = Class.forName(className);
            }
            catch (Throwable e) {
                logService.warn("Error while load class", e);
                continue;
            }

            JobDetail job = JobBuilder.newJob(jobClass).build();
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                    .build();
            try {
                scheduler.scheduleJob(job, trigger);
            }
            catch (SchedulerException e){
                logService.warn("Error while add job to scheduler", e);
            }
        }
    }

    private AppResources getAppResources(){
        return application.getByType(AppResources.class);
    }
}
