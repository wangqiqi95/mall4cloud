
package com.mall4j.cloud.api.biz.dto.livestore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class Spu {

    @JsonProperty("audit_info")
    private AuditInfo auditInfo;
    @JsonProperty("brand_id")
    private Long brandId;
    @JsonProperty("create_time")
    private String createTime;
    @JsonProperty("desc_info")
    private DescInfo descInfo;
    @JsonProperty("head_img")
    private List<String> headImg;
    @JsonProperty("info_version")
    private String infoVersion;
    @JsonProperty("out_product_id")
    private String outProductId;
    private String path;
    @JsonProperty("third_cat_id")
    private Long thirdCatId;
    private String title;
    @JsonProperty("update_time")
    private String updateTime;

    @ApiModelProperty("商品草稿状态 0:初始值 1:编辑中 2:审核中 3:审核失败 4:审核成功")
    @JsonProperty("edit_status")
    private String editStatus;

    @ApiModelProperty("商品状态 0-初始值 5-上架 11-自主下架 13-违规下架/风控系统下架")
    private int status;

    private List<Skus> skus;

}
