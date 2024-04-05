package com.mall4j.cloud.product.event;

import com.mall4j.cloud.product.dto.SpuPageSearchDTO;
import lombok.Data;

/**
 * @Date 2022年4月27日, 0027 14:19
 * @Created by eury
 */
@Data
public class SoldSpuEvent {

    private SpuPageSearchDTO spuDTO;

    private Long downLoadHisId;
}
