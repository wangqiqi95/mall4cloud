package com.mall4j.cloud.coupon.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.coupon.bo.ExportUserToCreateEventBO;
import com.mall4j.cloud.coupon.dto.ChooseMemberEventMobileRelationPageDTO;
import com.mall4j.cloud.coupon.mapper.ChooseMemberEventMobileRelationMapper;
import com.mall4j.cloud.coupon.model.ChooseMemberEventMobileRelation;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventMobileRelationVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChooseMemberEventMobileRelationManager {


    @Autowired
    private ChooseMemberEventMobileRelationMapper chooseMemberEventUserRelationMapper;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Transactional(rollbackFor = Exception.class)
    public void addEventUserRelationBatch(Long eventId, List<String> mobileList){

        List<ChooseMemberEventMobileRelation> chooseMemberEventMobileRelationList = mobileList.stream().map(mobile -> {
            ChooseMemberEventMobileRelation eventUserRelation = new ChooseMemberEventMobileRelation();
            eventUserRelation.setMobile(mobile);
            eventUserRelation.setChooseMemberEventId(eventId);
            return eventUserRelation;
        }).collect(Collectors.toList());

        chooseMemberEventUserRelationMapper.insertBatch(chooseMemberEventMobileRelationList);

    }

    public ChooseMemberEventMobileRelation checkEventUserRelation(Long eventId, String mobile){
        ChooseMemberEventMobileRelation eventUserRelation = chooseMemberEventUserRelationMapper.selectOne(
                new LambdaQueryWrapper<ChooseMemberEventMobileRelation>()
                        .eq(ChooseMemberEventMobileRelation::getChooseMemberEventId, eventId)
                        .eq(ChooseMemberEventMobileRelation::getMobile, mobile)
        );
        return eventUserRelation;
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeRelationByEventId(Long eventId){
        chooseMemberEventUserRelationMapper.delete(
                new LambdaQueryWrapper<ChooseMemberEventMobileRelation>()
                        .eq(ChooseMemberEventMobileRelation::getChooseMemberEventId, eventId)
        );
    }

    public Integer getMobileCountByEventId(Long eventId){

        Integer memberCount = chooseMemberEventUserRelationMapper.selectCount(
                new LambdaQueryWrapper<ChooseMemberEventMobileRelation>()
                        .eq(ChooseMemberEventMobileRelation::getChooseMemberEventId, eventId)
        );

        return memberCount;
    }

    public PageVO<ChooseMemberEventMobileRelationVO> getMobileRelationList(ChooseMemberEventMobileRelationPageDTO pageDTO){
        LambdaQueryWrapper<ChooseMemberEventMobileRelation> queryWrapper = new LambdaQueryWrapper<ChooseMemberEventMobileRelation>()
                .eq(ChooseMemberEventMobileRelation::getChooseMemberEventId, pageDTO.getEventId());
        if (StringUtils.isNotEmpty(pageDTO.getMobile())){
            queryWrapper.eq(ChooseMemberEventMobileRelation::getMobile, pageDTO.getMobile());
        }

        Page<ChooseMemberEventMobileRelation> relationPage = chooseMemberEventUserRelationMapper
                .selectPage(new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize()), queryWrapper);

        PageVO<ChooseMemberEventMobileRelationVO> relationVOPage = new PageVO<ChooseMemberEventMobileRelationVO>();
        relationVOPage.setPages(Long.valueOf(relationPage.getPages()).intValue());
        relationVOPage.setTotal(relationPage.getTotal());

        List<ChooseMemberEventMobileRelationVO> mobileRelationVOList = relationPage.getRecords().stream()
                .map(relation -> {
                    ChooseMemberEventMobileRelationVO mobileRelationVO = new ChooseMemberEventMobileRelationVO();
                    BeanUtils.copyProperties(relation, mobileRelationVO);
                    return mobileRelationVO;
                }).collect(Collectors.toList());

        relationVOPage.setList(mobileRelationVOList);

        return relationVOPage;
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeRelationByIdListAndEventId(List<Long> relationIdList, Long eventId){
        chooseMemberEventUserRelationMapper.delete(
                new LambdaQueryWrapper<ChooseMemberEventMobileRelation>()
                        .in(ChooseMemberEventMobileRelation::getEventUserRelationId, relationIdList)
                        .eq(ChooseMemberEventMobileRelation::getChooseMemberEventId, eventId)
        );
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

    public Boolean checkDownloadData(List downLoadDataList, FinishDownLoadDTO finishDownLoadDTO){
        if (CollectionUtil.isEmpty(downLoadDataList)){
            finishDownLoadDTO.setRemarks("无会员名单数据导出");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error("无会员名单数据导出");
            return true;
        }
        return false;
    }


    public String createExcelFile(List<ExportUserToCreateEventBO> recordExcelVOList, String  fileName){
        String file=null;
        try {
            String pathExport= SkqUtils.getExcelFilePath()+"/"+fileName+".xlsx";
            EasyExcel.write(pathExport, ExportUserToCreateEventBO.class).sheet(ExcelModel.SHEET_NAME).doWrite(recordExcelVOList);
            return pathExport;//返回文件生成路径
        }catch (Exception e){
            log.error("会员礼物活动会员名单导出异常，message is:{}",e);
        }
        return file;
    }

}
