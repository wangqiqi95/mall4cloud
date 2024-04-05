package com.mall4j.cloud.seckill.mapper;

import com.mall4j.cloud.seckill.model.SeckillStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SeckillStoreMapper {

    void saveBatch(@Param("seckillStoreList") List<SeckillStore> seckillStoreList);

    void deleteBySeckillId(@Param("seckillId") Long seckillId);

    List<SeckillStore> listBySeckillId(@Param("seckillId") Long seckillId);

    List<SeckillStore> listBySeckillIdList(@Param("seckillIdList") List<Long> seckillIdList);

    SeckillStore findBySeckillIdAndStoreId(@Param("seckillId") Long seckillId, @Param("storeId") Long storeId);

}
