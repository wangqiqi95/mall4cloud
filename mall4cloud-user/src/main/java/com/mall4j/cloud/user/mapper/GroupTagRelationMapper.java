package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.user.model.GroupTagRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 标签组与标签关联表 Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2023-02-08
 */
public interface GroupTagRelationMapper extends BaseMapper<GroupTagRelation> {


    Integer getTagCountByGroup(@Param("tagName") String tagName, @Param("groupId") Long groupId);
}
