package com.mall4j.cloud.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.mall4j.cloud.biz.wx.wx.api.live.SpuApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.LiveBrandDTO;
import com.mall4j.cloud.api.biz.dto.livestore.request.BrandAuditReq;
import com.mall4j.cloud.api.biz.dto.livestore.request.BrandInfo;
import com.mall4j.cloud.api.biz.dto.livestore.request.BrandRequest;
import com.mall4j.cloud.api.biz.dto.livestore.response.AuditResponse;
import com.mall4j.cloud.biz.mapper.WechatBrandQualificationMapper;
import com.mall4j.cloud.biz.model.WechatBrandQualificationDO;
import com.mall4j.cloud.biz.service.WechatLiveBrandService;
import com.mall4j.cloud.biz.service.WechatLiveMediaService;
import com.mall4j.cloud.biz.vo.LiveBrandVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 上传文件记录表
 *
 * @author YXF
 * @date 2020-11-21 10:21:40
 */
@Service
@Slf4j
public class WechatLiveBrandServiceImpl implements WechatLiveBrandService {

    @Autowired
    private WechatBrandQualificationMapper wechatBrandQualificationMapper;

    @Autowired
    private SpuApi spuApi;

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private WechatLiveMediaService mediaService;

    /**
     * 查询品牌列表
     *
     * @return
     */
    @Override
    public PageVO<LiveBrandVO> list(PageDTO pageDTO) {
        PageVO<LiveBrandVO> result = new PageVO<>();
        PageVO<WechatBrandQualificationDO> doPage = PageUtil.doPage(pageDTO, () -> wechatBrandQualificationMapper.list());
        result.setPages(doPage.getPages());
        result.setTotal(doPage.getTotal());
        List<WechatBrandQualificationDO> list = wechatBrandQualificationMapper.list();
        result.setList(list == null? Lists.newArrayList() : list.stream()
                .map(o -> {
                    LiveBrandVO dto = new LiveBrandVO();
                    dto.setBrandId(o.getBrandId());
                    dto.setBrandName(o.getBrandWording());
                    dto.setStatus(o.getStatus());
                    dto.setAuditContent(o.getAuditContent());
                    dto.setAuditTime(o.getAuditTime());
                    return dto;
                }).collect(Collectors.toList()));

        return result;
    }

    /**
     * 添加品牌
     *
     * @param liveBrandDTO
     */
    @Override
    public void add(LiveBrandDTO liveBrandDTO) {
        // TODO 必填校验
        log.info("添加品牌输入参数信息：{}",JSONObject.toJSONString(liveBrandDTO));

        // 先调用微信接口 在微信侧审核
        BrandRequest auditBrandRequest = new BrandRequest();
        BrandAuditReq auditReq = new BrandAuditReq();
        BrandInfo brandInfo = new BrandInfo();
        brandInfo.setBrandAuditType(liveBrandDTO.getBrandAuditType());
        brandInfo.setBrandManagementType(liveBrandDTO.getBrandManagementType());
        brandInfo.setBrandWording(liveBrandDTO.getBrandWording());
        brandInfo.setCommodityOriginType(liveBrandDTO.getCommodityOriginType());
        brandInfo.setImportedGoodsForm(Lists.newArrayList(mediaService.uploadImage(liveBrandDTO.getImportedGoodsForm())));
        brandInfo.setSaleAuthorization(Lists.newArrayList(mediaService.uploadImage(liveBrandDTO.getSaleAuthorization())));
        brandInfo.setTrademarkApplicant(liveBrandDTO.getTrademarkApplicant());
        if(liveBrandDTO.getTrademarkApplicationTime()!=null){
            brandInfo.setTrademarkApplicationTime(DateUtils.dateToString(liveBrandDTO.getTrademarkApplicationTime()));
        }
        brandInfo.setTrademarkAuthorizationPeriod(DateUtils.dateToString(liveBrandDTO.getTrademarkAuthorizationPeriod()));
        brandInfo.setTrademarkChangeCertificate(Lists.newArrayList(mediaService.uploadImage(liveBrandDTO.getTrademarkChangeCertificate())));
        brandInfo.setTrademarkRegistrant(liveBrandDTO.getTrademarkRegistrant());
        brandInfo.setTrademarkRegistrantNu(liveBrandDTO.getTrademarkRegistrantNu());
        brandInfo.setTrademarkRegistrationApplication(Lists.newArrayList(mediaService.uploadImage(liveBrandDTO.getTrademarkRegistrationApplication())));
        brandInfo.setTrademarkRegistrationCertificate(Lists.newArrayList(mediaService.uploadImage(liveBrandDTO.getTrademarkRegistrationCertificate())));
        brandInfo.setTrademarkType(liveBrandDTO.getTrademarkType());

        auditReq.setBrandInfo(brandInfo);
        auditReq.setSceneGroupList(liveBrandDTO.getSceneGroupList());
        auditReq.setLicense(Lists.newArrayList((mediaService.uploadImage(liveBrandDTO.getLicense()))));

        auditBrandRequest.setAuditReq(auditReq);
        log.info("提交品牌审核参数对象：{}", JSONObject.toJSONString(auditBrandRequest));
        AuditResponse auditResponse = spuApi.auditBrand(wxConfig.getWxMaToken(), auditBrandRequest);
        log.info("提交品牌审核执行结束，参数对象：{}，执行结果{}", JSONObject.toJSONString(auditBrandRequest),JSONObject.toJSONString(auditResponse));
        if (auditResponse.getErrcode() != 0) {
            log.error("提交审核品牌失败, req={}, res={}", JSON.toJSONString(auditBrandRequest), JSON.toJSONString(auditResponse));
            throw new LuckException("提交审核品牌失败, 错误信息:" + auditResponse.getErrmsg());
        }

        // 入库
        WechatBrandQualificationDO entity = new WechatBrandQualificationDO();
        entity.setLicense(liveBrandDTO.getLicense());
        entity.setBrandAuditType(liveBrandDTO.getBrandAuditType());
        entity.setTrademarkType(liveBrandDTO.getTrademarkType());
        entity.setBrandManagementType(liveBrandDTO.getBrandManagementType());
        entity.setCommodityOriginType(liveBrandDTO.getCommodityOriginType());
        entity.setBrandWording(liveBrandDTO.getBrandWording());
        entity.setSaleAuthorization(liveBrandDTO.getSaleAuthorization());
        entity.setTrademarkRegistrationCertificate(liveBrandDTO.getTrademarkRegistrationCertificate());
        entity.setTrademarkRegistrant(liveBrandDTO.getTrademarkRegistrant());
        entity.setTrademarkRegistrantNu(liveBrandDTO.getTrademarkRegistrantNu());
        entity.setTrademarkAuthorizationPeriod(liveBrandDTO.getTrademarkAuthorizationPeriod());
        entity.setTrademarkRegistrationApplication(liveBrandDTO.getTrademarkRegistrationApplication());
        entity.setTrademarkApplicant(liveBrandDTO.getTrademarkApplicant());
        entity.setTrademarkApplicationTime(liveBrandDTO.getTrademarkApplicationTime());
        entity.setAuditId(auditResponse.getAuditId());
        entity.setAuditTime(new Date());
        entity.setStatus(0);
        entity.setCreateBy(AuthUserContext.get().getBizUserId());
        entity.setUpdateBy(AuthUserContext.get().getBizUserId());
        entity.setIsDeleted(0);

        wechatBrandQualificationMapper.insert(entity);
    }

    @Override
    public int syncAuditResult(String auditId, String auditContent, Integer status,Long wxBrandId) {
        return wechatBrandQualificationMapper.syncAuditResult(auditId,auditContent,status,wxBrandId);
    }

    /**
     * 查询已审核的品牌列表
     *
     * @return
     */
    @Override
    public List<LiveBrandVO> auditedList() {
        List<WechatBrandQualificationDO> list = wechatBrandQualificationMapper.list();
        return list == null? Lists.newArrayList() : list.stream()
                // 审核通过的
                .filter(o-> o.getStatus() == 1)
                .map(o->{
                    LiveBrandVO dto = new LiveBrandVO();
                    dto.setBrandId(o.getWxBrandId());
                    dto.setBrandName(o.getBrandWording());

                    return dto;
                }).collect(Collectors.toList());
    }
}
