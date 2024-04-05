package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

import java.util.List;

/**
 * 查询商品详情
 */
@Data
public class EcProductDetail {
    //小店内部商品ID
    private String product_id;
    //商家自定义商品ID。如果添加时没录入，回包可能不包含该字段
    private String out_product_id;
    //标题
    private String title;
    //副标题。如果添加时没录入，回包可能不包含该字段
    private String sub_title;
    //主图,多张,列表,最多9张,每张不超过2MB
    private List<String> head_imgs;
    //商品详情图片(最多20张)。如果添加时没录入，回包可能不包含该字段
    private EcDescInfo desc_info;
    //运费模板
    private EcExpressInfo express_info;
    //售后说明
    private String aftersale_desc;
    //限购活动
    private EcLimitedInfo limited_info;
    /**
     * 商品线上状态
     * 枚举值	描述
     * 0	初始值
     * 5	上架
     * 6	回收站
     * 11	自主下架
     * 13	违规下架/风控系统下架
     */
    private Integer status;
    /**
     * 商品草稿状态
     * 0	初始值
     * 1	编辑中
     * 2	审核中
     * 3	审核失败
     * 4	审核成功
     * 5	商品信息写入中
     */
    private Integer edit_status;
    //商品 SKU 最小价格（单位：分）
    private Long min_price;
    //商家需要先申请可使用类目
    private List<EcCat> cats;
    //属性
    private List<EcAttrInfo> attrs;
    //商品编码
    private String spu_code;
    //品牌id，无品牌为“2100000000”
    private String brand_id;

    private List<EcSkus> skus;

}
