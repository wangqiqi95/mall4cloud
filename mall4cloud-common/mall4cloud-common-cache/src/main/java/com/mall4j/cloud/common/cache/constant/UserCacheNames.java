package com.mall4j.cloud.common.cache.constant;

/**
 * @author FrozenWatermelon
 * @date 2020/11/23
 */
public interface UserCacheNames {

    /**
     *
     * 参考CacheKeyPrefix
     * UserCacheNames 与 key 之间的默认连接字符
     */
    String UNION = "::";

    /**
     * 前缀
     */
    String USER_PREFIX = "scrm_user:";

    /**
     * 用户默认地址缓存key
     */
    String USER_INFO = USER_PREFIX + "info:";

    /**
     * 用户默认地址缓存key
     */
    String USER_DEFAULT_ADDR = USER_PREFIX + "user_addr:user_id:";

    /**
     * 用户默认地址缓存key
     */
    String USER_DEFAULT_CONSIGNEE = USER_PREFIX + "user_consignee:user_id:";

    /**
     * 用户余额表
     */
    String USER_RECHARGE_LIST = USER_PREFIX + "user_recharge:list";
    /**
     * 用户余额表
     */
    String USER_RECHARGE_INFO = USER_PREFIX + "user_recharge:info:";

    /**
     * 会员等级列表缓存key
     */
    String LEVEL_LIST_KEY = USER_PREFIX + "level_list:";

    /**
     * 单个会员等级缓存key
     */
    String LEVEL_GET_KEY = USER_PREFIX + "level_get:";

    /**
     * 会员等级缓存key
     */
    String LEVEL_GET_LIST_KEY = USER_PREFIX + "level_get_list:";

    /**
     * 单个权益缓存
     */
    String RIGHT_BY_RIGHTS_ID_KEY = USER_PREFIX + "right_by_rights_id:";

    /**
     * 去重后的权益列表缓存
     */
    String RIGHTS_BY_LEVEL_TYPE = USER_PREFIX + "righs_by_level_type:";

    /**
     * 用户最近浏览的直播间id
     */
    String BORROW_LIVING_ROOMG  = USER_PREFIX + "info:living_room:";

    /**
     * 会员关联动态会员码的key
     */
    String USER_DYNAMIC_CODE_KEY = USER_PREFIX + "user_dynamic_code_key:";

    /**
     * 动态会员码缓存key
     */
    String DYNAMIC_CODE_KEY = USER_PREFIX + "dynamic_code_key:";

    String REC_ENABLED = USER_PREFIX + "rec_enabled:";

    //群发素材缓存key前缀
    String CP_EXTERN_CONTACT_ATTACHMENT_LIST = USER_PREFIX+"cp_external_attachment_list:";

}
