package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionJointVentureCommissionCustomerSearchDTO;
import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionCustomer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 联营分佣客户mapper
 *
 * @author Zhang Fan
 * @date 2022/8/4 11:21
 */
@Repository
public interface DistributionJointVentureCommissionCustomerMapper {

    /**
     * 获取联营分佣列表
     *
     * @param distributionJointVentureCommissionCustomerSearchDTO 查询参数
     * @return 联营分佣列表
     */
    List<DistributionJointVentureCommissionCustomer> list(@Param("searchDTO") DistributionJointVentureCommissionCustomerSearchDTO distributionJointVentureCommissionCustomerSearchDTO);

    /**
     * 根据联营分佣id获取联营分佣
     *
     * @param id 联营分佣id
     * @return 联营分佣
     */
    DistributionJointVentureCommissionCustomer getById(@Param("id") Long id);

    /**
     * 保存联营分佣
     *
     * @param distributionJointVentureCommissionCustomer 联营分佣
     */
    void save(@Param("entity") DistributionJointVentureCommissionCustomer distributionJointVentureCommissionCustomer);

    /**
     * 更新联营分佣
     *
     * @param distributionJointVentureCommissionCustomer 联营分佣
     */
    void update(@Param("entity") DistributionJointVentureCommissionCustomer distributionJointVentureCommissionCustomer);

    /**
     * 根据联营分佣id删除联营分佣
     *
     * @param id
     */
    void deleteById(@Param("id") Long id);

    DistributionJointVentureCommissionCustomer getByIdCard(@Param("idCard") String idCard);
}
