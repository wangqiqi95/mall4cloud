package com.mall4j.cloud.biz.controller.wx.cp;



import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.dto.cp.CustGroupAssignDetailDTO;
import com.mall4j.cloud.biz.dto.cp.CustGroupAssignDetailPageDTO;
import com.mall4j.cloud.biz.model.cp.CustGroupAssignDetail;
import com.mall4j.cloud.biz.service.cp.CustGroupAssignDetailService;
import com.mall4j.cloud.biz.service.cp.ResignAssignLogService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Date;

/**
 * 客群分配明细表 
 *
 * @author hwy
 * @date 2022-02-10 18:25:57
 */
@RestController("platformCustGroupAssignDetailController")
@RequestMapping("/p/cp/cust_group_assign_detail")
@Api(tags = "客群分配明细表 ")
@RequiredArgsConstructor
public class CustGroupAssignDetailController {

    private final  CustGroupAssignDetailService custGroupAssignDetailService;
    private final ResignAssignLogService resignAssignLogService;
    private final StaffFeignClient staffClient;

	@GetMapping("/page")
	@ApiOperation(value = "获取客群分配明细表 列表", notes = "分页获取客群分配明细表 列表")
	public ServerResponseEntity<PageVO<CustGroupAssignDetail>> page(@Valid PageDTO pageDTO, CustGroupAssignDetailPageDTO request) {
		PageVO<CustGroupAssignDetail> custGroupAssignDetailPage = custGroupAssignDetailService.page(pageDTO,request);
		return ServerResponseEntity.success(custGroupAssignDetailPage);
	}
    @PostMapping("/reAssign")
    @ApiOperation(value = "重新分配 ", notes = "重新分配 ")
    public ServerResponseEntity<Void> reAssign(@Valid @RequestBody CustGroupAssignDetailDTO request) {
        ServerResponseEntity<StaffVO> response =  staffClient.getStaffById(request.getReplaceBy());
        if(response==null||response.getData()==null){
             throw new LuckException("替换的员工不存在");
        }
        StaffVO replaceStaff = response.getData();
        CustGroupAssignDetail detail = custGroupAssignDetailService.getById(request.getId());
        detail.setReplaceBy(request.getReplaceBy());
        detail.setReplaceByName(replaceStaff.getStaffName());
        detail.setStoreId(replaceStaff.getStoreId());
        detail.setStoreName(replaceStaff.getStoreName());
        detail.setReplaceByUserId(replaceStaff.getQiWeiUserId());
        detail.setStatus(0);
        detail.setUpdateTime(new Date());
        resignAssignLogService.reAssign(detail);
        return ServerResponseEntity.success();
    }

}
