package com.mall4j.cloud.api.group.feign.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserFollowWechatActivityDTO implements Serializable {
    /**
     * 用户标识
     */
    private Long userId;
    /**
     * 门店标识
     */
    private Long shopId;
    /**
     * 内部数据中的WechatId
     * <p>
     * 不建议再使用此Id
     */
    @Deprecated
    private Long wechatId;
    /**
     * 公众号AppId
     */
    private String appId;
    /**
     * 用户UnionId
     */
    private String unionId;
}
