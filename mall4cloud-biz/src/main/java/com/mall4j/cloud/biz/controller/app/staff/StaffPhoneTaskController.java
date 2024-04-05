package com.mall4j.cloud.biz.controller.app.staff;

import com.mall4j.cloud.biz.dto.cp.CpSelectPhoneTaskUserDTO;
import com.mall4j.cloud.biz.dto.cp.SelectCpPhoneTaskDTO;
import com.mall4j.cloud.biz.model.cp.CpPhoneTask;
import com.mall4j.cloud.biz.service.cp.CpPhoneTaskService;
import com.mall4j.cloud.biz.service.cp.CpPhoneTaskUserService;
import com.mall4j.cloud.biz.vo.cp.CpPhoneTaskUserVO;
import com.mall4j.cloud.biz.vo.cp.CpPhoneTaskVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 引流手机号任务
 *
 * @author gmq
 * @date 2023-10-30 17:13:30
 */
@Slf4j
@RestController("StaffPhoneTaskController")
@RequestMapping("/s/phone/task")
@Api(tags = "导购-引流手机号任务")
public class StaffPhoneTaskController {

    @Autowired
    private CpPhoneTaskService cpPhoneTaskService;
    @Autowired
    private CpPhoneTaskUserService cpPhoneTaskUserService;
    @Autowired
    private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取引流手机号任务列表", notes = "分页获取引流手机号任务列表")
	public ServerResponseEntity<PageVO<CpPhoneTaskVO>> pageMobile(@Valid PageDTO pageDTO, SelectCpPhoneTaskDTO dto) {
		PageVO<CpPhoneTaskVO> cpPhoneTaskPage = cpPhoneTaskService.pageMobile(pageDTO,dto);
		return ServerResponseEntity.success(cpPhoneTaskPage);
	}

    @GetMapping("/user/page")
    @ApiOperation(value = "查看任务明细列表-手机号", notes = "查看任务明细列表-手机号")
    public ServerResponseEntity<PageVO<CpPhoneTaskUserVO>> userPage(@Valid PageDTO pageDTO, CpSelectPhoneTaskUserDTO dto) {
        PageVO<CpPhoneTaskUserVO> taskUserVOPageVO = cpPhoneTaskUserService.pageMobile(pageDTO,dto);
        return ServerResponseEntity.success(taskUserVOPageVO);
    }

	@GetMapping
    @ApiOperation(value = "获取引流手机号任务详情", notes = "根据id获取引流手机号任务详情")
    public ServerResponseEntity<CpPhoneTaskVO> getById(@RequestParam Long id) {
        CpPhoneTask cpPhoneTask=cpPhoneTaskService.getById(id);
        if(Objects.isNull(cpPhoneTask)){
            throw new LuckException("未获取到任务详情");
        }
        return ServerResponseEntity.success(mapperFacade.map(cpPhoneTask,CpPhoneTaskVO.class));
    }

}
