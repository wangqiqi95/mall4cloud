package com.mall4j.cloud.platform.event;

import com.mall4j.cloud.platform.dto.StoreQueryParamDTO;
import lombok.Data;

/**
 * @Date 2022年4月27日, 0027 14:19
 * @Created by eury
 */
@Data
public class SoldStoreEvent {

    private StoreQueryParamDTO storeQueryParamDTO;

    private Long downLoadHisId;
}
