package com.mall4j.cloud.group.mapper;

import com.mall4j.cloud.group.model.PopUpAdUserOperate;
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
public interface PopUpAdUserOperateMapper extends BaseMapper<PopUpAdUserOperate> {

    void insertBatch(@Param("unionId") String unionId, @Param("userId") Long userId,
                     @Param("adIdList")List<Long> adIdList, @Param("operate") Integer operate,
                     @Param("storeId") Long storeId, @Param("entrance") String entrance);


    Integer getBrowsePeopleCountByAdId(@Param("popUpAdId") Long popUpAdId);

    Integer getClickPeopleCountByAdId(@Param("popUpAdId") Long popUpAdId);
}
