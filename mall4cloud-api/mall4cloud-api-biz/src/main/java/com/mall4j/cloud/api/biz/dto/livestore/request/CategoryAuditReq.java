
package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CategoryAuditReq {

    @JsonProperty("category_info")
    private CategoryInfo categoryInfo;

    @JsonProperty("scene_group_list")
    private List<Integer> sceneGroupList;

    private List<String> license;

}
