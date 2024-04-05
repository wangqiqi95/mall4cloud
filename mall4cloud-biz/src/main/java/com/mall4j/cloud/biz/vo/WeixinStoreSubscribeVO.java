package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信门店回复内容VO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 16:43:04
 */
@Data
public class WeixinStoreSubscribeVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("关注回复id")
    private String subscribeId;

    @ApiModelProperty("门店id")
    private String storeId;

	@ApiModelProperty("门店名称")
	private String storeName;

    @ApiModelProperty("公众号原始id")
    private String appId;

    @ApiModelProperty("创建人名称")
    private String createBy;

    @ApiModelProperty("修改人名称")
    private String updateBy;

    @ApiModelProperty("删除标识0-正常,1-已删除")
    private Integer delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    protected Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    protected Date updateTime;

	@ApiModelProperty("消息类型(text:文本消息,news:图文消息,voice:音频消息,video:视频消息,image:图片消息,wxma:小程序)")
	private String msgType;

    @ApiModelProperty("消息类型(文字描述)")
    private String msgTypeName;

    @ApiModelProperty("文字内容")
    private WeixinTextTemplateVO textTemplate;

    @ApiModelProperty("图片内容")
    private WeixinImgTemplateVO imgTemplate;

    @ApiModelProperty("小程序内容")
    private WeixinMaTemplateVO maTemplate;

    @ApiModelProperty("图文内容")
    private WeixinNewsTemplateContentVO newsTemplate;
}
