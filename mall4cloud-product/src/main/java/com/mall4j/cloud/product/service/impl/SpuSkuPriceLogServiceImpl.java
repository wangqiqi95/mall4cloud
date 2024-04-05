package com.mall4j.cloud.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.model.SpuSkuPriceLog;
import com.mall4j.cloud.product.mapper.SpuSkuPriceLogMapper;
import com.mall4j.cloud.product.service.SpuSkuPriceLogService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-05-31 16:07:13
 */
@Service
public class SpuSkuPriceLogServiceImpl extends ServiceImpl<SpuSkuPriceLogMapper, SpuSkuPriceLog> implements SpuSkuPriceLogService {

}
