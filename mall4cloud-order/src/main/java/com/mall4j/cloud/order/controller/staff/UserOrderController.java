package com.mall4j.cloud.order.controller.staff;

import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.dto.OrderSelectDto;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmOrderFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.vo.OrderSelectVo;
import com.mall4j.cloud.api.user.dto.UserQueryDTO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.dto.OrderSearchDTO;
import com.mall4j.cloud.common.order.vo.EsOrderVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.order.dto.app.DistributionSalesStatDTO;
import com.mall4j.cloud.order.dto.app.OrderRefundPageDTO;
import com.mall4j.cloud.order.service.OrderRefundService;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.vo.OrderRefundVO;
import com.mall4j.cloud.order.vo.UserSalesStatVO;
import io.seata.common.util.CollectionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author ZengFanChang
 * @Date 2022/02/11
 */

@RestController("staffUserOrderController")
@RequestMapping("/s/userOrder")
@Api(tags = "导购小程序-会员订单")
public class UserOrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRefundService orderRefundService;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private CrmOrderFeignClient crmOrderFeignClient;


    @ApiOperation("会员购买分析")
    @PostMapping("/getUserBuyAnalyze")
    public ServerResponseEntity<UserSalesStatVO> getUserBuyAnalyze(@RequestBody UserQueryDTO queryDTO) {
        UserSalesStatVO vo = new UserSalesStatVO();
        queryDTO.setStaffId(AuthUserContext.get().getUserId());
        DistributionSalesStatDTO distributionSalesStatDTO = new DistributionSalesStatDTO();
        distributionSalesStatDTO.setStartTime(queryDTO.getStartTime());
        distributionSalesStatDTO.setEndTime(queryDTO.getEndTime());
        queryDTO.setStartTime(null);
        queryDTO.setEndTime(null);
        ServerResponseEntity<List<UserApiVO>> userListData = userFeignClient.listUserByStaff(queryDTO);
        if (!userListData.isSuccess() || CollectionUtils.isEmpty(userListData.getData())) {
            return ServerResponseEntity.success(vo);
        }
        List<Long> userIds = userListData.getData().stream().map(UserApiVO::getUserId).distinct().collect(Collectors.toList());
        distributionSalesStatDTO.setUserIds(userIds);
        vo.setTotalSales(orderService.storeDistributionSalesStat(distributionSalesStatDTO));
        vo.setOrderNum(orderService.countDistributionOrderNum(distributionSalesStatDTO));
        vo.setRefundNum(orderRefundService.countDistributionRefundNum(distributionSalesStatDTO));
        vo.setBuyUserNum(orderService.countDistributionOrderUserNum(distributionSalesStatDTO));
        if (null != vo.getBuyUserNum() && vo.getBuyUserNum() > 0) {
            vo.setCustomerOrderPrice(vo.getTotalSales() / vo.getBuyUserNum());
            vo.setCustomerOrderNum(orderService.countDistributionOrderSpuNum(distributionSalesStatDTO) / vo.getBuyUserNum());
            vo.setNotBuyUserNum(userIds.size() - vo.getBuyUserNum());
        } else {
            vo.setNotBuyUserNum(userIds.size());
        }
        return ServerResponseEntity.success(vo);
    }


    @ApiOperation("本月生日会员购买分析")
    @PostMapping("/getBirthdayUserBuyAnalyze")
    public ServerResponseEntity<UserSalesStatVO> getBirthdayUserBuyAnalyze(@RequestBody UserQueryDTO queryDTO) {
        UserSalesStatVO vo = new UserSalesStatVO();
        queryDTO.setStaffId(AuthUserContext.get().getUserId());
        queryDTO.setBirthStartDate(DateUtils.getMonthFirstOrLastDay(new Date(), 0));
        queryDTO.setBirthEndDate(DateUtils.getMonthFirstOrLastDay(new Date(), 1));
        ServerResponseEntity<List<UserApiVO>> userListData = userFeignClient.listUserByStaff(queryDTO);
        if (!userListData.isSuccess() || CollectionUtils.isEmpty(userListData.getData())) {
            return ServerResponseEntity.success(vo);
        }
        List<Long> userIds = userListData.getData().stream().map(UserApiVO::getUserId).distinct().collect(Collectors.toList());
        DistributionSalesStatDTO distributionSalesStatDTO = new DistributionSalesStatDTO();
        distributionSalesStatDTO.setUserIds(userIds);
        vo.setTotalSales(orderService.storeDistributionSalesStat(distributionSalesStatDTO));
        vo.setOrderNum(orderService.countDistributionOrderNum(distributionSalesStatDTO));
        vo.setBuyUserNum(orderService.countDistributionOrderUserNum(distributionSalesStatDTO));
        if (null != vo.getBuyUserNum() && vo.getBuyUserNum() > 0) {
            vo.setBuyRate(String.valueOf(new BigDecimal(vo.getBuyUserNum()).divide(new BigDecimal(userIds.size()), 2, BigDecimal.ROUND_HALF_UP)));
            vo.setNotBuyUserNum(userIds.size() - vo.getBuyUserNum());
        } else {
            vo.setNotBuyUserNum(userIds.size());
        }
        return ServerResponseEntity.success(vo);
    }
    

    @GetMapping("/orderPage")
    @ApiOperation(value = "订单列表", notes = "订单列表")
    public ServerResponseEntity<PageVO<EsOrderVO>> orderPage(@Valid PageDTO pageDTO, Long userId) {
        OrderSearchDTO orderSearchDTO = new OrderSearchDTO();
        orderSearchDTO.setUserId(userId);
        orderSearchDTO.setDeleteStatus(0);
        orderSearchDTO.setPageNum(pageDTO.getPageNum());
        orderSearchDTO.setPageSize(pageDTO.getPageSize());
        return ServerResponseEntity.success(orderService.orderPage(orderSearchDTO));
    }

    @GetMapping("/crmOrderPage")
    @ApiOperation(value = "crm-订单列表", notes = "crm-订单列表")
    public ServerResponseEntity<CrmPageResult<List<OrderSelectVo>>> crmOrderPage(@Valid PageDTO pageDTO, Long
            userId) {
        ServerResponseEntity<UserApiVO> userRep = userFeignClient.getInsiderUserData(userId);
        if (userRep.isSuccess()) {
            OrderSelectDto orderSelectDto = new OrderSelectDto();
            orderSelectDto.setPage_index(pageDTO.getPageNum());
            orderSelectDto.setPage_size(pageDTO.getPageSize());
            orderSelectDto.setMobile(userRep.getData().getPhone());
            return crmOrderFeignClient.orderSelect(orderSelectDto);
        }
        CrmPageResult<List<OrderSelectVo>> crmPageResult = new CrmPageResult<>();
        crmPageResult.setTotal_count(0);
        return ServerResponseEntity.success(crmPageResult);
    }


    @GetMapping("/refundPage")
    @ApiOperation(value = "退单列表", notes = "退单列表")
    public ServerResponseEntity<PageVO<OrderRefundVO>> refundPage(@Valid PageDTO pageDTO, Long userId) {
        OrderRefundPageDTO orderRefundPageDTO = new OrderRefundPageDTO();
        orderRefundPageDTO.setUserId(userId);
        return ServerResponseEntity.success(orderRefundService.page(pageDTO, orderRefundPageDTO));
    }


}
