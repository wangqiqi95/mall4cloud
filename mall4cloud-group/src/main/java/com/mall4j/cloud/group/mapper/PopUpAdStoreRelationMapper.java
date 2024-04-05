package com.mall4j.cloud.group.mapper;

import com.mall4j.cloud.group.model.PopUpAdStoreRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2023-04-21
 */
public interface PopUpAdStoreRelationMapper extends BaseMapper<PopUpAdStoreRelation> {

    void insertBatch(@Param("storeIdList")List<Long> storeIdList, @Param("popUpAdId") Long popUpAdId);

}
