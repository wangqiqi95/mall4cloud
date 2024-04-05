package com.mall4j.cloud.biz.model.channels;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 视频号4.0商品
 *
 * @author FrozenWatermelon
 * @date 2023-02-07 15:01:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChannelsSpu extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("ID")
    private Long id;

    /**
     * 商品id
     */
    @ApiModelProperty("内部SpuId")
    private Long spuId;

    /**
     * 商品货号
     */
    @ApiModelProperty("spuCode")
    private String spuCode;

    /**
     * 视频号商品id
     */
    @ApiModelProperty("微信spuId")
    private Long outSpuId;

    /**
     * 品牌id，无品牌为“2100000000”
     */
    @ApiModelProperty("品牌ID")
    private Long brandId;

    /**
     * 标题，最少3字符，最多60字符。文本内容规则请看注意事项
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 副标题，最多18字符
     */
    @ApiModelProperty("副标题")
    private String subTitle;

    /**
     * 主图，多张，列表，最少1张，最多9张
     */
    @ApiModelProperty( "主图")
    private String headImgs;

    /**
     * 发货方式，若为无需快递（仅对部分类目开放），则无需填写运费模版id。0:快递发货；1:无需快递；默认0
     */
    @ApiModelProperty( "发货方式")
    private Integer deliverMethod;

    /**
     * 运费模板id
     */
    @ApiModelProperty( "运费模板Id")
    private String freightTemplate;

    /**
     * 类目ID
     */
    @ApiModelProperty( "类目Id")
    private String cats;

    /**
     * 审核备注
     */
    @ApiModelProperty( "审核备注")
    private String auditReason;

    /**
     *0:初始值 2上架中 5:上架 6:回收站 11:自主下架 13:违规下架/风控系统下架;14:保证金违规下架；15:品牌到期下架; 20:封禁下架。
     */
    @ApiModelProperty( "0:初始值 2上架中 5:上架 6:回收站 11:自主下架 13:违规下架/风控系统下架;14:保证金违规下架；15:品牌到期下架; 20:封禁下架。")
    private Integer status;

    /**
     * 0:初始值 1:编辑中 2:审核中 3:审核失败 4:审核成功 5:商品信息写入中
     */
    @ApiModelProperty( "0:初始值 1:编辑中 2:审核中 3:审核失败 4:审核成功 5:商品信息写入中")
    private Integer editStatus;

    /**
     * 是否在橱窗上架 0否1是
     */
    @ApiModelProperty( "是否在橱窗上架 0否1是")
    private Integer inWinodws;

    /**
     * 商家提交审核时间
     */
    @ApiModelProperty( "商家提交审核时间")
    private Date verifyTime;

    /**
     * 商品审核通过时间
     */
    @ApiModelProperty( "商品审核通过时间")
    private Date successTime;

    /**
     * 商品审核未通过时间
     */
    @ApiModelProperty( "商品审核未通过时间")
    private Date failTime;

    /**
     * 违规下架时间
     */
    @ApiModelProperty( "违规下架时间")
    private Date shelfTime;

    /**
     * 平台撤销时间
     */
    @ApiModelProperty( "平台撤销时间")
    private Date cancelTime;

    /**
     * 商家删除直播商品时间
     */
    @ApiModelProperty( "商家删除直播商品时间")
    private Date deleteTime;
    /**
     * 更新人
     */
    @ApiModelProperty( "更新人")
    private String updateBy;
}
