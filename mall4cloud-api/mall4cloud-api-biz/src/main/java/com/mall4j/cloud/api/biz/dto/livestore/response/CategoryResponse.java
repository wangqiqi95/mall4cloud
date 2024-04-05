
package com.mall4j.cloud.api.biz.dto.livestore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class CategoryResponse extends BaseResponse {

    /**
     * 类目列表
     */
    @JsonProperty("third_cat_list")
    private List<ThirdCatList> thirdCatList;

}
