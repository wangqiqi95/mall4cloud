package com.mall4j.cloud.biz.feign;

import com.mall4j.cloud.api.biz.feign.StaffCodeFeignClient;
import com.mall4j.cloud.biz.service.cp.StaffCodePlusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gmq
 * @date 2021-08-25
 */
@RestController
@Slf4j
public class StaffCodeFeignController implements StaffCodeFeignClient {

    @Autowired
    private StaffCodePlusService staffCodeService;

}
