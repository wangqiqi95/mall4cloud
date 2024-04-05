package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 视频号4.0 橱窗商品
 * @date 2023/3/8
 */
@Data
public class EcWindowProduct {
    /**
     * 橱窗商品ID
     */
    private String product_id;

    /**
     * 商家侧外部商品ID
     */
    private String out_product_id;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品头图url
     */
    private String img_url;

    /**
     * 商品所属三级类目ID
     */
    private String third_category_id;

    /**
     * 商品状态 1	已上架到橱窗 2 未上架到橱窗 3 已在商品来源处删除
     */
    private Integer status;

    /**
     * 价格区间最大值(单位分) (市场价，原价）
     */
    private BigDecimal market_price;

    /**
     *价格区间最小值(单位分) (销售价)
     */
    private BigDecimal selling_price;

    /**
     * 剩余库存
     */
    private Integer stock;

    /**
     * 销量
     */
    private Integer sales;

    /**
     * appid
     */
    private String appid;

    /**
     * 商品详情半屏页、全屏页
     */
    private EcWindowPagePath page_path;

    /**
     * 商品所属电商平台ID
     */
    private Long platform_id;

    /**
     * 商品所属电商平台名
     */
    private String platform_name;

    /**
     * 是否在个人橱窗页隐藏
     */
    private Boolean is_hide_for_window;

    /**
     * 商品是否处于禁止售卖的状态
     */
    private Boolean banned;

    /**
     * 商品禁售详情
     */
    private EcWindowBannedDetails banned_details;

    /**
     * 分店
     */
    private EcWindowBranchInfo branch_info;

    /**
     * 抢购信息
     */
    private EcWindowLimitDiscountInfo limit_discount_info;
}
