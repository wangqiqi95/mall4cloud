
package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BrandAuditReq {

    @JsonProperty("brand_info")
    private BrandInfo brandInfo;

    /**
     * 商品使用场景,1:视频号，3:订单中心
     */
    @JsonProperty("scene_group_list")
    private List<Integer> sceneGroupList;

    /**
     * 营业执照或组织机构代码证，图片url/media_id
     */
    private List<String> license;


}
