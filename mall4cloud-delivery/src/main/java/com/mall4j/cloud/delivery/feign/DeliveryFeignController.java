package com.mall4j.cloud.delivery.feign;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.delivery.bo.TransfeeBO;
import com.mall4j.cloud.api.delivery.bo.TransfeeFreeBO;
import com.mall4j.cloud.api.delivery.bo.TransportBO;
import com.mall4j.cloud.delivery.vo.TransfeeFreeVO;
import com.mall4j.cloud.delivery.vo.TransfeeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.mall4j.cloud.api.delivery.dto.CalculateAndGetDeliverInfoDTO;
import com.mall4j.cloud.api.delivery.dto.ChangeOrderAddrDTO;
import com.mall4j.cloud.api.delivery.dto.DeliveryOrderDTO;
import com.mall4j.cloud.api.delivery.feign.DeliveryFeignClient;
import com.mall4j.cloud.api.delivery.vo.DeliveryCompanyExcelVO;
import com.mall4j.cloud.api.delivery.vo.DeliveryCompanyVO;
import com.mall4j.cloud.api.delivery.vo.DeliveryOrderFeignVO;
import com.mall4j.cloud.api.delivery.vo.ShopTransportVO;
import com.mall4j.cloud.api.user.feign.UserAddrFeignClient;
import com.mall4j.cloud.api.user.feign.UserConsigneeFeignClient;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.constant.DeliveryType;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.order.vo.UserAddrVO;
import com.mall4j.cloud.common.order.vo.UserConsigneeVO;
import com.mall4j.cloud.common.order.vo.UserDeliveryInfoVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.delivery.manager.TransportManager;
import com.mall4j.cloud.delivery.service.DeliveryCompanyService;
import com.mall4j.cloud.delivery.service.DeliveryOrderService;
import com.mall4j.cloud.delivery.service.SameCityService;
import com.mall4j.cloud.delivery.service.TransportService;
import com.mall4j.cloud.delivery.vo.TransportVO;

import ma.glasnost.orika.MapperFacade;

/**
 * @author FrozenWatermelon
 * @date 2020/12/14
 */
@RestController
@Slf4j
public class DeliveryFeignController implements DeliveryFeignClient {


    @Autowired
    private UserAddrFeignClient userAddrFeignClient;

    @Autowired
    private UserConsigneeFeignClient userConsigneeFeignClient;

    @Autowired
    private SameCityService sameCityService;

    @Autowired
    private TransportManager transportManager;

    @Autowired
    private TransportService transportService;

    @Autowired
    private DeliveryOrderService deliveryOrderService;

    @Autowired
    private DeliveryCompanyService deliveryCompanyService;

    @Autowired
    private MapperFacade mapperFacade;

    private final static long TWO = -2;


    @Override
    public ServerResponseEntity<UserDeliveryInfoVO> calculateAndGetDeliverInfo(CalculateAndGetDeliverInfoDTO param) {
        UserDeliveryInfoVO userDeliveryInfo = new UserDeliveryInfoVO();

        // 如果是自提的话，查询出用户使用的自提用户信息
        if (Objects.equals(param.getDvyType(), DeliveryType.STATION.value())) {
            ServerResponseEntity<UserConsigneeVO> userConsigneeResponse = userConsigneeFeignClient.getUseConsignee();

            if (!userConsigneeResponse.isSuccess()) {
                return ServerResponseEntity.transform(userConsigneeResponse);
            }
            userDeliveryInfo.setUserConsignee(userConsigneeResponse.getData());
            return ServerResponseEntity.success(userDeliveryInfo);
        }

        // 如果是物流配送，就获取用户的地址信息

        ServerResponseEntity<UserAddrVO> userAddrResponse = userAddrFeignClient.getUserAddrByAddrId(param.getAddrId());
        if (!userAddrResponse.isSuccess()) {
            return ServerResponseEntity.transform(userAddrResponse);
        }
        UserAddrVO userAddr = userAddrResponse.getData();
        if (userAddr == null) {
            userDeliveryInfo.setTotalTransfee(0L);
            userDeliveryInfo.setShopCityStatus(-1);
            return ServerResponseEntity.success(userDeliveryInfo);
        }


        userDeliveryInfo.setUserAddr(userAddr);


        long transfee = 0L;

        if (Objects.equals(param.getDvyType(), DeliveryType.SAME_CITY.value())) {
            transfee = sameCityService.calculateTransFee(param.getShopCartItems(), userAddr);
        }


        //快递运费计算
        if (Objects.equals(param.getDvyType(), DeliveryType.DELIVERY.value())) {
            // 将所有订单传入处理，计算运费
            transfee = transportManager.calculateTransfee(param.getShopCartItems(), userAddr, userDeliveryInfo);
        }
        long actualTransfee = Math.max(transfee, 0);
        userDeliveryInfo.setTotalTransfee(actualTransfee);
        userDeliveryInfo.setShopCityStatus(transfee >= 0 ? 1 : (int) transfee);
        return ServerResponseEntity.success(userDeliveryInfo);
    }

    @Override
    public ServerResponseEntity<UserDeliveryInfoVO> staffCalculateAndGetDeliverInfo(CalculateAndGetDeliverInfoDTO param) {
        UserDeliveryInfoVO userDeliveryInfo = new UserDeliveryInfoVO();

        // 如果是自提的话，查询出用户使用的自提用户信息
        if (Objects.equals(param.getDvyType(), DeliveryType.STATION.value())) {
            ServerResponseEntity<UserConsigneeVO> userConsigneeResponse = userConsigneeFeignClient.getStaffUseConsignee(param.getUserId());

            if (!userConsigneeResponse.isSuccess()) {
                return ServerResponseEntity.transform(userConsigneeResponse);
            }
            userDeliveryInfo.setUserConsignee(userConsigneeResponse.getData());
            return ServerResponseEntity.success(userDeliveryInfo);
        }

        // 如果是物流配送，就获取用户的地址信息

        ServerResponseEntity<UserAddrVO> userAddrResponse = userAddrFeignClient.getStaffUserAddrByAddrId(param.getAddrId(), param.getUserId());
        if (!userAddrResponse.isSuccess()) {
            return ServerResponseEntity.transform(userAddrResponse);
        }
        UserAddrVO userAddr = userAddrResponse.getData();
        if (userAddr == null) {
            userDeliveryInfo.setTotalTransfee(0L);
            userDeliveryInfo.setShopCityStatus(-1);
            return ServerResponseEntity.success(userDeliveryInfo);
        }


        userDeliveryInfo.setUserAddr(userAddr);


        long transfee = 0L;

        if (Objects.equals(param.getDvyType(), DeliveryType.SAME_CITY.value())) {
            transfee = sameCityService.calculateTransFee(param.getShopCartItems(), userAddr);
        }


        //快递运费计算
        if (Objects.equals(param.getDvyType(), DeliveryType.DELIVERY.value())) {
            // 将所有订单传入处理，计算运费
            transfee = transportManager.calculateTransfee(param.getShopCartItems(), userAddr, userDeliveryInfo);
        }
        long actualTransfee = Math.max(transfee, 0);
        userDeliveryInfo.setTotalTransfee(actualTransfee);
        userDeliveryInfo.setShopCityStatus(transfee >= 0 ? 1 : (int) transfee);
        return ServerResponseEntity.success(userDeliveryInfo);
    }

    @Override
    public ServerResponseEntity<List<DeliveryOrderFeignVO>> getByDeliveryByOrderId(Long orderId) throws UnsupportedEncodingException {
        return ServerResponseEntity.success(deliveryOrderService.getByDeliveryByOrderId(orderId));
    }

    @Override
    public ServerResponseEntity<Void> saveDeliveryInfo(DeliveryOrderDTO deliveryOrderDTO) {
        deliveryOrderService.saveDeliveryInfo(deliveryOrderDTO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<ShopTransportVO>> listTransportByShopId(Long shopId) {
        List<TransportVO> list = transportService.listTransport(shopId);
        return ServerResponseEntity.success(mapperFacade.mapAsList(list, ShopTransportVO.class));
    }

    @Override
    public ServerResponseEntity<List<ShopTransportVO>> listTransportByShopIdInsider(Long shopId) {
        List<TransportVO> list = transportService.listTransport(shopId);
        return ServerResponseEntity.success(mapperFacade.mapAsList(list, ShopTransportVO.class));
    }

    @Override
    public ServerResponseEntity<List<DeliveryCompanyExcelVO>> listDeliveryCompany() {
        List<DeliveryCompanyVO> list = deliveryCompanyService.list();
        return ServerResponseEntity.success(mapperFacade.mapAsList(list, DeliveryCompanyExcelVO.class));
    }

    @Override
    public ServerResponseEntity<Double> getOrderChangeAddrAmount(ChangeOrderAddrDTO param) {
        UserAddrVO userAddr = param.getUserAddrVO();
        double changeAmount;
        Long freightAmount = param.getFreightAmount();
        List<ShopCartItemVO> shopCartItems = param.getShopCartItems();
        long transFee = 0;
        if (Objects.equals(param.getDvyType(), DeliveryType.DELIVERY.value())) {
            // 物流配送
            transFee = transportManager.calculateTransfee(shopCartItems, userAddr, new UserDeliveryInfoVO());
        } else if (Objects.equals(param.getDvyType(), DeliveryType.SAME_CITY.value())) {
            // 同城配送
            transFee = sameCityService.calculateTransFee(shopCartItems, userAddr);
            if (transFee == TWO) {
                throw new LuckException("当前店铺未开启同城配送!");
            } else if (transFee == -1) {
                throw new LuckException("超出当前设置的配送距离!");
            }
        }
        changeAmount = Arith.sub(transFee, freightAmount);
        return ServerResponseEntity.success(changeAmount);
    }
    @Override
    public ServerResponseEntity<TransportBO> getByTransportId(Long transportId) {
        TransportVO transportVO = transportService.getTransportAndAllItemsById(transportId);
        TransportBO transportBO = new TransportBO();
        
        List<TransfeeFreeVO> transFeeFrees = transportVO.getTransFeeFrees();
        List<TransfeeFreeBO> transfeeFreeBOS = BeanUtil.copyToList(transFeeFrees, TransfeeFreeBO.class);
    
        List<TransfeeVO> transFees = transportVO.getTransFees();
        List<TransfeeBO> transfeeBOS = BeanUtil.copyToList(transFees, TransfeeBO.class);
    
        BeanUtils.copyProperties(transportVO ,transportBO);
        transportBO.setTransFeeFrees(transfeeFreeBOS);
        transportBO.setTransFees(transfeeBOS);
    
        return ServerResponseEntity.success(transportBO);
    }
}
