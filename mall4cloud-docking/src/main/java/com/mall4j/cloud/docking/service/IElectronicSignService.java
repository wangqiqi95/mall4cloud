package com.mall4j.cloud.docking.service;

import com.mall4j.cloud.api.docking.dto.BatchResultsDto;
import com.mall4j.cloud.api.docking.dto.ElectronicSignDto;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import java.util.List;

/**
 * 类描述：电子加签业务对接处理接口
 */
public interface IElectronicSignService {

	ServerResponseEntity<List<BatchResultsDto>> batchInsertItems(ElectronicSignDto electronicSignDto);
}
