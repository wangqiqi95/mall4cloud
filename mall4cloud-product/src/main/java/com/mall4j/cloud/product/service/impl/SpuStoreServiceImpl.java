package com.mall4j.cloud.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.product.mapper.SpuStoreMapper;
import com.mall4j.cloud.product.model.SpuStore;
import com.mall4j.cloud.product.service.SpuStoreService;
import org.springframework.stereotype.Service;

/**
 * spu信息(SpuStore)表服务实现类
 *
 * @author makejava
 * @since 2022-02-21 21:28:52
 */
@Service("spuStoreService")
public class SpuStoreServiceImpl extends ServiceImpl<SpuStoreMapper, SpuStore> implements SpuStoreService {

}

