package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;

import com.mall4j.cloud.biz.wx.cp.constant.OriginEumn;
import com.mall4j.cloud.biz.dto.cp.MaterialMsgDTO;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 素材消息表
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialMsg extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;
    public MaterialMsg(){}
    public MaterialMsg(MaterialMsgDTO dto, OriginEumn originEumn){
        this.appId = dto.getAppId();
        this.appPage = dto.getAppPage();
        this.appPic = dto.getAppPic();
        this.appTitle = dto.getAppTitle();
        this.content = dto.getContent();
        this.type =dto.getType();
        this.url = dto.getUrl();
        this.origin = originEumn.getCode();
        this.picUrl = dto.getPicUrl();
    }
    /**
     * 
     */
    private Long id;

    /**
     * 素材id
     */
    private Long matId;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息图片url
     */
    private String url;

    /**
     * 素材id
     */
    private String mediaId;

    /**
     * 消息图片url
     */
    private String appId;

    /**
     * 小程序APPid
     */
    private String appTitle;

    /**
     * 小程序标题
     */
    private String appPage;

    /**
     * 小程序封面url
     */
    private String appPic;

    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 来源
     */
    private Integer origin;

    @ApiModelProperty("文章url")
    private String articleUrl;
    @ApiModelProperty("文章描述")
    private String articleDesc;

    /**
     * 素材链接
     */
    private String picUrl;

    private String fileName;

}
