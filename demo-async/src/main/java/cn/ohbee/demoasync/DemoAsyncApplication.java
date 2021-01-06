package cn.ohbee.demoasync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DemoAsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoAsyncApplication.class, args);
    }

}
