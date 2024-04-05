package com.mall4j.cloud.flow.controller.platform;


import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.flow.dto.UserAnalysisDTO;
import com.mall4j.cloud.flow.model.UserAnalysis;
import com.mall4j.cloud.flow.service.CustomerAnalysisService;
import com.mall4j.cloud.flow.service.UserAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 流量分析—用户数据
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
@RestController("platformUserAnalysisController")
@RequestMapping("/p/user_analysis")
@Api(tags = "platform—用户数据")
public class UserAnalysisController {

    @Autowired
    private UserAnalysisService userAnalysisService;

    @Autowired
    private CustomerAnalysisService customerAnalysisService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取流量分析—用户数据列表", notes = "分页获取流量分析—用户数据列表")
	public ServerResponseEntity<PageVO<UserAnalysis>> page(@Valid PageDTO pageDTO) {
		PageVO<UserAnalysis> userAnalysisPage = userAnalysisService.page(pageDTO);
		return ServerResponseEntity.success(userAnalysisPage);
	}

	@GetMapping
    @ApiOperation(value = "获取流量分析—用户数据", notes = "根据userAnalysisId获取流量分析—用户数据")
    public ServerResponseEntity<UserAnalysis> getByUserAnalysisId(@RequestParam Long userAnalysisId) {
        return ServerResponseEntity.success(userAnalysisService.getByUserAnalysisId(userAnalysisId));
    }

    @PostMapping
    @ApiOperation(value = "保存流量分析—用户数据", notes = "保存流量分析—用户数据")
    public ServerResponseEntity<Void> save(@Valid @RequestBody UserAnalysisDTO userAnalysisDTO) {
        UserAnalysis userAnalysis = mapperFacade.map(userAnalysisDTO, UserAnalysis.class);
        userAnalysis.setUserAnalysisId(null);
        userAnalysisService.save(userAnalysis);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新流量分析—用户数据", notes = "更新流量分析—用户数据")
    public ServerResponseEntity<Void> update(@Valid @RequestBody UserAnalysisDTO userAnalysisDTO) {
        UserAnalysis userAnalysis = mapperFacade.map(userAnalysisDTO, UserAnalysis.class);
        userAnalysisService.update(userAnalysis);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除流量分析—用户数据", notes = "根据流量分析—用户数据id删除流量分析—用户数据")
    public ServerResponseEntity<Void> delete(@RequestParam Long userAnalysisId) {
        userAnalysisService.deleteById(userAnalysisId);
        return ServerResponseEntity.success();
    }

}
