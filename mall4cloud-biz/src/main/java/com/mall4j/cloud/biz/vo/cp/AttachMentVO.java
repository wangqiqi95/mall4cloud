package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.dto.cp.AttachMentBaseDTO;
import com.mall4j.cloud.biz.dto.cp.AttachmentExtDTO;
import com.mall4j.cloud.biz.dto.cp.WelcomeDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.chanjar.weixin.cp.bean.external.msg.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Administrator
 * @Description:
 * @Date: 2022-01-24 17:12
 * @Version: 1.0
 */
@NoArgsConstructor
@Data
public class AttachMentVO implements Serializable {
    public static final String IMAGE = "image";
    public static final String MINIPROGRAM = "miniprogram";
    public static final String LINK = "link";
    public static final String VIDEO = "video";
    public static final String MATERIAL = "material";
    public static final String FILE = "file";
    @ApiModelProperty("类型:图片为-image/小程序-miniprogram/链接-link/video-视频/素材-material/文件-file")
    private String  msgtype;

    public static AttachmentExtDTO  getAttachMent (AttachMentBaseDTO welcomeDTO){
        if(AttachMentVO.IMAGE.equals(welcomeDTO.getMsgType())){
            Attachment imageAttach =  new Attachment();
            imageAttach.setMsgType(welcomeDTO.getMsgType());
            Image image = new Image();
            image.setPicUrl(welcomeDTO.getPicUrl());
            image.setMediaId(welcomeDTO.getMediaId());
            imageAttach.setImage(image);
            return new AttachmentExtDTO(imageAttach,welcomeDTO.getLocalUrl());
        }
        if(AttachMentVO.MINIPROGRAM.equals(welcomeDTO.getMsgType())){
            Attachment attachment =  new Attachment();
            attachment.setMsgType(welcomeDTO.getMsgType());
            MiniProgram image = new MiniProgram();
            image.setAppid(welcomeDTO.getAppId());
            image.setPage(welcomeDTO.getPage());
            image.setTitle(welcomeDTO.getMiniProgramTitle());
            image.setPicMediaId(welcomeDTO.getMediaId());
            attachment.setMiniProgram(image);
            return new AttachmentExtDTO(attachment,welcomeDTO.getLocalUrl());
        }
        return null;
    }

    public static List<AttachmentExtDTO> getAttachMents (List<AttachMentBaseDTO> attachMents){
        List<AttachmentExtDTO> extDTOS=new ArrayList<>();
        for(AttachMentBaseDTO welcomeDTO:attachMents){
            if(AttachMentVO.IMAGE.equals(welcomeDTO.getMsgType())){
                Attachment imageAttach =  new Attachment();
                imageAttach.setMsgType(welcomeDTO.getMsgType());
                Image image = new Image();
                image.setPicUrl(welcomeDTO.getPicUrl());
                image.setMediaId(welcomeDTO.getMediaId());
                imageAttach.setImage(image);
                extDTOS.add(new AttachmentExtDTO(imageAttach,welcomeDTO.getLocalUrl()));
            }
            if(AttachMentVO.VIDEO.equals(welcomeDTO.getMsgType())){
                Attachment videoAttach =  new Attachment();
                videoAttach.setMsgType(welcomeDTO.getMsgType());
                Video video = new Video();
                video.setMediaId(welcomeDTO.getMediaId());
                videoAttach.setVideo(video);
                extDTOS.add(new AttachmentExtDTO(videoAttach,welcomeDTO.getLocalUrl()));
            }
            if(AttachMentVO.LINK.equals(welcomeDTO.getMsgType())){
                Attachment linkAttach =  new Attachment();
                linkAttach.setMsgType(welcomeDTO.getMsgType());
                Link link = new Link();
                link.setUrl(welcomeDTO.getUrl());
                link.setMediaId(welcomeDTO.getMediaId());
                link.setPicUrl(welcomeDTO.getPicUrl());
                link.setDesc(welcomeDTO.getDesc());
                link.setTitle(welcomeDTO.getMiniProgramTitle());
                linkAttach.setLink(link);
                extDTOS.add(new AttachmentExtDTO(linkAttach,welcomeDTO.getLocalUrl()));
            }
            if(AttachMentVO.MINIPROGRAM.equals(welcomeDTO.getMsgType())){
                Attachment attachment =  new Attachment();
                attachment.setMsgType(welcomeDTO.getMsgType());
                MiniProgram image = new MiniProgram();
                image.setAppid(welcomeDTO.getAppId());
                image.setPage(welcomeDTO.getPage());
                image.setTitle(welcomeDTO.getMiniProgramTitle());
                image.setPicMediaId(welcomeDTO.getMediaId());
                attachment.setMiniProgram(image);
                extDTOS.add(new AttachmentExtDTO(attachment,welcomeDTO.getLocalUrl()));
            }
            if(AttachMentVO.MATERIAL.equals(welcomeDTO.getMsgType())){
                Attachment attachment =  new Attachment();
                attachment.setMsgType(welcomeDTO.getMsgType());
                AttachmentExtDTO attachmentExtDTO=new AttachmentExtDTO(attachment,welcomeDTO.getLocalUrl());
                attachmentExtDTO.setMaterialId(welcomeDTO.getMaterialId());
                extDTOS.add(attachmentExtDTO);
            }
            if(AttachMentVO.FILE.equals(welcomeDTO.getMsgType())){
                Attachment attachment =  new Attachment();
                attachment.setMsgType(welcomeDTO.getMsgType());
                File file=new File();
                file.setMediaId(welcomeDTO.getMediaId());
                attachment.setFile(file);
                AttachmentExtDTO attachmentExtDTO=new AttachmentExtDTO(attachment,welcomeDTO.getLocalUrl());
                attachmentExtDTO.setFileName(welcomeDTO.getFileName());
                extDTOS.add(attachmentExtDTO);
            }
        }

        return extDTOS;
    }
}
