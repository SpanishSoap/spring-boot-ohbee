package com.ohbee.demoproperties;

import cn.hutool.core.lang.Dict;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class DemoPropertiesApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoPropertiesApplication.class, args);
    }

    @Autowired
    private ApplicationProperty applicationProperty;
    @Autowired
    private TeleplayInfoProperty teleplayInfoProperty;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Dict set = Dict.create().set("applicationProperty", applicationProperty).set("blogInfoProperty", teleplayInfoProperty);
        log.info(new ObjectMapper().writeValueAsString(set));
    }
}

