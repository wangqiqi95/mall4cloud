package com.mall4j.cloud.distribution.controller.admin;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionUserIncomeDTO;
import com.mall4j.cloud.distribution.service.DistributionUserIncomeService;
import com.mall4j.cloud.distribution.vo.DistributionUserIncomeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author lth
 * @Date 2021/8/10 16:30
 */
@RestController("adminDistributionUserIncomeController")
@RequestMapping("/mp/distribution_user_income")
@Api(tags = "admin-分销收入记录信息")
public class DistributionUserIncomeController {

    @Autowired
    private DistributionUserIncomeService distributionUserIncomeService;

    @GetMapping("/page_sales_record")
    @ApiOperation(value = "获取销售记录", notes = "获取销售记录")
    public ServerResponseEntity<PageVO<DistributionUserIncomeVO>> pageSalesRecord(@Valid PageDTO pageDTO, DistributionUserIncomeDTO distributionUserIncomeDTO) {
        PageVO<DistributionUserIncomeVO> distributionUserIncomePageVO = distributionUserIncomeService.pageSalesRecord(pageDTO, distributionUserIncomeDTO);
        return ServerResponseEntity.success(distributionUserIncomePageVO);
    }
}
