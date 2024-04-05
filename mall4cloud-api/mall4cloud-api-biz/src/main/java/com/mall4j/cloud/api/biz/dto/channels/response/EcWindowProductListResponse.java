package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.EcWindowProduct;
import com.mall4j.cloud.api.biz.dto.channels.EcWindowProducts;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 视频号4.0 橱窗商品集合response
 * @date 2023/3/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EcWindowProductListResponse extends EcBaseResponse{

    private List<EcWindowProducts> products;
}
