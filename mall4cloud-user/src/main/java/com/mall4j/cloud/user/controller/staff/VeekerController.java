package com.mall4j.cloud.user.controller.staff;

import com.mall4j.cloud.api.user.dto.UserQueryDTO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.dto.VeekerQueryDTO;
import com.mall4j.cloud.user.service.VeekerService;
import com.mall4j.cloud.user.vo.VeekerStatVO;
import com.mall4j.cloud.user.vo.VeekerVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 微客管理
 * @author gww
 */
@RestController("staffVeekerController")
@RequestMapping("/s/veeker")
@Api(tags = "导购小程序-微客")
public class VeekerController {

    @Autowired
    private VeekerService veekerService;

    @GetMapping("/page")
    @ApiOperation(value = "列表", notes = "获取当前导购下微客列表")
    public ServerResponseEntity<PageVO<VeekerVO>> page(@Valid PageDTO pageDTO, VeekerQueryDTO veekerQueryDTO) {
        List<Long> staffIdList = new ArrayList<>();
        staffIdList.add(AuthUserContext.get().getUserId());
        veekerQueryDTO.setStaffIdList(staffIdList);
        veekerQueryDTO.setVeekerStatus(1);
        return ServerResponseEntity.success(veekerService.staffPage(pageDTO, veekerQueryDTO));
    }


    @GetMapping("/stat")
    @ApiOperation(value = "统计", notes = "统计当前导购下微客数量(今日、当月、累计)")
    public ServerResponseEntity<VeekerStatVO> stat() {
        Long staffId = AuthUserContext.get().getUserId();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime monthStart = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth());

        UserQueryDTO userQueryDTO = new UserQueryDTO();
        userQueryDTO.setStaffId(staffId);
        Integer total = veekerService.countStaffWeeker(userQueryDTO);
        userQueryDTO.setStartTime(Date.from(todayStart.atZone(zone).toInstant()));
        userQueryDTO.setEndTime(Date.from(todayEnd.atZone(zone).toInstant()));
        Integer today = veekerService.countStaffWeeker(userQueryDTO);
        userQueryDTO.setStartTime(Date.from(monthStart.atZone(zone).toInstant()));
        Integer month = veekerService.countStaffWeeker(userQueryDTO);

        return ServerResponseEntity.success(new VeekerStatVO(total, today, month));
    }

}
