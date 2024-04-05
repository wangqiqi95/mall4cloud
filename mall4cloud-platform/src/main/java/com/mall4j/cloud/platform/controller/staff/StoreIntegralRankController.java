package com.mall4j.cloud.platform.controller.staff;

import com.mall4j.cloud.api.platform.vo.StoreIntegralRankVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.service.StoreIntegralRankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description 门店积分抵现榜单
 * @Author axin
 * @Date 2023-02-15 16:46
 **/
@RestController("staffUserActionController")
@RequestMapping("/s/storeIntegral")
@Api(tags = "导购小程序-线下门店积分抵现榜单")
public class StoreIntegralRankController {

    @Resource
    private StoreIntegralRankService storeIntegralRankService;

    @GetMapping("/getStoreIntegralRank")
    @ApiOperation(value = "线下门店积分抵现榜单", notes = "线下门店积分抵现榜单")
    public ServerResponseEntity<StoreIntegralRankVO> getStoreIntegralRank() {
        return ServerResponseEntity.success(storeIntegralRankService.getStoreIntegralRank());
    }
}
