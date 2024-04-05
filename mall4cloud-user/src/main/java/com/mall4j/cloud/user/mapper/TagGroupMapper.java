package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.user.dto.CheckVipCodeSingleTagCountDTO;
import com.mall4j.cloud.user.model.TagGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.vo.CheckVipCodeSingleTagCountVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 标签组表 Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2023-02-08
 */
public interface TagGroupMapper extends BaseMapper<TagGroup> {

    List<CheckVipCodeSingleTagCountVO> selectSingleCountByVipCode(@Param("param") CheckVipCodeSingleTagCountDTO checkVipCodeSingleTagCountDTO);

}
