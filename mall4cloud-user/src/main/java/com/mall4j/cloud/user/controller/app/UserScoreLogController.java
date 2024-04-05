package com.mall4j.cloud.user.controller.app;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.service.UserScoreLogService;
import com.mall4j.cloud.user.vo.UserScoreLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户积分记录
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@RestController("appUserScoreLogController")
@RequestMapping("/user_score_log")
@Api(tags = "app-用户积分记录")
public class UserScoreLogController {

    @Autowired
    private UserScoreLogService userScoreLogService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取用户积分记录列表", notes = "根据用户id分页获取用户积分记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ioType", value = "出入类型 0:支出 1:收入", dataType = "Integer"),
            @ApiImplicitParam(name = "source", value = "积分类型 0.注册送积分1.购物 2.会员等级提升加积分 3.签到加积分 4.购物抵扣使用积分 5.积分过期 6.余额充值 7.系统更改积分 8.购物抵扣使用积分退还", dataType = "Integer")
    })
	public ServerResponseEntity<PageVO<UserScoreLogVO>> page(@Valid PageDTO pageDTO,
                                                             @RequestParam(value = "ioType",required = false) Integer ioType,
                                                             @RequestParam(value = "source",required = false) Integer source) {
		PageVO<UserScoreLogVO> userScoreLogPage = userScoreLogService.pageByIoTypeAndSource(pageDTO,ioType,source);
		return ServerResponseEntity.success(userScoreLogPage);
	}

	@GetMapping("/score_prod_page")
	@ApiOperation(value = "获取用户积分商品兑换记录列表", notes = "分页获取用户积分商品兑换记录列表")
	public ServerResponseEntity<PageVO<UserScoreLogVO>> scoreProdPage(@Valid PageDTO pageDTO) {
		PageVO<UserScoreLogVO> userScoreLogPage = userScoreLogService.scoreProdPage(pageDTO);
		return ServerResponseEntity.success(userScoreLogPage);
	}
}
