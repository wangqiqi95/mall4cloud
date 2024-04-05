package com.mall4j.cloud.biz.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.biz.config.DomainConfig;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.manager.MaterialManager;
import com.mall4j.cloud.biz.model.cp.MaterialMsg;
import com.mall4j.cloud.biz.service.cp.MaterialMsgService;
import com.mall4j.cloud.biz.service.cp.MaterialService;
import com.mall4j.cloud.biz.wx.cp.utils.ZipUtils;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * @Description 同步素材中心素材状态，将超过有效期的素材状态更改为【禁用】
 * @author Peter_Tan
 * @date 2023-04-23 18:09
 **/
@Component
@Slf4j
public class CpMaterialTask {
    @Autowired
    private MaterialService materialService;
    @Autowired
    private MaterialManager materialManager;
    @Autowired
    private  MaterialMsgService materialMsgService;
    @Autowired
    private DomainConfig domainConfig;

    @XxlJob("syncCpMaterialStatus")
    public void syncCpMaterialStatus(){
        log.info("同步素材中心素材状态，将超过有效期的素材状态更改为【禁用】======================");
        materialService.syncCpMaterialStatus();
        log.info("同步分享员绑定任务结束======================");
    }


    /**
     * 根据浏览时长，给满足条件浏览记录的会员打上对应的标签
     */
    @XxlJob("setUserTagByMaterialBrowse")
    public void setUserTagByMaterialBrowse(){
        log.info("根据浏览时长，给满足条件浏览记录的会员打上对应的标签 开始======================");
        materialManager.setUserTagByMaterialBrowse();
        log.info("根据浏览时长，给满足条件浏览记录的会员打上对应的标签 结束======================");
    }

    //---------------------------素材中心图片视频自动同步微信续期----------------------------------------------------
    /**
     * 素材中心图片自动同步微信续期
     */
    @XxlJob("refreshCPMeterialMediaIdTask")
    public void refreshCPMeterialMediaIdTask()  {
        log.info("refreshCPMeterialMediaIdTask 开始执行");
        List<MaterialMsg> list =materialMsgService.waitRefreshMediaIdList();
        if(CollUtil.isNotEmpty(list)){
            for (MaterialMsg materialMsg : list) {
                if(materialMsg!= null && StrUtil.isNotEmpty(materialMsg.getMediaId()) && StrUtil.isNotEmpty(materialMsg.getUrl())){
                    String mediaType=materialMsg.getType();
                    try {
                        materialMsg.setUrl(domainConfig.parseUrl(materialMsg.getUrl()));
                        File tempFile=null;
                        if(mediaType.equals("file")){
                            String fileName=StrUtil.isNotEmpty(materialMsg.getFileName())?materialMsg.getFileName():materialMsg.getAppPic();
                            tempFile = ZipUtils.creatTempFile(fileName,materialMsg.getUrl(),false);
                        }else{
                            tempFile = ZipUtils.creatTempImageFile("tmpImg_",materialMsg.getUrl());
                        }
//                        File tempFile = ZipUtils.creatTempImageFile("tmpImg_",materialMsg.getUrl());
//                        String mediaType=materialMsg.getType();
//                        if(mediaType.endsWith("miniprogram") || mediaType.endsWith("h5")){
//                            mediaType="image";
//                        }
                        if(Objects.isNull(tempFile)){
                            continue;
                        }
                        WxMediaUploadResult tempPath  = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getMediaService().upload(mediaType,tempFile);
//                        WxMediaUploadResult tempPath  = WxCpConfiguration.getWxCpService(WxCpConfiguration.CP_CONNECT_AGENT_ID).getMediaService().upload(materialMsg.getType(),tempFile);
                        tempFile.delete();

                        log.info("refreshCPMeterialMediaIdTask 更新图片到企业微信成功，id:{},新的MediaId:{}",materialMsg.getId(),tempPath.getMediaId());
                        materialMsgService.refreshMediaId(materialMsg.getId(),tempPath.getMediaId());
                    }catch (Exception e){
                        log.info("refreshCPMeterialMediaIdTask 更新图片到企业微信失败，id:{} mediaType:{} getUrl:{} e:{} message:{}",materialMsg.getId(),mediaType,materialMsg.getUrl(),e,e.getMessage());
//                        throw new LuckException(e.getMessage());
                    }
                }
            }

        }
        log.info("refreshCPMeterialMediaIdTask 执行结束");

    }

}
