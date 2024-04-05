package com.mall4j.cloud.docking.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.cloudesl.model.v20200201.BatchInsertItemsRequest;
import com.aliyuncs.cloudesl.model.v20200201.BatchInsertItemsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.mall4j.cloud.api.docking.dto.BatchResultsDto;
import com.mall4j.cloud.api.docking.dto.ElectronicSignDto;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.client.ElectronicSignClient;
import com.mall4j.cloud.docking.service.IElectronicSignService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：电子加签业务对接处理类
 */
@Service("electronicSignService")
public class ElectronicSignServiceImpl implements IElectronicSignService {

    private static final Logger logger = LoggerFactory.getLogger(ElectronicSignServiceImpl.class);

    @Autowired
    ElectronicSignClient electronicSignClient;

    @Override
    public ServerResponseEntity<List<BatchResultsDto>> batchInsertItems(ElectronicSignDto electronicSignDto) {
        if (electronicSignDto == null) {
            return ServerResponseEntity.fail(ResponseEnum.HTTP_MESSAGE_NOT_READABLE);
        }
        long start = System.currentTimeMillis();
        BatchInsertItemsRequest batchInsertItemsRequest = convert(electronicSignDto);

        if (StringUtils.isBlank(batchInsertItemsRequest.getStoreId())) {
            batchInsertItemsRequest.setStoreId(electronicSignClient.getDefaultStorId());
            electronicSignDto.setStoreId(electronicSignClient.getDefaultStorId());
        }

        BatchInsertItemsResponse acsResponse = null;
        try {
            acsResponse = electronicSignClient.getClient().getAcsResponse(batchInsertItemsRequest);
            ServerResponseEntity serverResponseEntity = new ServerResponseEntity();
            if (acsResponse != null) {
                logger.info("阿里云云价签-->请求响应结果：{}",JSON.toJSONString(acsResponse));
                String code = acsResponse.getCode();
                Boolean success = acsResponse.getSuccess();
                List<BatchResultsDto> batchResults = convert(acsResponse.getBatchResults());
                if ("200".equals(code) || Boolean.TRUE.equals(success)) {
                    serverResponseEntity.setCode(ResponseEnum.OK.value());
                } else {
                    serverResponseEntity.setCode("500");
                    serverResponseEntity.setMsg("请求阿里云BatchInsertItems批量新增或修改商品接口失败");
                }
                serverResponseEntity.setData(batchResults);
            } else {
                serverResponseEntity.setCode("500");
                serverResponseEntity.setMsg("请求阿里云BatchInsertItems批量新增或修改商品接口失败");
            }
            return serverResponseEntity;
        } catch (ClientException e) {
            logger.error("阿里云云价签->调用BatchInsertItems批量新增或修改商品,调用异常,请求参数为:" + JSON.toJSONString(electronicSignDto), e);
        } finally {
            logger.info("阿里云云价签->BatchInsertItems批量新增或修改商品,调用结束，请求参数为:{},请求响应为:{},共耗时:{}", JSON.toJSONString(electronicSignDto), JSON.toJSONString(acsResponse), System.currentTimeMillis() - start);
        }
        return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
    }

    private BatchInsertItemsRequest convert(ElectronicSignDto electronicSignDto) {
        BatchInsertItemsRequest batchInsertItemsRequest = new BatchInsertItemsRequest();
        batchInsertItemsRequest.setStoreId(electronicSignDto.getStoreId());
        batchInsertItemsRequest.setSyncByItemId(electronicSignDto.getSyncByItemId());
        List<BatchInsertItemsRequest.ItemInfo> itemInfos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(electronicSignDto.getItemInfos())) {
            for (ElectronicSignDto.ItemInfo itemInfo : electronicSignDto.getItemInfos()) {
                BatchInsertItemsRequest.ItemInfo item = new BatchInsertItemsRequest.ItemInfo();
                BeanUtils.copyProperties(itemInfo, item);
                itemInfos.add(item);
            }
        }
        batchInsertItemsRequest.setItemInfos(itemInfos);
        return batchInsertItemsRequest;
    }

    private List<BatchResultsDto> convert(List<BatchInsertItemsResponse.BatchResult> batchResults) {
        List<BatchResultsDto> resultsDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(batchResults)) {
            for (BatchInsertItemsResponse.BatchResult batchResult : batchResults) {
                BatchResultsDto resultsDto = new BatchResultsDto();
                BeanUtils.copyProperties(batchResult, resultsDto);
                resultsDtos.add(resultsDto);
            }
        }
        return resultsDtos;
    }
}
