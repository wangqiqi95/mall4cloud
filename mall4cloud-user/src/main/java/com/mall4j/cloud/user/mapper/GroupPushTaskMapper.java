package com.mall4j.cloud.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.user.dto.QueryGroupPushTaskPageDTO;
import com.mall4j.cloud.user.model.GroupPushTask;
import com.mall4j.cloud.user.vo.GroupPushTaskPageVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 群发任务组表 Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
public interface GroupPushTaskMapper extends BaseMapper<GroupPushTask> {

    Page<GroupPushTaskPageVO> selectGroupPushTaskPage(@Param("page") IPage page, @Param("param")QueryGroupPushTaskPageDTO pageDTO);

}
