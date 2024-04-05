package com.mall4j.cloud.biz.controller.wx.cp;

import cn.hutool.core.collection.ListUtil;
import com.mall4j.cloud.api.biz.vo.SoldStaffCodeErrorExcelVO;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.biz.dto.cp.ReqStaffCodeLogDTO;
import com.mall4j.cloud.biz.service.cp.CpStaffCodeLogService;
import com.mall4j.cloud.biz.vo.cp.CpStaffCodeLogVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 
 *
 */
@Slf4j
//@RestController("platformCpStaffCodeLogController")
//@RequestMapping("/p/staff_code_log")
//@Api(tags = "员工活码失败记录")
public class CpStaffCodeLogController {

    @Autowired
    private CpStaffCodeLogService cpStaffCodeLogService;
    @Autowired
	private DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取列表", notes = "分页获取列表")
	public ServerResponseEntity<PageVO<CpStaffCodeLogVO>> page(@Valid PageDTO pageDTO, ReqStaffCodeLogDTO reqStaffCodeLogDTO) {
		reqStaffCodeLogDTO.setNoErrorCodes(ListUtil.of("skqqw002"));
		PageVO<CpStaffCodeLogVO> cpStaffCodeLogPage = cpStaffCodeLogService.listPage(pageDTO,reqStaffCodeLogDTO);
		return ServerResponseEntity.success(cpStaffCodeLogPage);
	}

	@PostMapping("/rCreateCode")
	@ApiOperation(value = "重新生成(传员工id)", notes = "重新生成")
	public ServerResponseEntity<Void> addTag(@Valid @RequestBody ()List<Long> staffIds) {
		cpStaffCodeLogService.rCreateCode(staffIds);
		return ServerResponseEntity.success();
	}

	@GetMapping("/staffCodeErrorCreate")
	@ApiOperation(value = "定时任务执行", notes = "定时任务执行")
	public ServerResponseEntity<Void> staffCodeErrorCreate() {
		cpStaffCodeLogService.staffCodeErrorCreate();
		return ServerResponseEntity.success();
	}

	@GetMapping("/sold_excel")
	@ApiOperation(value = "导出excel", notes = "导出excel")
	public ServerResponseEntity<String> spuSoldExcel(HttpServletResponse response, ReqStaffCodeLogDTO reqStaffCodeLogDTO) {

		try {
			CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
			downloadRecordDTO.setDownloadTime(new Date());
			downloadRecordDTO.setFileName(SoldStaffCodeErrorExcelVO.EXCEL_NAME);
			downloadRecordDTO.setCalCount(0);
			downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
			downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
			ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
			Long downLoadHisId=null;
			if(serverResponseEntity.isSuccess()){
				downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
			}

			cpStaffCodeLogService.soldStaffCodeLog(reqStaffCodeLogDTO,downLoadHisId);

			return ServerResponseEntity.success("操作成功，请转至下载中心下载");
		}catch (Exception e){
			log.error("导出错误信息错误: "+e.getMessage(),e);
			return ServerResponseEntity.showFailMsg("操作失败");
		}
	}

}
