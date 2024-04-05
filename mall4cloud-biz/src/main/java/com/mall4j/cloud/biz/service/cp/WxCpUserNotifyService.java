package com.mall4j.cloud.biz.service.cp;

/**
 * @Author: Administrator
 * @Description: 企业微信成员操作服务
 * @Date: 2022-02-10 15:10
 * @Version: 1.0
 */
public interface WxCpUserNotifyService {
    /**
     * 创建成员回调
     * @param userId 成员的企业微信id
     * @param mobile 成员手机号码
     * @param status 成员的时间
     */
    void create(String userId, String mobile, String status);
    /**
     *更新成员回调
     * @param userId  成员的企业微信id
     * @param newUserId 成员的新的企业微信id
     * @param mobile 手机号码
     * @param status 状态
     */
    void update(String userId, String newUserId, String mobile, String status);

    /**
     * 删除成员回调
     * @param userId 成员id
     */
    void delete(String userId);


    /**
     * 客户继承失败
     * @param userId 接替员工id
     * @param externalUserId 客户外部联系人id
     * @param failReason 失败原因
     */
    void tranSferFail(String userId, String externalUserId, String failReason);

}
