package cn.ohbee.demointerceptor;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zf
 */
@Slf4j
@RestController
@Validated
public class DemoController {


    @GetMapping("simpleWebApi")
    public List<String> simpleWebApi(@NotNull Boolean isBoys){
        log.info("方法执行中:simpleWebApi");
        ArrayList<String> friends ;
        if(isBoys){
            friends =  CollUtil.newArrayList("Joey", "Chandler", "Rose");
        }else {
            friends =  CollUtil.newArrayList("Monica", "Rachel", "Phoebe");
        }
        return friends;
    }
}
