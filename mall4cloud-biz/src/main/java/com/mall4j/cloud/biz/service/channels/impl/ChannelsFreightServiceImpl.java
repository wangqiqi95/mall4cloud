package com.mall4j.cloud.biz.service.channels.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.biz.dto.channels.AddressInfo;
import com.mall4j.cloud.api.biz.dto.channels.EcAllConditionFreeDetail;
import com.mall4j.cloud.api.biz.dto.channels.EcAllFreightCalcMethod;
import com.mall4j.cloud.api.biz.dto.channels.EcConditionFreeDetail;
import com.mall4j.cloud.api.biz.dto.channels.EcFreightTemplate;
import com.mall4j.cloud.api.biz.dto.channels.EcFreightCalcMethod;
import com.mall4j.cloud.api.biz.dto.channels.request.EcFreightTemplateRequest;
import com.mall4j.cloud.api.biz.dto.channels.EcNotSendArea;
import com.mall4j.cloud.api.biz.dto.channels.response.EcFreightTemplateResponse;
import com.mall4j.cloud.api.delivery.bo.TransfeeBO;
import com.mall4j.cloud.api.delivery.bo.TransfeeFreeBO;
import com.mall4j.cloud.api.delivery.bo.TransportBO;
import com.mall4j.cloud.api.delivery.feign.AreaFeignClient;
import com.mall4j.cloud.api.delivery.feign.DeliveryFeignClient;
import com.mall4j.cloud.api.delivery.vo.AreaVO;
import com.mall4j.cloud.biz.dto.channels.ChannelsFreightDTO;
import com.mall4j.cloud.biz.dto.channels.ChannelsFreightUpdateDTO;
import com.mall4j.cloud.biz.mapper.channels.ChannelsFreightTemplateMapper;
import com.mall4j.cloud.biz.model.channels.ChannelsFreightTemplate;
import com.mall4j.cloud.biz.service.channels.ChannelsFreightService;
import com.mall4j.cloud.biz.service.channels.EcFreightService;
import com.mall4j.cloud.biz.vo.channels.ChannelsFreightVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChannelsFreightServiceImpl implements ChannelsFreightService {
	
	@Autowired
	private DeliveryFeignClient deliveryFeignClient;
	
	@Autowired
	private EcFreightService ecFreightService;
	
	@Autowired
	private AreaFeignClient areaFeignClient;
	
	@Autowired
	private ChannelsFreightTemplateMapper channelsFreightTemplateMapper;
	
	@Override
	public PageVO<ChannelsFreightVO> list(PageDTO pageDTO, String templateName) {
		return PageUtil.doPage(pageDTO, () -> channelsFreightTemplateMapper.list(templateName));
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(ChannelsFreightDTO channelsFreightDTO) {
		Long transportId = channelsFreightDTO.getTransportId();
		//获取小程序运费模板
		ServerResponseEntity<TransportBO> responseEntity = deliveryFeignClient.getByTransportId(transportId);
		if (!Objects.equals(responseEntity.getCode(), ResponseEnum.OK.value())) {
			throw new LuckException("获取运费模板失败,模板id:"+ channelsFreightDTO.getTransportId());
		}
		UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
		TransportBO transportBO = responseEntity.getData();
		log.info("运费模板数据为："+ JSONObject.toJSONString(transportBO));
		
		//映射视频号运费模板
		EcFreightTemplateRequest request = new EcFreightTemplateRequest();
		EcFreightTemplate ecFreightTemplate = handleData(transportBO);
		
		request.setEcFreightTemplate(ecFreightTemplate);
		
		EcFreightTemplateResponse templateResponse = ecFreightService.add(request);
		
		//入库
		ChannelsFreightTemplate channelsFreightTemplate = new ChannelsFreightTemplate();
		channelsFreightTemplate.setTransportId(transportBO.getTransportId());
		channelsFreightTemplate.setTransName(transportBO.getTransName());
		channelsFreightTemplate.setWxTemplateId(templateResponse.getTemplateId());
		channelsFreightTemplate.setWxTemplateName(transportBO.getTransName());
		channelsFreightTemplate.setCreateBy(userInfoInTokenBO.getUsername());
		channelsFreightTemplate.setCreateTime(new Date());
		
		channelsFreightTemplateMapper.insert(channelsFreightTemplate);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update( ChannelsFreightUpdateDTO channelsFreightUpdateDTO) {
		//获取小程序运费模板
		ServerResponseEntity<TransportBO> responseEntity = deliveryFeignClient.getByTransportId(channelsFreightUpdateDTO.getTransportId());
		if (!Objects.equals(responseEntity.getCode(), ResponseEnum.OK.value())) {
			throw new LuckException("获取运费模板失败,模板id:"+ channelsFreightUpdateDTO.getTransportId());
		}
		UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
		TransportBO transportBO = responseEntity.getData();
		log.info("运费模板数据为："+ JSONObject.toJSONString(transportBO));
		
		//映射视频号运费模板
		EcFreightTemplateRequest request = new EcFreightTemplateRequest();
		EcFreightTemplate ecFreightTemplate = handleData(transportBO);
		ecFreightTemplate.setTemplateId(channelsFreightUpdateDTO.getWxTemplateId());
		
		request.setEcFreightTemplate(ecFreightTemplate);
		ecFreightService.update(request);
		
		//入库
		ChannelsFreightTemplate channelsFreightTemplate = new ChannelsFreightTemplate();
		channelsFreightTemplate.setTransportId(transportBO.getTransportId());
		channelsFreightTemplate.setTransName(transportBO.getTransName());
		channelsFreightTemplate.setWxTemplateId(channelsFreightUpdateDTO.getWxTemplateId());
		channelsFreightTemplate.setWxTemplateName(transportBO.getTransName());
		channelsFreightTemplate.setUpdateBy(userInfoInTokenBO.getUsername());
		channelsFreightTemplate.setUpdateTime(new Date());
		channelsFreightTemplateMapper.update(channelsFreightTemplate,new LambdaUpdateWrapper<ChannelsFreightTemplate>()
				.eq(ChannelsFreightTemplate::getWxTemplateId,channelsFreightUpdateDTO.getWxTemplateId()));
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long transportId) {
		Integer count = channelsFreightTemplateMapper.selectCount(new LambdaQueryWrapper<ChannelsFreightTemplate>()
				.eq(ChannelsFreightTemplate::getIsDeleted, 0));
		
		if(count <= 1){
			throw  new LuckException("运费模板数量小于等于1,无法删除");
		}
		
		channelsFreightTemplateMapper.update(null,new LambdaUpdateWrapper<ChannelsFreightTemplate>()
				.set(ChannelsFreightTemplate::getIsDeleted,1)
				.eq(ChannelsFreightTemplate::getTransportId,transportId));
	}
	
	public EcFreightTemplate handleData(TransportBO transportBO){
		
		EcFreightTemplate ecFreightTemplate = new EcFreightTemplate();
		
		//运费项
		List<TransfeeBO> transFeeBOs = transportBO.getTransFees();
		//指定条件包邮项
		List<TransfeeFreeBO> transFeeFreeBOs = transportBO.getTransFeeFrees();
		
		//包邮条件详情集合
		EcAllConditionFreeDetail ecAllConditionFreeDetail = new EcAllConditionFreeDetail();
		ArrayList<EcConditionFreeDetail> ecConditionFreeDetails = new ArrayList<>();
		
		//运费计算集合
		EcAllFreightCalcMethod ecAllFreightCalcMethod = new EcAllFreightCalcMethod();
		ArrayList<EcFreightCalcMethod> ecFreightCalcMethods = new ArrayList<>();
		
		ecFreightTemplate.setName(transportBO.getTransName());
		ecFreightTemplate.setDeliveryType("EXPRESS");
		ecFreightTemplate.setIsDefault(false);
		ecFreightTemplate.setSendTime("SendTime_THREE_DAY");
		AddressInfo addressInfo = new AddressInfo();
		addressInfo.setProvinceName("江苏省");
		ecFreightTemplate.setAddressInfo(addressInfo);
		
		//不包邮
		if(transportBO.getIsFreeFee() == 0 ){
			//含有包邮条件
			if(transportBO.getHasFreeCondition() == 1){
				EcConditionFreeDetail ecConditionFreeDetail = new EcConditionFreeDetail();
				
				if(CollectionUtil.isNotEmpty(transFeeFreeBOs)){
					for (TransfeeFreeBO freeBO : transFeeFreeBOs) {
						//这里给的是3等级的地址,要找到2等级和1等级的地址名称
						List<AreaVO> freeCityList = freeBO.getFreeCityList();
						List<AreaVO> level2 = handleAreaData(freeCityList);
						List<AreaVO> level1 = handleAreaData(level2);
						
						List<AddressInfo> addressInfos = new ArrayList<AddressInfo>();
						for (AreaVO areaVO : level1) {
							String areaName = areaVO.getAreaName();//省
							List<AreaVO> areas = areaVO.getAreas();
							for (AreaVO area : areas) {
								String cityName = area.getAreaName();//市
								//List<AreaVO> list = area.getAreas();
								//for (AreaVO vo : list) {
									AddressInfo info = new AddressInfo();
									info.setProvinceName(areaName);
									info.setCityName(cityName);
									//info.setCountyName(vo.getAreaName());//区
									addressInfos.add(info);
								//}
							}
						}
						ecConditionFreeDetail.setAddressInfos(addressInfos);
						if(Objects.nonNull(freeBO.getPiece())){
							ecConditionFreeDetail.setMinPiece(freeBO.getPiece().intValue());
							ecConditionFreeDetail.setMinWeight(freeBO.getPiece().intValue());
							ecConditionFreeDetail.setValuationFlag(1);
						}
						if(Objects.nonNull(freeBO.getAmount())){
							ecConditionFreeDetail.setMinAmount(freeBO.getAmount().intValue());
							ecConditionFreeDetail.setAmountFlag(1);
						}
						
						ecConditionFreeDetails.add(ecConditionFreeDetail);
					}
				}
				ecFreightTemplate.setShippingMethod("CONDITION_FREE");
				
			}else{  //不含包邮条件
				ecFreightTemplate.setShippingMethod("NO_FREE");
			}
			
			//收费方式（0 按件数,1 按重量 2 按体积）
			Integer chargeType = transportBO.getChargeType();
			if(chargeType == 0){ //按件数
				ecFreightTemplate.setValuationType("PIECE");
			}else if(chargeType == 1){ //按体重
				ecFreightTemplate.setValuationType("WEIGHT");
			}else if(chargeType == 2){ //按体积
				throw new LuckException("按体积收费的模板无法添加为视频号运费模板");
			}
			
			if(CollectionUtil.isNotEmpty(transFeeBOs)){
				for (TransfeeBO transFeeBO : transFeeBOs) {
					//运费计算方法
					EcFreightCalcMethod ecFreightCalcMethod = new EcFreightCalcMethod();
					List<AddressInfo> calcAddressInfos = new ArrayList<AddressInfo>();
					
					if(CollectionUtil.isEmpty(transFeeBO.getCityList())){
						// 默认的运费计算规则
						ecFreightCalcMethod.setFirstValAmount(Objects.nonNull(transFeeBO.getFirstPiece())?transFeeBO.getFirstPiece().intValue():0);
						ecFreightCalcMethod.setFirstPrice(Objects.nonNull(transFeeBO.getFirstFee())?transFeeBO.getFirstFee().intValue():0);
						ecFreightCalcMethod.setSecondValAmount(Objects.nonNull(transFeeBO.getContinuousPiece())?transFeeBO.getContinuousPiece().intValue():0);
						ecFreightCalcMethod.setSecondPrice(Objects.nonNull(transFeeBO.getContinuousFee())?transFeeBO.getContinuousFee().intValue():0);
						
						ecFreightCalcMethod.setIsDefault(true);
						ecFreightCalcMethod.setAddressInfos(calcAddressInfos);
					}else {
						// 指定地区运费计算规则
						ecFreightCalcMethod.setFirstValAmount(Objects.nonNull(transFeeBO.getFirstPiece())?transFeeBO.getFirstPiece().intValue():0);
						ecFreightCalcMethod.setFirstPrice(Objects.nonNull(transFeeBO.getFirstFee())?transFeeBO.getFirstFee().intValue():0);
						ecFreightCalcMethod.setSecondValAmount(Objects.nonNull(transFeeBO.getContinuousPiece())?transFeeBO.getContinuousPiece().intValue():0);
						ecFreightCalcMethod.setSecondPrice(Objects.nonNull(transFeeBO.getContinuousFee())?transFeeBO.getContinuousFee().intValue():0);
						
						ecFreightCalcMethod.setIsDefault(false);
						
						List<AreaVO> cityList = transFeeBO.getCityList();
						
						List<AreaVO> level2 = handleAreaData(cityList);
						List<AreaVO> level1 = handleAreaData(level2);
						
						for (AreaVO areaVO : level1) {
							String areaName = areaVO.getAreaName();//省
							List<AreaVO> areas = areaVO.getAreas();
							for (AreaVO area : areas) {
								String cityName = area.getAreaName();//市
								//List<AreaVO> list = area.getAreas();
								//for (AreaVO vo : list) {
									AddressInfo info = new AddressInfo();
									info.setProvinceName(areaName);
									info.setCityName(cityName);
								//	info.setCountyName(vo.getAreaName());//区
									calcAddressInfos.add(info);
								//}
							}
							
						}
						ecFreightCalcMethod.setAddressInfos(calcAddressInfos);

					}
					ecFreightCalcMethods.add(ecFreightCalcMethod);
				}
			}
			
		}else{  //包邮
			ecFreightTemplate.setShippingMethod("FREE");
			ecFreightTemplate.setValuationType("PIECE");
		}
		
		ecAllConditionFreeDetail.setEcConditionFreeDetailList(ecConditionFreeDetails);
		ecAllFreightCalcMethod.setEcFreightCalcMethodList(ecFreightCalcMethods);
		
		// 不支持发货地址集合
		EcNotSendArea ecNotSendArea = new EcNotSendArea();
		ArrayList<AddressInfo> addressInfos = new ArrayList<>();
		ecNotSendArea.setAddressInfos(addressInfos);
		ecFreightTemplate.setEcNotSendArea(ecNotSendArea);

		ecFreightTemplate.setEcAllConditionFreeDetail(ecAllConditionFreeDetail);
		ecFreightTemplate.setEcAllFreightCalcMethod(ecAllFreightCalcMethod);
		
		return ecFreightTemplate;
	}
	
	public List<AreaVO> handleAreaData(List<AreaVO> freeCityList){
		Map<Long, List<AreaVO>> listMap = freeCityList.stream().collect(Collectors.groupingBy(AreaVO::getParentId));
		
		ArrayList<AreaVO> result = new ArrayList<>();
		listMap.forEach( (parentId ,areaVOList) ->{
			ServerResponseEntity<AreaVO> byAreaId = areaFeignClient.getByAreaId(parentId);
			AreaVO vo = byAreaId.getData();
			if(Objects.nonNull(vo)){
				AreaVO area = BeanUtil.copyProperties(vo, AreaVO.class);
				area.setAreas(areaVOList);
				result.add(area);
			}
		});
		return result;
	}
}
