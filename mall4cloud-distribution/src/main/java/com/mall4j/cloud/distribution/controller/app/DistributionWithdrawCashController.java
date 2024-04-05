package com.mall4j.cloud.distribution.controller.app;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.platform.vo.DistributionConfigApiVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.distribution.dto.AppDistributionWithdrawCashDTO;
import com.mall4j.cloud.distribution.dto.RangeTimeDTO;
import com.mall4j.cloud.distribution.model.DistributionUserWallet;
import com.mall4j.cloud.distribution.service.DistributionConfigService;
import com.mall4j.cloud.distribution.service.DistributionUserService;
import com.mall4j.cloud.distribution.service.DistributionUserWalletService;
import com.mall4j.cloud.distribution.service.DistributionWithdrawCashService;
import com.mall4j.cloud.distribution.vo.*;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 分销员提现记录
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
@RestController("appDistributionWithdrawCashController")
@RequestMapping("/distribution_withdraw_cash")
@Api(tags = "分销员提现记录")
public class DistributionWithdrawCashController {

    @Autowired
    private DistributionWithdrawCashService distributionWithdrawCashService;
    @Autowired
    private DistributionUserService distributionUserService;
    @Autowired
    private DistributionConfigService distributionConfigService;
    @Autowired
    private DistributionUserWalletService distributionUserWalletService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取分销员提现记录列表", notes = "分页获取分销员提现记录列表")
	public ServerResponseEntity<PageVO<AppDistributionWithdrawCashLogVO>> pageByUserId(@Valid PageDTO pageDTO) {
        Long userId = AuthUserContext.get().getUserId();
        DistributionUserVO distributionUserVO = distributionUserService.getByUserId(userId);
        if (Objects.isNull(distributionUserVO)) {
            // 您还不是分销员
            throw new LuckException("您还不是分销员");
        }
        PageVO<AppDistributionWithdrawCashVO> distributionWithdrawCashPage = distributionWithdrawCashService.pageByDistributionUserId(pageDTO, distributionUserVO.getDistributionUserId());
        // 将结果封装一下，以年月为分界线处理,
        PageVO<AppDistributionWithdrawCashLogVO> resPageVO = handleGroupByYearAndMonth(distributionWithdrawCashPage);
        return ServerResponseEntity.success(resPageVO);
	}

    private PageVO<AppDistributionWithdrawCashLogVO> handleGroupByYearAndMonth(PageVO<AppDistributionWithdrawCashVO> distributionWithdrawCashPage) {
        PageVO<AppDistributionWithdrawCashLogVO> resPage = new PageVO<>();
        resPage.setTotal(distributionWithdrawCashPage.getTotal());
        resPage.setPages(distributionWithdrawCashPage.getPages());
        List<AppDistributionWithdrawCashVO> list = distributionWithdrawCashPage.getList();
        if (CollUtil.isEmpty(list)){
            return resPage;
        }
        //  list的必须是已经倒序updateTime排序后的顺序
        // 将日期设置为月开始的第一天，然后直接根据时间进行分组就可以了
        for (AppDistributionWithdrawCashVO withdrawCashVO : list) {
            Date createTime = withdrawCashVO.getCreateTime();
            DateTime beginOfMonth = DateUtil.beginOfMonth(createTime);
            withdrawCashVO.setMonthTimeStr(DateUtil.formatDateTime(beginOfMonth));
        }
        // 分组， 由数据库update_time字段设计，不可能为空，无需做为空处理
        Map<String, List<AppDistributionWithdrawCashVO>> listMap = list.stream().collect(Collectors.groupingBy(AppDistributionWithdrawCashVO::getMonthTimeStr));
        // 为改变前的数据
        List<AppDistributionWithdrawCashLogVO> resList = new ArrayList<>();
        for (String dateStr : listMap.keySet()) {
            AppDistributionWithdrawCashLogVO logVO = new AppDistributionWithdrawCashLogVO();
            logVO.setDate(DateUtil.parseDate(dateStr));
            logVO.setDistributionWithdrawCashList(listMap.get(dateStr));
            resList.add(logVO);
        }
        resPage.setList(resList.stream().sorted(Comparator.comparing(AppDistributionWithdrawCashLogVO::getDate).reversed()).collect(Collectors.toList()));
        return resPage;
    }


    @PostMapping("/apply")
    @ApiOperation(value = "用户发起提现申请", notes = "填入提现参数")
    public ServerResponseEntity<Void> apply(@Valid @RequestBody AppDistributionWithdrawCashDTO distributionWithdrawCashDTO) {
        Long userId = AuthUserContext.get().getUserId();
        DistributionUserVO distributionUserVO = distributionUserService.getByUserId(userId);
        if (Objects.isNull(distributionUserVO)) {
            // 您还不是分销员
            throw new LuckException("您还不是分销员");
        }
        //发起提现
        distributionWithdrawCashService.apply(distributionWithdrawCashDTO, distributionUserVO);
        return ServerResponseEntity.success();
    }
    @GetMapping("/is_withdraw_cash")
    @ApiOperation(value = "当前是否可以提现")
    public ServerResponseEntity<Boolean> isWithdrawCash() {
        Long userId = AuthUserContext.get().getUserId();
        DistributionUserVO distributionUserVO = distributionUserService.getByUserId(userId);
        Long distributionUserId = distributionUserVO.getDistributionUserId();
        //获取店铺提现设置
        DistributionConfigApiVO distributionConfig = distributionConfigService.getDistributionConfig();
        //获取店铺设置的提现频率算出时间区间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - distributionConfig.getFrequency());
        //获取玩家最近的提现次数
        //判断是否能够提现
        Integer count = distributionWithdrawCashService.getCountByRangeTimeAndDistributionUserId(new RangeTimeDTO(calendar.getTime(), new Date()), distributionUserId);
        if (count >= distributionConfig.getNumber()) {
            // 提现次数为x天x次
            throw new LuckException("提现次数为" + distributionConfig.getFrequency() + "天" + distributionConfig.getNumber() + "次");
        }
        return ServerResponseEntity.success(true);
    }

    @GetMapping("/total_withdraw_cash")
    @ApiOperation(value = "查看用户总提现金额")
    public ServerResponseEntity<BigDecimal> getUserTotalWithdrawCash() {
        Long userId = AuthUserContext.get().getUserId();
        DistributionUserVO distributionUserVO = distributionUserService.getByUserId(userId);
        if (Objects.isNull(distributionUserVO)) {
            // 您还不是分销员
            throw new LuckException("您还不是分销员");
        }
        Long distributionUserId = distributionUserVO.getDistributionUserId();
        DistributionUserWallet distributionUserWallet = distributionUserWalletService.getByDistributionUserId(distributionUserId);
        BigDecimal totalWithdrawCash = distributionWithdrawCashService.getUserTotalWithdrawCash(distributionUserWallet.getWalletId());
        return ServerResponseEntity.success(totalWithdrawCash);
    }

}
