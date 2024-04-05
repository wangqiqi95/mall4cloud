package com.mall4j.cloud.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.bo.AddPushTaskStoreRelationBO;
import com.mall4j.cloud.user.model.GroupPushTaskStoreRelation;
import com.mall4j.cloud.user.vo.GroupPushTaskStoreCountVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 群发任务店铺关联表 Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
public interface GroupPushTaskStoreRelationMapper extends BaseMapper<GroupPushTaskStoreRelation> {

    void insertBatch(@Param("relationList")List<AddPushTaskStoreRelationBO> relationBOList);

    List<GroupPushTaskStoreCountVO> selectTheCountByTaskIdList(@Param("taskIdList") List<Long> taskIdList);

}
