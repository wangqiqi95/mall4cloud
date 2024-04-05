package com.mall4j.cloud.order.controller.platform;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.PhoneUtil;
import com.mall4j.cloud.api.distribution.feign.DistributionJointVentureCommissionFeignClient;
import com.mall4j.cloud.api.order.dto.DistributionJointVentureOrderSearchDTO;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.vo.EsOrderVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.vo.DistributionJointVentureOrderPreApplyInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 联营分佣订单controller
 *
 * @author Zhang Fan
 * @date 2022/8/8 10:09
 */
@Slf4j
@RestController("platformDistributionJointVentureCommissionOrderController")
@RequestMapping("/p/distribution_joint_venture_order")
@Api(tags = "联营分佣订单")
public class DistributionJointVentureCommissionOrderController {

    @Value("${mall4cloud.expose.permission:}")
    private Boolean permission;

    @Autowired
    private DistributionJointVentureCommissionFeignClient jointVentureCommissionFeignClient;
    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private OrderService orderService;

    /**
     * 分页获取
     */
    @ApiOperation("分页")
    @PostMapping("/page")
    public ServerResponseEntity<PageVO<EsOrderVO>> page(@Valid @RequestBody DistributionJointVentureOrderSearchDTO searchDTO) {
        // 获取客户的门店列表
        if (CollectionUtils.isEmpty(searchDTO.getStoreIdList())) {
            ServerResponseEntity<List<Long>> customerStoresResponse = jointVentureCommissionFeignClient.getCustomerStoreIdListByCustomerId(searchDTO.getJointVentureCommissionCustomerId());
            if (CollectionUtils.isEmpty(customerStoresResponse.getData())) {
                throw new LuckException("获取联营客户门店失败");
            }
            searchDTO.setStoreIdList(customerStoresResponse.getData());
        }
        PageVO<EsOrderVO> orderPage = orderService.distributionJointVentureOrderPage(searchDTO);
        List<Long> storeidList = orderPage.getList().stream().filter(s -> s != null).map(EsOrderVO::getStoreId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<StoreVO>> storesResponse = storeFeignClient.listByStoreIdList(storeidList);
        Map<Long, StoreVO> storeMaps = new HashMap<>();
        if (storesResponse != null && storesResponse.isSuccess() && storesResponse.getData().size() > 0) {
            storeMaps = storesResponse.getData().stream().collect(Collectors.toMap(StoreVO::getStoreId, p -> p));
        }


        /**
         * 查询用户列表
         */
        List<Long> useridList = orderPage.getList().stream().map(EsOrderVO::getUserId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<UserApiVO>> usersResponse = userFeignClient.getUserBypByUserIds(useridList);
        Map<Long, UserApiVO> userMaps = new HashMap<>();
        if (usersResponse != null && usersResponse.isSuccess() && usersResponse.getData().size() > 0) {
            userMaps = usersResponse.getData().stream().collect(Collectors.toMap(UserApiVO::getUserId, p -> p));
        }

        for (EsOrderVO esOrderVO : orderPage.getList()) {
            StoreVO storeVO = storeMaps.get(esOrderVO.getStoreId());
            if (storeVO != null) {
                esOrderVO.setStoreName(storeVO.getName());
            }
            //用户信息
            UserApiVO user = userMaps.get(esOrderVO.getUserId());
            if (user != null) {
                esOrderVO.setUserName(user.getNickName());
                esOrderVO.setUserMobile(user.getPhone());
//                esOrderVO.setUserNo(user.getVipcode());
            }
        }
        if (BooleanUtil.isFalse(permission)) {
            for (EsOrderVO esOrderVO : orderPage.getList()) {
                esOrderVO.setMobile(PhoneUtil.hideBetween(esOrderVO.getMobile()).toString());
            }
        }
        return ServerResponseEntity.success(orderPage);
    }

    /**
     * 获取待申请信息
     */
    @ApiOperation("获取待申请信息")
    @GetMapping("/getPreApplyInfo")
    public ServerResponseEntity<DistributionJointVentureOrderPreApplyInfoVO> getPreApplyInfo(@Valid DistributionJointVentureOrderSearchDTO searchDTO) {
        // 获取客户的门店列表
        if (CollectionUtils.isEmpty(searchDTO.getStoreIdList())) {
            ServerResponseEntity<List<Long>> customerStoresResponse = jointVentureCommissionFeignClient.getCustomerStoreIdListByCustomerId(searchDTO.getJointVentureCommissionCustomerId());
            if (CollectionUtils.isEmpty(customerStoresResponse.getData())) {
                throw new LuckException("获取联营客户门店失败");
            }
            searchDTO.setStoreIdList(customerStoresResponse.getData());
        }
        DistributionJointVentureOrderPreApplyInfoVO applyInfoVO = orderService.countDistributionJointVentureOrderPreApplyInfo(searchDTO);
        return ServerResponseEntity.success(applyInfoVO);
    }
}
