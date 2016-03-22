package ispb.base.eventsys;


import ispb.base.Application;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

public abstract class BaseJob  implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        Object obj;

        try {
            obj = context.getScheduler().getContext().get("application");
        }
        catch (SchedulerException e){
            return;
        }

        if (obj instanceof Application) {
            Application application = (Application) obj;
            execute(application);
        }
    }

    public abstract void execute(Application application);
}
