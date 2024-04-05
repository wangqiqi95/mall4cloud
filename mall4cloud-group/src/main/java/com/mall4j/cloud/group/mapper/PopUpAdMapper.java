package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall4j.cloud.group.dto.QueryPopUpAdPageDTO;
import com.mall4j.cloud.group.model.PopUpAd;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.group.vo.PageAdByUserVO;
import com.mall4j.cloud.group.vo.PopUpAdDataPageVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2023-04-21
 */
public interface PopUpAdMapper extends BaseMapper<PopUpAd> {

    List<PageAdByUserVO> getThePageAdByUser(@Param("storeId") Long serviceStoreId, @Param("time")LocalDateTime time);

    IPage<PopUpAdDataPageVO> selectPageVO(@Param("page") IPage<PopUpAdDataPageVO> page, @Param("param")QueryPopUpAdPageDTO param);
}
