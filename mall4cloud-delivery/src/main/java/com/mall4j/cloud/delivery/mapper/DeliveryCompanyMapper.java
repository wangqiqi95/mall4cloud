package com.mall4j.cloud.delivery.mapper;

import com.mall4j.cloud.api.delivery.dto.DeliveryCompanyDTO;
import com.mall4j.cloud.api.delivery.vo.DeliveryCompanyVO;
import com.mall4j.cloud.delivery.model.DeliveryCompany;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物流公司
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
public interface DeliveryCompanyMapper {

	/**
	 * 获取物流公司列表
	 *
	 * @return 物流公司列表
	 */
	List<DeliveryCompanyVO> list();

	/**
	 * 根据物流公司id获取物流公司
	 *
	 * @param deliveryCompanyId 物流公司id
	 * @return 物流公司
	 */
	DeliveryCompanyVO getByDeliveryCompanyId(@Param("deliveryCompanyId") Long deliveryCompanyId);

	DeliveryCompanyVO getByDeliveryCompanyName(@Param("name") String name);

	/**
	 * 保存物流公司
	 *
	 * @param deliveryCompany 物流公司
	 */
	void save(@Param("deliveryCompany") DeliveryCompany deliveryCompany);

	/**
	 * 更新物流公司
	 *
	 * @param deliveryCompany 物流公司
	 */
	void update(@Param("deliveryCompany") DeliveryCompany deliveryCompany);

	/**
	 * 根据物流公司id删除物流公司
	 *
	 * @param deliveryCompanyId
	 */
	void deleteById(@Param("deliveryCompanyId") Long deliveryCompanyId);

	/**
	 * 按条件统计物流公司数量
	 *
	 * @param deliveryCompanyDTO
	 * @return
	 */
	Long count(@Param("deliveryCompany") DeliveryCompanyDTO deliveryCompanyDTO);

	/**
	 * 分页获取物流公司列表
	 *
	 * @param deliveryCompanyDTO
	 * @return
	 */
	List<DeliveryCompanyVO> listBySearch(@Param("deliveryCompany") DeliveryCompanyDTO deliveryCompanyDTO);

	/**
	 * 根据快递公司名称统计已存在的快递公司数量
	 * @param name 快递公司名称
	 * @param deliveryCompanyId
	 * @return 快递公司数量
	 */
    Integer countName(@Param("name") String name, @Param("deliveryCompanyId") Long deliveryCompanyId);
}
