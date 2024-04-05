package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionJointVentureCommissionCustomerDTO;
import com.mall4j.cloud.distribution.dto.DistributionJointVentureCommissionCustomerSearchDTO;
import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionCustomer;

/**
 * 联营分佣客户service
 *
 * @author Zhang Fan
 * @date 2022/8/4 10:36
 */
public interface DistributionJointVentureCommissionCustomerService {

    /**
     * 分页获取联营客户列表
     *
     * @param pageDTO                                             分页参数
     * @param distributionJointVentureCommissionCustomerSearchDTO 查询参数
     * @return 联营客户分页数据
     */
    PageVO<DistributionJointVentureCommissionCustomer> page(PageDTO pageDTO, DistributionJointVentureCommissionCustomerSearchDTO distributionJointVentureCommissionCustomerSearchDTO);

    /**
     * 根据id获取联营客户
     *
     * @param id 联营客户id
     * @return 联营客户
     */
    DistributionJointVentureCommissionCustomer getById(Long id);

    /**
     * 根据身份证号获取联营客户
     *
     * @param idCard 身份证号码
     * @return
     */
    DistributionJointVentureCommissionCustomer getByIdCard(String idCard);

    /**
     * 保存联营客户
     *
     * @param distributionJointVentureCommissionCustomerDTO 联营客户
     */
    void save(DistributionJointVentureCommissionCustomerDTO distributionJointVentureCommissionCustomerDTO);

    /**
     * 更新联营客户
     *
     * @param distributionJointVentureCommissionCustomerDTO 联营客户
     */
    void update(DistributionJointVentureCommissionCustomerDTO distributionJointVentureCommissionCustomerDTO);

    /**
     * 更新联营客户状态
     *
     * @param id     联营客户id
     * @param status 联营客户状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 根据联营客户id删除联营客户
     *
     * @param id 联营客户id
     */
    void deleteById(Long id);

}
