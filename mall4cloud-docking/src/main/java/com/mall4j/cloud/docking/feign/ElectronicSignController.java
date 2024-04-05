package com.mall4j.cloud.docking.feign;

import com.mall4j.cloud.api.docking.dto.BatchResultsDto;
import com.mall4j.cloud.api.docking.dto.ElectronicSignDto;
import com.mall4j.cloud.api.docking.feign.ElectronicSignFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.service.IElectronicSignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "电子加签")
public class ElectronicSignController implements ElectronicSignFeignClient {

	@Autowired
	private IElectronicSignService electronicSignService;

	/**
	 * 调用BatchInsertItems批量新增或修改商品
	 *
	 * @param electronicSignDto 请求参数dto
	 */
	@Override
	@ApiOperation(value = "调用BatchInsertItems批量新增或修改商品", notes = "调用BatchInsertItems批量新增或修改商品")
	public ServerResponseEntity<List<BatchResultsDto>>  batchInsertItems(ElectronicSignDto electronicSignDto) {
		return electronicSignService.batchInsertItems(electronicSignDto);
	}
}
