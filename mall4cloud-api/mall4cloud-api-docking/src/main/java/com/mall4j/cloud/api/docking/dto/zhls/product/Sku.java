package com.mall4j.cloud.api.docking.dto.zhls.product;

import com.mall4j.cloud.api.docking.dto.zhls.product.validation.AddSku;
import com.mall4j.cloud.api.docking.dto.zhls.product.validation.UpdateSku;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @Description
 * @Author axin
 * @Date 2023-03-10 15:37
 **/
@Data
public class Sku{
    @ApiModelProperty(value = "您为商品SKU分配的唯一ID。 一般而言，是您在商品库为该商品SKU分配的id。 字段长度最小 1 字节，长度最大 128 字节",required = true)
    @NotBlank(message = "skuID不能为空",groups = {AddSku.class, UpdateSku.class})
    private String external_sku_id;

    @ApiModelProperty(value = "商品的sku聚合id，供推荐、搜索场景使用。 字段长度最小 1 字节，长度最大 128 字节")
    private String poly_sku_id;

    @ApiModelProperty(value = "腾讯广告商品的skuid。 字段长度最小 1 字节，长度最大 128 字节")
    private String gdt_sku_id;

    @ApiModelProperty(value = "您为商品SPU分配的唯一ID。 色码款商品必填。 字段长度最小 1 字节，长度最大 128 字节")
    private String external_spu_id;

    @ApiModelProperty(value = "商品条形码，超市商品必填。字段长度最小 1 字节，长度最大 64 字节")
    private String sku_barcode;

    @ApiModelProperty(value = "商品可被搜索的编码，数组")
    private List<String> seo_id;

    @ApiModelProperty(value = "品牌名称。字段长度最小 1 字节，长度最大 64 字节")
    private String brand_name;

    @ApiModelProperty(value = "商品对客展示的图片及视频信息，最大长度 10",required = true)
    @NotNull(message = "商品图片信息不能为空",groups = {AddSku.class})
    private MediaInfo media_info;

    @ApiModelProperty(value = "商品的卖家信息，供平台类商家填写自己的卖家信息")
    private SellerInfo seller_info;

    @ApiModelProperty(value = "商品属性")
    private ProductProp product_props;

    @ApiModelProperty(value = "类目信息，数组",required = true)
    private List<CategoryInfo> category_info;

    @ApiModelProperty(value = "商品销售信息",required = true)
    private SalesInfo sales_info;

    @ApiModelProperty(value = "商品的标签信息 数组最大长度 10")
    private TagInfo tag_info;

    @ApiModelProperty(value = "商品描述信息",required = true)
    private DescInfo desc_info;

    @ApiModelProperty(value = "商品销售信息",required = true)
    private Price price;

    @ApiModelProperty(value = "配送信息")
    private List<DeliveryInfo> delivery_info;

    @ApiModelProperty(value = "商品目标页信息")
    private TargetUrl target_url;

    @ApiModelProperty(value = "店铺信息")
    private StoreInfo store_info;

    @ApiModelProperty(value = "优惠券信息")
    private List<CouponInfo> coupon_info;

    @ApiModelProperty(value = "活动信息，数组")
    private List<PromotionInfo> promotion_info;

    @ApiModelProperty(value = "商品售后信息")
    private ServiceInfo service_info;

    @ApiModelProperty(value = "评论信息")
    private CommentInfo comment_info;

    @ApiModelProperty(value = "CPS业务必填，第三方推广信息")
    private ThirdPromotionInfo third_promotion_info;

    @ApiModelProperty(value = "商品使用情况")
    private List<UsageInfo> usage_info;

    @ApiModelProperty(value = "商家标记商品已删除；0：未删除，1：已删除，为空默认为0")
    private Integer is_deleted;

    @Data
    public static class MediaInfo {

        @ApiModelProperty(value = "数组最大长度 10 商品主图地址；图片320*320以上，1张，正方形图片可正常打开 字段长度最小 1 字节，长度最大 2048 字节 ",required = true)
        @NotNull(message = "商品主图不能为空",groups = {AddSku.class})
        private Img primary_imgs;

        @ApiModelProperty(value = "商品轮播图 数组最大长度 10",required = true)
        @NotEmpty(message = "商品轮播图不能为空",groups = {AddSku.class})
        private List<Img> imgs;

        @ApiModelProperty(value = "详情图片 数组最大长度 50")
        private List<DetailImg> detail_imgs;

        @ApiModelProperty(value = "商品视频信息，数组最大长度 10")
        private List<Video> videos;

        @Data
        public static class Img {
            private String img_url;
        }

        @Data
        private static class DetailImg extends Img {
            @ApiModelProperty(value = "商品富文本地址，最大长度50，url可正常打开，字段长度最小 1 字节，长度最大 2048 字节")
            private String rich_text_url;
        }

        @Data
        private static class Video extends Img {
            @ApiModelProperty(value = "视频地址；字段长度最小 1 字节，长度最大 2048 字节")
            private String video_url;
        }
    }

    @Data
    @ApiModel(value = "商品的卖家信息，供平台类商家填写自己的卖家信息")
    private static class SellerInfo {
        @ApiModelProperty(value = "商品卖家 id，由平台类商家自己生成")
        private String seller_id;

        @ApiModelProperty(value = "平台类商家的子卖家中文名称")
        private String seller_name_chinese;

        @ApiModelProperty(value = "平台类商家的子卖家英文名称")
        private String seller_name_english;
    }

    @Data
    @ApiModel(value = "商品属性")
    private static class ProductProp {
        @ApiModelProperty(value = "商品颜色属性")
        private Color color;

        @ApiModelProperty(value = "商品属性信息，数组")
        private PropInfo prop_info;

        @ApiModelProperty(value = "视频类商品属性")
        private VideoProps video_props;

        @Data
        private static class Color {
            @ApiModelProperty(value = "颜色RGB编码 字段长度最小 1 字节，长度最大 64 字节")
            private String color_rgb;

            @ApiModelProperty(value = "颜色名称，如黄色 字段长度最小 1 字节，长度最大 64 字节")
            private String color_name;
        }

        @Data
        private static class PropInfo {
            @ApiModelProperty(value = "属性ID，字段长度最小 1 字节，长度最大 64 字节")
            private Integer prop_id;

            @ApiModelProperty(value = "属性名称，字段长度最小 1 字节，长度最大 64 字节")
            private String prop_name;

            @ApiModelProperty(value = "属性值ID，字段长度最小 1 字节，长度最大 64 字节")
            private Integer value_id;

            @ApiModelProperty(value = "属性值名称，字段长度最小 1 字节，长度最大 64 字节")
            private String value_name;
        }

        @Data
        private static class VideoProps {
            @ApiModelProperty(value = "视频的分类，如爱情，字段长度最小 1 字节，长度最大 64 字节")
            private String genre;

            @ApiModelProperty(value = "视频时长，单位秒，字段长度最小 1 字节，长度最大 64 字")
            private Integer duration;

            @ApiModelProperty(value = "导演姓名，字段长度最小 1 字节，长度最大 64 字")
            private String director;

            @ApiModelProperty(value = "主演姓名，字段长度最小 1 字节，长度最大 64 字节")
            private String star;

            @ApiModelProperty(value = "上线时间，字段长度最小 1 字节，长度最大 64 字节")
            private String release_date;

            @ApiModelProperty(value = "评分，字段长度最小 1 字节，长度最大 64 字节")
            private Float rating;
        }
    }

    @Data
    public static class CategoryInfo {
        @ApiModelProperty(value = "类目类型，1：前台类目；2：后台类目",required = true)
        @NotNull(message = "类目类型不能为空",groups = {AddSku.class})
        private Integer category_type;

        @ApiModelProperty(value = "一级类目id",required = true)
        @NotBlank(message = "一级类目id不能为空",groups = {AddSku.class})
        private String category_level_1_id;

        @ApiModelProperty(value = "一级类目名称",required = true)
        @NotBlank(message = "一级类目id不能名称",groups = {AddSku.class})
        private String category_level_1_name;

        @ApiModelProperty(value = "二级类目id")
        private String category_level_2_id;

        @ApiModelProperty(value = "二级类目名称")
        private String category_level_2_name;

        @ApiModelProperty(value = "三级类目id")
        private String category_level_3_id;

        @ApiModelProperty(value = "三级类目名称")
        private String category_level_3_name;

        @ApiModelProperty(value = "四级类目id")
        private String category_level_4_id;

        @ApiModelProperty(value = "四级类目名称")
        private String category_level_4_name;
    }

    @Data
    public static class SalesInfo {

        @ApiModelProperty(value = "商品总库存量，当product_type=3（小样）时必填")
        private Integer stock_total;

        @ApiModelProperty(value = "上架状态；true：上架；false：下架",required = true)
        @NotNull(message = "上架状态不能为空",groups = {AddSku.class})
        private Boolean is_available;

        @ApiModelProperty(value = "商品的销售状态 1：库存＞0；2：库存≤0（常见为售罄/暂无库存商品在页面底部展示，可标记为状态2）",required = true)
        @NotNull(message = "商品的销售状态不能为空",groups = {AddSku.class})
        private Integer sku_stock_status;

        @ApiModelProperty(value = "展示销量")
        private Integer sales_num;

        @ApiModelProperty(value = "商品类型，1：普通商品；2：赠品；3：小样；4：套装商品",required = true)
        @NotNull(message = "商品类型不能为空",groups = {AddSku.class})
        private Integer product_type;
    }


    @Data
    private static class TagInfo {
        private CustomTag custom_tag;

        @Data
        private static class CustomTag {
            @ApiModelProperty(value = "标签名称，长度最大 2048 字节")
            private String  tag_name;

            @ApiModelProperty(value = "标签值，数组，长度最大 2048 字节")
            private String  tag_value;
        }
    }

    @Data
    public static class DescInfo {
        @ApiModelProperty(value = "商品sku名称，字段长度最小 1 字节，长度最大 100 字节",required = true)
        @NotBlank(message = "商品sku名称不能为空",groups = {AddSku.class})
        private String product_name_chinese;

        @ApiModelProperty(value = "商品英文名 字段长度最小 1 字节，长度最大 1000 字节")
        private String product_name_english;

        @ApiModelProperty(value = "商品spu 名称，若无 spu，传输内容请与 product_name_chinese 保持一致",required = true)
        private String spu_name_chinese;

        @ApiModelProperty(value = "商品描述，字段长度最小 1 字节，长度最大 1000 字节")
        private String product_desc;

        @ApiModelProperty(value = "商品SEO词，数组，搜索中的扩展词包")
        private List<String> seo_desc;
    }


    @Data
    public static class Price {

        @ApiModelProperty(value = "商品现价，单位元，保留 2 位小数",required = true)
        @NotNull(message = "商品现价不能为空",groups = {AddSku.class})
        private Float current_price;

        @ApiModelProperty(value = "商品原价，单位元，保留 2 位小数",required = true)
        @NotNull(message = "商品原价不能为空",groups = {AddSku.class})
        private Float sku_price;

        @ApiModelProperty(value = "券后价，券后价=商品现价-券面额，单位元，保留 2 位小数")
        private Float after_coupon_price;

        @ApiModelProperty(value = "惠买专项券后价，券后价=商品现价-券面额，单位元，保留 2 位小数")
        private Float after_coupon_price_hm;

        @ApiModelProperty(value = "商品价格是否为外币汇率折算后价格，true:是；false：否，默认为false")
        private Float currency_translation;

        @ApiModelProperty(value = "关联的正品的价格，当该商品为小样时需填写，单位元，保留 2 位小数")
        private List<NormalProductInfo> normal_product_info;

        @Data
        private static class NormalProductInfo {
            @ApiModelProperty(value = "关联的商品id字段长度最小 1 字节，长度最大 128 字节")
            private String sku_id;

            @ApiModelProperty(value = "正品价格")
            private Float normal_product_price;
        }
    }

    @Data
    private static class DeliveryInfo {
        @ApiModelProperty(value = "运费金额，单位元，保留 2 位小数")
        private Float freight_amount;

        @ApiModelProperty(value = "地址信息")
        private List<Location> location;

        @Data
        private static class Location {

            @ApiModelProperty(value = "国家编码，CN 字段长度最小 1 字节，长度最大 64 字节")
            private String country_code;

            @ApiModelProperty(value = "国家名称，中国 字段长度最小 1 字节，长度最大 64 字节")
            private String country_name;

            @ApiModelProperty(value = "省份编码，使用《民政部行政区划代码》，440000")
            private Integer province_code;

            @ApiModelProperty(value = "省份名称，XX省 字段长度最小 1 字节，长度最大 64 字节")
            private String province_name;

            @ApiModelProperty(value = "城市编码，使用《民政部行政区划代码》")
            private Integer city_code;

            @ApiModelProperty(value = "城市名称，XX市 字段长度最小 1 字节，长度最大 64 字节")
            private String city_name;

            @ApiModelProperty(value = "区/县名称编码，使用《民政部行政区划代码》")
            private Integer district_code;

            @ApiModelProperty(value = "区/县名称，字段长度最小 1 字节，长度最大 64 字节")
            private String district_name;
        }
    }

    @Data
    private static class TargetUrl {
        @ApiModelProperty(value = "微信小程序落地页 url，当落地页为微信小程序时必填 字段长度最小 1 字节，长度最大 100 字节")
        private String url_miniprogram;

        @ApiModelProperty(value = "微信小程序 appid，当落地页为微信小程序时必填 字段长度最小 1 字节，长度最大 100 字节")
        private String miniprogram_appid;

        @ApiModelProperty(value = "小程序原始ID，登录小程序管理后台-设置-基本设置-帐号信息中，gh_xx，当落地页为微信小程序时必填字段长度最小 1 字节，长度最大 100 字节")
        private String miniprogram_username;

        @ApiModelProperty(value = "qq小程序落地页 url，当落地页为QQ小程序时必填 字段长度最小 1 字节，长度最大 100 字节")
        private String url_miniprogram_qq;

        @ApiModelProperty(value = "qq小程序 appid，当落地页为QQ小程序时必填 字段长度最小 1 字节，长度最大 100 字节")
        private String miniprogram_appid_qq;

        @ApiModelProperty(value = "h5落地页url 字段长度最小 1 字节，长度最大 100 字节")
        private String url_h5;

        @ApiModelProperty(value = "h5落地页url 字段长度最小 1 字节，长度最大 1000 字节")
        private String url_h5_2;

        @ApiModelProperty(value = "h5落地页url 字段长度最小 1 字节，长度最大 1000 字节")
        private String url_h5_3;
    }

    @Data
    private static class StoreInfo {
        @ApiModelProperty(value = "C 店铺id，填写`store_name`时必填，字段长度最小 1 字节，长度最大 100 字节")
        private String external_store_id;

        @ApiModelProperty(value = "店铺名称，填写`external_store_id`时必填，字段长度最小 1 字节，长度最大 100 字节")
        private String store_name;

        @ApiModelProperty(value = "按满分5分换算的店铺评分，保留 2 位小数")
        private Float store_grade;

        @ApiModelProperty(value = "店铺图片地址，1张，字段长度最小 1 字节，长度最大 2048 字节")
        private String logo;

        @ApiModelProperty(value = "店铺 h5 首页url，字段长度最小 1 字节，长度最大 2048 字节")
        private String url_store_h5;

        @ApiModelProperty(value = "店铺微信小程序首页url，字段长度最小 1 字节，长度最大 2048 字节")
        private String url_store_miniprogram;
    }

    @Data
    private static class CouponInfo {
        @ApiModelProperty(value = "券批次id 字段长度最小 1 字节，长度最大 100 字节")
        private String coupon_id;

        @ApiModelProperty(value = "券类型，1：公开券；2：CPS优惠券；3：渠道专享券")
        private Integer coupon_type;

        @ApiModelProperty(value = "券面额，单位元，保留 2 位小数，字段长度最小 1 字节，长度最大 100 字节")
        private Float amount_coupon;

        @ApiModelProperty(value = "券地址")
        private String url_list;

        @ApiModelProperty(value = "使用卡券的最低消费金额，单位元，保留 2 位小数")
        private Float amount_minimum;

        @ApiModelProperty(value = "展示开始时间/券领取开始时间，unix毫秒级时间戳，为空表示永久")
        private String show_start_time;

        @ApiModelProperty(value = "展示结束时间/券领取结束时间，unix毫秒级时间戳，为空表示永久")
        private String show_end_time;

        @ApiModelProperty(value = "券有效使用开始时间，unix毫秒级时间戳，为空表示永久")
        private String start_time;

        @ApiModelProperty(value = "券有效使用结束时间，unix毫秒级时间戳，为空表示永久")
        private String end_time;

        @ApiModelProperty(value = "每个用户可领取优惠券数量上限")
        private Integer available_num_per_person;

        @ApiModelProperty(value = "实际总数量")
        private Integer total_num;

        @ApiModelProperty(value = "优惠券剩余张数")
        private Integer store_num;

        @ApiModelProperty(value = "优惠券状态，1：过期；0：未过期")
        private Integer status;
    }

    @Data
    private static class PromotionInfo {
        @ApiModelProperty(value = "活动id 字段长度最小 1 字节，长度最大 100 字节")
        private String promotion_id;

        @ApiModelProperty(value = "活动名称 字段长度最小 1 字节，长度最大 2048 字节")
        private String promotion_name;

        @ApiModelProperty("活动类型，1：满减；2：秒杀；3：超市百货；4：品牌促销；5：新人专享；6：大促活动；7：精选推荐；8：每日热榜；9：高佣专区；10；历史低价；11：30天热榜")
        private Integer promotion_type;

        @ApiModelProperty(value = "活动地址")
        private String url_list;

        @ApiModelProperty(value = "活动生效开始时间，unix毫秒级时间戳，为空表示永久")
        private String start_time;

        @ApiModelProperty(value = "活动生效结束时间/，unix毫秒级时间戳，为空表示永久")
        private String end_time;

        @ApiModelProperty(value = "活动展示开始时间，unix毫秒级时间戳，为空表示永久")
        private String show_start_time;

        @ApiModelProperty(value = "活动展示结束时间，unix毫秒级时间戳，为空表示永久")
        private String show_end_time;

        @ApiModelProperty(value = "是否最优活动，1：是；0：否")
        private Integer is_best;
    }

    @Data
    private static class ServiceInfo {
        @ApiModelProperty(value = "售后服务类型：1：无售后服务；2：支持退换货")
        private Integer service_type;
    }

    @Data
    private static class CommentInfo {
        @ApiModelProperty(value = "商品评论数")
        private Integer comment_num;

        @ApiModelProperty(value = "商品好评率，2位小数，不带百分号，如 99.99% 填 99.99")
        private Float positive_comment_rating;
    }

    @Data
    private static class ThirdPromotionInfo {
        @ApiModelProperty(value = "佣金比例，2位小数，不带百分号，如 10% 填10.00")
        private Float commission_rate;

        @ApiModelProperty(value = "推广状态；1：推广中；0：停止推广")
        private Integer promote_status;

        @ApiModelProperty(value = "佣金 = 商品现价*佣金比例")
        private Float commission_amount;

        @ApiModelProperty(value = "券后佣金 =（商品现价-券面额）*佣金比例")
        private Float commission_amount_after_coupon;

        @ApiModelProperty(value = "推广计划开始时间,unix毫秒级时间戳，为空表示永久")
        private String start_time;

        @ApiModelProperty(value = "推广计划结束时间,unix毫秒级时间戳，为空表示永久")
        private String end_time;

        @ApiModelProperty(value = "专属商品信息")
        private ExclusiveInfo exclusive_info;

        @Data
        private static class ExclusiveInfo {
            @ApiModelProperty(value = "专属商品标识，1：专属商品；0：普通商品")
            private Integer exclusive_status;

            @ApiModelProperty(value = "可推广商品的达人ID，以英文逗号分割，如54321836,85623992")
            private String kol_id;
        }
    }

    @Data
    private static class UsageInfo {
        @ApiModelProperty(value = "商品的特殊应用场景。为空：商品将应用到您在有数数据管理后台绑定的全部场景；11：有数；16：直跳；19：广告CPS；20：搜一搜品牌专区；21：云选联盟；22：商品广告；例如：商家开通了场景16、19，如果不填，则所有商品应用到场景16、19；如果填了16，则改商品仅应用到场景16")
        private Integer inner_appid;
    }
}
