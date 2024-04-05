package com.mall4j.cloud.platform.feign;

import com.mall4j.cloud.api.platform.feign.TzTagFeignClient;
import com.mall4j.cloud.api.platform.vo.TzTagDetailVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.service.TzTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class TzTagFeignController implements TzTagFeignClient {

    @Autowired
    private TzTagService tzTagService;

    @Override
    public ServerResponseEntity<List<TzTagDetailVO>> listTagByStaffId(Long staffId) {
        return ServerResponseEntity.success(tzTagService.listTagByStaffId(staffId));
    }
}
