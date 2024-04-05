package com.mall4j.cloud.user.feign;

import com.mall4j.cloud.api.user.feign.TagClient;
import com.mall4j.cloud.api.user.vo.TagVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.service.TagService;
import com.mall4j.cloud.user.service.UserTagRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户地址feign连接
 *
 * @author FrozenWatermelon
 * @date 2020/12/07
 */
@Slf4j
@RestController
public class TagController implements TagClient {

    @Autowired
    TagService tagService;
    @Autowired
    UserTagRelationService userTagRelationService;

    @Override
    public ServerResponseEntity<TagVO> getTag(Long tagId){
        return tagService.getTag(tagId);
    }

    @Override
    public ServerResponseEntity<Boolean> isInTag(Long tagId, String vipcode) {
        return ServerResponseEntity.success(userTagRelationService.isInTag(tagId,vipcode));
    }

    @Override
    public ServerResponseEntity<Boolean> isInTags(List<Long> tagIds, String vipcode) {
        return ServerResponseEntity.success(userTagRelationService.isInTags(tagIds,vipcode));
    }

    @Override
    public ServerResponseEntity<Integer> countByTag(List<Long> tagIds) {
        Integer count =  userTagRelationService.countByTagIds(tagIds);
        return ServerResponseEntity.success(count);
    }

    @Override
    public ServerResponseEntity<List<Long>> listUserIdByTag(List<Long> tagIds) {
        if (tagIds.isEmpty()){
            return ServerResponseEntity.success(new ArrayList<>());
        }

        List<Long> userIdList = userTagRelationService.listUserIdByTagIds(tagIds);
        return ServerResponseEntity.success(userIdList);
    }
}
