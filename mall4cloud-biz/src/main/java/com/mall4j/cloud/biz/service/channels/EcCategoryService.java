package com.mall4j.cloud.biz.service.channels;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.channels.EcAddCategoryInfo;
import com.mall4j.cloud.api.biz.dto.channels.request.EcAddCategoryRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcCategoryAuditCancelRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcCategoryDetailRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcPageRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.*;
import com.mall4j.cloud.biz.wx.wx.channels.EcCategoryApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.channels.ChannelsCategoryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
public class EcCategoryService {

    @Autowired
    EcCategoryApi ecCategoryApi;
    @Autowired
    WxConfig wxConfig;

    public EcAllCategoryResponse all(EcPageRequest ecPageRequest){
        EcAllCategoryResponse response = ecCategoryApi.allCategory(wxConfig.getWxEcToken());
        return response;
    }

    public EcCategoryDetailResponse detail(Long catId){
        EcCategoryDetailRequest request = new EcCategoryDetailRequest();
        request.setCat_id(catId);
        EcCategoryDetailResponse response = ecCategoryApi.detail(wxConfig.getWxEcToken(), request);
        return response;
    }

    public EcAddCategoryResponse add(ChannelsCategoryDTO categoryDTO){

        EcAddCategoryRequest request = getEcAddCategoryRequest(categoryDTO);
        log.info("视频号申请类目接口，执行参数：{}", JSONObject.toJSONString(request));
        EcAddCategoryResponse response = ecCategoryApi.add(wxConfig.getWxEcToken(), request);
        log.info("视频号申请类目接口执行结束，执行参数：{},执行结果:{}", JSONObject.toJSONString(request),JSONObject.toJSONString(response));
        return response;
    }

    private static EcAddCategoryRequest getEcAddCategoryRequest(ChannelsCategoryDTO categoryDTO) {
        EcAddCategoryRequest request = new EcAddCategoryRequest();

        EcAddCategoryInfo categoryInfo = new EcAddCategoryInfo();
        categoryInfo.setLevel1(categoryDTO.getLevel1());
        categoryInfo.setLevel2(categoryDTO.getLevel2());
        categoryInfo.setLevel3(categoryDTO.getLevel3());
        categoryInfo.setCertificate(Arrays.asList(categoryDTO.getCertificate().split(",")));
        if (StrUtil.isNotBlank(categoryDTO.getBaobeihan())) {
            categoryInfo.setBaobeihan(Arrays.asList(categoryDTO.getBaobeihan().split(",")));
        }
        if (StrUtil.isNotBlank(categoryDTO.getJingyingzhengming())) {
            categoryInfo.setJingyingzhengming(Arrays.asList(categoryDTO.getJingyingzhengming().split(",")));
        }
        if (StrUtil.isNotBlank(categoryDTO.getDaihuokoubei())) {
            categoryInfo.setDaihuokoubei(Arrays.asList(categoryDTO.getDaihuokoubei().split(",")));
        }
        if (StrUtil.isNotBlank(categoryDTO.getRuzhuzhizhi())) {
            categoryInfo.setRuzhuzhizhi(Arrays.asList(categoryDTO.getRuzhuzhizhi().split(",")));
        }
        if (StrUtil.isNotBlank(categoryDTO.getJingyingliushui())) {
            categoryInfo.setJingyingliushui(Arrays.asList(categoryDTO.getJingyingliushui().split(",")));
        }
        if (StrUtil.isNotBlank(categoryDTO.getBuchongcailiao())) {
            categoryInfo.setBuchongcailiao(Arrays.asList(categoryDTO.getBuchongcailiao().split(",")));
        }
        if (StrUtil.isNotBlank(categoryDTO.getJingyingpingtai())) {
            categoryInfo.setJingyingpingtai(categoryDTO.getJingyingpingtai());
        }
        if (StrUtil.isNotBlank(categoryDTO.getZhanghaomingcheng())) {
            categoryInfo.setZhanghaomingcheng(categoryDTO.getZhanghaomingcheng());
        }

        request.setCategory_info(categoryInfo);
        return request;
    }

    /**
     * 取消申请
     * @param auditId 审核Id
     * @return response
     */
    public EcBaseResponse cancel(Long auditId){
        EcCategoryAuditCancelRequest request = new EcCategoryAuditCancelRequest();
        request.setAudit_id(auditId);
        log.info("视频号取消申请，执行参数：{}", auditId);
        EcBaseResponse response = ecCategoryApi.cancel(wxConfig.getWxEcToken(), request);
        log.info("视频会取消申请接口响应参数：{}", response);
        return response;
    }

}
