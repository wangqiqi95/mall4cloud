package com.mall4j.cloud.auth.service;

import com.mall4j.cloud.auth.model.AuthSocial;
import org.apache.ibatis.annotations.Param;

/**
 * @author FrozenWatermelon
 * @date 2020/7/2
 */
public interface AuthSocialService {

    /**
     * 获取社交账号信息
     * @param bizUserId 第三方社交账号的id
     * @param socialType 社交账号类型 1 小程序 2 公众号
     * @return
     */
    AuthSocial getByBizUserId(String bizUserId, Integer socialType);

    AuthSocial getByBizUnionIdAndType(String bizUserId,  Integer socialType);

    /**
     * 保存或更新社交账户
     * @param authSocial authSocial
     */
    void saveOrUpdate(AuthSocial authSocial);

    /**
     * 获取根据尝试社交登录时，保存的临时的uid获取社交
     *
     * @param tempUid tempUid
     * @return 用户社交账号信息
     */
    AuthSocial getByTempUid(@Param("tempUid") String tempUid);

    /**
     * 获取在微信公众号/小程序 绑定的账号数量
     * @param uid uid
     * @param socialType 社交帐号类型
     * @return 绑定账号的数量
     */
    int countByUidAndSocialType(Long uid, Integer socialType);
}
