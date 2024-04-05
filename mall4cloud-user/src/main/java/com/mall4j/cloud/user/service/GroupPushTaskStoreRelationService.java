package com.mall4j.cloud.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.QueryStorePageByPushTaskPageDTO;
import com.mall4j.cloud.user.model.GroupPushTaskStoreRelation;

import java.util.List;

/**
 * <p>
 * 群发任务店铺关联表 服务类
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
public interface GroupPushTaskStoreRelationService extends IService<GroupPushTaskStoreRelation> {


    ServerResponseEntity<PageVO<SelectedStoreVO>> getTheStorePageByPushTaskId(QueryStorePageByPushTaskPageDTO pageDTO);


    ServerResponseEntity<List<Long>> getTheStoreIdListByTask(Long pushTaskId);
}
