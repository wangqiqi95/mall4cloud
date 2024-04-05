
package com.mall4j.cloud.biz.dto.live;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author LGH
 * @date 2020-08-04 16:57:17
 */
@Data
public class LiveProdStoreDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播商品库id
     */
    private Long liveProdStoreId;
    /**
     * 商家id
     */
    private Long shopId;
    /**
     * 商品id
     */
    private Long prodId;
    /**
     * 商品封面
     */
    private String coverPic;
    /**
     * 商品封面，存储的是mediaID（mediaID获取后，三天内有效）
     * 在对接微信平台的时候，在请求提交审核的时候，获取mediaID
     */
    private String converImgUrl;
    /**
     * 商品名称，提交给第三方接口的名称
     */
    private String name;
    /**
     * 商品原名称
     */

    private String prodName;
    /**
     * 商品图片
     */
    private String pic;
    /**
     * 价格类型，1：一口价（只需要传入price，price2不传） 2：价格区间（price字段为左边界，price2字段为右边界，price和price2必传） 3：显示折扣价（price字段为原价，price2字段为现价， price和price2必传）
     */
    private Integer priceType;
    /**
     * 商品价格(元)
     */
    private Double price;
    /**
     * 商品价格(元)
     */
    private Double price2;
    /**
     * 商品详情页的小程序路径，路径参数存在 url 的，该参数的值需要进行 encode 处理再填入
     */
    private String url;
    /**
     * 商品类型(0普通商品 1拼团 2秒杀 3积分)
     */
    private Integer prodType;

    /**
     * 商品本来的商品类型 商品类型(0普通商品 1拼团 2秒杀 3积分)
     */
    private Integer oriProdType;
    /**
     * 活动id(对应prod_type)
     */
    private Long activityId;
    /**
     * -1:删除时间  0:商家创建商品 1:商家提交审核 2:审核通过 3:审核不通过 4:微信直播服务平台，违规下架 5:平台撤销直播商品
     */
    private Integer status;
    /**
     * 微信商品id
     */
    private Long goodsId;
    /**
     * 版本，乐观锁
     */

    private Integer version;
    /**
     * 更新时间
     */
    private Date updateTime;

}
