package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.CrmVipInfo1;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户_会员_会员基础信息
 * <p>
 * 入会时间段：数字，0,1,2,......12,13....23,
 *
 * @author FrozenWatermelon
 * @date 2022-04-05 14:44:13
 */
public interface CrmVipInfo1Mapper {

    /**
     * 获取客户_会员_会员基础信息
     * <p>
     * 入会时间段：数字，0,1,2,......12,13....23, 列表
     *
     * @return 客户_会员_会员基础信息
     * <p>
     * 入会时间段：数字，0,1,2,......12,13....23, 列表
     */
    List<CrmVipInfo1> list();

    List<CrmVipInfo1> listByTable(@Param("tableName")String tableName);

    /**
     * 根据客户_会员_会员基础信息
     * <p>
     * 入会时间段：数字，0,1,2,......12,13....23, id获取客户_会员_会员基础信息
     * <p>
     * 入会时间段：数字，0,1,2,......12,13....23,
     *
     * @param id 客户_会员_会员基础信息
     *           <p>
     *           入会时间段：数字，0,1,2,......12,13....23, id
     * @return 客户_会员_会员基础信息
     * <p>
     * 入会时间段：数字，0,1,2,......12,13....23,
     */
    CrmVipInfo1 getById(@Param("id") Long id);

    CrmVipInfo1 getByOldCode(@Param("oldCode") String oldCode);

    CrmVipInfo1 getByOldCode2(@Param("oldCode") String oldCode);

    CrmVipInfo1 getByOldCode3(@Param("oldCode") String oldCode);

    CrmVipInfo1 getByOldCode4(@Param("oldCode") String oldCode);

    /**
     * 保存客户_会员_会员基础信息
     * <p>
     * 入会时间段：数字，0,1,2,......12,13....23,
     *
     * @param crmVipInfo1 客户_会员_会员基础信息
     *                    <p>
     *                    入会时间段：数字，0,1,2,......12,13....23,
     */
    void save(@Param("crmVipInfo1") CrmVipInfo1 crmVipInfo1);

    /**
     * 更新客户_会员_会员基础信息
     * <p>
     * 入会时间段：数字，0,1,2,......12,13....23,
     *
     * @param crmVipInfo1 客户_会员_会员基础信息
     *                    <p>
     *                    入会时间段：数字，0,1,2,......12,13....23,
     */
    void update(@Param("crmVipInfo1") CrmVipInfo1 crmVipInfo1);

    /**
     * 根据客户_会员_会员基础信息
     * <p>
     * 入会时间段：数字，0,1,2,......12,13....23, id删除客户_会员_会员基础信息
     * <p>
     * 入会时间段：数字，0,1,2,......12,13....23,
     *
     * @param id
     */
    void deleteById(@Param("id") Long id);
}
