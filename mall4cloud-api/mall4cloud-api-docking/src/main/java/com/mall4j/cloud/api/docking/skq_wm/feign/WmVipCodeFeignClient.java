package com.mall4j.cloud.api.docking.skq_wm.feign;

import com.mall4j.cloud.api.docking.skq_wm.dto.GetMemberCodeDTO;
import com.mall4j.cloud.api.docking.skq_wm.vo.MemberCodeVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 类描述：微盟会员码接口
 *
 * @date 2023/04/26
 */
@FeignClient(value = "mall4cloud-docking",contextId = "wm-vipCode")
public interface WmVipCodeFeignClient {

	/**
	 * 查询会员码信息
	 * @param getMemberCodeDTO 查询参数
	 * @return 返回requestId  （推单是异步的，这里请求成功后会返回requestId作为本次请求的编号），在回执的时候会用到
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wm/memberCard/getMemberCode")
	ServerResponseEntity<MemberCodeVO> getMemberCode(@RequestBody GetMemberCodeDTO getMemberCodeDTO);

}
