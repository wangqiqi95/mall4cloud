package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpuListRequest {

  @JsonProperty("end_create_time")
  private String endCreateTime;
  @JsonProperty("end_update_time")
  private String endUpdateTime;

  /**
   * 默认0:获取线上数据, 1:获取草稿数据
   */
  @JsonProperty("need_edit_spu")
  private Integer needEditSpu;

  /**
   * 页号
   */
  private Integer page;

  @JsonProperty("start_create_time")
  private String startCreateTime;
  @JsonProperty("start_update_time")
  private String startUpdateTime;

  /**
   * 商品状态
   */
  private Integer status;

  /**
   * 页面大小
   */
  @JsonProperty("page_size")
  private Integer pageSize;
}
