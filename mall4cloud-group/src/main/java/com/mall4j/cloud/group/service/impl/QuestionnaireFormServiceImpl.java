package com.mall4j.cloud.group.service.impl;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.util.ExcelUtil;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.group.dto.questionnaire.QuestionnaireFormNames;
import com.mall4j.cloud.group.mapper.QuestionnaireUserAnswerRecordContentMapper;
import com.mall4j.cloud.group.mapper.QuestionnaireUserAnswerRecordMapper;
import com.mall4j.cloud.group.model.QuestionnaireForm;
import com.mall4j.cloud.group.mapper.QuestionnaireFormMapper;
import com.mall4j.cloud.group.model.QuestionnaireUserAnswerRecord;
import com.mall4j.cloud.group.model.QuestionnaireUserAnswerRecordContent;
import com.mall4j.cloud.group.service.QuestionnaireExcelService;
import com.mall4j.cloud.group.service.QuestionnaireFormService;
import com.mall4j.cloud.group.vo.questionnaire.QuestionnaireUserUnSubmitVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 问卷表单内容
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
@Service
public class QuestionnaireFormServiceImpl implements QuestionnaireFormService {

    @Autowired
    private QuestionnaireFormMapper questionnaireFormMapper;

    @Autowired
    private QuestionnaireUserAnswerRecordContentMapper questionnaireUserAnswerRecordContentMapper;

    @Autowired
    private QuestionnaireUserAnswerRecordMapper questionnaireUserAnswerRecordMapper;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private QuestionnaireExcelService questionnaireExcelService;

    @Override
    public PageVO<QuestionnaireForm> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> questionnaireFormMapper.list());
    }

    @Override
    public QuestionnaireForm getById(Long id) {
        return questionnaireFormMapper.getById(id);
    }

    @Override
    public void save(QuestionnaireForm questionnaireForm) {
        questionnaireFormMapper.save(questionnaireForm);
    }

    @Override
    public void update(QuestionnaireForm questionnaireForm) {
        questionnaireFormMapper.update(questionnaireForm);
    }

    @Override
    public void deleteById(Long id) {
        questionnaireFormMapper.deleteById(id);
    }

    @Override
    public void exportExcel(Long id, Boolean isSubmit) {
        String isSubmitStr = isSubmit? "(已提交)" : "(未提交)";
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = SkqUtils.getExcelFilePath() + "/" + time +"问卷调查" + isSubmitStr + ".xlsx";

        WriteSheet writeSheet;
        List writeData;
        Integer size = 0;
        if (isSubmit) {
            // 获取标题
            QuestionnaireForm questionnaireForm = questionnaireFormMapper.getById(id);
            String formNames = questionnaireForm.getFormNames();
            List<QuestionnaireFormNames> questionnaireFormNames = JSONObject.parseArray(formNames, QuestionnaireFormNames.class);
            List<String> tableNameList = new ArrayList<>();
            tableNameList.add("会员昵称");
            tableNameList.add("会员ID");
            tableNameList.add("提交时间");
            tableNameList.add("会员卡号");
            tableNameList.add("门店编码");
            tableNameList.add("门店名称");
            tableNameList.addAll(questionnaireFormNames.stream().map(QuestionnaireFormNames::getName).collect(Collectors.toList()));
            // 配置excel表头
            List<List<String>> easyExcelTableHeadList = new ArrayList<>();
            for (String s : tableNameList) {
                List<String> str = new ArrayList<>();
                str.add(s);
                easyExcelTableHeadList.add(str);
            }

            // 获取内容
            List<List<Object>> dataList = new ArrayList<>();
            List<QuestionnaireUserAnswerRecordContent> recordContentList = questionnaireUserAnswerRecordContentMapper.selectAllByActivityId(id);
            if (recordContentList.isEmpty()) {
                throw new LuckException("名单为空");
            }
            Set<Long> userIdSet = recordContentList.stream().map(QuestionnaireUserAnswerRecordContent::getUserId).collect(Collectors.toSet());
            List<QuestionnaireUserAnswerRecord> answerRecordList = questionnaireUserAnswerRecordMapper.selectAllByActivityIdInAndUserId(id, userIdSet);
            Map<Long, QuestionnaireUserAnswerRecord> userIdEntity = answerRecordList.stream().collect(Collectors.toMap(QuestionnaireUserAnswerRecord::getUserId, Function.identity()));

            if (recordContentList.isEmpty()) {
                throw new LuckException("名单为空");
            }
            // 配置excel 列数据
            for (QuestionnaireUserAnswerRecordContent recordContent : recordContentList) {
                List<Object> data = new ArrayList<>();
                Long userId = recordContent.getUserId();
                QuestionnaireUserAnswerRecord userAnswerRecord = userIdEntity.getOrDefault(userId, new QuestionnaireUserAnswerRecord());
                data.add(userAnswerRecord.getNickName());
                data.add(userAnswerRecord.getUserId());
                data.add(userAnswerRecord.getCreateTime());
                data.add(userAnswerRecord.getVipcode());
                data.add(userAnswerRecord.getStoreCode());
                data.add(userAnswerRecord.getStoreName());

                String content = recordContent.getContent();
                List<QuestionnaireFormNames> formNamesList = JSONObject.parseArray(content, QuestionnaireFormNames.class);
                for (QuestionnaireFormNames names : formNamesList) {
                    data.add(names.getValue());
                }
                dataList.add(data);
            }

            writeSheet = ExcelUtil.getDynamicHeadWriteSheet("sheet", easyExcelTableHeadList);
            writeData = dataList;
            size = recordContentList.size();
        } else {
            writeSheet = ExcelUtil.getWriteSheet("sheet", QuestionnaireUserUnSubmitVO.class);
            List<QuestionnaireUserUnSubmitVO> questionnaireUserUnSubmitVOS = questionnaireUserAnswerRecordMapper.selectCountUnSubmit(id);
            if (questionnaireUserUnSubmitVOS.isEmpty()) {
                throw new LuckException("名单为空");
            }
            writeData = questionnaireUserUnSubmitVOS;
            size = questionnaireUserUnSubmitVOS.size();
        }
        Long downLoadHisId = questionnaireExcelService.getDownLoadHisId(time, "问卷调查活动" + isSubmitStr + "ID:" + id);
        ExcelWriter excelWriter = ExcelUtil.getExcelWriter(fileName);
        ExcelUtil.write(writeData, excelWriter, writeSheet, true);
        questionnaireExcelService.uploadFileAndFinishDown(fileName, time, downLoadHisId, size, "问卷调查导出活动" + isSubmitStr + "ID:" + id);

    }

}
