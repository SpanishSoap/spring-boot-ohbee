package cn.ohbee.demoasync;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AsyncTask {

    //------------------ 创建不同的异步任务，模拟方法执行 ----------------------------------------

    @Async
    public Future<String> asyncTask1() throws InterruptedException {
        log.info("异步方法 asyncTask1 开始执行，执行线程为{}",  Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(10);
        log.info("异步方法 asyncTask1 结束执行，执行线程为{}",  Thread.currentThread().getName());
        return  new AsyncResult<>("this is asyncTask1");
    }

    @Async
    public Future<String> asyncTask2() throws InterruptedException {
        log.info("异步方法 asyncTask2 开始执行，执行线程为{}",  Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(7);
        log.info("异步方法 asyncTask2 结束执行，执行线程为{}",  Thread.currentThread().getName());
        return  new AsyncResult<>("this is asyncTask2");
    }

    @Async
    public Future<String> asyncTask3() throws InterruptedException {
        log.info("异步方法 asyncTask3 开始执行，执行线程为{}",  Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(1);
        log.info("异步方法 asyncTask3 结束执行，执行线程为{}",  Thread.currentThread().getName());
        return  new AsyncResult<>("this is asyncTask3");
    }


    //------------------ 创建不同的同步任务，模拟方法执行 ----------------------------------------

    public String syncTask1() throws InterruptedException {
        log.info("同步方法 asyncTask1 开始执行，执行线程为{}",  Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(10);
        log.info("同步方法 asyncTask1 结束执行，执行线程为{}",  Thread.currentThread().getName());
        return  "this is syncTask1";
    }

    public String syncTask2() throws InterruptedException {
        log.info("同步方法 syncTask2 开始执行，执行线程为{}",  Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(7);
        log.info("同步方法 syncTask2 结束执行，执行线程为{}",  Thread.currentThread().getName());
        return  "this is syncTask2";
    }

    public String syncTask3() throws InterruptedException {
        log.info("同步方法 syncTask3 开始执行，执行线程为{}", Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(1);
        log.info("同步方法 syncTask3 结束执行，执行线程为{}",  Thread.currentThread().getName());
        return  "this is syncTask3";
    }
}
