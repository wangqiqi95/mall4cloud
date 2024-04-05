package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.product.model.SpuStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * spu信息(SpuStore)表数据库访问层
 *
 * @author makejava
 * @since 2022-02-21 21:28:47
 */
public interface SpuStoreMapper extends BaseMapper<SpuStore> {

    /**
     * 批量修改
     */
    void updateBatchPriceFee(@Param("spuStoreList") List<SpuStore> spuList);

}

