package com.mall4j.cloud.product.controller.app;


import com.mall4j.cloud.product.service.SpuStoreService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * spu信息(SpuStore)表控制层
 *
 * @author makejava
 * @since 2022-02-21 21:28:45
 */
@RestController
@RequestMapping("/ua/spuStore")
public class AppSpuStoreController {
    /**
     * 服务对象
     */
    @Resource
    private SpuStoreService spuStoreService;


}

