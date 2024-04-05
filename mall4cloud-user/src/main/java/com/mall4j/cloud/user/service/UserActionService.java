package com.mall4j.cloud.user.service;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.UserActionQueryDTO;
import com.mall4j.cloud.user.model.UserAction;
import com.mall4j.cloud.user.vo.UserActionListVO;

import java.util.List;

public interface UserActionService {

    PageVO<UserActionListVO> page(PageDTO pageDTO, UserActionQueryDTO userActionQueryDTO);

    void save(UserAction userAction);

    void saveBatch(List<UserAction> userActionList);

}
