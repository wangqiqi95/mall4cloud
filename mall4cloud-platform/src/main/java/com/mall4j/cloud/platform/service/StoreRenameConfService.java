package com.mall4j.cloud.platform.service;

import com.mall4j.cloud.platform.model.StoreRenameConf;

import java.util.List;

/**
 * @Description 门店修改名称配置表
 * @Author axin
 * @Date 2022-11-08 11:44
 **/
public interface StoreRenameConfService {
    /**
     * 查询全部配置
     * @return
     */
    List<StoreRenameConf> listAll();

    /**
     * 修改配置
     * @param list
     * @return
     */
    void update(List<StoreRenameConf> list);

    /**
     * 修改店铺名定时任务
     */
    void updateStoreNameTask();
}
