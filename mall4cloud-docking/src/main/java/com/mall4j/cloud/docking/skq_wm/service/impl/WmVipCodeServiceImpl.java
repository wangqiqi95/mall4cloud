package com.mall4j.cloud.docking.skq_wm.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.docking.skq_wm.config.DockingWmParams;
import com.mall4j.cloud.api.docking.skq_wm.dto.GetMemberCodeDTO;
import com.mall4j.cloud.api.docking.skq_wm.dto.request.GetMemberCodeReq;
import com.mall4j.cloud.api.docking.skq_wm.dto.response.GetAccessTokenResp;
import com.mall4j.cloud.api.docking.skq_wm.dto.response.GetMemberCodeResp;
import com.mall4j.cloud.api.docking.skq_wm.dto.response.WMData;
import com.mall4j.cloud.api.docking.skq_wm.vo.MemberCodeVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_wm.config.DockingWmParamsProperties;
import com.mall4j.cloud.docking.skq_wm.weimob.MemberCardApi;
import com.mall4j.cloud.docking.skq_wm.service.WmVipCodeService;
import com.mall4j.cloud.docking.utils.WmClients;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service("wmVipCodeService")
public class WmVipCodeServiceImpl implements WmVipCodeService {

	@Autowired
	private MemberCardApi memberCardApi;

	@Resource
	private RedisTemplate redisTemplate;

	@Autowired
	private DockingWmParamsProperties dockingWmParamsProperties;

	private final static String REDIS_WM_ACCESS_TOKEN="mall4cloud_docking:WM_ACCESS_TOKEN_";

	/**
	 * 查询会员码信息
	 * @param getMemberCodeDTO 查询会员码信息
	 * @return
	 */
	@Override
	public ServerResponseEntity<MemberCodeVO> getMemberCode(GetMemberCodeDTO getMemberCodeDTO) {
		if (ObjectUtil.isEmpty(getMemberCodeDTO)) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}
		String accessToken = null;
		GetMemberCodeReq getMemberCodeReq = new GetMemberCodeReq();
		getMemberCodeReq.setCodeSource(getMemberCodeDTO.getCodeSource());
		getMemberCodeReq.setWid(getMemberCodeDTO.getWid());
		getMemberCodeReq.setDynamicCode(getMemberCodeDTO.getDynamicCode());
		//BeanUtils.copyProperties(getMemberCodeDTO, getMemberCodeReq);

//		DockingWmParams wmParams = WmClients.clients().getWmParams();
		log.info("获取微盟云AccessToken入参:{}", JSON.toJSONString(dockingWmParamsProperties));
		String redisKey = REDIS_WM_ACCESS_TOKEN + dockingWmParamsProperties.getClientId();
		BoundValueOperations<String, String> boundValueOperations = redisTemplate.boundValueOps(redisKey);
		String redisAccessToken = boundValueOperations.get();
		if(StringUtils.isNotEmpty(redisAccessToken)){
			accessToken = redisAccessToken;
		}else {
			// 获取 accessToken
			accessToken = getAccessToken(accessToken, boundValueOperations);
		}

		if(StringUtils.isBlank(accessToken)){
			ServerResponseEntity<MemberCodeVO> fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("获取微盟云授权令牌时发生异常");
			return fail;
		}

		// 调用微盟云获取会员码信息
		GetMemberCodeResp getMemberCodeResp = getMemberCodeData(getMemberCodeReq, accessToken, memberCardApi);

		// 授权令牌过期，重新获取一遍授权令牌再进行调用
		if(getMemberCodeResp.getCode().getErrCode().equals("80001001000119")){
			redisTemplate.delete(redisKey);
			accessToken = getAccessToken(accessToken, boundValueOperations);

			getMemberCodeResp = getMemberCodeData(getMemberCodeReq, accessToken, memberCardApi);
		}
		if(!getMemberCodeResp.getCode().getErrCode().equals("0")){
			throw new LuckException(getMemberCodeResp.getCode().getErrMsg());
		}

		WMData data = getMemberCodeResp.getData();
		MemberCodeVO memberCodeVO = new MemberCodeVO();
		memberCodeVO.setWxCode(data.getWxCode());
		memberCodeVO.setCustomCardNo(data.getCustomCardNo());
		memberCodeVO.setMembershipPlanId(data.getMembershipPlanId());
		memberCodeVO.setPhone(data.getPhone());
		memberCodeVO.setWid(data.getWid());
		memberCodeVO.setDynamicExpireAtTime(data.getDynamicExpireAtTime());
		memberCodeVO.setExpireTime(data.getExpireTime());
		//BeanUtils.copyProperties(data, memberCodeVO);
		log.info("查询会员码信息方法出参:{}",JSON.toJSONString(memberCodeVO));

		return ServerResponseEntity.success(memberCodeVO);
	}

	private GetMemberCodeResp getMemberCodeData(GetMemberCodeReq getMemberCodeReq, String accessToken, MemberCardApi memberCardApi) {
		log.info("查询微盟云会员码信息入参:{},授权令牌为{}", JSON.toJSONString(getMemberCodeReq), accessToken);
		GetMemberCodeResp getMemberCodeResp = memberCardApi.getMemberCode(accessToken, getMemberCodeReq);
		log.info("查询微盟云会员码信息出参:{}",JSON.toJSONString(getMemberCodeResp));
		return getMemberCodeResp;
	}

	private String getAccessToken(String accessToken,  BoundValueOperations<String, String> boundValueOperations) {
		GetAccessTokenResp accessTokenResp = memberCardApi.getAccessToken(dockingWmParamsProperties.getGrantType(), dockingWmParamsProperties.getClientId(), dockingWmParamsProperties.getClientSecret(), dockingWmParamsProperties.getShopId(), dockingWmParamsProperties.getShopType());
		log.info("查询微盟云授权令牌出参:{}",JSON.toJSONString(accessTokenResp));
		accessToken = accessTokenResp.getAccessToken();
		Long expireTime = accessTokenResp.getExpiresIn() - dockingWmParamsProperties.getTokenExpire();
		boundValueOperations.set(accessToken, expireTime, TimeUnit.SECONDS);
		return accessToken;
	}

}
