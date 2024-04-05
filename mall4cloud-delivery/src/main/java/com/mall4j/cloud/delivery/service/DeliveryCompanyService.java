package com.mall4j.cloud.delivery.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.mall4j.cloud.api.delivery.vo.DeliveryCompanyVO;
import com.mall4j.cloud.api.delivery.vo.DeliveryInfoVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.api.delivery.dto.DeliveryCompanyDTO;
import com.mall4j.cloud.delivery.model.DeliveryCompany;
import org.apache.ibatis.annotations.Param;

/**
 * 物流公司
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
public interface DeliveryCompanyService {

	/**
	 * 获取物流公司列表
	 * @return 物流公司列表数据
	 */
	List<DeliveryCompanyVO> list();

	/**
	 * 根据物流公司id获取物流公司
	 *
	 * @param deliveryCompanyId 物流公司id
	 * @return 物流公司
	 */
	DeliveryCompanyVO getByDeliveryCompanyId(Long deliveryCompanyId);

	DeliveryCompanyVO getByDeliveryCompanyName(String name);

	/**
	 * 保存物流公司
	 * @param deliveryCompany 物流公司
	 */
	void save(DeliveryCompany deliveryCompany);

	/**
	 * 更新物流公司
	 * @param deliveryCompany 物流公司
	 */
	void update(DeliveryCompany deliveryCompany);

	/**
	 * 根据物流公司id删除物流公司
	 * @param deliveryCompanyId
	 */
	void deleteById(Long deliveryCompanyId);

	/**
	 * 分页获取物流公司列表数据
	 * @param pageDTO
	 * @param deliveryCompanyDTO
	 * @return
	 */
    PageVO<DeliveryCompanyVO> page(PageDTO pageDTO, DeliveryCompanyDTO deliveryCompanyDTO);

	/**
	 * 根据物流ID与物流单号查找物流信息
	 * @param dvyId 物流ID
	 * @param expNo 物流单号
	 * @param consigneeMobile 收件人手机号
	 * @return 物流信息
	 * @throws UnsupportedEncodingException
	 */
	DeliveryInfoVO query(Long dvyId, String expNo, String consigneeMobile) throws UnsupportedEncodingException;
}
