package cn.ohbee.demoscheduling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DemoSchedulingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSchedulingApplication.class, args);
    }

}
