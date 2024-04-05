package com.mall4j.cloud.user.feign;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.user.dto.UserTageAddDto;
import com.mall4j.cloud.api.user.feign.UserTagClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.api.user.vo.UserTagApiVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.service.UserTagService;
import com.mall4j.cloud.user.service.UserTagUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @author cl
 * @date 2021-05-20 14:14:36
 */
@RestController
public class UserTagClientController implements UserTagClient {

    @Autowired
    private UserTagService userTagService;

    @Autowired
    private UserTagUserService userTagUserService;

    @Override
    public ServerResponseEntity<List<UserTagApiVO>> getUserTagList(List<Long> tagIds) {
        if (CollUtil.isEmpty(tagIds)) {
            return ServerResponseEntity.success(Collections.emptyList());
        }
        List<UserTagApiVO> list = userTagService.getUserTagList(tagIds);
        return ServerResponseEntity.success(list);
    }

    @Override
    public ServerResponseEntity<List<UserApiVO>> getUserByTagIds(List<Long> userTagIds) {
        return ServerResponseEntity.success(userTagUserService.getUserByTagIds(userTagIds));
    }

    @Override
    public ServerResponseEntity<Void> addUserTags(UserTageAddDto userTageAddDto) {
        return ServerResponseEntity.success();
    }
}
