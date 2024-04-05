package com.mall4j.cloud.coupon.controller.test;

import com.mall4j.cloud.common.limiter.anns.RateLimit;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController("TestContorller")
@RequestMapping("/ua/rl")
public class TestContorller {



    @GetMapping("/aq")
    @RateLimit(burstCapacity=1000)
    public ServerResponseEntity aq() {
        System.out.println("123");
        ServerResponseEntity<Object> success = ServerResponseEntity.success();

        success.setData("123");
        return success;
    }

}
