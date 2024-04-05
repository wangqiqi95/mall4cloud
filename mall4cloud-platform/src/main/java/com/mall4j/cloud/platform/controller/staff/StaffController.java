package com.mall4j.cloud.platform.controller.staff;

import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.vo.StaffInfoVO;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.dto.UserQueryDTO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.platform.model.Staff;
import com.mall4j.cloud.platform.service.StaffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 员工信息
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
@RestController("staffStaffController")
@RequestMapping("/s/staff")
@Api(tags = "导购小程序-导购管理")
public class StaffController {

    @Autowired
    private StaffService staffService;
    @Autowired
    private UserFeignClient userFeignClient;

	@GetMapping("/page")
	@ApiOperation(value = "员工信息列表", notes = "分页获取员工信息列表")
	public ServerResponseEntity<PageVO<StaffVO>> page(@Valid PageDTO pageDTO, StaffQueryDTO queryDTO) {
        List<Long> storeIdList = new ArrayList<>();
        storeIdList.add(AuthUserContext.get().getStoreId());
        queryDTO.setStoreIdList(storeIdList);
        queryDTO.setStatus(0);
        if(Objects.isNull(queryDTO.getQiweiUserStatus())){
            queryDTO.setQiweiUserStatus(1);
        }
		return ServerResponseEntity.success(staffService.page(pageDTO, queryDTO));
	}

    @GetMapping("/getById")
    @ApiOperation(value = "获取员工信息", notes = "根据员工id获取员工信息")
    public ServerResponseEntity<StaffVO> getById(@RequestParam Long id) {
        StaffVO staffVO = staffService.getById(id);
        if (Objects.nonNull(staffVO)) {
            UserQueryDTO userQueryDTO = new UserQueryDTO();
            userQueryDTO.setStaffId(id);
            ServerResponseEntity<Integer> staffUserRep = userFeignClient.countStaffUser(userQueryDTO);
            if (staffUserRep.isSuccess()) {
                staffVO.setMemberNum(staffUserRep.getData());
            }
            ServerResponseEntity<Integer> staffWeekerRep = userFeignClient.countStaffWeeker(userQueryDTO);
            if (staffWeekerRep.isSuccess()) {
                staffVO.setVeekerNum(staffWeekerRep.getData());
            }
        }
        return ServerResponseEntity.success(staffVO);
    }

	@GetMapping("/loginInfo")
    @ApiOperation(value = "登录员工信息", notes = "当前登录用户信息")
    public ServerResponseEntity<StaffVO> loginInfo() {
        return ServerResponseEntity.success(staffService.getById(AuthUserContext.get().getUserId()));
    }

    @GetMapping("/info")
    @ApiOperation(value = "员工信息", notes = "当前登录员工信息")
    public ServerResponseEntity<StaffInfoVO> info() {
        return ServerResponseEntity.success(staffService.info(AuthUserContext.get().getUserId()));
    }

}
