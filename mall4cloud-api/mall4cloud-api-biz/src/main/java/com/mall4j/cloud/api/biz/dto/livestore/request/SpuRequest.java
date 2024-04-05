
package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SpuRequest {

    /**
     * 品牌id
     */
    @JsonProperty("brand_id")
    private Long brandId;
    /**
     * 详情
     */
    @JsonProperty("desc_info")
    private DescInfo descInfo;
    /**
     * 主图,多张,列表
     */
    @JsonProperty("head_img")
    private List<String> headImg;
    /**
     * 预留字段，用于版本控制
     */
    @JsonProperty("info_version")
    private String infoVersion;
    /**
     * 商家自定义商品ID
     */
    @JsonProperty("out_product_id")
    private String outProductId;
    /**
     * 绑定的小程序商品路径
     */
    private String path;
    /**
     * 商品资质图片
     */
    @JsonProperty("qualification_pics")
    private List<String> qualificationPics;
    /**
     * sku数组
     */
    private List<Skus> skus;
    /**
     * 第三级类目ID
     */
    @JsonProperty("third_cat_id")
    private Long thirdCatId;
    /**
     * 标题
     */
    private String title;

    /**
     * 商品使用场景,1:视频号，3:订单中心
     */
    @JsonProperty("scene_group_list")
    private List<Integer> sceneGroupList;

}
