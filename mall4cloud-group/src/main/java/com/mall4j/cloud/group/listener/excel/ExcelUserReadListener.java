package com.mall4j.cloud.group.listener.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.RowTypeEnum;
//import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.ReadListener;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.vo.questionnaire.QuestionnaireUserExcelVO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author baiqingtao
 * @date 2023/5/19
 */
@Slf4j
public class ExcelUserReadListener implements ReadListener<Map<Integer, String>> {

    private final UserFeignClient userFeignClient;

    List<QuestionnaireUserExcelVO> userExcelList;

    // 控制最大手机号读取限制，防止查询时间过长、拼接过长
    final Integer PHONE_MAX_SIZE = 2000;

    List<String> phoneList = new ArrayList<>();

    // 控制最大用户录入限制
    final Integer USER_MAX_SIZE = 10000;

    private final MapperFacade mapperFacade;

    public ExcelUserReadListener(UserFeignClient userFeignClient,
                                 List<QuestionnaireUserExcelVO> userExcelList,
                                 MapperFacade mapperFacade) {
        this.userFeignClient = userFeignClient;
        this.userExcelList = userExcelList;
        this.mapperFacade = mapperFacade;
    }

    @Override
    public void onException(Exception e, AnalysisContext analysisContext) throws Exception {

    }

//    @Override
//    public void invokeHead(Map<Integer, CellData> map, AnalysisContext analysisContext) {
//
//    }

    @Override
    public void invoke(Map<Integer, String> objectObjectMap, AnalysisContext analysisContext) {
        String phone = objectObjectMap.get(0);
        phoneList.add(phone);
        if (phoneList.size() >= PHONE_MAX_SIZE){
            selectUser();
        }
    }

    @Override
    public void extra(CellExtra cellExtra, AnalysisContext analysisContext) {

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (phoneList.size() > 0){
            selectUser();
        }
    }

    @Override
    public boolean hasNext(AnalysisContext analysisContext) {
        if (userExcelList.size() > USER_MAX_SIZE){
            return false;
        }

        return analysisContext.readRowHolder().getRowType().equals(RowTypeEnum.DATA);
    }

    private void selectUser(){
        ServerResponseEntity<List<UserApiVO>> response = userFeignClient.listByPhone(phoneList);
        List<UserApiVO> data = response.getData();
        if (!data.isEmpty()){
            List<QuestionnaireUserExcelVO> userExcelVOList = mapperFacade.mapAsList(data, QuestionnaireUserExcelVO.class);
            // 加入用户数
            if (userExcelList.size() + userExcelVOList.size() > USER_MAX_SIZE){
                int iSize = USER_MAX_SIZE - userExcelList.size();
                for (int i = 0; i < iSize; i++) {
                    userExcelList.add(userExcelVOList.get(i));
                }
            }else {
                userExcelList.addAll(userExcelVOList);
            }
        }
        phoneList.clear();
    }

}
