package com.mall4j.cloud.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.bo.ExportSonTaskStaffPageBO;
import com.mall4j.cloud.user.dto.QuerySonTaskStaffPageDTO;
import com.mall4j.cloud.user.model.GroupPushTaskStaffRelation;
import com.mall4j.cloud.user.vo.SonTaskStaffPageVO;

import java.util.List;

/**
 * <p>
 * 群发任务导购关联表 服务类
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
public interface GroupPushTaskStaffRelationService extends IService<GroupPushTaskStaffRelation> {

    PageVO<SonTaskStaffPageVO> getSonTaskStaffList(QuerySonTaskStaffPageDTO pageDTO);

    List<ExportSonTaskStaffPageBO> getDataByTaskAndSonTask(Long pushTaskId, Long sonTaskId);

}
