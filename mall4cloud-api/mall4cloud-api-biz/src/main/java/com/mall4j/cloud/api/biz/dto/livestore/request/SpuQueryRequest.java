package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpuQueryRequest {


  @JsonProperty("out_product_id")
  private String outProductId;

  @JsonProperty("product_id")
  private String productId;

  /**
   * 默认0:获取线上数据, 1:获取草稿数据
   */
  @JsonProperty("need_edit_spu")
  private Integer needEditSpu;

}
