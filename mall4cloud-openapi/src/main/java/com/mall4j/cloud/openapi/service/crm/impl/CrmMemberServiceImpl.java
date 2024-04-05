package com.mall4j.cloud.openapi.service.crm.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mall4j.cloud.api.openapi.skq_crm.dto.CrmUpdateUserDto;
import com.mall4j.cloud.api.user.dto.CrmUserSyncDTO;
import com.mall4j.cloud.api.openapi.skq_crm.response.CrmResponse;
import com.mall4j.cloud.api.openapi.skq_crm.response.CrmResponseEnum;
import com.mall4j.cloud.api.user.dto.FriendlyWalkRegisterRequest;
import com.mall4j.cloud.api.user.dto.MemberUpdateDto;
import com.mall4j.cloud.api.user.feign.crm.CrmUserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.crm.ICrmMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @description: 会员信息-crm 处理逻辑
 * @date 2021/12/23 22:10
 */
@Service("crmMemberService")
public class CrmMemberServiceImpl implements ICrmMemberService {
    private final Logger logger = LoggerFactory.getLogger(CrmMemberServiceImpl.class);

    @Autowired
    CrmUserFeignClient crmUserFeignClient;

    /**
     * 方法描述：会员信息更新（crm端）
     * crm端会员基础信息（包含等级）更新时推送给小程序
     *
     * @param crmUpdateUserDto
     * @return com.mall4j.cloud.common.response.ServerResponseEntity
     */
    @Override
    public CrmResponse updateSync(CrmUpdateUserDto crmUpdateUserDto) {
//        long start = System.currentTimeMillis();
//        CrmResponse crmResponse = CrmResponse.fail();
//		ServerResponseEntity responseEntity = null;
//        try {
//        	if (crmUpdateUserDto == null) {
//        		crmResponse = CrmResponse.fail(CrmResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(), "请求参数为空");
//        		return crmResponse;
//			}
//			crmResponse = crmUpdateUserDto.check();
//        	if (crmResponse.isSuccess()) {
//				// feign调用 user服务更新数据
//				responseEntity = crmUserFeignClient.crmUserUpdate(BeanUtil.copyProperties(crmUpdateUserDto, MemberUpdateDto.class));
//				if (responseEntity.isSuccess()) {
//					crmResponse = CrmResponse.success();
//				} else {
//					crmResponse = CrmResponse.fail(CrmResponseEnum.EXCEPTION.value(), responseEntity.getMsg());
//				}
//			}
//			return crmResponse;
//		} finally {
//            logger.info("会员信息更新（crm端）接收到请求参数：{}，feign调用响应为：{},返回响应为：{}，共耗时：{}", crmUpdateUserDto, responseEntity, crmResponse, System.currentTimeMillis() - start);
//        }
		return null;
    }

	@Override
	public CrmResponse userSync(CrmUserSyncDTO crmUserSyncDTO) {
//		long start = System.currentTimeMillis();
//		CrmResponse crmResponse = CrmResponse.fail();
//		ServerResponseEntity responseEntity = null;
//		try {
//			if (crmUserSyncDTO == null) {
//				crmResponse = CrmResponse.fail(CrmResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(), "请求参数为空");
//				return crmResponse;
//			}
//			//TODO 为避免微盟注册用户没传UnionId，然后来到小程序参加其他活动出现缓存。最终导致user表出现重复数据，隔离UnionId为空的数据
//			if(Objects.isNull(crmUserSyncDTO.getUnionId())){
//				crmResponse = CrmResponse.fail(CrmResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(), "UnionId为空");
//				return crmResponse;
//			}
//			// feign调用 user服务同步数据
//			responseEntity = crmUserFeignClient.crmUserSyncRegister(crmUserSyncDTO);
//			if (responseEntity.isSuccess()) {
//				crmResponse = CrmResponse.success();
//			} else {
//				crmResponse = CrmResponse.fail(CrmResponseEnum.EXCEPTION.value(), responseEntity.getMsg());
//			}
//			return crmResponse;
//		} finally {
//			logger.info("会员信息同步（crm端）接收到请求参数：{}，feign调用响应为：{},返回响应为：{}，共耗时：{}", crmUserSyncDTO, responseEntity, crmResponse, System.currentTimeMillis() - start);
//		}
		return null;
	}

}
