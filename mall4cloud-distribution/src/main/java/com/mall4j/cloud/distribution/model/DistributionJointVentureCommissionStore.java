package com.mall4j.cloud.distribution.model;

import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 联营分佣门店
 *
 * @author Zhang Fan
 * @date 2022/8/4 11:56
 */
@Data
public class DistributionJointVentureCommissionStore extends BaseModel {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 联营分佣ID
     */
    private Long jointVentureId;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 门店编码
     */
    private String storeCode;
}
