package com.mall4j.cloud.common.cache.constant;

/**
 * @author FrozenWatermelon
 * @date 2020/11/23
 */
public interface PlatformCacheNames {

    /**
     * 前缀
     */
    String PLATFORM_PREFIX = "mall4cloud_platform:";


    String PLATFORM_SIMPLE_INFO_KEY = PLATFORM_PREFIX + "simple_info";

    String PLATFORM_ORG_TOTAL = PLATFORM_PREFIX + "org_total";

    String PLATFORM_ORG_ALL = PLATFORM_PREFIX + "org_all";

    String PLATFORM_ORG_CHILD = PLATFORM_PREFIX + "child:";


    String PLATFORM_TENTACLE = PLATFORM_PREFIX + "Tentacle:";

    String PLATFORM_STORE = PLATFORM_PREFIX + "store:";

}
