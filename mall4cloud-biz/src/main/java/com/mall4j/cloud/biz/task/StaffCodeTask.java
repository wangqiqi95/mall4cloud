package com.mall4j.cloud.biz.task;

import com.mall4j.cloud.biz.config.DomainConfig;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.dto.cp.AttachmentExtDTO;
import com.mall4j.cloud.biz.manager.CpStaffCodeTimeRefeshManager;
import com.mall4j.cloud.biz.manager.CpTagGroupCodeManager;
import com.mall4j.cloud.biz.model.cp.CpWelcomeAttachment;
import com.mall4j.cloud.biz.service.cp.CpMediaRefService;
import com.mall4j.cloud.biz.service.cp.WelcomeAttachmentService;
import com.mall4j.cloud.biz.vo.cp.AttachMentVO;
import com.mall4j.cloud.biz.wx.cp.utils.ZipUtils;
import com.mall4j.cloud.common.util.Json;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 */
@RestController
@Component("StaffCodeTask")
@Slf4j
@RequiredArgsConstructor
public class StaffCodeTask {


    private final CpStaffCodeTimeRefeshManager cpStaffCodeTimeRefeshManager;
    private final WelcomeAttachmentService welcomeAttachmentService;
    private final CpMediaRefService cpMediaRefService;
    private final CpTagGroupCodeManager tagGroupCodeManager;
    private final DomainConfig domainConfig;

    /**
     * 渠道活码分时段接待人员刷新
     */
    @PostMapping("refeshTimeStaff")
    @XxlJob("refeshTimeStaff")
    public void refeshTimeStaff()  {
        Long startTime=System.currentTimeMillis();
        log.info("--start refeshTimeStaff");
        cpStaffCodeTimeRefeshManager.refeshTimeStaff();
        log.info("--end refeshTimeStaff 耗时：{}ms",System.currentTimeMillis() - startTime);

    }

    //---------------------------欢迎语及员工活码小程序图片自动同步微信续期----------------------------------------------------
    /**
     * 欢迎语及员工活码小程序图片自动同步微信续期
     */
    /**
     * 欢迎语及员工活码小程序图片自动同步微信续期
     */
    @XxlJob("refreshPicMediaIdTask")
    public void refreshPicMediaIdTask()  {
        List<CpWelcomeAttachment> list  =  welcomeAttachmentService.listAfterThreeDayPicMediaIds();
        if(!CollectionUtils.isEmpty(list)){
            for (CpWelcomeAttachment welcomeAttachment : list) {
                String mediaType=welcomeAttachment.getType();
                String url=null;
                try {
                    AttachmentExtDTO attachmentExtDTO = Json.parseObject(welcomeAttachment.getData(), AttachmentExtDTO.class);
                    String picPath = domainConfig.parseUrl(attachmentExtDTO.getLocalUrl());
                    url=picPath;
                    if(StringUtils.isNotEmpty(attachmentExtDTO.getLocalUrl())) {
                        File tempFile=null;
                        if(mediaType.equals("file")){
                            String fileName= welcomeAttachment.getFileName();
                            tempFile = ZipUtils.creatTempFile(fileName,picPath,false);
                        }else{
                            tempFile = ZipUtils.creatTempImageFile("tmpImg_",picPath);
                        }
//                        String picPath = domainConfig.parseUrl(attachmentExtDTO.getLocalUrl());
//                        File tempFile = ZipUtils.creatTempImageFile("tmpImg_", picPath);
                        if(Objects.isNull(tempFile)){
                            continue;
                        }
                        WxMediaUploadResult tempPath = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getMediaService().upload(mediaType, tempFile);
//                        WxMediaUploadResult tempPath = WxCpConfiguration.getWxCpService(WxCpConfiguration.getAgentId()).getMediaService().upload("image", tempFile);
                        if(AttachMentVO.IMAGE.equals(mediaType)){
                            attachmentExtDTO.getAttachment().getImage().setMediaId(tempPath.getMediaId());
                        }
                        if(AttachMentVO.VIDEO.equals(mediaType)){
                            attachmentExtDTO.getAttachment().getVideo().setMediaId(tempPath.getMediaId());
                        }
                        if(AttachMentVO.LINK.equals(mediaType)){
                            attachmentExtDTO.getAttachment().getLink().setMediaId(tempPath.getMediaId());
                        }
                        if(AttachMentVO.MINIPROGRAM.equals(mediaType)){
                            attachmentExtDTO.getAttachment().getMiniProgram().setPicMediaId(tempPath.getMediaId());
                        }
                        if(AttachMentVO.FILE.equals(mediaType)){
                            attachmentExtDTO.getAttachment().getFile().setMediaId(tempPath.getMediaId());
                        }
                        welcomeAttachment.setData(Json.toJsonString(attachmentExtDTO));
                        welcomeAttachment.setUpdateTime(new Date());
                        welcomeAttachmentService.updateWelcomeAttachment(welcomeAttachment);
                        log.info("渠道活码素材更新成功，id:{},新的MediaId:{}",welcomeAttachment.getId(),tempPath.getMediaId());
                    }
                }catch (Exception e){
                    log.error("",e);
                    log.info("渠道活码素材更新失败，id:{} mediaType:{} getUrl:{} e:{} message:{}",welcomeAttachment.getId(),mediaType,url,e,e.getMessage());
                }
            }
        }
    }
//    @XxlJob("refreshPicMediaIdTask")
//    public void refreshPicMediaIdTask()  {
//        List<CpWelcomeAttachment> list  =  welcomeAttachmentService.listAfterThreeDayPicMediaIds();
//        if(!CollectionUtils.isEmpty(list)){
//            for (CpWelcomeAttachment welcomeAttachment : list) {
//                try {
//                    AttachmentExtDTO attachmentExtDTO = Json.parseObject(welcomeAttachment.getData(), AttachmentExtDTO.class);
//                    if(StringUtils.isNotEmpty(attachmentExtDTO.getLocalUrl())) {
////                        String picPath = WxCpConfiguration.getPicUrl() + attachmentExtDTO.getLocalUrl();
//                        String picPath = domainConfig.parseUrl(attachmentExtDTO.getLocalUrl());
//
//                        File tempFile = ZipUtils.creatTempImageFile("tmpImg_", picPath);
////                        WxMediaUploadResult tempPath = WxCpConfiguration.getWxCpService(WxCpConfiguration.CP_CONNECT_AGENT_ID).getMediaService().upload("image", tempFile);
//                        WxMediaUploadResult tempPath = WxCpConfiguration.getWxCpService(WxCpConfiguration.getAgentId()).getMediaService().upload("image", tempFile);
//                        boolean bool = tempFile.delete();
//                        attachmentExtDTO.getAttachment().getMiniProgram().setPicMediaId(tempPath.getMediaId());
//                        welcomeAttachment.setData(Json.toJsonString(attachmentExtDTO));
//                        welcomeAttachment.setUpdateTime(new Date());
//                        welcomeAttachmentService.updateWelcomeAttachment(welcomeAttachment);
//                    }
//                }catch (Exception e){
//                    log.error("",e);
//                }
//            }
//        }
//    }





    //---------------------------渠道引流图片自动同步微信续期----------------------------------------------------
    /**
     * 渠道引流图片自动同步微信续期
     */
    @XxlJob("refreshCpMediaRefIdTask")
    public void refreshCpMediaRefIdTask()  {
        log.info("refreshCpMediaRefIdTask 开始执行");
        Long startTime=System.currentTimeMillis();
        cpMediaRefService.refreshMediaId();
        log.info("refreshCpMediaRefIdTask 执行结束，耗时:{}s",System.currentTimeMillis() - startTime);

    }

    //---------------------------渠道引流图片自动同步微信续期----------------------------------------------------
    /**
     * 标签建群-员工&客户执行状态更新
     */
    @XxlJob("refeshStaffAndExternalStatus")
    public void refeshStaffAndExternalStatus()  {
        log.info("refeshStaffAndExternalStatus 开始执行");
        Long startTime=System.currentTimeMillis();
        tagGroupCodeManager.refeshStaffAndExternalStatus();
        log.info("refeshStaffAndExternalStatus 执行结束，耗时:{}s",System.currentTimeMillis() - startTime);

    }


}

