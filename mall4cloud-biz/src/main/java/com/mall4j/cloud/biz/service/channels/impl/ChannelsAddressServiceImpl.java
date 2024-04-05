package com.mall4j.cloud.biz.service.channels.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mall4j.cloud.api.biz.dto.channels.EcAddressDetail;
import com.mall4j.cloud.api.biz.dto.channels.AddressInfo;
import com.mall4j.cloud.api.biz.dto.channels.request.EcAddressRequest;
import com.mall4j.cloud.api.biz.dto.channels.EcOfflineAddressType;
import com.mall4j.cloud.api.biz.dto.channels.response.EcAddressResponse;
import com.mall4j.cloud.biz.dto.channels.ChannelsAddressDTO;
import com.mall4j.cloud.biz.dto.channels.ChannelsAddressPageDTO;
import com.mall4j.cloud.biz.mapper.channels.ChannelsAddressMapper;
import com.mall4j.cloud.biz.model.channels.ChannelsAddress;
import com.mall4j.cloud.biz.service.channels.ChannelsAddressService;
import com.mall4j.cloud.biz.service.channels.EcAddressService;
import com.mall4j.cloud.api.biz.vo.ChannelsAddressVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.security.AuthUserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class ChannelsAddressServiceImpl implements ChannelsAddressService {
	
	@Autowired
	private ChannelsAddressMapper channelsAddressMapper;
	
	@Autowired
	private EcAddressService ecAddressService;
	
	@Override
	public PageVO<ChannelsAddressVO> page(PageDTO pageDTO, ChannelsAddressPageDTO channelsAddressPageDTO) {
		return PageUtil.doPage(pageDTO, () -> channelsAddressMapper.list(channelsAddressPageDTO));
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(ChannelsAddressDTO channelsAddressDTO) {
		log.info("添加视频号4.0退货地址请求参数信息：{}", JSONObject.toJSONString(channelsAddressDTO));
		
		//调用微信接口添加退货地址
		EcAddressRequest ecAddressRequest = new EcAddressRequest();
		EcAddressDetail ecAddressDetail = new EcAddressDetail();
		EcOfflineAddressType ecOfflineAddressType = new EcOfflineAddressType();
		
		AddressInfo addressInfo = new AddressInfo();
		addressInfo.setUserName(channelsAddressDTO.getReceiverName());
		addressInfo.setPostalCode(channelsAddressDTO.getPostCode());
		addressInfo.setProvinceName(channelsAddressDTO.getProvince());
		addressInfo.setCityName(channelsAddressDTO.getCity());
		addressInfo.setCountyName(channelsAddressDTO.getTown());
		addressInfo.setTelNumber(channelsAddressDTO.getTelNumber());
		addressInfo.setDetailInfo(channelsAddressDTO.getDetailedAddress());
		
		ecAddressDetail.setSendAddr(false);
		ecAddressDetail.setDefaultSend(false);
		ecAddressDetail.setRecvAddr(true);
		ecAddressDetail.setDefaultRecv(Objects.equals(1,channelsAddressDTO.getDefaultRecv()));
		ecAddressDetail.setAddressType(ecOfflineAddressType);
		ecAddressDetail.setAddressInfo(addressInfo);
		ecAddressRequest.setEcAddressDetail(ecAddressDetail);
		
		EcAddressResponse ecAddressResponse = ecAddressService.add(ecAddressRequest);
		//入库
		ChannelsAddress channelsAddress = new ChannelsAddress();
		BeanUtil.copyProperties(channelsAddressDTO, channelsAddress);
		channelsAddress.setAddressId(ecAddressResponse.getAddressId());
		channelsAddress.setCreateBy(AuthUserContext.get().getUsername());
		channelsAddress.setCreateTime(new Date());
		channelsAddress.setIsDeleted(0);
		
		channelsAddressMapper.insert(channelsAddress);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(ChannelsAddressDTO channelsAddressDTO) {
		log.info("更新视频号4.0退货地址请求参数信息：{}", JSONObject.toJSONString(channelsAddressDTO));
		
		//调用微信接口更新退货地址
		EcAddressRequest ecAddressRequest = new EcAddressRequest();
		EcAddressDetail ecAddressDetail = new EcAddressDetail();
		EcOfflineAddressType ecOfflineAddressType = new EcOfflineAddressType();
		
		AddressInfo addressInfo = new AddressInfo();
		addressInfo.setUserName(channelsAddressDTO.getReceiverName());
		addressInfo.setPostalCode(channelsAddressDTO.getPostCode());
		addressInfo.setProvinceName(channelsAddressDTO.getProvince());
		addressInfo.setCityName(channelsAddressDTO.getCity());
		addressInfo.setCountyName(channelsAddressDTO.getTown());
		addressInfo.setTelNumber(channelsAddressDTO.getTelNumber());
		addressInfo.setDetailInfo(channelsAddressDTO.getDetailedAddress());
		
		ecAddressDetail.setAddressId(channelsAddressDTO.getAddressId());
		ecAddressDetail.setSendAddr(false);
		ecAddressDetail.setDefaultSend(false);
		ecAddressDetail.setRecvAddr(true);
		ecAddressDetail.setDefaultRecv(Objects.equals(1,channelsAddressDTO.getDefaultRecv()));
		ecAddressDetail.setAddressType(ecOfflineAddressType);
		ecAddressDetail.setAddressInfo(addressInfo);
		ecAddressRequest.setEcAddressDetail(ecAddressDetail);
		
		ecAddressService.update(ecAddressRequest);
		//入库
		ChannelsAddress channelsAddress = new ChannelsAddress();
		BeanUtil.copyProperties(channelsAddressDTO, channelsAddress);
		channelsAddress.setUpdateBy(AuthUserContext.get().getUsername());
		channelsAddress.setUpdateTime(new Date());
		
		channelsAddressMapper.update(channelsAddress,new UpdateWrapper<ChannelsAddress>()
				.lambda().eq(ChannelsAddress::getAddressId, channelsAddressDTO.getAddressId()));
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(String addressId) {
		Integer count = channelsAddressMapper.selectCount(new LambdaQueryWrapper<ChannelsAddress>()
				.eq(ChannelsAddress::getIsDeleted, 0));
		
		if(count <= 1){
			throw new LuckException("退货地址数量小于等于1,无法删除");
		}
		
		EcAddressDetail ecAddressDetail = new EcAddressDetail();
		ecAddressDetail.setAddressId(addressId);
		ecAddressService.delete(ecAddressDetail);
		channelsAddressMapper.update(null,new LambdaUpdateWrapper<ChannelsAddress>()
				.set(ChannelsAddress::getIsDeleted,1)
				.eq(ChannelsAddress::getAddressId,addressId));
	}
	
	@Override
	public ChannelsAddressVO getDefaultAddress() {
		return channelsAddressMapper.getDefaultAddress();
	}

	@Override
	public ChannelsAddressVO getAddressById(Long id) {
		return channelsAddressMapper.getAddressById(id);
	}
}
