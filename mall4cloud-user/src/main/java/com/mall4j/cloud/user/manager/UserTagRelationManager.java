package com.mall4j.cloud.user.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.SystemUtils;
import com.mall4j.cloud.user.bo.ExcelMarkingUserPageBO;
import com.mall4j.cloud.user.bo.ImportTagUserFailBO;
import com.mall4j.cloud.user.bo.UserTagRelationBO;
import com.mall4j.cloud.user.dto.QueryMarkingUserPageDTO;
import com.mall4j.cloud.user.mapper.UserTagRelationMapper;
import com.mall4j.cloud.user.model.UserTagRelation;
import com.mall4j.cloud.user.vo.MarkingUserPageVO;
import com.mall4j.cloud.user.vo.UserTagListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserTagRelationManager {

    @Autowired
    private UserTagRelationMapper userTagRelationMapper;

    @Autowired
    private StoreFeignClient storeFeignClient;

    @Autowired
    private StaffFeignClient staffFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;


    @Transactional(rollbackFor = Exception.class)
    public void addList(List<UserTagRelationBO> relationBOList){
        userTagRelationMapper.insertBatch(relationBOList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addListForImport(List<String> codeList, Long tagId, Long groupId,
                                 Long groupTagRelationId, Long createUser){
        userTagRelationMapper.insertBatchForImport(codeList, tagId, groupId, groupTagRelationId, createUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeListByTagIdAndGroupId(Long tagId, Long groupId){
        userTagRelationMapper.delete(
                new LambdaQueryWrapper<UserTagRelation>()
                        .eq(UserTagRelation::getGroupId, groupId)
                        .eq(UserTagRelation::getTagId, tagId)
        );
    }

    public List<UserTagRelationBO> exportVipCodes(MultipartFile file){
        log.info("导入文件为：{}", file);
        if (file == null) {
            throw new LuckException("导入文件不能为空！");
        }

        // 1.获取上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            log.error("获取输入流异常");
            e.printStackTrace();
        }

        List<UserTagRelationBO> importObjects = new ArrayList<>();
        //调用 hutool 方法读取数据 默认调用第一个sheet
        EasyExcel.read(inputStream, UserTagRelationBO.class, new AnalysisEventListener<UserTagRelationBO>() {

            @Override
            public void invoke(UserTagRelationBO relationBO, AnalysisContext analysisContext) {
                importObjects.add(relationBO);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {

            }
        }).sheet().doRead();
//        log.info("它很长：{}",importObjects.size());
        return importObjects;
    }

    public IPage<MarkingUserPageVO> getMarkingUserByTagPage(QueryMarkingUserPageDTO pageDTO){

        IPage<MarkingUserPageVO> markingUserPageVOIPage = new Page<>(pageDTO.getPageNum().longValue(), pageDTO.getPageSize().longValue());

        markingUserPageVOIPage = userTagRelationMapper.getTheMarkingUser(markingUserPageVOIPage, pageDTO);

        return markingUserPageVOIPage;

    }

    public List<StoreVO> getServiceStoreList(List<Long> serviceStoreIdList){
        ServerResponseEntity<List<StoreVO>> serverResponseEntity = storeFeignClient.listByStoreIdList(serviceStoreIdList);

        if (serverResponseEntity.isSuccess() && CollectionUtil.isNotEmpty(serverResponseEntity.getData())){
            return serverResponseEntity.getData();
        }

        return null;
    }

    public List<StaffVO> getStaffList(List<Long> staffIdList){
        ServerResponseEntity<List<StaffVO>> serverResponseEntity = staffFeignClient.getStaffByIds(staffIdList);

        if (serverResponseEntity.isSuccess() && CollectionUtil.isNotEmpty(serverResponseEntity.getData())){
            return serverResponseEntity.getData();
        }

        return null;
    }

    public List<StaffVO> getStaffListManager(List<Long> serviceStoreIdList){
        log.info("BEGIN TO GET THE STORE MANAGER LIST, PARAM IS:{}",serviceStoreIdList);
        ServerResponseEntity<List<StaffVO>> serverResponseEntity = staffFeignClient.getStaffListManager(serviceStoreIdList);
        log.info("GET THE STORE MANAGER LIST, RESPONSE IS:{}", JSON.toJSONString(serverResponseEntity));
        if (serverResponseEntity.isSuccess()){
            return serverResponseEntity.getData();
        }

        throw new LuckException("获取门店管理员失败");
    }

    public void removeBatchByTagIdAndVipCodeList(Long tagId, List<String> vipCodeList){
        userTagRelationMapper.delete(
                new LambdaQueryWrapper<UserTagRelation>()
                        .eq(UserTagRelation::getTagId, tagId)
                        .in(UserTagRelation::getVipCode, vipCodeList)
        );
    }

    public Integer getTheTagVipCount(Long tagId){
        Integer useCount = userTagRelationMapper.selectCount(
                new LambdaQueryWrapper<UserTagRelation>()
                        .eq(UserTagRelation::getTagId, tagId)
        );

        return useCount;
    }

    /**
     * 导购根据【是否加好友状态】查询会员
     * @param weChatType 是否加好友状态 1:是 2:否
     * @param staffId 导购用户ID
     * @return
     */
    public List<Long> staffGetUserIdByWeChatType(Integer weChatType, Long staffId){
        return userTagRelationMapper.staffGetUserIdByWeChatType(weChatType, staffId);
    }


    public FinishDownLoadDTO wrapperDownLoadCenterParam(String fileName){
        CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
        downloadRecordDTO.setDownloadTime(new Date());
        downloadRecordDTO.setFileName(fileName);
        downloadRecordDTO.setCalCount(0);
        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
        downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
        ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);

        if(serverResponseEntity.isSuccess()){
            Long downLoadHisId = Long.parseLong(serverResponseEntity.getData().toString());
            FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
            finishDownLoadDTO.setId(downLoadHisId);
            finishDownLoadDTO.setFileName(fileName);
            return finishDownLoadDTO;
        }


        return null;
    }

    public Boolean checkDownloadData(List downLoadDataList, FinishDownLoadDTO finishDownLoadDTO, String remark){
        if (CollectionUtil.isEmpty(downLoadDataList)){
            finishDownLoadDTO.setRemarks(remark);
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error(remark);
            return true;
        }
        return false;
    }


    public String createExcelFile(List<ExcelMarkingUserPageBO> recordExcelVOList, String  fileName){
        String file=null;
        try {
            String pathExport= SystemUtils.getExcelFilePath()+"/"+fileName+".xlsx";
            EasyExcel.write(pathExport, ExcelMarkingUserPageBO.class).sheet(ExcelModel.SHEET_NAME).doWrite(recordExcelVOList);
            return pathExport;//返回文件生成路径
        }catch (Exception e){
            log.error("标签会员名单导出异常，message is:{}",e);
        }
        return file;
    }

    public String createFailBOExcelFile(List<ImportTagUserFailBO> failBOList, String  fileName){
        String file=null;
        try {
            String pathExport= SystemUtils.getExcelFilePath()+"/"+fileName+".xlsx";
            EasyExcel.write(pathExport, ImportTagUserFailBO.class).sheet(ExcelModel.SHEET_NAME).doWrite(failBOList);
            return pathExport;//返回文件生成路径
        }catch (Exception e){
            log.error("标签会员名单导出异常，message is:{}",e);
        }
        return file;
    }

    public List<String> getVipCodeByTagId(Long tagId){
        List<String> vipCodeList = userTagRelationMapper.selectVipCodeByTagId(tagId);
        return vipCodeList;
    }

    public List<UserTagListVO> getTagByVipCode( String vipCode){
        return userTagRelationMapper.selectTagByVipCode(vipCode);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeListByVipCode(String vipCode){
        userTagRelationMapper.delete(
                new LambdaQueryWrapper<UserTagRelation>()
                        .eq(UserTagRelation::getVipCode, vipCode)
        );
    }

}
