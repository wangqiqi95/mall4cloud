package com.mall4j.cloud.distribution.vo;

import java.util.Date;
import java.util.List;

/**
 * 分月份分销提现记录信息
 * @author cl
 * @date 2021-08-27 16:20:02
 */
public class AppDistributionWithdrawCashLogVO {

    /**
     * 年月的时间
     */
    private Date date;

    /**
     * 分销收入记录信息VO
     */
    private List<AppDistributionWithdrawCashVO> distributionWithdrawCashList;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<AppDistributionWithdrawCashVO> getDistributionWithdrawCashList() {
        return distributionWithdrawCashList;
    }

    public void setDistributionWithdrawCashList(List<AppDistributionWithdrawCashVO> distributionWithdrawCashList) {
        this.distributionWithdrawCashList = distributionWithdrawCashList;
    }

    @Override
    public String toString() {
        return "AppDistributionWithdrawCashLogVO{" +
                "date=" + date +
                ", distributionWithdrawCashList=" + distributionWithdrawCashList +
                '}';
    }
}
