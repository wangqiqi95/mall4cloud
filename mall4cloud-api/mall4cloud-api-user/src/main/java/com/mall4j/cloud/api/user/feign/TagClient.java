package com.mall4j.cloud.api.user.feign;

import com.mall4j.cloud.api.user.vo.TagVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户标签
 * @author Peter_Tan
 */
@FeignClient(value = "mall4cloud-user", contextId = "userTag")
public interface TagClient {

    /**
     * 根据标签ID获取标签信息
     * @param tagId 标签ID
     * @return 标签信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/getTag")
    ServerResponseEntity<TagVO> getTag(@RequestParam("tagId") Long tagId);

    /**
     * 判断当前会员是否属于当前标签
     * @param tagId 标签ID
     * @return 标签信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/isInTag")
    ServerResponseEntity<Boolean> isInTag(@RequestParam("tagId") Long tagId,@RequestParam("vipcode") String vipcode);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/isInTags")
    ServerResponseEntity<Boolean> isInTags(@RequestParam("tagId") List<Long> tagIds, @RequestParam("vipcode") String vipcode);

    /**
     * 统计 tagIds 涉及的用户总数
     * @param tagIds 标签集合
     * @return 人数
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/countByTag")
    ServerResponseEntity<Integer> countByTag(@RequestParam("tagIds") List<Long> tagIds);

    /**
     * 根据 tagIds 获取涉及的用户ID
     * @param tagIds 标签ID集合
     * @return userId list
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/listUserIdByTag")
    ServerResponseEntity<List<Long>> listUserIdByTag(@RequestParam("tagIds") List<Long> tagIds);
}
