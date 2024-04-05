package com.mall4j.cloud.biz.service.cp;

/**
 * @Author: Administrator
 * @Description: 企业群操作
 * @Date: 2022-02-10 15:10
 * @Version: 1.0
 */
public interface WxCpChatNotifyService {
    /**
     * 创建群回调
     * @param chatId 群id
     */
    void create(String chatId);
    /**
     * 删除群回调
     * @param userId 成员id
     */
    void delete(String userId);

    /**
     * 客群增加客户
     * @param chatId
     */
    void addCust(String chatId);
    /**
     * 客群删除客户
     * @param chatId
     */
    void deleteCust(String chatId);
    /**
     * 客群修改群名称
     * @param chatId
     */
    void changeName(String chatId);

    /**
     * 客群修改群主
     * @param chatId
     */
    void changeOwner(String chatId);
}
