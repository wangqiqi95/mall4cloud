package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.api.biz.dto.ExcelUploadDTO;
import com.mall4j.cloud.api.distribution.vo.DistributionActivityShareDetailsExcelVO;
import com.mall4j.cloud.api.distribution.vo.DistributionActivityShareExcelVO;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mall4j.cloud.distribution.dto.DistributionActivityShareRecordDTO;
import com.mall4j.cloud.distribution.dto.DistributionMomentsDTO;
import com.mall4j.cloud.distribution.mapper.DistributionActivityShareRecordMapper;
import com.mall4j.cloud.distribution.model.DistributionActivityShareRecord;
import com.mall4j.cloud.distribution.model.DistributionMoments;
import com.mall4j.cloud.distribution.model.DistributionPoster;
import com.mall4j.cloud.distribution.model.DistributionSpecialSubject;
import com.mall4j.cloud.distribution.service.*;
import com.mall4j.cloud.distribution.vo.DistributionShareRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 分销推广-活动分享效果
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@Service
@Slf4j
public class DistributionActivityShareRecordServiceImpl implements DistributionActivityShareRecordService {

    @Autowired
    private DistributionActivityShareRecordMapper distributionActivityShareRecordMapper;

    @Autowired
    private DistributionShareRecordService distributionShareRecordService;

    @Autowired
    private DistributionPurchaseRecordService distributionPurchaseRecordService;

    @Autowired
    private DistributionBrowseRecordService distributionBrowseRecordService;

    @Autowired
    private DistributionBuyRecordService distributionBuyRecordService;

    @Autowired
    private DistributionPosterService distributionPosterService;

    @Autowired
    private DistributionMomentsService distributionMomentsService;

    @Autowired
    private DistributionSpecialSubjectService distributionSpecialSubjectService;

    @Autowired
    private SpuFeignClient spuFeignClient;

    @Autowired
    private StaffFeignClient staffFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Autowired
    private OnsMQTemplate soldUploadExcelTemplate;


    @Override
    public PageVO<DistributionActivityShareRecord> page(PageDTO pageDTO, DistributionActivityShareRecordDTO dto) {
        PageVO<DistributionActivityShareRecord> page = PageUtil.doPage(pageDTO, () -> distributionActivityShareRecordMapper.list(dto));
        supplementDataIfNecessary(page, dto);
        return page;
    }

    private void supplementDataIfNecessary(PageVO<DistributionActivityShareRecord> page, DistributionActivityShareRecordDTO dto) {
        page.getList().forEach(distributionActivityShareRecord -> {
            if (distributionActivityShareRecord.getActivityType() != null) {


                // 商品类型查询spuCode spuName
                if (distributionActivityShareRecord.getActivityType() == 4 && distributionActivityShareRecord.getActivityId() != null) {
                    log.info("查询商品数据:{}", distributionActivityShareRecord.getActivityId());
                    ServerResponseEntity<SpuVO> entity = spuFeignClient.getById(distributionActivityShareRecord.getActivityId());
                    if (entity != null && entity.isSuccess()) {
                        SpuVO data = entity.getData();
                        log.info("商品编码数据:{}", data.getSpuCode());
                        distributionActivityShareRecord.setSpuCode(data.getSpuCode());
                        distributionActivityShareRecord.setSpuName(data.getName());
                    }
                }
                // 朋友圈
                if (distributionActivityShareRecord.getActivityType() == 3 && distributionActivityShareRecord.getActivityId() != null) {
                    DistributionMomentsDTO moments = distributionMomentsService.getMomentsById(distributionActivityShareRecord.getActivityId());
                    if (moments != null) {
                        distributionActivityShareRecord.setActivityName(moments.getTitle());
                    }
                }
                // 专题
                if (distributionActivityShareRecord.getActivityType() == 2 && distributionActivityShareRecord.getActivityId() != null) {
                    DistributionSpecialSubject specialSubject = distributionSpecialSubjectService.getById(distributionActivityShareRecord.getActivityId());
                    if (specialSubject != null) {
                        distributionActivityShareRecord.setActivityName(specialSubject.getSubjectName());
                    }
                }
                // 海报
                if (distributionActivityShareRecord.getActivityType() == 1 && distributionActivityShareRecord.getActivityId() != null) {
                    DistributionPoster poster = distributionPosterService.getById(distributionActivityShareRecord.getActivityId());
                    if (poster != null) {
                        distributionActivityShareRecord.setActivityName(poster.getPosterName());
                    }
                }
            }
            if (null != dto.getQueryStartDate() && null != dto.getQueryEndDate()) {
                distributionActivityShareRecord.setShareNum(distributionShareRecordService.countByActivityAndDate(distributionActivityShareRecord.getActivityId(), distributionActivityShareRecord.getActivityType(), dto.getQueryStartDate(), dto.getQueryEndDate()));
                distributionActivityShareRecord.setPurchaseNum(distributionPurchaseRecordService.countNumByActivityAndDate(distributionActivityShareRecord.getActivityId(), distributionActivityShareRecord.getActivityType(), dto.getQueryStartDate(), dto.getQueryEndDate()));
                distributionActivityShareRecord.setPurchaseUserNum(distributionPurchaseRecordService.countUserNumByActivityAndDate(distributionActivityShareRecord.getActivityId(), distributionActivityShareRecord.getActivityType(), dto.getQueryStartDate(), dto.getQueryEndDate()));
                distributionActivityShareRecord.setBrowseNum(distributionBrowseRecordService.countNumByActivityAndDate(distributionActivityShareRecord.getActivityId(), distributionActivityShareRecord.getActivityType(), dto.getQueryStartDate(), dto.getQueryEndDate()));
                distributionActivityShareRecord.setBrowseUserNum(distributionBrowseRecordService.countUserNumByActivityAndDate(distributionActivityShareRecord.getActivityId(), distributionActivityShareRecord.getActivityType(), dto.getQueryStartDate(), dto.getQueryEndDate()));
                distributionActivityShareRecord.setBuyNum(distributionBuyRecordService.countNumByActivityAndDate(distributionActivityShareRecord.getActivityId(), distributionActivityShareRecord.getActivityType(), dto.getQueryStartDate(), dto.getQueryEndDate()));
                distributionActivityShareRecord.setBuyUserNum(distributionBuyRecordService.countUserNumByActivityAndDate(distributionActivityShareRecord.getActivityId(), distributionActivityShareRecord.getActivityType(), dto.getQueryStartDate(), dto.getQueryEndDate()));
            }
        });
    }

    @Override
    public DistributionActivityShareRecord getById(Long id) {
        return distributionActivityShareRecordMapper.getById(id);
    }

    @Override
    public void save(DistributionActivityShareRecord distributionActivityShareRecord) {
        distributionActivityShareRecordMapper.save(distributionActivityShareRecord);
    }

    @Override
    public void update(DistributionActivityShareRecord distributionActivityShareRecord) {
        distributionActivityShareRecordMapper.update(distributionActivityShareRecord);
    }

    @Override
    public void deleteById(Long id) {
        distributionActivityShareRecordMapper.deleteById(id);
    }

    @Override
    public DistributionActivityShareRecord getByActivity(Long activityId, Integer activityType) {
        return distributionActivityShareRecordMapper.getByActivity(activityId, activityType);
    }

    @Override
    public void exportActivityShareRecord(DistributionActivityShareRecordDTO dto) {
        int totalPage = 1;
        int currentPage = 1;

        int pageSize = 2000;

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageNum(currentPage);
        pageDTO.setPageSize(pageSize);
        PageVO<DistributionActivityShareRecord> pageVO = PageUtil.doPage(pageDTO, () -> distributionActivityShareRecordMapper.list(dto));
        supplementDataIfNecessary(pageVO, dto);
        totalPage = pageVO.getTotal().intValue() / pageSize + 1;
        doExportExcel(pageVO);


        /**
         * 根据总条数拆分要导出的数据， 拆分excel。
         * 每2000条记录拆分一个文件
         */
        for (int i = 2; i < totalPage; i++) {
            PageDTO param = new PageDTO();
            param.setPageNum(i);
            param.setPageSize(pageSize);
            PageVO<DistributionActivityShareRecord> pageStoreStatistics = PageUtil.doPage(pageDTO, () -> distributionActivityShareRecordMapper.list(dto));
            supplementDataIfNecessary(pageStoreStatistics, dto);
            doExportExcel(pageStoreStatistics);
        }
    }

    @Override
    public void exportActivityShareDetails(DistributionActivityShareRecordDTO dto) {
        String activityName = "";
        if (dto.getActivityType() == 1){
            DistributionPoster poster = distributionPosterService.getById(dto.getActivityId());
            if (null != poster){
                activityName = poster.getPosterName();
            }
        } else if (dto.getActivityType() == 2){
            DistributionSpecialSubject specialSubject = distributionSpecialSubjectService.getById(dto.getActivityId());
            if (null != specialSubject){
                activityName = specialSubject.getSubjectName();
            }
        } else if (dto.getActivityType() == 3){
            DistributionMoments moments = distributionMomentsService.getById(dto.getActivityId());
            if (null != moments){
                activityName = moments.getTitle();
            }
        } else if (dto.getActivityType() == 4){
            ServerResponseEntity<SpuVO> supData = spuFeignClient.getById(dto.getActivityId());
            if (supData.isSuccess() && null != supData.getData()){
                activityName = supData.getData().getName();
            }
        }
        List<DistributionShareRecordVO> distributionShareRecordVOS = distributionShareRecordService.listStaffByActivity(dto.getActivityId(), dto.getActivityType(), dto.getQueryStartDate(), dto.getQueryEndDate());
        if (CollectionUtils.isEmpty(distributionShareRecordVOS)){
            throw new LuckException("暂无导购分享数据");
        }
        Map<Long, StaffVO> staffMap = new HashMap<>(2);
        ServerResponseEntity<List<StaffVO>> staffListData = staffFeignClient.getStaffByIds(distributionShareRecordVOS.stream().map(DistributionShareRecordVO::getShareId).collect(Collectors.toList()));
        if (staffListData.isSuccess() && CollectionUtils.isNotEmpty(staffListData.getData())){
            staffMap = LambdaUtils.toMap(staffListData.getData(), StaffVO::getId);
        }
        List<DistributionActivityShareDetailsExcelVO> distributionActivityShareDetailsExcelVOList = new ArrayList<>();
        Map<Long, StaffVO> finalStaffMap = staffMap;
        String finalActivityName = activityName;
        distributionShareRecordVOS.forEach(distributionBrowseRecordVO -> {
            DistributionActivityShareDetailsExcelVO vo = new DistributionActivityShareDetailsExcelVO();
            vo.setActivityId(dto.getActivityId());
            vo.setActivityName(finalActivityName);
            if (null != finalStaffMap.get(distributionBrowseRecordVO.getShareId())){
                vo.setStaffName(finalStaffMap.get(distributionBrowseRecordVO.getShareId()).getStaffName());
                vo.setStaffNo(finalStaffMap.get(distributionBrowseRecordVO.getShareId()).getStaffNo());
            }
            vo.setType(buildType(dto.getActivityType()));
            vo.setShareNum(distributionBrowseRecordVO.getTotalNum());
            vo.setBrowseNum(distributionBrowseRecordService.countNumByActivityAndDate(distributionBrowseRecordVO.getShareId(), dto.getActivityId(), dto.getActivityType(), dto.getQueryStartDate(), dto.getQueryEndDate()));
            vo.setBrowseUserNum(distributionBrowseRecordService.countUserNumByActivityAndDate(distributionBrowseRecordVO.getShareId(), dto.getActivityId(), dto.getActivityType(), dto.getQueryStartDate(), dto.getQueryEndDate()));
            vo.setPurchaseNum(distributionPurchaseRecordService.countNumByShareActivityAndDate(distributionBrowseRecordVO.getShareId(), dto.getActivityId(), dto.getActivityType(), dto.getQueryStartDate(), dto.getQueryEndDate()));
            vo.setPurchaseUserNum(distributionPurchaseRecordService.countUserNumByShareActivityAndDate(distributionBrowseRecordVO.getShareId(), dto.getActivityId(), dto.getActivityType(), dto.getQueryStartDate(), dto.getQueryEndDate()));
            distributionActivityShareDetailsExcelVOList.add(vo);
        });
        CalcingDownloadRecordDTO downloadRecordDTO = new CalcingDownloadRecordDTO();
        downloadRecordDTO.setDownloadTime(new Date());
        downloadRecordDTO.setFileName(DistributionActivityShareDetailsExcelVO.EXCEL_NAME);
        downloadRecordDTO.setCalCount(distributionActivityShareDetailsExcelVOList.size());
        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
        downloadRecordDTO.setOperatorNo("" + AuthUserContext.get().getUserId());
        ServerResponseEntity serverResponseEntity = downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
        Long downLoadHisId = null;
        if (serverResponseEntity.isSuccess()) {
            downLoadHisId = Long.parseLong(serverResponseEntity.getData().toString());
        }

        ExcelUploadDTO excelUploadDTO = new ExcelUploadDTO(downLoadHisId,
                distributionActivityShareDetailsExcelVOList,
                DistributionActivityShareDetailsExcelVO.EXCEL_NAME,
                DistributionActivityShareDetailsExcelVO.MERGE_ROW_INDEX,
                DistributionActivityShareDetailsExcelVO.MERGE_COLUMN_INDEX,
                DistributionActivityShareDetailsExcelVO.class);
        soldUploadExcelTemplate.syncSend(excelUploadDTO);
    }

    private void doExportExcel(PageVO<DistributionActivityShareRecord> pageVO) {
        if (CollectionUtils.isEmpty(pageVO.getList())){
            return;
        }
        List<DistributionActivityShareExcelVO> distributionActivityShareExcelVOList = new ArrayList<>();
        pageVO.getList().forEach(distributionActivityShareRecord -> {
            DistributionActivityShareExcelVO vo = new DistributionActivityShareExcelVO();
            BeanUtils.copyProperties(distributionActivityShareRecord, vo);
            vo.setType(buildType(distributionActivityShareRecord.getActivityType()));
            distributionActivityShareExcelVOList.add(vo);
        });
        CalcingDownloadRecordDTO downloadRecordDTO = new CalcingDownloadRecordDTO();
        downloadRecordDTO.setDownloadTime(new Date());
        downloadRecordDTO.setFileName(DistributionActivityShareExcelVO.EXCEL_NAME);
        downloadRecordDTO.setCalCount(distributionActivityShareExcelVOList.size());
        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
        downloadRecordDTO.setOperatorNo("" + AuthUserContext.get().getUserId());
        ServerResponseEntity serverResponseEntity = downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
        Long downLoadHisId = null;
        if (serverResponseEntity.isSuccess()) {
            downLoadHisId = Long.parseLong(serverResponseEntity.getData().toString());
        }

        ExcelUploadDTO excelUploadDTO = new ExcelUploadDTO(downLoadHisId,
                distributionActivityShareExcelVOList,
                DistributionActivityShareExcelVO.EXCEL_NAME,
                DistributionActivityShareExcelVO.MERGE_ROW_INDEX,
                DistributionActivityShareExcelVO.MERGE_COLUMN_INDEX,
                DistributionActivityShareExcelVO.class);
        soldUploadExcelTemplate.syncSend(excelUploadDTO);
    }

    private String buildType(Integer type) {
        if (null == type) {
            return null;
        }
        switch (type) {
            case 1:
                return "海报";
            case 2:
                return "专题";
            case 3:
                return "朋友圈";
            case 4:
                return "商品";
            default:
                return null;
        }
    }
}
