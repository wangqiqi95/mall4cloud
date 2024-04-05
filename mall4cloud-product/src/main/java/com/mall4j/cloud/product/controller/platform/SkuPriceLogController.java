package com.mall4j.cloud.product.controller.platform;

import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.product.dto.SkuPriceLogDTO;
import com.mall4j.cloud.api.product.vo.SoldSkuPriceLogExcelVO;
import com.mall4j.cloud.api.product.vo.SoldSpuExcelVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.dto.SkuPriceLogParamDTO;
import com.mall4j.cloud.product.dto.SpuPageSearchDTO;
import com.mall4j.cloud.product.event.SoldSpuEvent;
import com.mall4j.cloud.product.model.SkuPriceLog;
import com.mall4j.cloud.product.service.SkuPriceLogService;
import com.mall4j.cloud.product.vo.SkuPriceLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;

/**
 * 
 *
 * @author gmq
 * @date 2022-09-20 10:58:25
 */
@Slf4j
@RestController("platformSkuPriceLogController")
@RequestMapping("/p/price/log")
@Api(tags = "商品价格日志")
public class SkuPriceLogController {

    @Autowired
    private SkuPriceLogService skuPriceLogService;
	@Autowired
	private DownloadCenterFeignClient downloadCenterFeignClient;


	@PostMapping("/page")
	@ApiOperation(value = "获取列表", notes = "分页获取列表")
	public ServerResponseEntity<PageVO<SkuPriceLogVO>> page( @RequestBody SkuPriceLogParamDTO skuPriceLogDTO) {
		PageVO<SkuPriceLogVO> skuPriceLogPage = skuPriceLogService.page(skuPriceLogDTO);
		return ServerResponseEntity.success(skuPriceLogPage);
	}

	@PostMapping("/sold_excel")
	@ApiOperation(value = "导出excel", notes = "导出商品excel")
	public ServerResponseEntity<String> spuSoldExcel(HttpServletResponse response, @RequestBody SkuPriceLogParamDTO skuPriceLogDTO) {

		try {
			CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
			downloadRecordDTO.setDownloadTime(new Date());
			downloadRecordDTO.setFileName(SoldSkuPriceLogExcelVO.EXCEL_NAME);
			downloadRecordDTO.setCalCount(0);
			downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
			downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
			ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
			Long downLoadHisId=null;
			if(serverResponseEntity.isSuccess()){
				downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
			}

			skuPriceLogService.soldExcel(downLoadHisId,skuPriceLogDTO);

			return ServerResponseEntity.success("操作成功，请转至下载中心下载");
		}catch (Exception e){
			log.error("导出商品信息错误: "+e.getMessage(),e);
			return ServerResponseEntity.showFailMsg("操作失败");
		}
	}

}
