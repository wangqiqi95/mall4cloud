package com.mall4j.cloud.biz.model.channels;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 视频号4.0商品
 *
 * @author FrozenWatermelon
 * @date 2023-02-07 15:01:48
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ChannelsSku extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 内部spu_id
     */
    private Long spuId;

    /**
     * 绑定channels_spu_id
     */
    private Long channelsSpuId;

	/**
	 * 视频号spu_id
	 */
	private Long outSpuId;

    /**
     * 内部sku_id
     */
    private Long skuId;

    /**
     * 视频号sku_id
     */
    private Long outSkuId;

    /**
     * sku小图
     */
    private String thumbImg;

    /**
     * 发货方式，若为无需快递（仅对部分类目开放），则无需填写运费模版id。0:快递发货；1:无需快递；默认0
     */
    private Integer deliverMethod;

    /**
     * 商品详情信息
     */
    private String descInfo;

    /**
     * 商品封面，存储的是mediaID（mediaID获取后，三天内有效）
     */
    private String converImgUrl;

    /**
     * 价格
     */
    private Long price;

    /**
     * 小程序预警库存
     */
    private Integer warningStock;

    /**
     * 小程序预警库存
     */
    private Integer channelsWarningStock;

	private Integer stockNum;

    /**
     * 商家删除直播商品时间
     */
    private Date deleteTime;

    private String skuCode;
}
