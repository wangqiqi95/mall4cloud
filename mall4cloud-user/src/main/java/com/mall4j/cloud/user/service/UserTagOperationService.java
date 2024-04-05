package com.mall4j.cloud.user.service;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.QueryUserTagOperationPageDTO;
import com.mall4j.cloud.user.model.UserTagOperation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.user.vo.UserTagOperationVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ben
 * @since 2023-02-09
 */
public interface UserTagOperationService extends IService<UserTagOperation> {

    ServerResponseEntity<PageVO<UserTagOperationVO>> getOperationByBeVipCode(QueryUserTagOperationPageDTO queryUserTagOperationPageDTO);


}
