package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.excel.EasyExcel;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.listener.excel.ExcelUserReadListener;
import com.mall4j.cloud.group.service.QuestionnaireExcelService;
import com.mall4j.cloud.group.vo.questionnaire.QuestionnaireResolveExcelVO;
import com.mall4j.cloud.group.vo.questionnaire.QuestionnaireUserExcelVO;
import ma.glasnost.orika.MapperFacade;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @date 2023/5/30
 */
@Service
public class QuestionnaireExcelServiceImpl implements QuestionnaireExcelService {

    private final String KEY_PREFIX = "mall4cloud_group:resolve::";

    // 缓存时间 4小时
    private final Integer KEY_TIME = 14400;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public QuestionnaireResolveExcelVO resolveExcelAndSetRedis(InputStream inputStream) {
        List<QuestionnaireUserExcelVO> userExcelList = new ArrayList<>();

        ExcelUserReadListener excelUserReadListener = new ExcelUserReadListener(userFeignClient, userExcelList, mapperFacade);
        try {
            // 只读第一个sheet
            EasyExcel.read(inputStream, excelUserReadListener).sheet(0).doRead();
        } catch (Exception e) {
            throw new LuckException("请检查模板正确性");
        }
        String uuid = UUID.randomUUID().toString();
        // 去重(unordered()启用无序流加快去重速度，如果有顺序要求可以改用sequential()发生利用率低问题，使用正常模式)
        List<QuestionnaireUserExcelVO> distinctList = userExcelList.stream().unordered().distinct().collect(Collectors.toList());
        RedisUtil.set(KEY_PREFIX + uuid, distinctList, KEY_TIME);
        QuestionnaireResolveExcelVO questionnaireResolveExcelVO = new QuestionnaireResolveExcelVO();
        questionnaireResolveExcelVO.setRedisKey(uuid);
        questionnaireResolveExcelVO.setCount(distinctList.size());
        return questionnaireResolveExcelVO;
    }

    @Override
    public List<QuestionnaireUserExcelVO> listUserVOByKey(String key) {
        Boolean hasKey = RedisUtil.hasKey(KEY_PREFIX + key);
        if (hasKey){
            Object result = RedisUtil.get(KEY_PREFIX + key);
            if (Objects.nonNull(result)){
                return (List<QuestionnaireUserExcelVO>) result;
            }
        }

        return new ArrayList<>();
    }

    @Override
    public Long getDownLoadHisId(String time, String fileName) {
        CalcingDownloadRecordDTO downloadRecordDTO = new CalcingDownloadRecordDTO();
        downloadRecordDTO.setDownloadTime(new Date());
        downloadRecordDTO.setFileName(time + fileName);
        downloadRecordDTO.setCalCount(0);
        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
        downloadRecordDTO.setOperatorNo("1");
        ServerResponseEntity serverResponseEntity = downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
        Long downLoadHisId = null;
        if (serverResponseEntity.isSuccess()) {
            downLoadHisId = Long.parseLong(serverResponseEntity.getData().toString());
        }
        return downLoadHisId;
    }

    @Override
    public void uploadFileAndFinishDown(String fileName, String time, Long downLoadHisId, Integer listCount, String downFileName) {
        // 上传文件
        MultipartFile multipartFile = null;
        try {
            multipartFile = new MultipartFileDto(fileName, fileName,
                    ContentType.APPLICATION_OCTET_STREAM.toString(), Files.newInputStream(Paths.get(fileName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String mimoPath = "excel/" + time + "/" + originalFilename;
        ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
        if (responseEntity.isSuccess()) {
            FinishDownLoadDTO finishDownLoadDTO = new FinishDownLoadDTO();
            finishDownLoadDTO.setId(downLoadHisId);
            finishDownLoadDTO.setCalCount(listCount);
            finishDownLoadDTO.setFileName(time + downFileName);
            finishDownLoadDTO.setStatus(1);
            finishDownLoadDTO.setFileUrl(responseEntity.getData());
            finishDownLoadDTO.setRemarks("导出成功");
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
        }
    }

    @Override
    public void updateExcelCache(String redisKey, List<QuestionnaireUserExcelVO> questionnaireUserExcelVOList) {
        RedisUtil.del(KEY_PREFIX + redisKey);
        RedisUtil.set(KEY_PREFIX + redisKey, questionnaireUserExcelVOList, KEY_TIME);
    }
}
