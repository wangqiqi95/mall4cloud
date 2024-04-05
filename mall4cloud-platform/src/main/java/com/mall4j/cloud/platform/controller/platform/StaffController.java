package com.mall4j.cloud.platform.controller.platform;

import com.alibaba.fastjson.JSONObject;
//import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.api.platform.dto.PageStaffQueryReqDto;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.platform.dto.StaffDTO;
import com.mall4j.cloud.platform.manager.StaffExcelManager;
import com.mall4j.cloud.platform.model.Staff;
import com.mall4j.cloud.platform.service.StaffOrgRelService;
import com.mall4j.cloud.platform.service.StaffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 员工信息
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
@Slf4j
@RestController("platformStaffController")
@RequestMapping("/p/staff")
@Api(tags = "员工信息")
public class StaffController {

    @Autowired
    private StaffService staffService;
    @Autowired
    private StaffOrgRelService staffOrgRelService;

	@GetMapping("/page")
	@ApiOperation(value = "获取员工信息列表", notes = "分页获取员工信息列表")
	public ServerResponseEntity<PageVO<StaffVO>> page(@Valid PageDTO pageDTO, StaffQueryDTO queryDTO) {
		PageVO<StaffVO> staffPage = staffService.page(pageDTO, queryDTO);
		return ServerResponseEntity.success(staffPage);
	}

    @PostMapping("/staffListByMN")
    @ApiOperation(value = "批量粘贴(手机号、工号)", notes = "批量粘贴(手机号、工号)")
    public ServerResponseEntity<List<StaffVO>> staffListByMN(@RequestBody StaffQueryDTO queryDTO) {
        return ServerResponseEntity.success(staffService.selectList(queryDTO));
    }

    @GetMapping("/selectList")
    @ApiOperation(value = "员工下拉选择列表", notes = "员工下拉选择列表")
    public ServerResponseEntity<List<StaffVO>> selectList(StaffQueryDTO queryDTO) {
        return ServerResponseEntity.success(staffService.selectList(queryDTO));
    }

    @PostMapping("/selectPostList")
    @ApiOperation(value = "员工下拉选择列表", notes = "员工下拉选择列表")
    public ServerResponseEntity<List<StaffVO>> selectPostList(@RequestBody StaffQueryDTO queryDTO) {
        return selectList(queryDTO);
    }

    @GetMapping("/checkIsExists")
    @ApiOperation(value = "验证手机号是否存在", notes = "验证手机号是否存在")
    public ServerResponseEntity<Boolean> checkIsExists(String mobile) {
        StaffVO staffVO = staffService.getByMobile(mobile);
        if (Objects.nonNull(staffVO)) {
            return ServerResponseEntity.success(true);
        }
        return ServerResponseEntity.success(false);
    }

	@GetMapping("/getById")
    @ApiOperation(value = "获取员工信息", notes = "根据id获取员工信息")
    public ServerResponseEntity<StaffVO> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(staffService.getById(id));
    }

    @GetMapping("/infoOrgs")
    @ApiOperation(value = "员工信息部门信息", notes = "员工信息部门信息")
    public ServerResponseEntity<List<Long>> getOrgAndChildByStaffIds(@RequestParam List<Long> ids) {
        return ServerResponseEntity.success(staffOrgRelService.getOrgAndChildByStaffIds(ids));
    }

//    @PostMapping("/save")
//    @ApiOperation(value = "保存员工信息", notes = "保存员工信息")
//    public ServerResponseEntity<Void> save(@Valid @RequestBody StaffDTO staffDTO) {
//        Staff staff = mapperFacade.map(staffDTO, Staff.class);
//        ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.SCRM_STAFF_USER);
//        if (!segmentIdResponse.isSuccess()) {
//            throw new LuckException(segmentIdResponse.getMsg());
//        }
//        staff.setId(segmentIdResponse.getData());
//        staff.setStatus(0);
//        staffService.save(staff);
//        return ServerResponseEntity.success();
//    }

//    @PutMapping("/update")
//    @ApiOperation(value = "更新员工信息", notes = "更新员工信息")
//    public ServerResponseEntity<Void> update(@Valid @RequestBody StaffDTO staffDTO) {
//	    if (Objects.isNull(staffDTO.getId())) {
//	        throw new LuckException("员工id不能为空");
//        }
//        log.info("更新员工信息->操作人【{}】 变更信息【{}】", AuthUserContext.get().getUsername(),JSONObject.toJSONString(staffDTO));
//        Staff staff = mapperFacade.map(staffDTO, Staff.class);
//        staffService.update(staff);
//        return ServerResponseEntity.success();
//    }
//
//    @PostMapping("/soldStaffs")
//    @ApiOperation(value = "导出员工信息", notes = "导出员工信息")
//    public ServerResponseEntity<String> soldStaffs(@Valid StaffQueryDTO queryDTO, HttpServletResponse response) {
//        try {
//            staffService.soldStaffs(queryDTO,response);
//            return ServerResponseEntity.success("操作成功，请转至下载中心下载");
//        }catch (Exception e){
//            log.error("导出员工信息",e.getMessage());
//            return ServerResponseEntity.showFailMsg("操作失败");
//        }
//    }
//
//    @PostMapping("/importStaffs")
//    @ApiOperation(value = "导入员工信息", notes = "导入员工信息")
//    public ServerResponseEntity<String> importStaffs(@RequestParam("excelFile") MultipartFile file) {
//        if(file == null) {
//            throw new LuckException("网络繁忙，请稍后重试");
//        }
//        String info=staffExcelManager.importStaffExcel(file);
//        return ServerResponseEntity.success(info);
//    }
//
//    @PostMapping("/importStaffWeChat")
//    @ApiOperation(value = "导入员工微信号", notes = "导入员工信息")
//    public ServerResponseEntity<String> importStaffWeChat(@RequestParam("excelFile") MultipartFile file) {
//        if(file == null) {
//            throw new LuckException("网络繁忙，请稍后重试");
//        }
//        String info=staffExcelManager.importStaffWeChat(file);
//        return ServerResponseEntity.success(info);
//    }
}
