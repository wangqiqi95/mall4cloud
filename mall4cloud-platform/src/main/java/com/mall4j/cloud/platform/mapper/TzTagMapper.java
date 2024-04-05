package com.mall4j.cloud.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.platform.dto.TzTagQueryParamDTO;
import com.mall4j.cloud.platform.model.TzTag;
import com.mall4j.cloud.platform.vo.TzTagVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 门店标签
 *
 * @author gmq
 * @date 2022-09-13 12:00:57
 */
public interface TzTagMapper extends BaseMapper<TzTag> {

	/**
	 * 获取门店标签列表
	 * @return 门店标签列表
	 */
	List<TzTagVO> listByParam(@Param("paramDTO") TzTagQueryParamDTO paramDTO);

}
