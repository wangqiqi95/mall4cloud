package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.distribution.model.DistributionUserIncome;
import com.mall4j.cloud.distribution.service.DistributionUserIncomeService;
import com.mall4j.cloud.distribution.service.DistributionUserService;
import com.mall4j.cloud.distribution.vo.DistributionUserIncomeOrderVO;
import com.mall4j.cloud.distribution.vo.DistributionUserIncomeVO;
import com.mall4j.cloud.distribution.dto.DistributionUserIncomeDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.vo.DistributionUserVO;
import com.mall4j.cloud.distribution.vo.StatisticsDisUserIncomeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 分销收入记录信息
 *
 * @author cl
 * @date 2021-08-09 14:14:09
 */
@RestController("appDistributionUserIncomeController")
@RequestMapping("/distribution_user_income")
@Api(tags = "app-分销收入记录信息")
public class DistributionUserIncomeController {

    @Autowired
    private DistributionUserIncomeService distributionUserIncomeService;

    @Autowired
    private DistributionUserService distributionUserService;

    @GetMapping("/page")
    @ApiOperation(value = "获取分销收入记录信息列表")
    public ServerResponseEntity<PageVO<DistributionUserIncomeVO>> page(@Valid PageDTO pageDTO) {
        DistributionUserVO distributionUser = distributionUserService.getByUserId(AuthUserContext.get().getUserId());
        if (Objects.isNull(distributionUser)) {
            throw new LuckException("您还不是分销员");
        }
        PageVO<DistributionUserIncomeVO> incomePageVO = distributionUserIncomeService.getDistributionUserIncomePage(pageDTO, distributionUser.getDistributionUserId());
        return ServerResponseEntity.success(incomePageVO);
    }

    @GetMapping("/statistics")
    @ApiOperation(value = "统计分销员今日、本月的收益")
    public ServerResponseEntity<StatisticsDisUserIncomeVO> statisticsDistributionUserIncome() {
        DistributionUserVO distributionUser = distributionUserService.getByUserId(AuthUserContext.get().getUserId());
        if (Objects.isNull(distributionUser)) {
            throw new LuckException("您还不是分销员");
        }
        return ServerResponseEntity.success(distributionUserIncomeService.statisticsDistributionUserIncome(distributionUser.getDistributionUserId()));
    }

    @GetMapping("/my_promotion_order")
    @ApiOperation(value = "我的推广订单", notes = "通过状态获取我的推广订单(0:待支付 1:待结算 2:已结算 -1:订单失效)")
    public ServerResponseEntity<PageVO<DistributionUserIncomeOrderVO>> getMyPromotionOrderByState(@Valid PageDTO pageDTO, Integer state) {
        DistributionUserVO distributionUser = distributionUserService.getByUserId(AuthUserContext.get().getUserId());
        if (Objects.isNull(distributionUser)) {
            throw new LuckException("您还不是分销员");
        }
        return ServerResponseEntity.success(distributionUserIncomeService.getMyPromotionOrderByState(pageDTO,distributionUser.getDistributionUserId(),state));
    }
}
