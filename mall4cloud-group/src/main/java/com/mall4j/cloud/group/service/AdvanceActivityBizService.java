package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.AdvanceActivityDTO;
import com.mall4j.cloud.group.dto.AdvanceActivityPageDTO;
import com.mall4j.cloud.group.dto.ModifyShopDTO;
import com.mall4j.cloud.group.vo.AdvanceActivityListVO;
import com.mall4j.cloud.group.vo.AdvanceActivityVO;

public interface AdvanceActivityBizService {
    ServerResponseEntity<Void> saveOrUpdateAdvanceActivity(AdvanceActivityDTO param);

    ServerResponseEntity<AdvanceActivityVO> detail(Integer id);

    ServerResponseEntity<PageVO<AdvanceActivityListVO>> page(AdvanceActivityPageDTO param);

    ServerResponseEntity<Void> enable(Integer id);

    ServerResponseEntity<Void> disable(Integer id);

    ServerResponseEntity<Void> delete(Integer id);

}
