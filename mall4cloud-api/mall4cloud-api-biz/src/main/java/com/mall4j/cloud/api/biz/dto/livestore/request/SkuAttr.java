
package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SkuAttr {

    /**
     * 销售属性key（自定义）
     */
    @JsonProperty("attr_key")
    private String attrKey;
    /**
     * 销售属性value（自定义）
     */
    @JsonProperty("attr_value")
    private String attrValue;

}
