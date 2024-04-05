package com.mall4j.cloud.group.mapper;

import com.mall4j.cloud.group.model.PopUpAdPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 开屏广告触发页面记录标 Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2023-04-21
 */
public interface PopUpAdPageMapper extends BaseMapper<PopUpAdPage> {

    void insertBatch(@Param("pageList") List<String> pageList, @Param("adId") Long adId,
                     @Param("createUser") Long createUser);

}
