package com.mall4j.cloud.group.service;

import com.mall4j.cloud.group.vo.questionnaire.QuestionnaireResolveExcelVO;
import com.mall4j.cloud.group.vo.questionnaire.QuestionnaireUserExcelVO;

import java.io.InputStream;
import java.util.List;

/**
 * 问卷调查ExcelService
 * @date 2023/5/30
 */
public interface QuestionnaireExcelService {

    /**
     * 解析excel用户手机号并加入缓存, redis key 为 uuid
     * @param inputStream excel io
     * @return vo
     */
    QuestionnaireResolveExcelVO resolveExcelAndSetRedis(InputStream inputStream);

    /**
     * 根据 key 获取缓存里面的用户信息
     * @param key uuidKey
     * @return list vo
     */
    List<QuestionnaireUserExcelVO> listUserVOByKey(String key);

    /**
     * 通过下载中心获取 hisId
     * @param time 时间
     * @param fileName 文件名称
     * @return hisId
     */
    Long getDownLoadHisId(String time, String fileName);

    /**
     * 下载中心完成下载
     * @param fileName 文件名称
     * @param time 时间
     * @param downLoadHisId hisId
     * @param listCount 数据量
     * @param downFileName 下载名称
     */
    void uploadFileAndFinishDown(String fileName, String time, Long downLoadHisId, Integer listCount, String downFileName);

    /**
     * 更新excel缓存, 先del后set
     * @param redisKey redis key name
     * @param questionnaireUserExcelVOList 变更的数据
     */
    void updateExcelCache(String redisKey, List<QuestionnaireUserExcelVO> questionnaireUserExcelVOList);
}
