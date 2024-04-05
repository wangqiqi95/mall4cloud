package com.mall4j.cloud.api.biz.dto.channels.request;

import com.mall4j.cloud.api.biz.dto.channels.*;
import lombok.Data;

import java.util.List;

@Data
public class EcAddProductRequest {

    /**
     * 小店内部商品ID
     */
    private String product_id;

    /**
     *  商家自定义商品ID，小店后台不作任何唯一性约束，开发者自行保证，一旦添加成功后该字段无法修改，最多32字符
     */
    private String out_product_id;

    /**
     * 标题，最少3字符，最多60字符。文本内容规则请看注意事项
     */
    private String title;

    /**
     * 副标题，最多18字符
     */
    private String sub_title;

    /**
     * 主图，多张，列表，最少1张，最多9张
     */
    private List<String> head_imgs;

    /**
     * 发货方式，若为无需快递（仅对部分类目开放），则无需填写运费模版id。0:快递发货；1:无需快递；默认0
     */
    private Integer deliver_method;

    /**
     * 商品详情信息
     */
    private EcDescInfo desc_info;

    /**
     * 商品类目，大小恒等于3（一二三级类目）
     */
    private List<EcCat> cats;

    /**
     * 商品参数
     */
    private List<EcAttrInfo> attrs;

    /**
     * 商品编码
     */
    private String spu_code;

    /**
     * 品牌id，无品牌为“2100000000”
     */
    private String brand_id;

    /**
     * 商品资质图片（最多5张）
     */
    private List<String> qualifications;
    /**
     * 售后说明
     */
    private String aftersale_desc;

    /**
     * 运费模板ID（先通过获取运费模板接口merchant/getfreighttemplatelist拿到），若deliver_method=1，则不用填写
     */
    private EcExpressInfo express_info;

    /**
     * 长度最少为1，最大为500
     */
    private List<EcSkus> skus;

    /**
     * 更新后是否立即上架。1:是；0:否；默认0
     */
    private Integer listing;
}
