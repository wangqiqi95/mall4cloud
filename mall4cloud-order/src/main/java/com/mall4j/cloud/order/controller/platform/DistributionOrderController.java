package com.mall4j.cloud.order.controller.platform;

import com.mall4j.cloud.api.distribution.dto.DistributionQueryDTO;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.vo.OrderDistributionExcelVO;
import com.mall4j.cloud.api.user.dto.DistributionUserQueryDTO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.DistributionUserVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.order.dto.app.DistributionSalesStatDTO;
import com.mall4j.cloud.order.service.DistributionOrderExcelService;
import com.mall4j.cloud.order.service.OrderExcelService;
import com.mall4j.cloud.order.service.OrderRefundService;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.vo.DistributionStatisticsVO;
import com.mall4j.cloud.order.vo.DistributionStoreStatisticsVO;
import io.seata.common.util.CollectionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController("platformDistributionOrderController")
@RequestMapping("/p/distribution_order")
@Api(tags = "分销数据-分销统计")
public class DistributionOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRefundService orderRefundService;

    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    OrderExcelService orderExcelService;

    @Autowired
    private DistributionOrderExcelService distributionOrderExcelService;


    @ApiOperation("分销/发展订单数据")
    @PostMapping("/pageDistributionOrders")
    public ServerResponseEntity<PageVO<EsOrderBO>> pageDistributionOrders(@Valid PageDTO pageDTO, @RequestBody DistributionQueryDTO queryDTO) {
        return ServerResponseEntity.success(orderService.pageDistributionOrders(pageDTO, queryDTO));
    }


    @ApiOperation("分销/发展订单数据导出")
    @PostMapping("/exportDistributionOrder")
    public ServerResponseEntity exportDistributionOrder(@RequestBody DistributionQueryDTO queryDTO, HttpServletResponse response){

        try {
             orderExcelService.excelDistributionOrderList(queryDTO);
            return ServerResponseEntity.success("操作成功，请转至下载中心下载");
        }catch (Exception e){
            log.error("导出分销/发展订单信息错误: "+e.getMessage(),e);
            return ServerResponseEntity.showFailMsg("操作失败");
        }
//        List<OrderDistributionExcelVO> list = orderExcelService.excelDistributionOrderList(queryDTO);
//        ExcelUtil.soleExcel(response, list, OrderDistributionExcelVO.EXCEL_NAME, OrderDistributionExcelVO.MERGE_ROW_INDEX, OrderDistributionExcelVO.MERGE_COLUMN_INDEX, OrderDistributionExcelVO.class);
//        return ServerResponseEntity.success();
    }


    @ApiOperation("导购分销数据-合计")
    @PostMapping("/getTotalDistribution")
    public ServerResponseEntity<DistributionStatisticsVO> getTotalDistribution(@RequestBody DistributionSalesStatDTO distributionSalesStatDTO) {
        DistributionStatisticsVO distributionStatisticsVO = new DistributionStatisticsVO();
        distributionSalesStatDTO.setDistributionUserType(1);
        distributionStatisticsVO.setTodaySales(orderService.storeDistributionSalesStat(distributionSalesStatDTO));
        distributionStatisticsVO.setPayAmount(orderService.storeDistributionSalesStat(distributionSalesStatDTO));
        distributionStatisticsVO.setOrderNum(orderService.countStoreDistributionOrderNum(distributionSalesStatDTO));
        distributionStatisticsVO.setOrderActualNum(orderService.countStoreDistributionOrderNum(distributionSalesStatDTO));
        distributionStatisticsVO.setRefundSuccessAmount(orderRefundService.countStoreDistributionRefundAmount(distributionSalesStatDTO));
        distributionStatisticsVO.setRefundSuccessNum(orderRefundService.countStoreDistributionRefundNum(distributionSalesStatDTO));
        distributionSalesStatDTO.setRefund(true);
        distributionStatisticsVO.setRefundAmount(orderRefundService.countStoreDistributionRefundAmount(distributionSalesStatDTO));
        distributionStatisticsVO.setRefundNum(orderRefundService.countStoreDistributionRefundNum(distributionSalesStatDTO));
        return ServerResponseEntity.success(distributionStatisticsVO);
    }

    @ApiOperation("导购分销数据-导购分销")
    @PostMapping("/getDistribution")
    public ServerResponseEntity<DistributionStatisticsVO> getDistribution(@RequestBody DistributionSalesStatDTO distributionSalesStatDTO) {
        DistributionStatisticsVO distributionStatisticsVO = new DistributionStatisticsVO();
        distributionSalesStatDTO.setDistributionUserType(1);
        distributionSalesStatDTO.setType(1);
        distributionStatisticsVO.setTodaySales(orderService.storeDistributionSalesStat(distributionSalesStatDTO));
        distributionStatisticsVO.setPayAmount(orderService.storeDistributionSalesStat(distributionSalesStatDTO));
        distributionStatisticsVO.setOrderNum(orderService.countStoreDistributionOrderNum(distributionSalesStatDTO));
        distributionStatisticsVO.setOrderActualNum(orderService.countStoreDistributionOrderNum(distributionSalesStatDTO));
        distributionStatisticsVO.setDistributionUserNum(orderService.countStoreDistributionUserNum(distributionSalesStatDTO));
        distributionStatisticsVO.setRefundSuccessAmount(orderRefundService.countStoreDistributionRefundAmount(distributionSalesStatDTO));
        distributionStatisticsVO.setRefundSuccessNum(orderRefundService.countStoreDistributionRefundNum(distributionSalesStatDTO));
        distributionSalesStatDTO.setRefund(true);
        distributionStatisticsVO.setRefundAmount(orderRefundService.countStoreDistributionRefundAmount(distributionSalesStatDTO));
        distributionStatisticsVO.setRefundNum(orderRefundService.countStoreDistributionRefundNum(distributionSalesStatDTO));
        return ServerResponseEntity.success(distributionStatisticsVO);
    }


    @ApiOperation("导购分销数据-导购二级分销")
    @PostMapping("/getDevelopingDistribution")
    public ServerResponseEntity<DistributionStatisticsVO> getDevelopingDistribution(@RequestBody DistributionSalesStatDTO distributionSalesStatDTO) {
        DistributionStatisticsVO distributionStatisticsVO = new DistributionStatisticsVO();
        distributionSalesStatDTO.setDistributionUserType(1);
        distributionSalesStatDTO.setType(2);
        distributionStatisticsVO.setTodaySales(orderService.storeDistributionSalesStat(distributionSalesStatDTO));
        distributionStatisticsVO.setPayAmount(orderService.storeDistributionSalesStat(distributionSalesStatDTO));
        distributionStatisticsVO.setOrderNum(orderService.countStoreDistributionOrderNum(distributionSalesStatDTO));
        distributionStatisticsVO.setOrderActualNum(orderService.countStoreDistributionOrderNum(distributionSalesStatDTO));
        distributionStatisticsVO.setRefundSuccessAmount(orderRefundService.countStoreDistributionRefundAmount(distributionSalesStatDTO));
        distributionStatisticsVO.setRefundSuccessNum(orderRefundService.countStoreDistributionRefundNum(distributionSalesStatDTO));
        distributionSalesStatDTO.setRefund(true);
        distributionStatisticsVO.setRefundAmount(orderRefundService.countStoreDistributionRefundAmount(distributionSalesStatDTO));
        distributionStatisticsVO.setRefundNum(orderRefundService.countStoreDistributionRefundNum(distributionSalesStatDTO));
        return ServerResponseEntity.success(distributionStatisticsVO);
    }


    @ApiOperation("导购分销数据-门店分页数据")
    @PostMapping("/pageStoreStatistics")
    public ServerResponseEntity<PageVO<DistributionStoreStatisticsVO>> pageStoreStatistics(@Valid PageDTO pageDTO, @RequestBody DistributionSalesStatDTO distributionSalesStatDTO){
        PageVO<DistributionStoreStatisticsVO> page = orderService.pageStoreStatistics(pageDTO, distributionSalesStatDTO);
        return ServerResponseEntity.success(page);
    }


    @ApiOperation("导购分销数据-导出门店分销数据")
    @PostMapping("/exportStoreStatistics")
    public ServerResponseEntity exportStoreStatistics(@RequestBody DistributionSalesStatDTO distributionSalesStatDTO){
        distributionOrderExcelService.exportStoreStatistics(distributionSalesStatDTO);
        return ServerResponseEntity.success("操作成功，请转至下载中心下载");
    }

    @ApiOperation("导购分销数据-导出导购分销数据")
    @PostMapping("/exportStaffStatistics")
    public ServerResponseEntity exportStaffStatistics(@RequestBody DistributionSalesStatDTO distributionSalesStatDTO){
        distributionOrderExcelService.exportStaffStatistics(distributionSalesStatDTO);
        return ServerResponseEntity.success("操作成功，请转至下载中心下载");
    }

    @ApiOperation("威客分销数据-导出威客分销数据")
    @PostMapping("/exportWitkeyStatistics")
    public ServerResponseEntity<Void> exportWitkeyStatistics(@RequestBody DistributionSalesStatDTO distributionSalesStatDTO){
        distributionOrderExcelService.exportWitkeyStatistics(distributionSalesStatDTO);
        return ServerResponseEntity.success();
    }


    @ApiOperation("威客分销数据-分销业绩")
    @PostMapping("/getWitkeyDistribution")
    public ServerResponseEntity<DistributionStatisticsVO> getWitkeyDistribution(@RequestBody DistributionSalesStatDTO distributionSalesStatDTO) {
        DistributionStatisticsVO distributionStatisticsVO = new DistributionStatisticsVO();
        distributionSalesStatDTO.setDistributionUserType(2);
        distributionStatisticsVO.setTodaySales(orderService.storeDistributionSalesStat(distributionSalesStatDTO));
        distributionStatisticsVO.setOrderNum(orderService.countStoreDistributionOrderNum(distributionSalesStatDTO));
        distributionStatisticsVO.setRefundSuccessNum(orderRefundService.countStoreDistributionRefundNum(distributionSalesStatDTO));
        distributionStatisticsVO.setDistributionUserNum(orderService.countStoreDistributionUserNum(distributionSalesStatDTO));
        return ServerResponseEntity.success(distributionStatisticsVO);
    }


    @ApiOperation("威客分销数据-威客分销成本")
    @PostMapping("/getWitkeyCommission")
    public ServerResponseEntity<DistributionStatisticsVO> getWitkeyCommission(@RequestBody DistributionSalesStatDTO distributionSalesStatDTO){
        DistributionStatisticsVO distributionStatisticsVO = new DistributionStatisticsVO();
        distributionSalesStatDTO.setDistributionUserType(2);
        distributionStatisticsVO.setExpectedCommission(orderService.countStoreDistributionCommission(distributionSalesStatDTO));
        distributionSalesStatDTO.setSettlement(true);
        distributionStatisticsVO.setTotalCommission(orderService.countStoreDistributionCommission(distributionSalesStatDTO));
        return ServerResponseEntity.success(distributionStatisticsVO);
    }


    @ApiOperation("威客分销数据-威客数据")
    @PostMapping("/getWitkeyNum")
    public ServerResponseEntity<DistributionStatisticsVO> getWitkeyNum(@RequestBody DistributionSalesStatDTO distributionSalesStatDTO){
        DistributionStatisticsVO vo = new DistributionStatisticsVO();
        distributionSalesStatDTO.setDistributionUserType(2);
        DistributionUserQueryDTO queryDTO = new DistributionUserQueryDTO();
        queryDTO.setStoreId(distributionSalesStatDTO.getDistributionStoreId());
        queryDTO.setStartTime(distributionSalesStatDTO.getStartTime());
        queryDTO.setEndTime(distributionSalesStatDTO.getEndTime());
        queryDTO.setWitkey(true);
        ServerResponseEntity<DistributionUserVO> responseEntity = userFeignClient.countUserNum(queryDTO);
        if (responseEntity.isSuccess() && null != responseEntity.getData()){
            vo.setAddWitkeyNum(Optional.of(responseEntity.getData().getUserNum()).orElse(0));
            Integer integer = orderService.countStoreDistributionUserNum(distributionSalesStatDTO, queryDTO);
            if (null != vo.getAddWitkeyNum() && vo.getAddWitkeyNum() > 0){
                vo.setAddWitkeyRate((new BigDecimal(Optional.ofNullable(integer).orElse(0)).divide(new BigDecimal(vo.getAddWitkeyNum()), 4, BigDecimal.ROUND_HALF_UP)).toString());
            }
        }
        queryDTO.setStoreId(null);
        queryDTO.setStartTime(null);
        queryDTO.setEndTime(null);
        ServerResponseEntity<DistributionUserVO> responseEntity1 = userFeignClient.countUserNum(queryDTO);
        if (responseEntity1.isSuccess() && null != responseEntity1.getData()){
            vo.setTotalWitkeyNum(Optional.of(responseEntity.getData().getUserNum()).orElse(0));
            distributionSalesStatDTO.setDistributionStoreId(null);
            distributionSalesStatDTO.setStartTime(null);
            distributionSalesStatDTO.setEndTime(null);
            Integer integer = orderService.countStoreDistributionUserNum(distributionSalesStatDTO, queryDTO);
            if (null != vo.getTotalWitkeyNum() && vo.getTotalWitkeyNum() > 0){
                vo.setTotalWitkeyRate((new BigDecimal(Optional.ofNullable(integer).orElse(0)).divide(new BigDecimal(vo.getTotalWitkeyNum()), 4, BigDecimal.ROUND_HALF_UP)).toString());
            }
        }
        return ServerResponseEntity.success(vo);
    }


    @ApiOperation("威客分销数据-会员数据")
    @PostMapping("/getUserNum")
    public ServerResponseEntity<DistributionStatisticsVO> getUserNum(@RequestBody DistributionSalesStatDTO distributionSalesStatDTO){
        DistributionStatisticsVO vo = new DistributionStatisticsVO();
        DistributionUserQueryDTO queryDTO = new DistributionUserQueryDTO();
        distributionSalesStatDTO.setDistributionUserType(2);
        queryDTO.setStoreId(distributionSalesStatDTO.getDistributionStoreId());
        queryDTO.setRecruit(true);
        queryDTO.setStartTime(distributionSalesStatDTO.getStartTime());
        queryDTO.setEndTime(distributionSalesStatDTO.getEndTime());
        ServerResponseEntity<DistributionUserVO> responseEntity = userFeignClient.countUserNum(queryDTO);
        if (responseEntity.isSuccess() && null != responseEntity.getData()){
            vo.setAddUserNum(responseEntity.getData().getUserNum());
        }
        if (Optional.ofNullable(vo.getAddUserNum()).orElse(0) == 0){
            return ServerResponseEntity.success(vo);
        }
        Integer integer = orderService.countStoreDistributionOrderUserNum(distributionSalesStatDTO, queryDTO);
        if (null != vo.getAddUserNum() && vo.getAddUserNum() > 0){
            vo.setUserRate((new BigDecimal(Optional.ofNullable(integer).orElse(0)).divide(new BigDecimal(vo.getAddUserNum()), 4, BigDecimal.ROUND_HALF_UP)).toString());
        }
        vo.setTotalUserAmount(orderService.countStoreDistributionOrderUserAmount(distributionSalesStatDTO, queryDTO));
        return ServerResponseEntity.success(vo);
    }

}
