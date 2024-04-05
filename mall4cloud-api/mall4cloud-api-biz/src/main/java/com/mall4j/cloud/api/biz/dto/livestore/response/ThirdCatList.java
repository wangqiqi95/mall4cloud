
package com.mall4j.cloud.api.biz.dto.livestore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ThirdCatList {

    /**
     * 类目ID
     */
    @JsonProperty("first_cat_id")
    private Long firstCatId;
    /**
     * 类目名称
     */
    @JsonProperty("first_cat_name")
    private String firstCatName;

    /**
     * 商品资质
     */
    @JsonProperty("product_qualification")
    private String productQualification;

    /**
     * 商品资质类型,0:不需要,1:必填,2:选填
     */
    @JsonProperty("product_qualification_type")
    private Integer productQualificationType;

    /**
     * 类目资质
     */
    private String qualification;
    /**
     * 类目资质类型,0:不需要,1:必填,2:选填
     */
    @JsonProperty("qualification_type")
    private Integer qualificationType;
    /**
     * 二级类目ID
     */
    @JsonProperty("second_cat_id")
    private Long secondCatId;
    /**
     * 二级类目名称
     */
    @JsonProperty("second_cat_name")
    private String secondCatName;
    /**
     * 一级类目ID
     */
    @JsonProperty("third_cat_id")
    private Long thirdCatId;
    /**
     * 一级类目名称
     */
    @JsonProperty("third_cat_name")
    private String thirdCatName;


}
