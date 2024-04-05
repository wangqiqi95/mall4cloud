package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.user.bo.BuyVipNotifyBO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.user.constant.TermTypeEnum;
import com.mall4j.cloud.user.mapper.UserExtensionMapper;
import com.mall4j.cloud.user.mapper.UserLevelLogMapper;
import com.mall4j.cloud.user.mapper.UserMapper;
import com.mall4j.cloud.user.model.UserExtension;
import com.mall4j.cloud.user.model.UserLevelLog;
import com.mall4j.cloud.user.service.UserLevelLogService;
import com.mall4j.cloud.user.service.UserLevelService;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.vo.UserLevelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户等级记录
 *
 * @author FrozenWatermelon
 * @date 2021-05-14 11:04:52
 */
@Service
public class UserLevelLogServiceImpl implements UserLevelLogService {

    @Autowired
    private UserLevelLogMapper userLevelLogMapper;
    @Autowired
    private UserLevelService userLevelService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserExtensionMapper userExtensionMapper;

    @Override
    public PageVO<UserLevelLog> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> userLevelLogMapper.list());
    }

    @Override
    public UserLevelLog getByLevelLogId(Long levelLogId) {
        return userLevelLogMapper.getByLevelLogId(levelLogId);
    }

    @Override
    public void save(UserLevelLog userLevelLog) {
        userLevelLogMapper.save(userLevelLog);
    }

    @Override
    public void update(UserLevelLog userLevelLog) {
        userLevelLogMapper.update(userLevelLog);
    }

    @Override
    public void deleteById(Long levelLogId) {
        userLevelLogMapper.deleteById(levelLogId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paySuccess(UserLevelLog userLevelLog, BuyVipNotifyBO message) {
        UserApiVO userInfo = userService.getByUserId(userLevelLog.getUserId());

        Integer day = TermTypeEnum.instance(userLevelLog.getTermType()).getDay();

        DateTime endTime = DateUtil.offsetDay(userInfo.getVipEndTime() == null? new Date(): userInfo.getVipEndTime(), day);

        // 通过购买会员更新用户等级
        userMapper.updateUserLevelByBuyVip(userLevelLog.getUserId(),userLevelLog.getAfterLevel(), DateUtil.endOfDay(endTime));
        userExtensionMapper.updateUserLevelByBuyVip(userLevelLog.getUserId(), userLevelLog.getAfterLevel());
        // 发放奖励
        List<UserLevelVO> userLevelVOList = new ArrayList<>();
        UserLevelVO userLevelVO = new UserLevelVO();
        userLevelVO.setLevel(userInfo.getLevel());
        userLevelVO.setVipLevel(userLevelLog.getAfterLevel());
        userLevelVO.setLevelType(userLevelLog.getAfterLevelType());
        userLevelVOList.add(userLevelVO);
        UserExtension userExtension = userExtensionMapper.getByUserId(userLevelLog.getUserId());
        userLevelService.levelUp(userLevelVOList,userLevelLog,userExtension,userInfo.getPhone());
        // 将日志变更为已支付
        int updateStatus = userLevelLogMapper.updateToSuccess(userLevelLog.getLevelLogId(), message.getPayId());
        if (updateStatus < 1) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }

    @Override
    public int batchSaveUserLevelLogs(List<UserLevelLog> userLevelLogs) {
        return userLevelLogMapper.batchSaveUserLevelLogs(userLevelLogs);
    }

    @Override
    public Integer getMaxLevelByUserId(Long userId) {
        return userLevelLogMapper.getMaxLevelByUserId(userId);
    }
}
