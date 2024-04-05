package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.product.dto.AttrFilterDto;
import com.mall4j.cloud.product.model.AttrProperties;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author axin
 * @Date 2023-06-13
 **/
@Mapper
public interface AttrPropertiesMapper extends BaseMapper<AttrProperties> {

    List<Long> selectByTypeAndSex(@Param("dto") AttrFilterDto dto);
}
