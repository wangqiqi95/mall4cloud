package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.i18n.LanguageEnum;
import com.mall4j.cloud.distribution.model.DistributionUserWalletBill;
import com.mall4j.cloud.distribution.mapper.DistributionUserWalletBillMapper;
import com.mall4j.cloud.distribution.service.DistributionUserWalletBillService;
import com.mall4j.cloud.distribution.vo.DistributionUserWalletBillVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

/**
 * 分销员钱包流水记录
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
@Service
public class DistributionUserWalletBillServiceImpl implements DistributionUserWalletBillService {

    @Autowired
    private DistributionUserWalletBillMapper distributionUserWalletBillMapper;

    @Override
    public PageVO<DistributionUserWalletBill> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionUserWalletBillMapper.list());
    }

    @Override
    public DistributionUserWalletBill getById(Long id) {
        return distributionUserWalletBillMapper.getById(id);
    }

    @Override
    public void save(DistributionUserWalletBill distributionUserWalletBill) {
        distributionUserWalletBillMapper.save(distributionUserWalletBill);
    }

    @Override
    public void update(DistributionUserWalletBill distributionUserWalletBill) {
        distributionUserWalletBillMapper.update(distributionUserWalletBill);
    }

    @Override
    public void deleteById(Long id) {
        distributionUserWalletBillMapper.deleteById(id);
    }

    @Override
    public PageVO<DistributionUserWalletBillVO> walletBillPage(PageDTO pageDTO, String userMobile) {
        PageVO<DistributionUserWalletBillVO> pageVO = PageUtil.doPage(pageDTO, () -> distributionUserWalletBillMapper.walletBillPage(userMobile));
        List<DistributionUserWalletBillVO> list = pageVO.getList();
        if (Objects.equals(I18nMessage.getLang(), LanguageEnum.LANGUAGE_EN.getLang())) {
            for (DistributionUserWalletBillVO walletBillVO : list) {
                if (walletBillVO.getRemarksEn() != null){
                    walletBillVO.setRemarks(walletBillVO.getRemarksEn());
                }
            }
            pageVO.setList(list);
        }
        return pageVO;
    }

    @Override
    public void saveBatch(List<DistributionUserWalletBill> distributionUserWalletBills) {
        if (CollUtil.isEmpty(distributionUserWalletBills)) {
            return;
        }
        distributionUserWalletBillMapper.saveBatch(distributionUserWalletBills);
    }
}
