package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.biz.constant.OriginType;
import com.mall4j.cloud.biz.dto.cp.AttachmentExtDTO;
import com.mall4j.cloud.common.model.BaseModel;
import com.mall4j.cloud.common.util.Json;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;

/**
 * 欢迎语附件列表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CpWelcomeAttachment extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;
    public CpWelcomeAttachment(){}
    public CpWelcomeAttachment(AttachmentExtDTO attachMent, OriginType originType, Long welId ){
        this.setData(Json.toJsonString(attachMent));
        this.setType(attachMent.getAttachment().getMsgType());
        this.setWelId(welId);
        this.setOrigin(originType.getCode());
        this.updateTime = new Date();
    }

    public CpWelcomeAttachment(AttachmentExtDTO attachMent, OriginType originType, Long welId, Long sourceId){
        this.setData(Json.toJsonString(attachMent));
        this.setType(attachMent.getAttachment().getMsgType());
        this.setWelId(welId);
        this.setOrigin(originType.getCode());
        this.updateTime = new Date();
        this.fileName=attachMent.getFileName();
        this.setSourceId(sourceId);
    }
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 欢迎语主表id
     */
    private Long welId;
//
    private Long baseWelId;

    private Long sourceId;

    /**
     * 类型:图片为-image/小程序-miniprogram/链接-url/video-视频/素材-material
     */
    private String type;

    /**
     * JSON数据
     */
    private String data;

    /**
     * 状态 1 有效 0 无效
     */
    private Integer status;

    /**
     * 删除标识  1 删除 0 未删除
     */
    private Integer flag;
    /**
     *   0 欢迎语 1 员工活吗
     */
    private Integer origin;

    /**
     * 创建人
     */
    private  String createBy;
    /**
     * 更新人
     */
    private  String updateBy;

    private Long materialId;

    /**
     * 分时段id
     */
    private Long timeStateId;

    /**
     * 文件原始名称
     */
    private String fileName;

}
