package com.mall4j.cloud.distribution.service.impl;
import java.math.BigDecimal;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.distribution.constant.DistributionWalletBillTypeEnum;
import com.mall4j.cloud.distribution.dto.DistributionUserWalletDTO;
import com.mall4j.cloud.distribution.model.DistributionUserWallet;
import com.mall4j.cloud.distribution.mapper.DistributionUserWalletMapper;
import com.mall4j.cloud.distribution.model.DistributionUserWalletBill;
import com.mall4j.cloud.distribution.service.DistributionUserWalletBillService;
import com.mall4j.cloud.distribution.service.DistributionUserWalletService;
import com.mall4j.cloud.distribution.vo.DistributionUserWalletVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分销员钱包信息
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
@Service
public class DistributionUserWalletServiceImpl implements DistributionUserWalletService {

    @Autowired
    private DistributionUserWalletMapper distributionUserWalletMapper;
    @Autowired
    private DistributionUserWalletBillService distributionUserWalletBillService;

    @Override
    public PageVO<DistributionUserWallet> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionUserWalletMapper.list());
    }

    @Override
    public DistributionUserWallet getByWalletId(Long walletId) {
        return distributionUserWalletMapper.getByWalletId(walletId);
    }

    @Override
    public void save(DistributionUserWallet distributionUserWallet) {
        distributionUserWalletMapper.save(distributionUserWallet);
    }

    @Override
    public void update(DistributionUserWallet distributionUserWallet) {
        distributionUserWalletMapper.update(distributionUserWallet);
    }

    @Override
    public void deleteById(Long walletId) {
        distributionUserWalletMapper.deleteById(walletId);
    }

    @Override
    public PageVO<DistributionUserWalletVO> walletPage(PageDTO pageDTO, String userMobile) {
        PageVO<DistributionUserWalletVO> pageVO = PageUtil.doPage(pageDTO, () -> distributionUserWalletMapper.walletPage(userMobile));
        return pageVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWallet(DistributionUserWalletDTO distributionUserWalletDTO, Long userId) {
        Long walletId = distributionUserWalletDTO.getWalletId();
        DistributionUserWallet oldDistributionUserWallet = distributionUserWalletMapper.getByWalletId(walletId);
        DistributionUserWallet distributionUserWallet = new DistributionUserWallet();
        distributionUserWallet.setWalletId(walletId);
        // 修改后的金额
        BigDecimal unsettledAmount = distributionUserWalletDTO.getUnsettledAmount();
        BigDecimal settledAmount = distributionUserWalletDTO.getSettledAmount();
        BigDecimal invalidAmount = distributionUserWalletDTO.getInvalidAmount();
        BigDecimal accumulateAmount = distributionUserWalletDTO.getAccumulateAmount();
        // 设置修改后的金额
        distributionUserWallet.setUnsettledAmount(PriceUtil.toLongCent(unsettledAmount));
        distributionUserWallet.setSettledAmount(PriceUtil.toLongCent(settledAmount));
        distributionUserWallet.setInvalidAmount(PriceUtil.toLongCent(invalidAmount));
        distributionUserWallet.setAccumulateAmount(PriceUtil.toLongCent(accumulateAmount));

        // 增加钱包流水记录
        DistributionUserWalletBill distributionUserWalletBill = new DistributionUserWalletBill();
        distributionUserWalletBill.setWalletId(walletId);
        // 改变的金额
        distributionUserWalletBill.setUnsettledAmount(longSub(distributionUserWallet.getUnsettledAmount(),oldDistributionUserWallet.getUnsettledAmount()));
        distributionUserWalletBill.setSettledAmount(longSub(distributionUserWallet.getSettledAmount(),oldDistributionUserWallet.getSettledAmount()));
        distributionUserWalletBill.setInvalidAmount(longSub(distributionUserWallet.getInvalidAmount(),oldDistributionUserWallet.getInvalidAmount()));
        distributionUserWalletBill.setAccumulateAmount(longSub(distributionUserWallet.getAccumulateAmount(),oldDistributionUserWallet.getAccumulateAmount()));
        distributionUserWalletBill.setRemarksEn("Manual modification");
        distributionUserWalletBill.setRemarks("人工修改");
        // 变更后的金额
        distributionUserWalletBill.setUnsettledAmountAfter(distributionUserWallet.getUnsettledAmount());
        distributionUserWalletBill.setSettledAmountAfter(distributionUserWallet.getSettledAmount());
        distributionUserWalletBill.setInvalidAmountAfter(distributionUserWallet.getInvalidAmount());
        distributionUserWalletBill.setAccumulateAmountAfter(distributionUserWallet.getAccumulateAmount());
        distributionUserWalletBill.setType(DistributionWalletBillTypeEnum.USER.value());
        distributionUserWalletBill.setModifier(userId);

        distributionUserWalletMapper.update(distributionUserWallet);
        distributionUserWalletBillService.save(distributionUserWalletBill);
    }

    @Override
    public DistributionUserWallet getByDistributionUserId(Long distributionUserId) {
        return distributionUserWalletMapper.getByDistributionUserId(distributionUserId);
    }

    @Override
    public int updateWalletAmount(DistributionUserWallet updateWallet) {
        return distributionUserWalletMapper.updateWalletAmount(updateWallet);
    }

    private long longSub(long v1, long v2) {
        BigDecimal decimal = new BigDecimal(Long.valueOf(v1).toString()).subtract(new BigDecimal(Long.valueOf(v2).toString()));
        return decimal.longValue();
    }
}
