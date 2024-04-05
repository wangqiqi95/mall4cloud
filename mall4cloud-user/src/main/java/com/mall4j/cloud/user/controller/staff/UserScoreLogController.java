package com.mall4j.cloud.user.controller.staff;

import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.vo.PointDetailVo;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户积分记录
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@Slf4j
@RestController("staffUserScoreLogController")
@RequestMapping("/s/userScoreLog")
@Api(tags = "导购-用户积分")
public class UserScoreLogController {

	@Autowired
	private UserService userService;

	@GetMapping("/ua/crmPage")
	@ApiOperation(value = "crm-积分明细列表", notes = "crm-积分明细列表")
	public ServerResponseEntity<CrmPageResult<List<PointDetailVo>>> pointDetailGet(@Valid PageDTO pageDTO, Long userId) {
		return userService.getScoreDetail(pageDTO.getPageNum(),pageDTO.getPageSize(), userId);
	}
}
