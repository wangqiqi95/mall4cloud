package com.mall4j.cloud.api.delivery.feign;

import com.mall4j.cloud.api.delivery.bo.TransportBO;
import com.mall4j.cloud.api.delivery.dto.CalculateAndGetDeliverInfoDTO;
import com.mall4j.cloud.api.delivery.dto.ChangeOrderAddrDTO;
import com.mall4j.cloud.api.delivery.dto.DeliveryOrderDTO;
import com.mall4j.cloud.api.delivery.vo.DeliveryCompanyExcelVO;
import com.mall4j.cloud.api.delivery.vo.DeliveryOrderFeignVO;
import com.mall4j.cloud.api.delivery.vo.ShopTransportVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.order.vo.UserDeliveryInfoVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/12/14
 */
@FeignClient(value = "mall4cloud-delivery",contextId ="delivery")
public interface DeliveryFeignClient {

	/**
	 * 计算运费，并计算获取配送信息
	 * @param param 用户地址和订单信息
	 * @return void
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/delivery/calculateAndGetDeliverInfo")
	ServerResponseEntity<UserDeliveryInfoVO> calculateAndGetDeliverInfo(@RequestBody CalculateAndGetDeliverInfoDTO param);


	/**
	 * 计算运费，并计算获取配送信息
	 * @param param 用户地址和订单信息
	 * @return void
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/delivery/staffCalculateAndGetDeliverInfo")
	ServerResponseEntity<UserDeliveryInfoVO> staffCalculateAndGetDeliverInfo(@RequestBody CalculateAndGetDeliverInfoDTO param);

	/**
	 * 根据订单号获取包裹信息
	 * @param orderId 订单id
	 * @throws UnsupportedEncodingException 编码异常
	 * @return 包裹信息
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/delivery/getByDeliveryByOrderId")
	ServerResponseEntity<List<DeliveryOrderFeignVO>> getByDeliveryByOrderId(@RequestParam("orderId") Long orderId) throws UnsupportedEncodingException;

	/**
	 * 生成物流信息及保存物流与订单关联
	 * @param deliveryOrderDTO 订单发货信息
	 * @return void
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/delivery/saveDeliveryInfo")
	ServerResponseEntity<Void> saveDeliveryInfo(@RequestBody DeliveryOrderDTO deliveryOrderDTO);

	/**
	 * 获取店铺运费模板列表
	 * @param shopId 店铺id
	 * @return 运费模板列表
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/delivery/listTransportByShopId")
	ServerResponseEntity<List<ShopTransportVO>> listTransportByShopId(@RequestParam("shopId") Long shopId);

	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/delivery/listTransportByShopId")
	ServerResponseEntity<List<ShopTransportVO>> listTransportByShopIdInsider(@RequestParam(value = "shopId",required = false) Long shopId);

	/**
	 * 获取物流公司名称
	 * @return 物流公司名称
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/delivery/listDeliveryCompany")
	ServerResponseEntity<List<DeliveryCompanyExcelVO>> listDeliveryCompany();

	/**
	 * 计算订单修改地址后的运费更改价格
	 * @param orderAddrDTO
	 * @return
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/delivery/getOrderChangeAddrAmount")
	ServerResponseEntity<Double> getOrderChangeAddrAmount(@RequestBody ChangeOrderAddrDTO orderAddrDTO);
	
	/**
	 * 根据transportId获取运费模板
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/delivery/getTransportById")
	ServerResponseEntity<TransportBO> getByTransportId(@RequestParam("transportId") Long transportId);

}
