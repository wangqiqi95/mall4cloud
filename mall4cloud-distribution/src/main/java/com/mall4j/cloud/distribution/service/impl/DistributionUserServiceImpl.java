package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.common.cache.constant.DistributionCacheNames;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.distribution.constant.BindStateEnum;
import com.mall4j.cloud.distribution.constant.DistributionAuditStateEnum;
import com.mall4j.cloud.distribution.constant.DistributionUserStateEnum;
import com.mall4j.cloud.distribution.dto.DistributionUserDTO;
import com.mall4j.cloud.distribution.mapper.*;
import com.mall4j.cloud.distribution.model.DistributionAuditing;
import com.mall4j.cloud.distribution.model.DistributionUser;
import com.mall4j.cloud.distribution.model.DistributionUserBan;
import com.mall4j.cloud.distribution.model.DistributionUserWallet;
import com.mall4j.cloud.distribution.service.DistributionUserService;
import com.mall4j.cloud.distribution.service.DistributionUserWalletService;
import com.mall4j.cloud.distribution.vo.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 分销员信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
@Service
public class DistributionUserServiceImpl implements DistributionUserService {

    @Autowired
    private DistributionUserMapper distributionUserMapper;
    @Autowired
    private SegmentFeignClient segmentFeignClient;
    @Autowired
    private DistributionUserBanMapper distributionUserBanMapper;
    @Autowired
    private DistributionUserBindMapper distributionUserBindMapper;
    @Autowired
    private DistributionSpuBindMapper distributionSpuBindMapper;
    @Autowired
    private DistributionUserIncomeMapper distributionUserIncomeMapper;
    @Autowired
    private DistributionUserWalletMapper distributionUserWalletMapper;
    @Autowired
    private DistributionWithdrawCashMapper distributionWithdrawCashMapper;
    @Autowired
    private DistributionUserWalletService distributionUserWalletService;
    @Autowired
    private DistributionAuditingMapper distributionAuditingMapper;

    @Override
    public PageVO<DistributionUserVO> distributionUserPage(PageDTO pageDTO, DistributionUserDTO distributionUserDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionUserMapper.listDistributionUser(distributionUserDTO));
    }

    @Override
    public DistributionUser getByDistributionUserId(Long distributionUserId) {
        return distributionUserMapper.getByDistributionUserId(distributionUserId);
    }

    @Override
    public void save(DistributionUser distributionUser) {
        if (Objects.isNull(distributionUser.getDistributionUserId())) {
            ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_DISTRIBUTION_USER);
            if (!segmentIdResponse.isSuccess()) {
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
            Long distributionUserId = segmentIdResponse.getData();
            distributionUser.setDistributionUserId(distributionUserId);
        }
        distributionUserMapper.save(distributionUser);
    }

    @Override
    public void update(DistributionUser distributionUser) {
        distributionUserMapper.update(distributionUser);
    }

    @Override
    public void deleteById(Long distributionUserId) {
        distributionUserMapper.deleteById(distributionUserId);
    }

    @Override
    public PageVO<DistributionUserAchievementVO> achievementPage(PageDTO pageDTO, DistributionUserDTO distributionUserDTO, String userMobile) {
        return PageUtil.doPage(pageDTO, () -> distributionUserMapper.achievementPage(distributionUserDTO, userMobile));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDistributionStateAndBan(DistributionUserBan distributionUserBan) {
        //写入封禁记录
        distributionUserBan.setUpdateTime(new Date());
        //修改人
        distributionUserBan.setModifier(AuthUserContext.get().getUserId());
        distributionUserBanMapper.save(distributionUserBan);
        //更新分销员的状态
        DistributionUser beforeDistributionUser = distributionUserMapper.getByDistributionUserId(distributionUserBan.getDistributionUserId());
        DistributionUser distributionUser = new DistributionUser();
        distributionUser.setDistributionUserId(distributionUserBan.getDistributionUserId());
        distributionUser.setState(distributionUserBan.getState());
        distributionUser.setBindTime(beforeDistributionUser.getBindTime());
        //如果是暂时封禁[2]或永久封禁[-1]
        if (Objects.equals(DistributionUserStateEnum.BAN.value(), distributionUserBan.getState()) ||
                Objects.equals(DistributionUserStateEnum.PER_BAN.value(), distributionUserBan.getState())) {
            distributionUser.setStateRecord(distributionUserBan.getState());
            //绑定的用户失效
            distributionUserBindMapper.updateStateAndReasonByDistributionUserId(distributionUserBan.getDistributionUserId(), BindStateEnum.INVALID.value());
            //将商品分享记录设为失效
            distributionSpuBindMapper.updateStateByDistributionUserId(distributionUserBan.getDistributionUserId(), -1);
            //判断是否为永久封禁
            if (Objects.equals(DistributionUserStateEnum.PER_BAN.value(), distributionUserBan.getState())) {
                //将正在处理中的佣金订单冻结
                distributionUserIncomeMapper.updateStateByDistributionUserId(distributionUserBan.getDistributionUserId(),-1);
                //将distribution_user_wallet累计收益改为0
                distributionUserWalletMapper.updateAmountByDistributionUserId(distributionUserBan.getDistributionUserId());
                //将distribution_user上级id清空[清空该被永久封禁的分销员的下级关系]
                distributionUserMapper.updateParentIdById(distributionUserBan.getDistributionUserId());
                //提现记录为申请中的变为拒绝提现
                distributionWithdrawCashMapper.updateUserByDistributionUserId(distributionUserBan.getDistributionUserId());
            }
        }
        // 如果恢复成正常状态就不需要记录之前的状态了
        if (Objects.equals(distributionUserBan.getState(),DistributionUserStateEnum.NORMAL.value())){
            distributionUser.setStateRecord(null);
        }
        distributionUserMapper.updateStatus(distributionUser);
    }

    @Override
    public List<DistributionUser> getDistributionUserByIdCardNumberAndUserMobile(String identityCardNumber, String userMobile) {
        return distributionUserMapper.getDistributionUserByIdCardNumberAndUserMobile(identityCardNumber, userMobile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerDistributionUser(DistributionUser distributionUser) {
        DistributionUserVO dbDistributionUser = distributionUserMapper.getByUserId(distributionUser.getUserId());
        // 返回自增id
        Long distributionUserId;
        if (dbDistributionUser == null) {
            //存入分销员数据库
            this.save(distributionUser);
            distributionUserId = distributionUser.getDistributionUserId();
            //为这个分销员创建一个钱包
            DistributionUserWallet distributionUserWallet = new DistributionUserWallet();
            distributionUserWallet.setAccumulateAmount(Constant.ZERO_LONG);
            distributionUserWallet.setSettledAmount(Constant.ZERO_LONG);
            distributionUserWallet.setInvalidAmount(Constant.ZERO_LONG);
            distributionUserWallet.setVersion(0);
            distributionUserWallet.setDistributionUserId(distributionUserId);
            distributionUserWallet.setUnsettledAmount(Constant.ZERO_LONG);
            distributionUserWalletService.save(distributionUserWallet);
        } else {
            dbDistributionUser.setState(DistributionUserStateEnum.WAIT_AUDIT.value());
            distributionUser.setDistributionUserId(dbDistributionUser.getDistributionUserId());
            distributionUserMapper.update(distributionUser);
            distributionUserId = dbDistributionUser.getDistributionUserId();
        }

        //创建申请记录该用户是否为本店分销员
        DistributionAuditing distributionAuditing = new DistributionAuditing();
        distributionAuditing.setAuditingTime(new Date());
        distributionAuditing.setDistributionUserId(distributionUserId);
        distributionAuditing.setParentDistributionUserId(distributionUser.getParentId());

        //符合条件，自动通过
        if (Objects.equals(distributionUser.getState(), DistributionUserStateEnum.NORMAL.value())) {
            distributionAuditing.setRemarks("系统判断自动通过审核");
            distributionAuditing.setState(DistributionAuditStateEnum.PASSED.value());
        } else {
            distributionAuditing.setState(DistributionAuditStateEnum.UNAUDITED.value());
        }
        if (dbDistributionUser == null) {
            //存入申请记录
            distributionAuditingMapper.save(distributionAuditing);
        } else {
            DistributionAuditingVO dbAuditing = distributionAuditingMapper.getByDistributionUserId(distributionUserId);
            distributionAuditing.setAuditingId(dbAuditing.getAuditingId());
            distributionAuditingMapper.update(distributionAuditing);
        }
    }

    @Override
    public AchievementDataVO getAchievementDataById(Long distributionUserId) {
        AchievementDataVO achievementDataVO = distributionUserMapper.getAchievementDataById(distributionUserId);
        // 将金额转换成元
        BigDecimal settledAmount = achievementDataVO.getSettledAmount();
        BigDecimal unsettledAmount = achievementDataVO.getUnsettledAmount();
        BigDecimal accumulateAmount = achievementDataVO.getAccumulateAmount();
        BigDecimal invalidAmount = achievementDataVO.getInvalidAmount();
        achievementDataVO.setSettledAmount(PriceUtil.toDecimalPrice(settledAmount.longValue()));
        achievementDataVO.setUnsettledAmount(PriceUtil.toDecimalPrice(unsettledAmount.longValue()));
        achievementDataVO.setAccumulateAmount(PriceUtil.toDecimalPrice(accumulateAmount.longValue()));
        achievementDataVO.setInvalidAmount(PriceUtil.toDecimalPrice(invalidAmount.longValue()));
        return achievementDataVO;
    }

    @Override
    public PageVO<DistributionUserSimpleInfoVO> getPageDistributionUserSimpleInfoByParentUserId(PageDTO pageDTO, Long parentDistributionUserId) {
        return PageUtil.doPage(pageDTO, () -> distributionUserMapper.getPageDistributionUserSimpleInfoByParentUserId(parentDistributionUserId));
    }

    @Override
    public DistributionUserBanVO getLatestBanInfoByDistributionUserId(Long distributionUserId) {
        DistributionUserBanVO distributionUserBanVO = distributionUserBanMapper.getLatestBanInfoByDistributionUserId(distributionUserId);
        if (Objects.isNull(distributionUserBanVO)) {
            // 没有被封禁过，获取用户的状态信息
            DistributionUser distributionUser = distributionUserMapper.getByDistributionUserId(distributionUserId);
            if (Objects.isNull(distributionUser)) {
                throw new LuckException("找不到当前分销员");
            }
            distributionUserBanVO = new DistributionUserBanVO();
            distributionUserBanVO.setState(distributionUser.getState());
        }
        return distributionUserBanVO;
    }


    @Override
    @Cacheable(cacheNames = DistributionCacheNames.DISTRIBUTION_USER_ID, key = "#userId")
    public DistributionUserVO getByUserId(Long userId) {
        return distributionUserMapper.getByUserId(userId);
    }

    @Override
    @CacheEvict(cacheNames = DistributionCacheNames.DISTRIBUTION_USER_ID, key = "#userId")
    public void removeCacheByUserId(Long userId) {
    }
}
