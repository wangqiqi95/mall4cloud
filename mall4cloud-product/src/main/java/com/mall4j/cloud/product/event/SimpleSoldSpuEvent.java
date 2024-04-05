package com.mall4j.cloud.product.event;

import com.mall4j.cloud.product.dto.SpuPageSearchDTO;
import lombok.Data;

/**
 * 全量商品导出
 */
@Data
public class SimpleSoldSpuEvent {

    private Long downLoadHisId;
}
