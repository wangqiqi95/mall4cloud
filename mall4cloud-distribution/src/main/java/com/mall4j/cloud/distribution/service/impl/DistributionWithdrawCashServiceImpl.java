package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.api.platform.vo.DistributionConfigApiVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.distribution.constant.DistributionWithdrawCashStateEnum;
import com.mall4j.cloud.distribution.dto.AppDistributionWithdrawCashDTO;
import com.mall4j.cloud.distribution.dto.DistributionWithdrawCashDTO;
import com.mall4j.cloud.distribution.dto.RangeTimeDTO;
import com.mall4j.cloud.distribution.model.DistributionUserWallet;
import com.mall4j.cloud.distribution.model.DistributionUserWalletBill;
import com.mall4j.cloud.distribution.model.DistributionWithdrawCash;
import com.mall4j.cloud.distribution.mapper.DistributionWithdrawCashMapper;
import com.mall4j.cloud.distribution.service.DistributionConfigService;
import com.mall4j.cloud.distribution.service.DistributionUserWalletBillService;
import com.mall4j.cloud.distribution.service.DistributionUserWalletService;
import com.mall4j.cloud.distribution.service.DistributionWithdrawCashService;
import com.mall4j.cloud.distribution.vo.AppDistributionWithdrawCashVO;
import com.mall4j.cloud.distribution.vo.DistributionUserVO;
import com.mall4j.cloud.distribution.vo.DistributionWithdrawCashVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 分销员提现记录
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
@Service
public class DistributionWithdrawCashServiceImpl implements DistributionWithdrawCashService {

    @Autowired
    private DistributionWithdrawCashMapper distributionWithdrawCashMapper;
    @Autowired
    private DistributionUserWalletService distributionUserWalletService;
    @Autowired
    private DistributionConfigService distributionConfigService;
    @Autowired
    private DistributionUserWalletBillService distributionUserWalletBillService;
    @Autowired
    private SegmentFeignClient segmentFeignClient;

    @Override
    public PageVO<DistributionWithdrawCashVO> page(PageDTO pageDTO, String userMobile, DistributionWithdrawCashDTO distributionWithdrawCashDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionWithdrawCashMapper.withdrawCashPage(userMobile,distributionWithdrawCashDTO));
    }

    @Override
    public DistributionWithdrawCash getByWithdrawCashId(Long withdrawCashId) {
        return distributionWithdrawCashMapper.getByWithdrawCashId(withdrawCashId);
    }

    @Override
    public void save(DistributionWithdrawCash distributionWithdrawCash) {
        distributionWithdrawCashMapper.save(distributionWithdrawCash);
    }

    @Override
    public void update(DistributionWithdrawCash distributionWithdrawCash) {
        distributionWithdrawCashMapper.update(distributionWithdrawCash);
    }

    @Override
    public void deleteById(Long withdrawCashId) {
        distributionWithdrawCashMapper.deleteById(withdrawCashId);
    }

    @Override
    public PageVO<AppDistributionWithdrawCashVO> pageByDistributionUserId(PageDTO pageDTO, Long distributionUserId) {
        return PageUtil.doPage(pageDTO, () -> distributionWithdrawCashMapper.pageByDistributionUserId(distributionUserId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void apply(AppDistributionWithdrawCashDTO distributionWithdrawCashDTO, DistributionUserVO distributionUserVO) {
        // 获取用户的钱包数据
        Long distributionUserId = distributionUserVO.getDistributionUserId();
        DistributionUserWallet distributionUserWallet = distributionUserWalletService.getByDistributionUserId(distributionUserId);
        // 获取店铺提现设置
        DistributionConfigApiVO distributionConfig = distributionConfigService.getDistributionConfig();
        BigDecimal amount = distributionWithdrawCashDTO.getAmount();

        //结算提现金额是否超出限制
        if (amount.compareTo(distributionConfig.getAmountMax()) > 0) {
            // 提现金额大于最大提现金额
            throw new LuckException("提现金额大于最大提现金额");
        }
        if (amount.compareTo(distributionConfig.getAmountMin()) < 0) {
            // 提现金额小于最小提现金额
            throw new LuckException("提现金额小于最小提现金额");
        }
        //获取店铺设置的提现频率算出时间区间
        Calendar calendar = Calendar.getInstance();
        if(Objects.equals(distributionConfig.getFrequency(), Constant.FREQUENCY_DAY)) {
            //每月的第一天
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        } else {
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - distributionConfig.getFrequency());
        }
        //将小时至0
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        calendar.set(Calendar.MINUTE, 0);
        //将秒至0
        calendar.set(Calendar.SECOND,0);
        //将毫秒至0
        calendar.set(Calendar.MILLISECOND, 0);
        RangeTimeDTO rangeTime = new RangeTimeDTO(calendar.getTime(), new Date());
        Integer number = distributionConfig.getNumber();
        //获取用户最近的提现次数，判断是否能够提现
        Integer count = distributionWithdrawCashMapper.getCountByRangeTimeAndDistributionUserId(rangeTime, distributionUserId);
        if (count >= number) {
            if (Objects.equals(distributionConfig.getFrequency(), Constant.FREQUENCY_DAY)){
                // 提现次数为每月 number 次
                throw new LuckException("提现次数为每月" + number + "次");
            } else {
                // 提现次数为 天number次
                throw new LuckException("提现次数为" + distributionConfig.getFrequency() + "天" + number + "次");
            }
        }
        //判断提现金额 是否大于钱包金额
        BigDecimal settledAmount = PriceUtil.toDecimalPrice(distributionUserWallet.getSettledAmount());
        if ( amount.compareTo(settledAmount) > 0) {
            // 提现失败,余额不足
            throw new LuckException("提现失败,余额不足");
        }
        //扣除可提现余额
        distributionUserWallet.setSettledAmount(PriceUtil.toLongCent(settledAmount.subtract(amount)));

        //插入一条提现记录
        Date now = new Date();
        DistributionWithdrawCash distributionWithdrawCash = new DistributionWithdrawCash();
        distributionWithdrawCash.setCreateTime(now);
        distributionWithdrawCash.setWalletId(distributionUserWallet.getWalletId());
        distributionWithdrawCash.setAmount(PriceUtil.toLongCent(amount));
        // 提现流水号
        ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_DISTRIBUTION_WITHDRAW_ORDER);
        if (!segmentIdResponse.isSuccess()) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
        Long merchantOrderId = segmentIdResponse.getData();
        distributionWithdrawCash.setMerchantOrderId(merchantOrderId.toString());
        distributionWithdrawCash.setState(DistributionWithdrawCashStateEnum.APPLY.value());
        distributionWithdrawCash.setMoneyFlow(1);
        distributionWithdrawCash.setVersion(0);
        distributionWithdrawCash.setUpdateTime(now);
        // 暂时没有手续费
        distributionWithdrawCash.setFee(Constant.ZERO_LONG);

        //设置为手动提现
        distributionWithdrawCash.setType(0);

        // 存入提现记录
        distributionWithdrawCashMapper.save(distributionWithdrawCash);

        // 增加钱包流水记录
        DistributionUserWalletBill distributionUserWalletBill = new DistributionUserWalletBill(distributionUserWallet, "用户提现","User withdrawals", Constant.ZERO_LONG, -PriceUtil.toLongCent(amount), Constant.ZERO_LONG, Constant.ZERO_LONG, 0);
        distributionUserWalletBillService.save(distributionUserWalletBill);

        // 修改钱包
        DistributionUserWallet updateWallet = new DistributionUserWallet();
        updateWallet.setWalletId(distributionUserWallet.getWalletId());
        updateWallet.setSettledAmount(distributionUserWallet.getSettledAmount());
        distributionUserWalletService.update(updateWallet);

    }

    @Override
    public Integer getCountByRangeTimeAndDistributionUserId(RangeTimeDTO rangeTimeDTO, Long distributionUserId) {
        return distributionWithdrawCashMapper.getCountByRangeTimeAndDistributionUserId(rangeTimeDTO, distributionUserId);
    }

    @Override
    public BigDecimal getUserTotalWithdrawCash(Long walletId) {
        BigDecimal total = distributionWithdrawCashMapper.getUserTotalWithdrawCash(walletId);
        return PriceUtil.toDecimalPrice(total.longValue());
    }
}
