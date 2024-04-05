package com.mall4j.cloud.docking.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.docking.dto.PushErpProductDto;
import com.mall4j.cloud.api.docking.skq_erp.dto.PushProductsDto;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.api.product.vo.StdPushSpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_erp.service.IStdProductService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Component
@RestController
@RequestMapping("/ua/erp/shop")
public class ErpProductSyncTask {

    private static final Logger log = LoggerFactory.getLogger(ErpProductSyncTask.class);

    @Autowired
    IStdProductService stdProductService;

    @Autowired
    private SpuFeignClient spuFeignClient;

    @XxlJob("erpPushProductSyncTask")
    @PostMapping("erpPushProductSyncTask")
    public void erpPushProductSyncTask(@RequestBody(required = false) PushErpProductDto pushErpProductDto) {
        log.info("开始执行售卖商品推送任务》》》》》》》》》》》》》》》》》》》》》");
        Long startTime=System.currentTimeMillis();
        Integer pageSize=Objects.nonNull(pushErpProductDto)?pushErpProductDto.getPageSize():0;
        String param = XxlJobHelper.getJobParam();
        log.info("开始执行售卖商品推送任务---> 接收調度中心参数...[{}]",param);
        log.info("开始执行售卖商品推送任务---> 接口参数...[{}]",pageSize);
        if(StrUtil.isNotBlank(param)){
            String[] methodParams = param.split(",");
            pageSize= Integer.parseInt(methodParams[0]);
            if(pageSize<50){
                pageSize=0;
            }
        }else{
            pageSize=0;
        }
        List<String> spuCodes=Objects.nonNull(pushErpProductDto)&&CollectionUtil.isNotEmpty(pushErpProductDto.getSpuCodes())?pushErpProductDto.getSpuCodes():new ArrayList<>();
        ServerResponseEntity<List<StdPushSpuVO>> response=spuFeignClient.pushStdSpus(spuCodes);
        if(response.isSuccess() && CollectionUtil.isNotEmpty(response.getData()) && response.getData().size()>0){
            log.info("售卖商品推送条数{}",response.getData().size());
            if(pageSize>0){
                List<List<StdPushSpuVO>> split = ListUtil.split(response.getData(), pageSize);
                log.info("售卖商品推送->分批推送 分批总数{} 每次推送条数{}",split.size(),pageSize);
                int seq=0;
                for(List<StdPushSpuVO> items:split){
                    seq++;
                    log.info("售卖商品推送->分批推送 当前批次{} 当前批次总条数{}",seq,items.size());
                    push(items);
                }
            }else{
                log.info("售卖商品推送->单次推送 总条数{}",response.getData().size());
                push(response.getData());
            }

        }
        log.info("结束执行售卖商品推送任务》》》》》》》》》》》》》》》》》》》》》耗时：{}ms",System.currentTimeMillis() - startTime);
    }

    private void push(List<StdPushSpuVO> stdPushSpuVOS){
        List<PushProductsDto> pushProductDtos=new ArrayList<>();
        stdPushSpuVOS.forEach(pushStoreDtos -> {
            PushProductsDto pushStoreDto=new PushProductsDto();
            pushStoreDto.setPlatform("XXY");
//            pushStoreDto.setPsCProEcode(pushStoreDtos.getSpuCode());
            pushStoreDto.setPsCProEcode(pushStoreDtos.getPriceCode());
            pushStoreDto.setApproveStatus(pushStoreDtos.getStatus());
            pushProductDtos.add(pushStoreDto);
        });
        stdProductService.pushProduct(pushProductDtos);
    }
}
