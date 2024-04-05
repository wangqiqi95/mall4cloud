package com.mall4j.cloud.group.service;

import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.QueryStorePageByAdPageDTO;
import com.mall4j.cloud.group.model.PopUpAdStoreRelation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ben
 * @since 2023-04-21
 */
public interface PopUpAdStoreRelationService extends IService<PopUpAdStoreRelation> {

    ServerResponseEntity<PageVO<SelectedStoreVO>> getTheStorePageByAdId(QueryStorePageByAdPageDTO pageDTO);

    ServerResponseEntity<List<Long>> getTheStoreIdListByAdId(Long adId);
}
