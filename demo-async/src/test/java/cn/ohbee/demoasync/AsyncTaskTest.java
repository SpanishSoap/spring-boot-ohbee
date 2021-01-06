package cn.ohbee.demoasync;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class AsyncTaskTest {

    @Autowired
    private AsyncTask asyncTask;

    @Test
    public void asyncTaskTest() throws InterruptedException, ExecutionException {

        Stopwatch started = Stopwatch.createStarted();
        Future<String> stringFuture1 = asyncTask.asyncTask1();
        Future<String> stringFuture2 = asyncTask.asyncTask2();
        Future<String> stringFuture3 = asyncTask.asyncTask3();

        // get() 会阻塞当前 main 线程 等待返回再向下执行
        log.info("{}, 执行完成", stringFuture1.get());
        log.info("{}, 执行完成", stringFuture3.get());
        log.info("{}, 执行完成", stringFuture2.get());
        log.info("异步任务执行完成共: {} 毫秒", started.stop().elapsed(TimeUnit.MILLISECONDS));
    }

    @Test
    public void syncTaskTest() throws InterruptedException {

        Stopwatch started = Stopwatch.createStarted();
        asyncTask.syncTask1();
        asyncTask.syncTask2();
        asyncTask.syncTask3();
        log.info("同步任务执行完成共: {} 毫秒", started.stop().elapsed(TimeUnit.MILLISECONDS));
    }

}