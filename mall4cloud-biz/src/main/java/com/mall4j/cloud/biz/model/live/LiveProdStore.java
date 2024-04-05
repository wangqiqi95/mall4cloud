
package com.mall4j.cloud.biz.model.live;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author LGH
 * @date 2020-08-04 16:57:17
 */
@Data
@ApiModel("直播商品库")
public class LiveProdStore implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播商品库id
     */
    @ApiModelProperty("直播商品库id")
    @TableId(type = IdType.AUTO)
    private Long liveProdStoreId;
    /**
     * 商家id
     */
    @ApiModelProperty("商家id")
    private Long shopId;
    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long prodId;
    /**
     * 商品封面
     */
    @ApiModelProperty("商品封面")
    private String coverPic;
    /**
     * 商品封面，存储的是mediaID（mediaID获取后，三天内有效）
     * 在对接微信平台的时候，在请求提交审核的时候，获取mediaID
     */
    @ApiModelProperty("商品封面，存储的是mediaID（mediaID获取后，三天内有效）")
    private String converImgUrl;
    /**
     * 商品名称，提交给第三方接口的名称
     */
    @ApiModelProperty("商品名称，提交给第三方接口的名称")
    private String name;
    @ApiModelProperty("商品图片")
    @TableField(exist = false)
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;
    /**
     * 价格类型，1：一口价（只需要传入price，price2不传） 2：价格区间（price字段为左边界，price2字段为右边界，price和price2必传） 3：显示折扣价（price字段为原价，price2字段为现价， price和price2必传）
     */
    @ApiModelProperty("价格类型，1：一口价（只需要传入price，price2不传） 2：价格区间（price字段为左边界，price2字段为右边界，price和price2必传） 3：显示折扣价（price字段为原价，price2字段为现价， price和price2必传）")
    private Integer priceType;
    /**
     * 商品价格(元)
     */
    @ApiModelProperty("商品价格(元)")
    private Double price;
    /**
     * 商品价格(元)
     */
     @ApiModelProperty("商品价格(元)")
    private Double price2;
    /**
     * 主播微信号
     */
    @ApiModelProperty("主播微信号")
    private String anchorWechat;
    /**
     * 商品详情页的小程序路径，路径参数存在 url 的，该参数的值需要进行 encode 处理再填入
     */
    @ApiModelProperty("商品详情页的小程序路径，路径参数存在 url 的，该参数的值需要进行 encode 处理再填入")
    private String url;
    /**
     * 商品类型(0普通商品 1拼团 2秒杀 3积分)
     */
    @ApiModelProperty("商品类型(0普通商品 1拼团 2秒杀 3积分)")
    private Integer prodType;

    /**
     * 商品本来的商品类型 商品类型(0普通商品 1拼团 2秒杀 3积分)
     */
    @ApiModelProperty("商品本来的商品类型 商品类型(0普通商品 1拼团 2秒杀 3积分)")
    @TableField(exist = false)
    private Integer oriProdType;
    /**
     * 活动id(对应prod_type)
     */
    @ApiModelProperty("活动id(对应prod_type)")
    private Long activityId;
    /**
     * -1:删除时间  0:商家创建商品 1:商家提交审核 2:审核通过 3:审核不通过 4:微信直播服务平台，违规下架 5:平台撤销直播商品
     */
    @ApiModelProperty("-1:删除时间  0:商家创建商品 1:商家提交审核 2:审核通过 3:审核不通过 4:微信直播服务平台，违规下架 5:平台撤销直播商品")
    private Integer status;
    /**
     * 微信商品id
     */
    @ApiModelProperty(value = "微信商品id" ,required = true)
    private Integer goodsId;

    /**
     * 审核id
     */
    @ApiModelProperty(value = "审核id")
    private Integer auditId;

    /**
     * 商家添加商品时间
     */
    @ApiModelProperty("商家添加商品时间")
    private Date createTime;
    /**
     * 商家提交审核时间
     */
    @ApiModelProperty("商家提交审核时间")
    private Date verifyTime;
    /**
     * 商品审核通过时间
     */
    @ApiModelProperty("商品审核通过时间")
    private Date successTime;
    /**
     * 商品审核未通过时间
     */
    @ApiModelProperty("商品审核未通过时间")
    private Date failTime;
    /**
     * 违规下架时间
     */
    @ApiModelProperty("违规下架时间")
    private Date shelfTime;
    /**
     * 平台撤销时间
     */
    @ApiModelProperty("平台撤销时间")
    private Date cancelTime;
    /**
     * 商家删除直播商品时间
     */
    @ApiModelProperty("商家删除直播商品时间")
    private Date deleteTime;
    /**
     * 版本，乐观锁
     */
    @ApiModelProperty("版本，乐观锁")
    private Integer version;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

}
