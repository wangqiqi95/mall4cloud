package com.mall4j.cloud.api.user.feign;

import com.mall4j.cloud.api.user.dto.UserTageAddDto;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.api.user.vo.UserTagApiVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 会员标签
 * @author cl
 */
@FeignClient(value = "mall4cloud-user",contextId = "user-tag")
public interface UserTagClient {

    /**
     * 通过标签id集合获取标签集合
     * @param tagIds 标签id
     * @return 标签列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/userTags")
    ServerResponseEntity<List<UserTagApiVO>> getUserTagList(@RequestParam("tagIds") List<Long> tagIds);

    /**
     * 通过标签获取标签下的用户信息
     * @param userTagIds 标签id集合
     * @return 用户列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/getUsersByTagIds")
    ServerResponseEntity<List<UserApiVO>> getUserByTagIds(@RequestParam("userTagIds") List<Long> userTagIds);

    /**
     * 添加用户标签
     * @param userTageAddDto
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/addUserTags")
    ServerResponseEntity<Void> addUserTags(@RequestBody UserTageAddDto userTageAddDto);
}
