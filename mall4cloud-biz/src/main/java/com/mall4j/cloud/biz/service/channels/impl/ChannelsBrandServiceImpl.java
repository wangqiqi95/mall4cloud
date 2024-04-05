package com.mall4j.cloud.biz.service.channels.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.biz.dto.channels.EcApplicationDetails;
import com.mall4j.cloud.api.biz.dto.channels.request.EcBrand;
import com.mall4j.cloud.api.biz.dto.channels.request.EcBrandStoreRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcBrandCancelRequest;
import com.mall4j.cloud.api.biz.dto.channels.EcGrantDetails;
import com.mall4j.cloud.api.biz.dto.channels.request.EcBrandListRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcBrandRequest;
import com.mall4j.cloud.api.biz.dto.channels.EcRegisterDetails;
import com.mall4j.cloud.api.biz.dto.channels.EcBasicBrand;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBrandResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBrandStoreResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBrandListResponse;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.AuditResponse;
import com.mall4j.cloud.biz.constant.ChannelsBrandStatusEnum;
import com.mall4j.cloud.biz.dto.channels.ChannelsBrandDTO;
import com.mall4j.cloud.biz.dto.channels.ChannelsBrandPageDTO;
import com.mall4j.cloud.biz.dto.channels.event.BrandAuditDTO;
import com.mall4j.cloud.biz.mapper.channels.ChannelsBrandQualificationMapper;
import com.mall4j.cloud.biz.model.channels.ChannelsBrandQualification;
import com.mall4j.cloud.biz.service.channels.ChannelsBrandService;
import com.mall4j.cloud.biz.service.channels.EcBasicsService;
import com.mall4j.cloud.biz.service.channels.EcBrandService;
import com.mall4j.cloud.biz.vo.channels.ChannelsBasicBrandVO;
import com.mall4j.cloud.biz.vo.channels.ChannelsBrandOneVO;
import com.mall4j.cloud.biz.vo.channels.ChannelsBrandVO;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.security.AuthUserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChannelsBrandServiceImpl implements ChannelsBrandService {
	
	private static final String CHANNELS_BASIC_BRAND_LIST = "CHANNELS_BASIC_BRAND_LIST";
	private static final String CHANNELS_BRAND_LIST = "CHANNELS_BRAND_LIST";
	private static final Integer CHANNELS_BRAND_PAGE_SIZE = 50;
	
	@Autowired
	private EcBrandService ecBrandService;
	
	@Autowired
	private ChannelsBrandQualificationMapper channelsBrandQualificationMapper;
	
	
	@Override
	public List<ChannelsBasicBrandVO> basicBrandList(String query) {
		List<ChannelsBasicBrandVO> result = new ArrayList<>();
		
		// 尝试从缓存获取
		List<EcBasicBrand> ecBasicBrandList = RedisUtil.get(CHANNELS_BASIC_BRAND_LIST, 3600, () -> {
			List<EcBasicBrand> ecBasicBrands = new ArrayList<>();
			EcBrandStoreRequest ecBrandStoreRequest = new EcBrandStoreRequest();
			Boolean continueFlag = false;
			do {
				ecBrandStoreRequest.setPageSize(CHANNELS_BRAND_PAGE_SIZE);
				EcBrandStoreResponse brandStoreResponse = ecBrandService.brandAll(ecBrandStoreRequest);
				continueFlag = brandStoreResponse.getContinueFlag();
				ecBrandStoreRequest.setNextKey(brandStoreResponse.getNextKey());
				ecBasicBrands.addAll(brandStoreResponse.getBrands());
			} while (continueFlag);
			
			return ecBasicBrands;
		});

		if (CollectionUtil.isNotEmpty(ecBasicBrandList)) {
			for (EcBasicBrand ecBasicBrand : ecBasicBrandList) {
				ChannelsBasicBrandVO vo = new ChannelsBasicBrandVO();
				vo.setBrandId(ecBasicBrand.getBrandId());
				vo.setBrandName(ecBasicBrand.getEnName()+ "" + ecBasicBrand.getChName());
				if (StringUtils.isNotBlank(query)) {
					if (ecBasicBrand.getChName().contains(query) || ecBasicBrand.getEnName().contains(query)) {
						result.add(vo);
					}
					continue;
				}
				result.add(vo);
			}
		}
		
		return result;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(ChannelsBrandDTO channelsBrandDTO) {
		
		log.info("视频号4.0添加品牌输入参数信息：{}",JSONObject.toJSONString(channelsBrandDTO));
		// 先调用微信接口 在微信侧审核
		EcBrandRequest ecBrandRequest = getEcBrandRequest(channelsBrandDTO);
		AuditResponse auditResponse = ecBrandService.addBrand(ecBrandRequest);
		
		// 入库
		ChannelsBrandQualification entity = new ChannelsBrandQualification();
		BeanUtil.copyProperties(channelsBrandDTO, entity);
		entity.setAuditId(auditResponse.getAuditId());
		entity.setStatus(0);
		entity.setCreateBy(AuthUserContext.get().getUsername());
		entity.setCreateTime(new Date());
		entity.setIsDeleted(0);
		channelsBrandQualificationMapper.insert(entity);
		
		//删除缓存
		RedisUtil.del(CHANNELS_BRAND_LIST);
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateBrand(ChannelsBrandDTO channelsBrandDTO) {
		
		log.info("视频号4.0更新品牌输入参数信息：{}",JSONObject.toJSONString(channelsBrandDTO));
		// 先调用微信接口 在微信侧审核
		EcBrandRequest ecBrandRequest = getEcBrandRequest(channelsBrandDTO);
		AuditResponse auditResponse = ecBrandService.updateBrand(ecBrandRequest);
		
		// 更新数据库
		ChannelsBrandQualification entity = new ChannelsBrandQualification();
		BeanUtil.copyProperties(channelsBrandDTO, entity);
		entity.setAuditId(auditResponse.getAuditId());
		entity.setUpdateBy(AuthUserContext.get().getUsername());
		entity.setUpdateTime(new Date());
		entity.setIsDeleted(0);
		channelsBrandQualificationMapper.update(entity,new UpdateWrapper<ChannelsBrandQualification>()
				.lambda().eq(ChannelsBrandQualification::getBrandId, channelsBrandDTO.getBrandId()));
		
		//删除缓存
		RedisUtil.del(CHANNELS_BRAND_LIST);
	}
	
	private EcBrandRequest getEcBrandRequest(ChannelsBrandDTO channelsBrandDTO){

		EcBrandRequest ecBrandRequest = new EcBrandRequest();
		EcBrand ecBrand = new EcBrand();
		EcRegisterDetails ecRegisterDetails = new EcRegisterDetails();
		EcApplicationDetails ecApplicationDetails = new EcApplicationDetails();
		EcGrantDetails ecGrantDetails = new EcGrantDetails();
		BeanUtil.copyProperties(channelsBrandDTO, ecBrand);
		
		ecRegisterDetails.setRegistrant(channelsBrandDTO.getRegistrant());
		ecRegisterDetails.setRegisterNo(channelsBrandDTO.getRegisterNo());
		if(Objects.nonNull(channelsBrandDTO.getRegisterStartTime())){ecRegisterDetails.setStartTime(channelsBrandDTO.getRegisterStartTime().getTime()/1000);}
		if(Objects.nonNull(channelsBrandDTO.getRegisterEndTime())){ecRegisterDetails.setEndTime(channelsBrandDTO.getRegisterEndTime().getTime()/1000);}
		ecRegisterDetails.setIsPermanent(Objects.equals(1,channelsBrandDTO.getRegisterIsPermanent()));
		if(StrUtil.isNotBlank(channelsBrandDTO.getRegisterCertifications())) {
			ecRegisterDetails.setRegisterCertifications(Arrays.asList(channelsBrandDTO.getRegisterCertifications().split(",")));
		}
		if(StrUtil.isNotBlank(channelsBrandDTO.getRenewCertifications())) {
			ecRegisterDetails.setRegisterCertifications(Arrays.asList(channelsBrandDTO.getRenewCertifications().split(",")));
		}
		
		if(Objects.nonNull(channelsBrandDTO.getAcceptanceTime())){ecApplicationDetails.setAcceptanceTime(channelsBrandDTO.getAcceptanceTime().getTime()/1000);}
		if(StrUtil.isNotBlank(channelsBrandDTO.getAcceptanceCertification())) {
			ecRegisterDetails.setRegisterCertifications(Arrays.asList(channelsBrandDTO.getAcceptanceCertification().split(",")));
		}
		ecApplicationDetails.setAcceptanceNo(channelsBrandDTO.getAcceptanceNo());
		
		if(StrUtil.isNotBlank(channelsBrandDTO.getGrantCertifications())) {
			ecRegisterDetails.setRegisterCertifications(Arrays.asList(channelsBrandDTO.getGrantCertifications().split(",")));
		}
		ecGrantDetails.setGrantLevel(channelsBrandDTO.getGrantLevel());
		if(Objects.nonNull(channelsBrandDTO.getGrantStartTime())){ecGrantDetails.setStartTime(channelsBrandDTO.getGrantStartTime().getTime()/1000);}
		if(Objects.nonNull(channelsBrandDTO.getGrantEndTime())){ecGrantDetails.setEndTime(channelsBrandDTO.getGrantEndTime().getTime()/1000);}
		ecGrantDetails.setIsPermanent(Objects.equals(1,channelsBrandDTO.getGrantIsPermanent()));
		if(StrUtil.isNotBlank(channelsBrandDTO.getBrandOwnerIdPhotos())) {
			ecRegisterDetails.setRegisterCertifications(Arrays.asList(channelsBrandDTO.getBrandOwnerIdPhotos().split(",")));
		}
		
		ecBrand.setEcApplicationDetails(ecApplicationDetails);
		ecBrand.setEcRegisterDetails(ecRegisterDetails);
		ecBrand.setEcGrantDetails(ecGrantDetails);
		ecBrandRequest.setEcBrand(ecBrand);
		
		return ecBrandRequest;
	}
	
	@Override
	public List<ChannelsBrandVO> page(ChannelsBrandPageDTO param) {
		List<EcBrandResponse> ecBrandRespons = getBrandResponse();
		
		String brandName = param.getBrandName();
		Integer status = param.getStatus();
		
		List<ChannelsBrandVO> listVO = new ArrayList<ChannelsBrandVO>();
		//封装参数返回给前端
		if(CollectionUtil.isNotEmpty(ecBrandRespons)){
			for (EcBrandResponse brand : ecBrandRespons) {
				ChannelsBrandVO vo = new ChannelsBrandVO();
				vo.setBrandId(brand.getBrandId());
				vo.setBrandName( brand.getEnName() + "" + brand.getChName());
				vo.setStatus(brand.getStatus());
				vo.setCreateTime(new Date(brand.getCreateTime()*1000));
				if(Objects.nonNull(brand.getEcAuditResult())){
					vo.setReason(brand.getEcAuditResult().getRejectReason());
				}
				
				if (StringUtils.isNotBlank(brandName) || Objects.nonNull(status)) {
					if ( (StringUtils.isNotBlank(brandName) && brand.getChName().contains(brandName)) || (StringUtils.isNotBlank(brandName) && brand.getEnName().contains(brandName)) || Objects.equals(status,brand.getStatus()) ) {
						listVO.add(vo);
					}
					continue;
				}
				listVO.add(vo);
			}
		}
		return listVO;
	}
	
	@Override
	public List<ChannelsBrandVO> brandList() {
		List<EcBrandResponse> list = getBrandResponse();
		return list == null? Lists.newArrayList() : list.stream()
				// 审核通过的
				.filter(o-> Objects.equals(o.getStatus(), ChannelsBrandStatusEnum.AUDIT_SUCCESS.value()))
				.map(o->{
					ChannelsBrandVO vo = new ChannelsBrandVO();
					vo.setBrandId(o.getBrandId());
					vo.setBrandName(o.getEnName() + "" + o.getChName());
					return vo;
				}).collect(Collectors.toList());
	}
	
	private List<EcBrandResponse> getBrandResponse() {
		// 尝试从缓存获取
		return RedisUtil.get(CHANNELS_BRAND_LIST, 3600, () -> {
			List<EcBrandResponse> responses = new ArrayList<>();
			Integer totalNum = 0;
			Integer pageSizeTotal = 0;
			EcBrandListRequest brandListRequest = new EcBrandListRequest();
			do {
				brandListRequest.setPageSize(CHANNELS_BRAND_PAGE_SIZE);
				EcBrandListResponse brandListResponse = ecBrandService.getBrandList(brandListRequest);
				brandListRequest.setNextKey(brandListResponse.getNextKey());
				totalNum = brandListResponse.getTotalNum();
				pageSizeTotal += brandListResponse.getBrands().size();
				responses.addAll(brandListResponse.getBrands());
			} while (totalNum > pageSizeTotal);
			
			return responses;
		});
		
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void cancelBrand(String brandId) {
		
		Integer count = channelsBrandQualificationMapper.selectCount(new LambdaQueryWrapper<ChannelsBrandQualification>().eq(ChannelsBrandQualification::getIsDeleted, 0));
		if(count <= 1){
			throw new LuckException("品牌数量小于等于1,无法撤回");
		}
		
		//查询品牌数据
		ChannelsBrandQualification one = channelsBrandQualificationMapper.selectOne(new LambdaQueryWrapper<ChannelsBrandQualification>().eq(ChannelsBrandQualification::getBrandId, brandId));
		
		Assert.isNull(one, "品牌申请单不存在");
		if( Objects.equals(one.getStatus() , ChannelsBrandStatusEnum.AUDIT_SUCCESS.value())){
			throw new LuckException("品牌已审核成功,无法撤回");
		}
		
		EcBrandCancelRequest request = new EcBrandCancelRequest();
		request.setBrandId(one.getBrandId());
		request.setAuditId(one.getAuditId());
		
		ecBrandService.cancelBrand(request);
		
		//删除缓存
		RedisUtil.del(CHANNELS_BRAND_LIST);
	}
	
	@Override
	public ChannelsBrandOneVO getByBrandId(String brandId) {
		//查询系统品牌数据
		ChannelsBrandQualification one = channelsBrandQualificationMapper.selectOne(new LambdaQueryWrapper<ChannelsBrandQualification>().eq(ChannelsBrandQualification::getBrandId, brandId));
		ChannelsBrandOneVO oneVO = new ChannelsBrandOneVO();
		BeanUtil.copyProperties(one, oneVO);
		return oneVO;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateBrandEvent(BrandAuditDTO brandAuditDTO) {
		channelsBrandQualificationMapper.updateBrandEvent(brandAuditDTO);
		//删除缓存
		RedisUtil.del(CHANNELS_BRAND_LIST);
	}
}
