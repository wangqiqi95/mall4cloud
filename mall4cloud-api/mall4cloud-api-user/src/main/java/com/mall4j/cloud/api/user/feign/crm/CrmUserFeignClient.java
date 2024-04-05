package com.mall4j.cloud.api.user.feign.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.user.crm.request.UpdateUserTagRequest;
import com.mall4j.cloud.api.user.crm.response.CrmResult;
import com.mall4j.cloud.api.user.dto.CrmUserSyncDTO;
import com.mall4j.cloud.api.user.dto.UpdateScoreDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mall4j.cloud.api.user.dto.MemberUpdateDto;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户信息feign连接
 * @author FrozenWatermelon
 * @date 2020/12/07
 */
@FeignClient(value = "mall4cloud-user",contextId = "crm-user")
public interface CrmUserFeignClient {

	/**
	 * 更新用户标签
	 * @param request
	 * @return
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX +"/insider/crm/user/updateMemberTag")
	ServerResponseEntity updateMemberTag(@RequestBody UpdateUserTagRequest request);

	/**
	 * 用户数据查询
	 * @return
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX +"/insider/crm/user/editageUser")
	ServerResponseEntity<CrmResult<JSONArray>> editageUser(@RequestParam("unionId")String unionId,
														   @RequestParam(value = "active",required = false)String active,
														   @RequestParam(value = "mobileNumber",required = false)String mobileNumber);


	/**
	 * 更新积分
	 * @param updateScoreDTO
	 * @return
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX +"/insider/crm/user/updateScore")
    ServerResponseEntity updateScore(@RequestBody UpdateScoreDTO updateScoreDTO);
}
