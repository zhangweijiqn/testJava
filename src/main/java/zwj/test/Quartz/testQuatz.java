package zwj.test.Quartz;

import static org.quartz.JobBuilder.newJob;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangwj on 16-1-17.
 *需要配置log4j.xml
 *
 * <dependency>
 <groupId>org.quartz-scheduler</groupId>
 <artifactId>quartz</artifactId>
 <version>2.2.1</version>
 </dependency>
 */
public class testQuatz {

    public static void main(String args[]) throws SchedulerException {
        Logger log = LoggerFactory.getLogger(testQuatz.class);
        JobDetail jobDetail= JobBuilder.newJob(HelloQuartzJob.class)
                .withIdentity("testJob_1", "group_1")
                .build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("trigger_1","group_1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10) //时间间隔
                        .withRepeatCount(5)        //重复次数(将执行6次)
                )
                .build();
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        sched.scheduleJob(jobDetail,trigger);

        sched.start();
        log.info("------- Waiting 65 seconds... -------------");
        try {
            // wait 65 seconds to show job
            Thread.sleep(65L * 1000L);
            // executing...
        } catch (Exception e) {
            //
        }
        sched.shutdown(true);
        //更多实例参考quartz源码中的examples
        }
}
