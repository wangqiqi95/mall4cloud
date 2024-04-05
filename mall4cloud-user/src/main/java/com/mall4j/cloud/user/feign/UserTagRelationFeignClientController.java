package com.mall4j.cloud.user.feign;

import com.mall4j.cloud.api.user.feign.UserTagRelationFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.service.UserTagRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserTagRelationFeignClientController implements UserTagRelationFeignClient {

    @Autowired
    private UserTagRelationService userTagRelationService;

    @Override
    public ServerResponseEntity<List<Long>> checkUserTagRelationByTagIdList(List<Long> tagIdList) {
        return userTagRelationService.checkUserTagRelationByTagIdList(tagIdList);
    }
}
