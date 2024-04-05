package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 微信二维码表VO
 *
 * @author gmq
 * @date 2022-01-28 22:25:17
 */
@Data
public class WeixinQrcodeVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("作业名称")
    private String actionInfo;

	@ApiModelProperty("触点公众号")
	private String appId;

	@ApiModelProperty("触点门店id")
	private String storeId;
    @ApiModelProperty("触点门店编号")
    private String storeCode;
	@ApiModelProperty("触点门店名称")
	private String storeName;

	private String staffName;

	@ApiModelProperty("二维码中间logo")
	private String logoUrl;

	@ApiModelProperty("有效期类型： 1永久 2临时")
	private Integer isExpire;

	@ApiModelProperty("状态： 0待审核 1审核通过")
	private Integer status;

    @ApiModelProperty("二维码类型：QR_SCENE：临时整型，QR_STR_SCENE：临时字符串，QR_LIMIT_SCENE：永久整型，QR_LIMIT_STR_SCENE：永久字符串")
    private String actionName;
//
    @ApiModelProperty("整型场景值ID")
    private Integer sceneId;

    @ApiModelProperty("字符串场景值ID")
    private String sceneStr;

    @ApiModelProperty("场景值来源")
    private Integer sceneSrc;
//
    @ApiModelProperty("二维码ticket")
    private String ticket;

    @ApiModelProperty("有效时间(秒)")
    private Integer expireSeconds;
//
    @ApiModelProperty("过期时间")
    private Date expireDate;
//
    @ApiModelProperty("二维码地址")
    private String qrcodeUrl;

    @NotNull
    @ApiModelProperty("触点类型：0员工/1自定义")
    private Integer contactType;

    @ApiModelProperty("自定义触点内容")
    private String contactStr;

//    @ApiModelProperty("触发类型：text文本/news图文")
//    private String msgType;
//
//    @ApiModelProperty("文本内容")
//    private String textContent;
//
//    @ApiModelProperty("图文选择类型（1：自定义，2：选择模板）")
//    private String actionNewsType;
//
//    @ApiModelProperty("图文标题")
//    private String newsTitle;
//
//    @ApiModelProperty("图文摘要")
//    private String newsDesc;
//
//    @ApiModelProperty("图文封面图")
//    private String newsImg;
//
//    @ApiModelProperty("图文跳转链接")
//    private String newsUrl;
//
//    @ApiModelProperty("图文选择模板ID")
//    private String newsTemplateid;
//
//    @ApiModelProperty("标签，逗号隔开")
//    private String tags;

}
