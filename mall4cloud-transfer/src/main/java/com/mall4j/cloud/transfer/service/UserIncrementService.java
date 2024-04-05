package com.mall4j.cloud.transfer.service;

import org.springframework.stereotype.Service;

/**
 * 用户增量处理sql
 *
 * @luzhengxiang
 * @create 2022-05-07 9:58 AM
 **/
@Service
public class UserIncrementService {


    /**
     * 增量读取
     * crm_vip_info1_increment
     * crm_vip_info2_increment
     * crm_vip_info3_increment
     * crm_vip_info4_increment
     *
     *
     * 增量用户写入到
     * user_increment
     * user_extension_increment
     * auth_account_increment
     * auth_social_increment
     *
     * 逻辑同原来的全量新增的逻辑，
     *
     * 查询出来的记录如果已经在mall4cloud_user.user中存在。则更新mall4cloud_user.user记录。
     * 否则做新增处理
     */
    public void transfer(){

    }

}
