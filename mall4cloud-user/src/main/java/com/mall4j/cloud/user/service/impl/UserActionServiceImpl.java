package com.mall4j.cloud.user.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.UserActionQueryDTO;
import com.mall4j.cloud.user.mapper.UserActionMapper;
import com.mall4j.cloud.user.model.UserAction;
import com.mall4j.cloud.user.service.UserActionService;
import com.mall4j.cloud.user.vo.UserActionListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class UserActionServiceImpl implements UserActionService {

    @Autowired
    private UserActionMapper userActionMapper;

    @Override
    public PageVO<UserActionListVO> page(PageDTO pageDTO, UserActionQueryDTO userActionQueryDTO) {
        return PageUtil.doPage(pageDTO, () -> userActionMapper.list(userActionQueryDTO));
    }

    @Override
    public void save(UserAction userAction) {
        userActionMapper.insert(userAction);
    }

    @Override
    public void saveBatch(List<UserAction> userActionList) {
        userActionMapper.saveBatch(userActionList);
    }
}
