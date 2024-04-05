package com.mall4j.cloud.docking.skq_wm.controller;

import com.mall4j.cloud.api.docking.skq_erp.dto.PushOrderDto;
import com.mall4j.cloud.api.docking.skq_erp.dto.PushRefundDto;
import com.mall4j.cloud.api.docking.skq_erp.feign.StdOrderFeignClient;
import com.mall4j.cloud.api.docking.skq_wm.dto.GetMemberCodeDTO;
import com.mall4j.cloud.api.docking.skq_wm.feign.WmVipCodeFeignClient;
import com.mall4j.cloud.api.docking.skq_wm.vo.MemberCodeVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_erp.service.IStdOrderService;
import com.mall4j.cloud.docking.skq_wm.service.WmVipCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类描述：对接中台订单相关接口
 *
 * @date 2022/1/7 19:00：45
 */
@RestController
@Api(tags = "对接微盟云会员卡相关接口")
public class WmVipCodeController implements WmVipCodeFeignClient {

	@Autowired
	WmVipCodeService wmVipCodeService;

	/**
	 * 查询会员码信息
	 * @param getMemberCodeDTO 查询参数
	 * @return
	 */
	@Override
	public ServerResponseEntity<MemberCodeVO> getMemberCode(GetMemberCodeDTO getMemberCodeDTO) {
		return wmVipCodeService.getMemberCode(getMemberCodeDTO);
	}

}
