package com.mall4j.cloud.biz.service.channels.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.biz.dto.channels.EcCategoryAuditResult;
import com.mall4j.cloud.api.biz.dto.channels.EcCats;
import com.mall4j.cloud.api.biz.dto.channels.EcFile;
import com.mall4j.cloud.api.biz.dto.channels.request.EcCategoryAuditCancelRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.*;
import com.mall4j.cloud.biz.wx.wx.channels.EcBasicsApi;
import com.mall4j.cloud.biz.wx.wx.channels.EcCategoryApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.channels.ChannelsCategoryDTO;
import com.mall4j.cloud.biz.dto.channels.event.ProductCategoryAuditInfoDTO;
import com.mall4j.cloud.biz.dto.channels.query.ChannelsCategoryQueryDTO;
import com.mall4j.cloud.biz.mapper.channels.ChannelsCategoryMapper;
import com.mall4j.cloud.biz.model.channels.ChannelsCategory;
import com.mall4j.cloud.biz.service.channels.ChannelsCategoryService;
import com.mall4j.cloud.biz.service.channels.EcCategoryService;
import com.mall4j.cloud.biz.vo.channels.ChannelsCategoryDetailVO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 视频号4.0 类目
 *
 * @author FrozenWatermelon
 * @date 2023-02-15 16:01:16
 */
@Slf4j
@Service
public class ChannelsCategoryServiceImpl extends ServiceImpl<ChannelsCategoryMapper, ChannelsCategory>
        implements ChannelsCategoryService {
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private ChannelsCategoryMapper channelsCategoryMapper;
    @Autowired
    EcCategoryApi ecCategoryApi;
    @Autowired
    EcCategoryService ecCategoryService;

    @Autowired
    WxConfig wxConfig;

    @Autowired
    EcBasicsApi ecBasicsApi;

    @Override
    public List<EcCats> allList() {
        EcAllCategoryResponse response = ecCategoryApi.allCategory(wxConfig.getWxEcToken());
        Assert.isNull(response, "请求微信API异常");
        Assert.isTrue(response.getErrcode() != 0, response.getErrmsg());
        return response.getCats();
    }

    @Override
    public PageVO<ChannelsCategory> page(ChannelsCategoryQueryDTO dto) {
        Wrapper<ChannelsCategory> wrapper = getCategoryWrapper(dto);
        return PageUtil.doPage(dto, () -> channelsCategoryMapper.selectList(wrapper));
    }

    private  Wrapper<ChannelsCategory> getCategoryWrapper(ChannelsCategoryQueryDTO dto) {
        return Wrappers.lambdaQuery(ChannelsCategory.class)
                .eq(Objects.nonNull(dto.getStatus()), ChannelsCategory::getStatus, dto.getStatus())
                .apply(StrUtil.isNotBlank(dto.getName()), "CONCAT(name1,name2,name3) LIKE '%" + dto.getName() + "%'");
    }

    @Override
    public ChannelsCategory getById(Long id) {
        return channelsCategoryMapper.getById(id);
    }


    @Override
    public void deleteById(Long id) {
        channelsCategoryMapper.deleteById(id);
    }

    @Override
    public ServerResponseEntity<Void> applyOrReApply(ChannelsCategoryDTO channelsCategoryDTO, Boolean isApply) {
        if (!isApply && channelsCategoryDTO.getId() == null){
            Assert.faild("未找到对应申请类目，请确认参数");
        }

        channelsCategoryDTO.setStatus(1);
        ChannelsCategory channelsCategory = mapperFacade.map(channelsCategoryDTO, ChannelsCategory.class);
        Long auditId;
        //先调用视频号的保存
        EcAddCategoryResponse ecAddCategoryResponse = ecCategoryService.add(channelsCategoryDTO);
        if (ecAddCategoryResponse == null || ecAddCategoryResponse.getErrcode() != 0) {
            if (ecAddCategoryResponse != null && StrUtil.isNotEmpty(ecAddCategoryResponse.getErrmsg())) {
                return ServerResponseEntity.showFailMsg(ecAddCategoryResponse.getErrmsg());
            } else {
                return ServerResponseEntity.showFailMsg("请求微信视频号API失败");
            }
        }
        auditId = ecAddCategoryResponse.getAudit_id();

        //保存数据库
        channelsCategory.setAuditId(auditId);
        if (isApply) {
            channelsCategoryMapper.insert(channelsCategory);
        } else {
            channelsCategory.setRejectReason("无");
            channelsCategoryMapper.updateById(channelsCategory);
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ChannelsCategoryDetailVO detail(Long id) {
        ChannelsCategoryDetailVO detailVO = new ChannelsCategoryDetailVO();

        ChannelsCategory channelsCategory = channelsCategoryMapper.getById(id);
        Assert.isNull(channelsCategory,"类目信息不存在，请检查数据后重试。");
        detailVO.setChannelsCategory(channelsCategory);

        // 微信视频号api返回
        EcCategoryDetailResponse response = ecCategoryService.detail(channelsCategory.getLevel3());
        if(response.getErrcode()!=0){
            log.error("视频号查询类目详情失败，参数:{},返回结果:{}", channelsCategory.getLevel3(), JSONObject.toJSONString(response));
            Assert.faild(response.getErrmsg());
        }
        detailVO.setAttr(response.getAttr());
        detailVO.setInfo(response.getInfo());

        return detailVO;
    }

    @Override
    public void cancel(Long id) {
        ChannelsCategory channelsCategory = channelsCategoryMapper.getById(id);
        Assert.isNull(channelsCategory, "类目申请单不存在");
        Assert.isTrue(channelsCategory.getStatus() != 1, "只能撤回审核中的申请单");

        EcBaseResponse response = ecCategoryService.cancel(channelsCategory.getAuditId());
        log.info("请求微信视频号4.0取消审核， 返回结果：{}", response);
        Assert.isNull(response, "微信视频号请求异常");
        Assert.isTrue(response.getErrcode() != 0, response.getErrmsg());

        ChannelsCategory updateBean = new ChannelsCategory();
        updateBean.setId(id);
        updateBean.setStatus(12);
        channelsCategoryMapper.updateById(updateBean);
    }

    @Override
    public void audit(ProductCategoryAuditInfoDTO productCategoryAuditInfoDTO) {
        ChannelsCategory channelsCategory = channelsCategoryMapper.selectOne(Wrappers
                .lambdaQuery(ChannelsCategory.class)
                .eq(ChannelsCategory::getAuditId, productCategoryAuditInfoDTO.getAuditId()));

        if (Objects.nonNull(channelsCategory)) {
            channelsCategory.setStatus(productCategoryAuditInfoDTO.getStatus());
            channelsCategory.setRejectReason(productCategoryAuditInfoDTO.getReason());
            channelsCategoryMapper.updateById(channelsCategory);
        }
    }

    @Override
    public EcFile uploadQualification(MultipartFile file) {
        MultipartBody.Part part;
        try {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file.getBytes());
            part = MultipartBody.Part.createFormData("media", file.getOriginalFilename(), requestBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        EcQualificationUploadResponse response = ecBasicsApi.qualificationUpload(wxConfig.getWxEcToken(), part);
        log.info("请求微信视频号4.0资质图片上传， 返回结果：{}", response);
        Assert.isNull(response, "请求微信视频号4.0资质图片上传异常");
        Assert.isTrue(response.getErrcode() != 0, response.getErrmsg());
        return response.getData();
    }

    @Override
    public EcCategoryAuditResult getAudit(Long id) {
        ChannelsCategory channelsCategory = channelsCategoryMapper.getById(id);
        Assert.isNull(channelsCategory, "类目申请单不存在");

        EcCategoryAuditCancelRequest request = new EcCategoryAuditCancelRequest();
        request.setAudit_id(channelsCategory.getAuditId());
        EcGetCategoryAuditResultResponse response = ecCategoryApi.getAuditResult(wxConfig.getWxEcToken(), request);
        log.info("请求微信视频号4.0获取审核结果接口， 返回结果：{}", response);
        Assert.isNull(response, "请求微信视频号4.0获取审核结果接口异常");
        Assert.isTrue(response.getErrcode() != 0, response.getErrmsg());
        return response.getData();
    }

}
