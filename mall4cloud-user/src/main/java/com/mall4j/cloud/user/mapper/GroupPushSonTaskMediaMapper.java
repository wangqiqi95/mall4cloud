package com.mall4j.cloud.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.vo.TaskSonMediaVO;
import com.mall4j.cloud.user.bo.AddPushSonTaskMediaBO;
import com.mall4j.cloud.user.bo.GroupPushSonTaskMediaBO;
import com.mall4j.cloud.user.model.GroupPushSonTaskMedia;
import com.mall4j.cloud.user.vo.GroupPushSonTaskMediaVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 推送子任务素材表 Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
public interface GroupPushSonTaskMediaMapper extends BaseMapper<GroupPushSonTaskMedia> {

    void insertBatch(@Param("mediaList")List<AddPushSonTaskMediaBO> mediaBOList);

    List<TaskSonMediaVO> sonTaskMediaList(@Param("sonTaskId") Long sonTaskId);

    List<GroupPushSonTaskMediaVO> selectMediaListBySonTaskId(@Param("sonTaskId") Long sonTaskId);

    void updateBatch(@Param("list")List<GroupPushSonTaskMediaBO> sonTaskMediaBOList);

}
