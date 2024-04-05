package com.mall4j.cloud.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mall4j.cloud.common.model.BaseModel;
import com.mall4j.cloud.platform.mapper.StoreRenameConfMapper;
import com.mall4j.cloud.platform.model.StoreRenameConf;
import com.mall4j.cloud.platform.model.TzStore;
import com.mall4j.cloud.platform.service.StoreRenameConfService;
import com.mall4j.cloud.platform.service.TzStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 门店修改名称配置表
 * @Author axin
 * @Date 2022-11-08 11:45
 **/
@Service
@Slf4j
public class StoreRenameConfServiceImpl implements StoreRenameConfService {
    @Resource
    private StoreRenameConfMapper storeRenameConfMapper;
    @Resource
    private TzStoreService tzStoreService;


    @Override
    public List<StoreRenameConf> listAll() {
        return storeRenameConfMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public void update(List<StoreRenameConf> list) {
        if(CollectionUtils.isNotEmpty(list)){
            list.forEach(conf ->{
                LambdaUpdateWrapper<StoreRenameConf> eq = new UpdateWrapper<StoreRenameConf>().lambda()
                        .set(StoreRenameConf::getName, conf.getName())
                        .set(BaseModel::getUpdateTime,new Date())
                        .eq(StoreRenameConf::getCode, conf.getCode());
                storeRenameConfMapper.update(new StoreRenameConf(),eq);
            });
        }
    }

    @Override
    public void updateStoreNameTask() {
        List<StoreRenameConf> storeRenameConfs = listAll();
        if(CollectionUtils.isEmpty(storeRenameConfs)){
            log.info("没有要修改的店铺");
            return;
        }

        Map<String, StoreRenameConf> storeRenameConfMap = storeRenameConfs.stream().collect(Collectors.toMap(StoreRenameConf::getCode, k -> k));
        List<TzStore> tzStores = tzStoreService.listByStoreCode(Arrays.asList(storeRenameConfMap.keySet().toArray(new String[0])));
        for (TzStore tzStore : tzStores) {
            StoreRenameConf storeRenameConf = storeRenameConfMap.get(tzStore.getStoreCode());
            storeRenameConf.setName(tzStore.getStationName());
            tzStore.setStationName(storeRenameConf.getRename());
        }
        tzStoreService.updateBatch(tzStores);
        update(new ArrayList<>(storeRenameConfMap.values()));
        log.info("修改的店铺名：【{}】", JSON.toJSONString(storeRenameConfMap.values()));
    }


}
