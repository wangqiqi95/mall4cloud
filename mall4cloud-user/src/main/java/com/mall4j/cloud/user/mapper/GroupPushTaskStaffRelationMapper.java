package com.mall4j.cloud.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall4j.cloud.user.bo.AddGroupPushTaskStaffRelationBO;
import com.mall4j.cloud.user.dto.QuerySonTaskStaffPageDTO;
import com.mall4j.cloud.user.model.GroupPushTaskStaffRelation;
import com.mall4j.cloud.user.vo.SonTaskStaffPageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 群发任务导购关联表 Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
public interface GroupPushTaskStaffRelationMapper extends BaseMapper<GroupPushTaskStaffRelation> {

    void insertBatch(@Param("relationList")List<GroupPushTaskStaffRelation> relationBOList);

    IPage<SonTaskStaffPageVO> selectSonTaskStaffList(@Param("page") IPage page, @Param("param")QuerySonTaskStaffPageDTO param);
}
