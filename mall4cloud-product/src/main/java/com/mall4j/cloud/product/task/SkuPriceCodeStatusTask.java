package com.mall4j.cloud.product.task;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.product.mapper.SkuStoreMapper;
import com.mall4j.cloud.product.model.SkuStore;
import com.mall4j.cloud.product.service.SkuStoreService;
import com.mall4j.cloud.product.service.TagActivityService;
import com.mall4j.cloud.product.vo.BatchSkuStorePriceCodeVO;
import com.mall4j.cloud.product.vo.BatchSkuStoreVO;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 营销标签活动定时任务
 *
 * @author hwy
 */
@Slf4j
@Component
@RestController
public class SkuPriceCodeStatusTask {

    @Resource
    private SkuStoreMapper skuStoreMapper;
    @Autowired
    private SkuStoreService skuStoreService;

    @XxlJob("SkuPriceCodeStatusTask")
    public void SkuPriceCodeStatusTask(){
        List<BatchSkuStorePriceCodeVO> priceCodeVOS= skuStoreMapper.batchSkuPriceCode();
        Long startTime=System.currentTimeMillis();
        if(CollectionUtil.isNotEmpty(priceCodeVOS)){
            for(BatchSkuStorePriceCodeVO priceCode:priceCodeVOS){
                List<BatchSkuStoreVO> batchSkuCodeStatus=skuStoreMapper.batchSkuCodeStatus(priceCode.getPriceCode());
                if(CollectionUtil.isNotEmpty(batchSkuCodeStatus)){
                    List<SkuStore> skuStores=new ArrayList<>();
                    for(BatchSkuStoreVO batchSkuStoreVO:batchSkuCodeStatus){
                        if(batchSkuStoreVO.getStatus()!=1){
                            SkuStore skuStore=new SkuStore();
                            skuStore.setSkuStoreId(batchSkuStoreVO.getSkuStoreId());
                            skuStore.setStatus(batchSkuStoreVO.getSkuStatus());
                            skuStore.setUpdateTime(new Date());
                            skuStores.add(skuStore);
                        }
                    }
                    if(CollectionUtil.isNotEmpty(skuStores)){
                        skuStoreService.updateBatchById(skuStores);

                        skuStoreMapper.batchSkuPriceCodeTempStatus(priceCode.getPriceCode());
                    }

                }
            }
        }

        log.info("-----结束执行批量启用sku_store条码状态------耗时：{}ms", System.currentTimeMillis() - startTime);
    }
}
