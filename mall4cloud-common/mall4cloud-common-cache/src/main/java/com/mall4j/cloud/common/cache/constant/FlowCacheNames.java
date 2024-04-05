package com.mall4j.cloud.common.cache.constant;

/**
 * @author lhd
 * @date 2020/12/28
 */
public interface FlowCacheNames {

    /**
     * 前缀
     */
    String FLOW_PREFIX = "mall4cloud_flow:";

    /**
     * 用户流量数据
     */
    String FLOW_FLOW_DATA = FLOW_PREFIX + "flow_data:";

    /**
     * 商品数据
     */
    String FLOW_PRODUCT_DATA = FLOW_PREFIX + "product_data:";

    /**
     * 访问留存分析
     */
    String FLOW_VISIT_RETAINED_KEY = FLOW_PREFIX + "visit_retained";

}
