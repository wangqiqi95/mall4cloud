package com.mall4j.cloud.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mall4j.cloud.biz.wx.wx.api.live.SellerApi;
import com.mall4j.cloud.biz.wx.wx.api.live.SpuApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.LiveCategoryDTO;
import com.mall4j.cloud.api.biz.dto.livestore.request.CategoryAuditReq;
import com.mall4j.cloud.api.biz.dto.livestore.request.CategoryInfo;
import com.mall4j.cloud.api.biz.dto.livestore.request.CategoryRequest;
import com.mall4j.cloud.api.biz.dto.livestore.response.AuditResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.CategoryResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.ThirdCatList;
import com.mall4j.cloud.biz.mapper.WechatCatogoryQualificationMapper;
import com.mall4j.cloud.biz.model.WechatCatogoryQualificationDO;
import com.mall4j.cloud.biz.service.WechatLiveCategoryService;
import com.mall4j.cloud.biz.service.WechatLiveMediaService;
import com.mall4j.cloud.biz.vo.LiveCategoryVO;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.security.AuthUserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 上传文件记录表
 *
 * @author YXF
 * @date 2020-11-21 10:21:40
 */
@Service
@Slf4j
public class WechatLiveCategoryServiceImpl implements WechatLiveCategoryService {

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private SpuApi spuApi;
    @Autowired
    SellerApi sellerApi;

    @Autowired
    private WechatCatogoryQualificationMapper catogoryQualificationMapper;

    @Autowired
    private WechatLiveMediaService mediaService;

    private static final String WX_CATEGORY_LIST = "WX_CATEGORY_LIST";

    /**
     * 查询腾讯的类目列表
     *
     * @param query 查询关键字
     * @param all 1-查询全部 0(默认)-只查需要资质的
     * @return
     */
    @Override
    public List<LiveCategoryVO> baseList(String query, String all) {
        // 获取腾讯类目列表
        CategoryResponse categoryResponse = fetchCategoryResponse("1".equals(all));

        List<LiveCategoryVO> result = new ArrayList<>();

        for (ThirdCatList thirdCat : categoryResponse.getThirdCatList()) {
            LiveCategoryVO vo = new LiveCategoryVO();
            vo.setCategoryId(thirdCat.getThirdCatId());
            vo.setCategoryName(thirdCat.getFirstCatName() + "/" + thirdCat.getSecondCatName() + "/" + thirdCat.getThirdCatName());
            if (StringUtils.isNotBlank(query)) {
                if (vo.getCategoryName().contains(query) || vo.getCategoryId().toString().contains(query)) {
                    result.add(vo);
                }
                continue;
            }

            result.add(vo);
        }

        return result;
    }

    @Override
    public List<LiveCategoryVO> applyList(String query) {
        // 获取腾讯不需要资质的类目列表
        CategoryResponse categoryResponse = fetchCategoryResponse2();

        List<LiveCategoryVO> result = new ArrayList<>();

        for (ThirdCatList thirdCat : categoryResponse.getThirdCatList()) {
            LiveCategoryVO vo = new LiveCategoryVO();
            vo.setCategoryId(thirdCat.getThirdCatId());
            vo.setCategoryName(thirdCat.getFirstCatName() + "/" + thirdCat.getSecondCatName() + "/" + thirdCat.getThirdCatName());
            if (StringUtils.isNotBlank(query)) {
                if (vo.getCategoryName().contains(query) || vo.getCategoryId().toString().contains(query)) {
                    result.add(vo);
                }
                continue;
            }

            result.add(vo);
        }

        return result;
    }

    /**
     * 查询基本类目信息
     *
     * @param wxCategoryId .
     * @return
     */
    @Override
    public ThirdCatList baseOne(String wxCategoryId) {
        CategoryResponse categoryResponse = fetchCategoryResponse(true);

        for (ThirdCatList thirdCat : categoryResponse.getThirdCatList()) {
            if (StringUtils.isNotBlank(wxCategoryId)) {
                if (thirdCat.getThirdCatId().equals(Long.valueOf(wxCategoryId))) {
                    return thirdCat;
                }
            }
        }
        throw new LuckException("该类目不存在,id=" + wxCategoryId);
    }

    /**
     * 获取腾讯类目列表
     * @return .
     */
    private CategoryResponse fetchCategoryResponse(boolean all) {
        // 尝试从缓存获取
        CategoryResponse categoryResponse = RedisUtil.get(WX_CATEGORY_LIST, 3600, () -> {
            // 调用微信接口获取类目列表
            CategoryResponse allCategory = spuApi.getAllCategory(wxConfig.getWxMaToken(), "{}");
            if (allCategory != null) {
                if (allCategory.getErrcode() != 0) {
                    log.error("调用微信接口获取类目列表异常 token={}, res={}", wxConfig.getWxMaToken(), JSON.toJSONString(allCategory));
                    return null;
                }
            }
            return allCategory;
        });

        if (categoryResponse == null) {
            throw new LuckException("查询腾讯的类目列表失败");
        }

        // 排除不需要资质的类目
        if (!all) {
            categoryResponse.getThirdCatList().removeIf(o->o.getQualificationType() != 1);
        }

        return categoryResponse;
    }

    /**
     * 获取不需要资质的类目列表
     * @return .
     */
    private CategoryResponse fetchCategoryResponse2() {
        // 尝试从缓存获取
        CategoryResponse categoryResponse = RedisUtil.get(WX_CATEGORY_LIST, 3600, () -> {
            // 调用微信接口获取类目列表
            CategoryResponse allCategory = spuApi.getAllCategory(wxConfig.getWxMaToken(), "{}");
            if (allCategory != null) {
                if (allCategory.getErrcode() != 0) {
                    log.error("调用微信接口获取类目列表异常 token={}, res={}", wxConfig.getWxMaToken(), JSON.toJSONString(allCategory));
                    return null;
                }
            }
            return allCategory;
        });

        if (categoryResponse == null) {
            throw new LuckException("查询腾讯的类目列表失败");
        }

        // 排除需要资质的类目
        categoryResponse.getThirdCatList().removeIf(o->o.getQualificationType() == 1);

        return categoryResponse;
    }

    /**
     * 查询类目列表
     *
     * @return .
     */
    @Override
    public List<LiveCategoryVO> list() {
        List<WechatCatogoryQualificationDO> list = catogoryQualificationMapper.list();
        List<LiveCategoryVO> result = new ArrayList<>();
        for (WechatCatogoryQualificationDO catogoryQualificationDO : list) {
            LiveCategoryVO vo = new LiveCategoryVO();
            vo.setCategoryId(Long.valueOf(catogoryQualificationDO.getWxCategoryId()));
            vo.setCategoryName(catogoryQualificationDO.getWxCategoryName());
            vo.setRejectReason(catogoryQualificationDO.getAuditContent());
            vo.setStatus(catogoryQualificationDO.getStatus());
            vo.setCreateTime(catogoryQualificationDO.getCreateTime());
            vo.setUpdateTime(catogoryQualificationDO.getUpdateTime());
            result.add(vo);
        }
        return result;
    }

    /**
     * 添加类目
     *
     * @param liveCategoryDTO .
     */
    @Override
    public void add(LiveCategoryDTO liveCategoryDTO) {
        // 缓存获取这个类目信息
        CategoryResponse categoryResponse = fetchCategoryResponse(false);
        ThirdCatList category = null;
        for (ThirdCatList thirdCat : categoryResponse.getThirdCatList()) {
            if (liveCategoryDTO.getCategoryId().equals(thirdCat.getThirdCatId().toString())) {
                category = thirdCat;
                break;
            }
        }

        if (category == null) {
            throw new LuckException("找不到对于的类目, req={}", JSON.toJSONString(liveCategoryDTO));
        }

        // 调用微信审核接口
        CategoryRequest categoryRequest = new CategoryRequest();
        CategoryAuditReq auditReq = new CategoryAuditReq();
        CategoryInfo categoryInfo = new CategoryInfo();
        categoryInfo.setCertificate(Lists.newArrayList(mediaService.uploadImage(liveCategoryDTO.getCertificate())));
        categoryInfo.setLevel1(category.getFirstCatId());
        categoryInfo.setLevel2(category.getSecondCatId());
        categoryInfo.setLevel3(category.getThirdCatId());

        auditReq.setCategoryInfo(categoryInfo);
        auditReq.setLicense(Lists.newArrayList(mediaService.uploadImage(liveCategoryDTO.getLicense())));
        if(CollUtil.isNotEmpty(liveCategoryDTO.getSceneGroupList())){
            auditReq.setSceneGroupList(liveCategoryDTO.getSceneGroupList());
        }


        categoryRequest.setAuditReq(auditReq);

        AuditResponse auditResponse = spuApi.auditCategory(wxConfig.getWxMaToken(), categoryRequest);
        if (auditResponse.getErrcode() != 0) {
            log.error("提交审核类目失败, req={}, res={}", JSON.toJSONString(categoryRequest), JSON.toJSONString(auditResponse));
            throw new LuckException("提交审核类目失败, 错误信息:" + auditResponse.getErrmsg());
        }

        WechatCatogoryQualificationDO entity = new WechatCatogoryQualificationDO();
        entity.setWxCategoryId(liveCategoryDTO.getCategoryId());
        entity.setWxCategoryName(category.getThirdCatName());
        entity.setWxQualification(category.getQualification());
        entity.setWxQualificationType(category.getQualificationType());
        entity.setWxProductQualification(category.getProductQualification());
        entity.setWxProductQualificationType(category.getProductQualificationType());
        entity.setWxSecondCatId(category.getSecondCatId().toString());
        entity.setWxSecondCatName(category.getSecondCatName());
        entity.setWxFirstCatId(category.getFirstCatId().toString());
        entity.setWxFirstCatName(category.getFirstCatName());
        entity.setStatus(0);
        entity.setAuditId(auditResponse.getAuditId());
        entity.setAuditTime(new Date());
        entity.setQualificationUrls(liveCategoryDTO.getCertificate());
        entity.setCreateBy(AuthUserContext.get().getBizUserId());
        entity.setUpdateBy(AuthUserContext.get().getBizUserId());

        catogoryQualificationMapper.insert(entity);
    }

    @Override
    public WechatCatogoryQualificationDO one(String wxCategoryId) {
        return catogoryQualificationMapper.getByWxCategoryId(wxCategoryId);
    }

    @Override
    public void auditResult(String auditId, int status, String reason) {
        catogoryQualificationMapper.auditResult(auditId,reason,status);
    }
}
