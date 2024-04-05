package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.platform.vo.DistributionConfigApiVO;
import com.mall4j.cloud.api.platform.vo.DistributionRecruitConfigApiVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.constant.BindInvalidReasonEnum;
import com.mall4j.cloud.distribution.constant.BindStateEnum;
import com.mall4j.cloud.distribution.constant.DistributionUserStateEnum;
import com.mall4j.cloud.distribution.controller.app.DistributionUserBindController;
import com.mall4j.cloud.distribution.dto.DistributionUserBindDTO;
import com.mall4j.cloud.distribution.model.DistributionUser;
import com.mall4j.cloud.distribution.model.DistributionUserBind;
import com.mall4j.cloud.distribution.mapper.DistributionUserBindMapper;
import com.mall4j.cloud.distribution.service.DistributionConfigService;
import com.mall4j.cloud.distribution.service.DistributionUserBindService;
import com.mall4j.cloud.distribution.service.DistributionUserService;
import com.mall4j.cloud.distribution.vo.DistributionUserBindInfoVO;
import com.mall4j.cloud.distribution.vo.DistributionUserBindVO;
import com.mall4j.cloud.distribution.vo.DistributionUserVO;
import ma.glasnost.orika.MapperFacade;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 分销员绑定关系
 *
 * @author cl
 * @date 2021-08-09 14:14:09
 */
@Service
public class DistributionUserBindServiceImpl implements DistributionUserBindService {

    private static final Logger logger = LoggerFactory.getLogger(DistributionUserBindServiceImpl.class);

    @Autowired
    private DistributionUserBindMapper distributionUserBindMapper;
    @Autowired
    private DistributionConfigService distributionConfigService;
    @Autowired
    private DistributionUserService distributionUserService;
    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public PageVO<DistributionUserBindVO> page(PageDTO pageDTO, DistributionUserBindDTO distributionUserBindDTO) {
        PageVO<DistributionUserBindVO> bindPage = PageUtil.doPage(pageDTO, () -> distributionUserBindMapper.list(distributionUserBindDTO));
        List<DistributionUserBindVO> bindPageList = bindPage.getList();
        if (CollUtil.isEmpty(bindPageList)){
            return bindPage;
        }
        List<Long> userIds = bindPageList.stream().map(DistributionUserBindVO::getUserId).distinct().collect(Collectors.toList());
        if (CollUtil.isEmpty(userIds)){
            return bindPage;
        }
        ServerResponseEntity<List<UserApiVO>> userApiResponse = userFeignClient.getUserByUserIds(userIds);
        if (!userApiResponse.isSuccess()) {
            throw new LuckException(userApiResponse.getMsg());
        }
        List<UserApiVO> userApiList = userApiResponse.getData();
        if (CollUtil.isEmpty(userApiList)) {
            return bindPage;
        }
        Map<Long, String> userMap = userApiList.stream().collect(Collectors.toMap(UserApiVO::getUserId, UserApiVO::getNickName));
        for (DistributionUserBindVO distributionUserBindVO : bindPageList) {
            Long userId = distributionUserBindVO.getUserId();
            String nickName = userMap.get(userId);
            if (Objects.isNull(nickName)){
                continue;
            }
            distributionUserBindVO.setUserNickName(nickName);
        }
        return bindPage;
    }

    @Override
    public DistributionUserBind getByBindId(Long bindId) {
        return distributionUserBindMapper.getByBindId(bindId);
    }

    @Override
    public void save(DistributionUserBind distributionUserBind) {
        distributionUserBindMapper.save(distributionUserBind);
    }

    @Override
    public void update(DistributionUserBind distributionUserBind) {
        distributionUserBindMapper.update(distributionUserBind);
    }

    @Override
    public void deleteById(Long bindId) {
        distributionUserBindMapper.deleteById(bindId);
    }

    @Override
    public ServerResponseEntity<DistributionUser> bindDistribution(DistributionUser shareUser, Long userId, int type) {
        //分享员信息
        if (shareUser == null) {
            // 获取推广员信息失败
            return ServerResponseEntity.showFailMsg("获取推广员信息失败");
        }
        if (!Objects.equals(shareUser.getState(), DistributionUserStateEnum.NORMAL.value())) {
            logger.info("推广员状态异常");
            // 推广员状态异常
            return ServerResponseEntity.showFailMsg("推广员状态异常");
        }
        DistributionConfigApiVO distributionConfig = distributionConfigService.getDistributionConfig();
//        if (!verifyType(distributionConfig, type)) {
//            logger.info("未开启该绑定用户方式,type:{}",type);
//            // 未开启该绑定用户方式
//            return ServerResponseEntity.showFailMsg("未开启该绑定用户方式");
//        }
        // 查询该用户以前绑定的分享人
        DistributionUserBind distributionUserBind = distributionUserBindMapper.getUserBindByUserIdAndState(userId, BindStateEnum.VALID.value());

        // 没有绑定分享人，或分享人已被冻结，可以与该用户进行绑定
        if (Objects.isNull(distributionUserBind)) {
            bindUser(userId, shareUser);
            return ServerResponseEntity.success(shareUser);
        }

        // 如果现在的分享人就是以前的分享人
        if (Objects.equals(distributionUserBind.getDistributionUserId(),shareUser.getDistributionUserId())) {
            return ServerResponseEntity.success(shareUser);
        }

        // 查询以前的绑定的用户信息
        DistributionUser oldDistributionUser = distributionUserService.getByDistributionUserId(distributionUserBind.getDistributionUserId());

        // 不绑定，分享人优先
        if (Objects.equals(distributionConfig.getAttribution(), 1)) {
            unBindUser(distributionUserBind.getBindId());
            bindUser(userId, shareUser);
            return ServerResponseEntity.success(shareUser);
        }
        return ServerResponseEntity.fail(ResponseEnum.SHOW_FAIL, oldDistributionUser);
    }

    @Override
    public PageVO<DistributionUserBindInfoVO> bindUserList(PageDTO pageDTO, Long userId) {
        PageVO<DistributionUserBindInfoVO> pageVO = PageUtil.doPage(pageDTO, () -> distributionUserBindMapper.bindUserList(userId));
        List<DistributionUserBindInfoVO> list = pageVO.getList();
        if (CollUtil.isEmpty(list)) {
            return pageVO;
        }
        List<Long> userIds = list.stream().map(DistributionUserBindInfoVO::getUserId).collect(Collectors.toList());
        // 获取用户信息
        ServerResponseEntity<List<UserApiVO>> userResponse = userFeignClient.getUserByUserIds(userIds);
        if (!userResponse.isSuccess()) {
            throw new LuckException(userResponse.getMsg());
        }
        List<UserApiVO> userList= userResponse.getData();
        if (CollUtil.isEmpty(userList)) {
            return pageVO;
        }
        Map<Long, UserApiVO> userMap = userList.stream().collect(Collectors.toMap(UserApiVO::getUserId, (k) -> k));
        for (DistributionUserBindInfoVO bindInfoVO : list) {
            Long userIdOri = bindInfoVO.getUserId();
            UserApiVO userApiVO = userMap.get(userIdOri);
            if (Objects.isNull(userApiVO)) {
                continue;
            }
            bindInfoVO.setNickName(userApiVO.getNickName());
            bindInfoVO.setPic(userApiVO.getPic());
        }
        pageVO.setList(list);
        return pageVO;
    }

    @Override
    public DistributionUserBind getUserBindByUserIdAndState(Long userId, Integer state) {
        return distributionUserBindMapper.getUserBindByUserIdAndState(userId, state);
    }

    private void unBindUser(Long bindId) {
        DistributionUserBind distributionUserBind = new DistributionUserBind();
        distributionUserBind.setBindId(bindId);
        // 系统修改
        distributionUserBind.setInvalidReason(BindInvalidReasonEnum.ADMIN.value());
        distributionUserBind.setState(BindStateEnum.INVALID.value());
        distributionUserBindMapper.update(distributionUserBind);

    }

    private void bindUser(Long userId, DistributionUser shareUser) {
        DistributionUserBind distributionUserBind = new DistributionUserBind();
        distributionUserBind.setBindTime(new Date());
        distributionUserBind.setUserId(userId);
        distributionUserBind.setState(BindStateEnum.VALID.value());
        distributionUserBind.setDistributionUserId(shareUser.getDistributionUserId());
        distributionUserBindMapper.save(distributionUserBind);
    }

    private boolean verifyType(DistributionConfigApiVO distributionConfig, int type) {
        // 不绑定，分享人优先--不需要检验
        if (Objects.equals(distributionConfig.getAttribution(), 1)) {
            return true;
        }
        return false;
    }
}
