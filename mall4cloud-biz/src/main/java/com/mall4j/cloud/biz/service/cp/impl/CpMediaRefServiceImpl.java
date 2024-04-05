package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.mapper.cp.CpMediaRefMapper;
import com.mall4j.cloud.biz.model.cp.CpMediaRef;
import com.mall4j.cloud.biz.service.cp.CpMediaRefService;
import com.mall4j.cloud.biz.wx.cp.utils.ZipUtils;
import com.mall4j.cloud.common.security.AuthUserContext;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 
 *
 * @author gmq
 * @date 2023-11-27 11:29:05
 */
@Slf4j
@Service
public class CpMediaRefServiceImpl extends ServiceImpl<CpMediaRefMapper, CpMediaRef> implements CpMediaRefService {

    @Autowired
    private CpMediaRefMapper cpMediaRefMapper;

    @Override
    public CpMediaRef getById(Integer sourceFrom, String sourceId) {
        LambdaQueryWrapper<CpMediaRef> lambdaQueryWrapper=new LambdaQueryWrapper<CpMediaRef>();
        lambdaQueryWrapper.eq(CpMediaRef::getSourceId,sourceId);
        lambdaQueryWrapper.eq(CpMediaRef::getSourceFrom,sourceFrom);
        lambdaQueryWrapper.eq(CpMediaRef::getIsDelete,0);
        CpMediaRef cpMediaRef=this.getOne(lambdaQueryWrapper,false);
        if(Objects.isNull(cpMediaRef)){
            return null;
        }
        //TODO 可校验mediaId是否失效
        return cpMediaRef;
    }

    @Override
    public void saveCpMediaRef(CpMediaRef cpMediaRef) {
        cpMediaRef.setCreateBy(AuthUserContext.get().getUsername());
        if(Objects.isNull(cpMediaRef.getCreateTime())){
            cpMediaRef.setCreateTime(new Date());
        }
        cpMediaRef.setUpdateTime(new Date());
        cpMediaRef.setIsDelete(0);
        this.save(cpMediaRef);

    }


    @Override
    public void refreshMediaId() {
        List<CpMediaRef> list =cpMediaRefMapper.listAfterThreeDayPicMediaIds();
        if(CollUtil.isEmpty(list)){
            log.info("refreshCpMediaRefIdTask 渠道引流图片自动同步微信续期 失败，无数据可刷");
            return;
        }
        for (CpMediaRef cpMediaRef : list) {
            if(cpMediaRef!= null && StrUtil.isNotEmpty(cpMediaRef.getMediaId()) && StrUtil.isNotEmpty(cpMediaRef.getUrl())){
                try {
                    File tempFile = ZipUtils.creatTempImageFile("tmpImg_",cpMediaRef.getUrl());
                    WxMediaUploadResult tempPath  = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getMediaService().upload(cpMediaRef.getType(),tempFile);
                    // 删除临时文件
                    if (tempFile.exists()) {
                        tempFile.delete();
                    }
                    log.info("refreshCpMediaRefIdTask 渠道引流图片自动同步微信续期 成功，id:{},新的MediaId:{}",cpMediaRef.getId(),tempPath.getMediaId());
                    this.updateById(CpMediaRef.builder()
                            .id(cpMediaRef.getId())
                            .mediaId(tempPath.getMediaId())
                            .updateTime(new Date())
                            .updateBy("定时任务刷新mediaId").build());
                }catch (Exception e){
                    log.info("refreshCpMediaRefIdTask 渠道引流图片自动同步微信续期 失败 {} {}",e,e.getMessage());
                }
            }
        }
    }
}
