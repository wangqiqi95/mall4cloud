package com.mall4j.cloud.product.task;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.product.mapper.SkuMapper;
import com.mall4j.cloud.product.model.Category;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.service.CategoryService;
import com.mall4j.cloud.product.service.SkuService;
import com.mall4j.cloud.product.service.ZhlsCommodityService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author axin
 * @Date 2023-05-05 18:15
 **/
@Component
@Slf4j
@RequestMapping
public class ZhlsCommoddityTask {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ZhlsCommodityService zhlsCommodityService;
    @Autowired
    private SkuMapper skuMapper;

    /**
     * 同步商品到有数商品池
     */
    @XxlJob("syncZhlsProduct")
    public void syncZhlsProduct(){
        String param = XxlJobHelper.getJobParam();
        long startTime = System.currentTimeMillis();
        int pageNo=1;
        int pageSize=1000;

        if(StringUtils.isNotBlank(param)){
            String[] split = param.split(",");
            pageNo=Integer.parseInt(split[0]);
            pageSize=Integer.parseInt(split[1]);
        }
        PageInfo<Long> pageInfo = null;
        do {
            long oneStartTime = System.currentTimeMillis();
            PageHelper.startPage(pageNo, pageSize);
            List<Long> list = skuMapper.getSkuIdListingList();
            pageInfo = new PageInfo<>(list);
            zhlsCommodityService.skuInfoAdd(list);
            log.info("全量同步有数商品单次耗时：{},当前页:{},总页数{}",System.currentTimeMillis()-oneStartTime,pageNo,pageInfo.getPages());

            pageNo++;
        }while (pageNo <= pageInfo.getPages());

        log.info("全量同步有数商品耗时：{}",System.currentTimeMillis()-startTime);
    }

    /**
     * 同步商品到有数商品池
     */
    @XxlJob("syncZhlsProductBySkuIds")
    public void syncZhlsProductBySkuIds(){
        String param = XxlJobHelper.getJobParam();
        long startTime = System.currentTimeMillis();
        if(StringUtils.isNotBlank(param)){
            List<Long> skuIds = JSON.parseArray(param, Long.class);
            zhlsCommodityService.skuInfoAdd(skuIds);
        }
        log.info("指定同步有数商品耗时：{}",System.currentTimeMillis()-startTime);
    }

    @XxlJob("syncZhlsProductCategories")
    public void syncProductCategories(){
        long startTime=System.currentTimeMillis();
        PageHelper.startPage(1,2000);
        List<Category> list = categoryService.list(Wrappers.emptyWrapper());
        PageInfo<Category> page = new PageInfo<>(list);
        zhlsCommodityService.productCategoriesAdd(page.getList(),0);
        log.info("全量同步分类耗时：{}",System.currentTimeMillis()-startTime);

    }
}
