package com.mall4j.cloud.user.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall4j.cloud.user.dto.QueryTagPageDTO;
import com.mall4j.cloud.user.model.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.user.vo.TagVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 标签表 Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2023-02-08
 */
public interface TagMapper extends BaseMapper<Tag> {

    IPage<TagVO> getTagPageByGroup(@Param("page") IPage<TagVO> page,@Param("queryTagPageDTO") QueryTagPageDTO queryTagPageDTO);


    List<TagVO> getTagListByGroup(@Param("groupId") Long groupId);

}
