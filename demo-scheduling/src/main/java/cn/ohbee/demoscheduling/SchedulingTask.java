package cn.ohbee.demoscheduling;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SchedulingTask {

    /**
     * 使用 cron 表达式
     */
    //@Scheduled(cron = "0/5 * * * * ?")
    public void  taskCorn(){
        log.info("taskCorn:执行时间{}", DateUtil.formatDateTime(new Date()));
    }

    /**
     * 上次执行完成后，经过 fixedDelay 间隔后，再执行下次任务，毫秒，long类型
     */
    //@Scheduled(fixedDelay = 2000)
    public void  fixedDelay() throws InterruptedException {
        log.info("fixedDelay:开始执行{}", DateUtil.formatDateTime(new Date()));
        TimeUnit.SECONDS.sleep(3L);
        log.info("fixedDelay:执行结束{}", DateUtil.formatDateTime(new Date()));
    }

    /**
     * 上次执行完成后，经过 fixedDelay 间隔后，再执行下次任务，毫秒，字符串
     */
    //@Scheduled(fixedDelayString = "5000")
    public void  fixedDelayString(){
        log.info("fixedDelayString:执行时间{}", DateUtil.formatDateTime(new Date()));
    }

    /**
     * 严格按间隔执行任务，（若上次任务完成，计算时间间隔开始，若上次任务未完成，则直接开始队列中的任务），毫秒，long类型
     */
    private static Integer number=0;
    //@Scheduled(fixedRate = 2000)
    public void  fixedRate() throws InterruptedException {
        number++;
        log.info("fixedRate: 第{}次开始执行{}",number, DateUtil.formatDateTime(new Date()));
        TimeUnit.SECONDS.sleep(1L);
        log.info("fixedRate: 第{}次执行结束{}", number, DateUtil.formatDateTime(new Date()));
    }

    /**
     * 严格按间隔执行任务，（若上次任务完成，计算时间间隔开始，若上次任务未完成，则直接开始队列中的任务），毫秒，字符串
     */
    //@Scheduled(fixedRateString = "5000")
    public void  fixedRateString(){
        log.info("fixedRateString:执行时间{}", DateUtil.formatDateTime(new Date()));
    }

    /**
     * 首次执行定时任务延迟时间，毫秒，long类型，配合 fixedRate() 或者 fixedDelay() 使用.
     */
    @Scheduled(initialDelay = 5000,fixedRate=2000)
    public void  initialDelay(){
        log.info("initialDelay:执行时间{}", DateUtil.formatDateTime(new Date()));
    }

    /**
     * 首次执行定时任务延迟时间，毫秒，字符串，配合 fixedRate() 或者 fixedDelay() 使用.
     */
    //@Scheduled(initialDelayString = "1000",fixedRate=2000)
    public void  initialDelayString(){
        log.info("initialDelayString:执行时间{}", DateUtil.formatDateTime(new Date()));
    }

    //zone  时区，默认为空字符串，跟随系统时区

}
