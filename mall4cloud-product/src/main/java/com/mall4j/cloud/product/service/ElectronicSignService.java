package com.mall4j.cloud.product.service;

import com.mall4j.cloud.api.docking.dto.ElectronicSignDto;

import java.util.List;

/**
 * @luzhengxiang
 * @create 2022-04-02 11:13 AM
 **/
public interface ElectronicSignService {

    void syncSpu(List<Long> spuIds);

    void syncStoreSpu(List<Long> storeIds);
}
