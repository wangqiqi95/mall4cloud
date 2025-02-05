package com.mall4j.cloud.distribution.controller.staff;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.dto.DistributionSpecialSubjectDTO;
import com.mall4j.cloud.distribution.service.DistributionSpecialSubjectService;
import com.mall4j.cloud.distribution.vo.DistributionSpecialSubjectVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 分销推广-分销专题
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:56
 */
@RestController("staffDistributionSpecialSubjectController")
@RequestMapping("/s/distribution_special_subject")
@Api(tags = "导购小程序-分销推广-分销专题")
public class DistributionSpecialSubjectController {

    @Autowired
    private DistributionSpecialSubjectService distributionSpecialSubjectService;

//	@GetMapping("/page")
//	@ApiOperation(value = "获取分销推广-分销专题列表", notes = "分页获取分销推广-分销专题列表")
//	public ServerResponseEntity<PageVO<DistributionSpecialSubjectVO>> page(@Valid PageDTO pageDTO, DistributionSpecialSubjectDTO dto) {
//		PageVO<DistributionSpecialSubjectVO> distributionSpecialSubjectPage = distributionSpecialSubjectService.page(pageDTO, dto);
//		return ServerResponseEntity.success(distributionSpecialSubjectPage);
//	}

	@GetMapping
    @ApiOperation(value = "获取分销推广-分销专题", notes = "根据id获取分销推广-分销专题")
    public ServerResponseEntity<DistributionSpecialSubjectDTO> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionSpecialSubjectService.getSpecialSubjectById(id));
    }

	@GetMapping("/pageEffect")
	@ApiOperation(value = "获取分销推广-分销专题生效列表", notes = "分页获取分销推广-分销专题生效列表")
	public ServerResponseEntity<PageVO<DistributionSpecialSubjectVO>> pageEffect(@Valid PageDTO pageDTO, DistributionSpecialSubjectDTO dto) {
		dto.setQueryStoreId(AuthUserContext.get().getStoreId());
		PageVO<DistributionSpecialSubjectVO> distributionSpecialSubjectPage = distributionSpecialSubjectService.pageEffect(pageDTO, dto);
		return ServerResponseEntity.success(distributionSpecialSubjectPage);
	}

}
