package cn.ohbee.demoexception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author zf
 */
@Slf4j
@RestController
public class SimpleExceptionController {


    @GetMapping("simpleWebApi")
    public void simpleWebApi(){
        throw new RuntimeException("运行时异常");
    }


    @GetMapping("nullWebApi")
    public void nullWebApi()  {
        throw new NullPointerException("空指针异常");
    }


}
