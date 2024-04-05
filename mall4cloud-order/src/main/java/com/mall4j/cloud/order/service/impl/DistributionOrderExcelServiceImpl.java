package com.mall4j.cloud.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.mall4j.cloud.api.biz.dto.ExcelUploadDTO;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.vo.DistributionStaffExcelVO;
import com.mall4j.cloud.api.order.vo.DistributionStoreExcelVO;
import com.mall4j.cloud.api.order.vo.DistributionWitkeyExcelVO;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.dto.UserQueryDTO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.order.dto.app.DistributionSalesStatDTO;
import com.mall4j.cloud.order.mapper.OrderMapper;
import com.mall4j.cloud.order.service.DistributionOrderExcelService;
import com.mall4j.cloud.order.service.OrderRefundService;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.vo.DistributionStoreStatisticsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author ZengFanChang
 * @Date 2022/04/05
 */
@Slf4j
@Service
public class DistributionOrderExcelServiceImpl implements DistributionOrderExcelService {

    @Resource
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRefundService orderRefundService;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Autowired
    private OnsMQTemplate soldUploadExcelTemplate;

    @Autowired
    private StaffFeignClient staffFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;


    @Override
    @Async
    public void exportStoreStatistics(DistributionSalesStatDTO distributionSalesStatDTO) {
//        int totalPage = 1;
//        int currentPage = 1;
//
//        int pageSize = 2000;
//
//        PageDTO pageDTO = new PageDTO();
//        pageDTO.setPageNum(currentPage);
//        pageDTO.setPageSize(pageSize);
//        PageVO<DistributionStoreStatisticsVO> pageVO = orderService.pageStoreStatistics(pageDTO, distributionSalesStatDTO);
//        totalPage = pageVO.getTotal().intValue() / pageSize + 1;
//        doStoreExportExcel(pageVO, distributionSalesStatDTO);
//
//
//        /**
//         * 根据总条数拆分要导出的数据， 拆分excel。
//         * 每2000条记录拆分一个文件
//         */
//        for (int i = 2; i < totalPage; i++) {
//            PageDTO param = new PageDTO();
//            param.setPageNum(i);
//            param.setPageSize(pageSize);
//            PageVO<DistributionStoreStatisticsVO> pageStoreStatistics = orderService.pageStoreStatistics(pageDTO, distributionSalesStatDTO);
//            doStoreExportExcel(pageStoreStatistics,distributionSalesStatDTO);
//        }

        CalcingDownloadRecordDTO downloadRecordDTO = new CalcingDownloadRecordDTO();
        downloadRecordDTO.setDownloadTime(new Date());
        downloadRecordDTO.setFileName(DistributionStoreExcelVO.EXCEL_NAME);
        downloadRecordDTO.setCalCount(0);
        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
        downloadRecordDTO.setOperatorNo("" + AuthUserContext.get().getUserId());
        ServerResponseEntity serverResponseEntity = downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
        Long downLoadHisId = null;
        if (serverResponseEntity.isSuccess()) {
            downLoadHisId = Long.parseLong(serverResponseEntity.getData().toString());
        }

        List<DistributionStoreStatisticsVO> list = orderService.storeStatistics(distributionSalesStatDTO);
        doStoreExportExcel(list,distributionSalesStatDTO,downLoadHisId);
    }

    @Async
    @Override
    public void exportStaffStatistics(DistributionSalesStatDTO distributionSalesStatDTO) {
//        int totalPage = 1;
//        int currentPage = 1;
//
//        int pageSize = 2000;
//
//        PageDTO pageDTO = new PageDTO();
//        pageDTO.setPageNum(currentPage);
//        pageDTO.setPageSize(pageSize,true);
//        PageVO<DistributionStaffExcelVO> objectPageVO = PageUtil.doPage(pageDTO, () -> orderMapper.listDistributionStaffExcel(distributionSalesStatDTO));
//        totalPage = objectPageVO.getTotal().intValue()/pageSize + 1;
//        doStaffExportExcel(objectPageVO, distributionSalesStatDTO);
//
//
//        /**
//         * 根据总条数拆分要导出的数据， 拆分excel。
//         * 每2000条记录拆分一个文件
//         */
//        for(int i = 2 ; i< totalPage ; i ++){
//            PageDTO param = new PageDTO();
//            param.setPageNum(i);
//            param.setPageSize(pageSize);
//            PageVO<DistributionStaffExcelVO> pageVO = PageUtil.doPage(pageDTO, () -> orderMapper.listDistributionStaffExcel(distributionSalesStatDTO));
//            doStaffExportExcel(pageVO, distributionSalesStatDTO);
//        }

        CalcingDownloadRecordDTO downloadRecordDTO = new CalcingDownloadRecordDTO();
        downloadRecordDTO.setDownloadTime(new Date());
        downloadRecordDTO.setFileName(DistributionStaffExcelVO.EXCEL_NAME);
        downloadRecordDTO.setCalCount(0);
        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
        downloadRecordDTO.setOperatorNo("" + AuthUserContext.get().getUserId());
        ServerResponseEntity serverResponseEntity = downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
        Long downLoadHisId = null;
        if (serverResponseEntity.isSuccess()) {
            downLoadHisId = Long.parseLong(serverResponseEntity.getData().toString());
        }

        List<DistributionStaffExcelVO> list = orderMapper.listDistributionStaffExcel(distributionSalesStatDTO);
        doStaffExportExcel(list, distributionSalesStatDTO,downLoadHisId);
    }

    @Override
    public void exportWitkeyStatistics(DistributionSalesStatDTO distributionSalesStatDTO) {
        int totalPage = 1;
        int currentPage = 1;

        int pageSize = 2000;

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageNum(currentPage);
        pageDTO.setPageSize(pageSize);
        PageVO<DistributionWitkeyExcelVO> objectPageVO = PageUtil.doPage(pageDTO, () -> orderMapper.listDistributionWitkeyExcel(distributionSalesStatDTO));
        totalPage = objectPageVO.getTotal().intValue()/pageSize + 1;
        doWitkeyExportExcel(objectPageVO, distributionSalesStatDTO);


        /**
         * 根据总条数拆分要导出的数据， 拆分excel。
         * 每2000条记录拆分一个文件
         */
        for(int i = 2 ; i< totalPage ; i ++){
            PageDTO param = new PageDTO();
            param.setPageNum(i);
            param.setPageSize(pageSize);
            PageVO<DistributionWitkeyExcelVO> pageVO = PageUtil.doPage(pageDTO, () -> orderMapper.listDistributionWitkeyExcel(distributionSalesStatDTO));
            doWitkeyExportExcel(pageVO, distributionSalesStatDTO);
        }
    }

    private void doWitkeyExportExcel(PageVO<DistributionWitkeyExcelVO> objectPageVO, DistributionSalesStatDTO distributionSalesStatDTO) {
        if (CollectionUtils.isEmpty(objectPageVO.getList())){
            return;
        }
        Map<Long, UserApiVO> userMap = new HashMap<>(2);
        Map<Long, StaffVO> staffMap = new HashMap<>(2);
        Map<Long, StoreVO> storeMap = new HashMap<>(2);
        ServerResponseEntity<List<UserApiVO>> userListData = userFeignClient.getUserBypByUserIds(objectPageVO.getList().stream().map(DistributionWitkeyExcelVO::getUserId).collect(Collectors.toList()));
        if (userListData.isSuccess() && CollectionUtils.isNotEmpty(userListData.getData())){
            userMap = LambdaUtils.toMap(userListData.getData(), UserApiVO::getUserId);
            ServerResponseEntity<List<StaffVO>> staffListData = staffFeignClient.getStaffBypByIds(userListData.getData().stream().map(UserApiVO::getStaffId).distinct().collect((Collectors.toList())));
            if (staffListData.isSuccess() && CollectionUtils.isNotEmpty(staffListData.getData())){
                staffMap = LambdaUtils.toMap(staffListData.getData(), StaffVO::getId);
            }
        }
        ServerResponseEntity<List<StoreVO>> storeListData = storeFeignClient.listBypByStoreIdList(objectPageVO.getList().stream().map(DistributionWitkeyExcelVO::getStoreId).collect(Collectors.toList()));
        if (storeListData.isSuccess() && CollectionUtils.isNotEmpty(storeListData.getData())){
            storeMap = LambdaUtils.toMap(storeListData.getData(), StoreVO::getStoreId);
        }
        Map<Long, StoreVO> finalStoreMap = storeMap;
        Map<Long, StaffVO> finalStaffMap = staffMap;
        Map<Long, UserApiVO> finalUserMap = userMap;
        distributionSalesStatDTO.setDistributionUserType(2);
        objectPageVO.getList().forEach(distributionWitkeyExcelVO -> {
            if (null != finalUserMap.get(distributionWitkeyExcelVO.getUserId())){
                UserApiVO userApiVO = finalUserMap.get(distributionWitkeyExcelVO.getUserId());
                distributionWitkeyExcelVO.setUserNo(userApiVO.getVipcode());
                distributionWitkeyExcelVO.setMobile(userApiVO.getPhone());
                distributionWitkeyExcelVO.setWitkeyPassTime(userApiVO.getCreateTime());
                if (null != finalStaffMap.get(userApiVO.getStaffId())){
                    distributionWitkeyExcelVO.setParentNo(finalStaffMap.get(userApiVO.getStaffId()).getStaffNo());
                    distributionWitkeyExcelVO.setParentMobile(finalStaffMap.get(userApiVO.getStaffId()).getMobile());
                }
            }
            if (null != finalStoreMap.get(distributionWitkeyExcelVO.getStoreId())){
                distributionWitkeyExcelVO.setStoreName(finalStoreMap.get(distributionWitkeyExcelVO.getStoreId()).getName());
            }
            distributionWitkeyExcelVO.setSales(new BigDecimal(Optional.ofNullable(distributionWitkeyExcelVO.getDistributionSales()).orElse(0L)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
            distributionWitkeyExcelVO.setTotalCommission(new BigDecimal(Optional.ofNullable(distributionWitkeyExcelVO.getDistributionCommission()).orElse(0L)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
            distributionSalesStatDTO.setDistributionUserId(distributionWitkeyExcelVO.getUserId());
            distributionWitkeyExcelVO.setOrderNum(orderService.countDistributionOrderNum(distributionSalesStatDTO));
            distributionWitkeyExcelVO.setRefundNum(orderRefundService.countDistributionRefundNum(distributionSalesStatDTO));
        });
        CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
        downloadRecordDTO.setDownloadTime(new Date());
        downloadRecordDTO.setFileName(DistributionWitkeyExcelVO.EXCEL_NAME);
        downloadRecordDTO.setCalCount(objectPageVO.getList().size());
        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
        downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
        ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
        Long downLoadHisId=null;
        if(serverResponseEntity.isSuccess()){
            downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
        }

        ExcelUploadDTO excelUploadDTO=new ExcelUploadDTO(downLoadHisId,
                objectPageVO.getList(),
                DistributionWitkeyExcelVO.EXCEL_NAME,
                DistributionWitkeyExcelVO.MERGE_ROW_INDEX,
                DistributionWitkeyExcelVO.MERGE_COLUMN_INDEX,
                DistributionWitkeyExcelVO.class);
        soldUploadExcelTemplate.syncSend(excelUploadDTO);
    }

    private void doStaffExportExcel(List<DistributionStaffExcelVO> list, DistributionSalesStatDTO distributionSalesStatDTO,Long downLoadHisId) {
        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(downLoadHisId);
        if (CollectionUtils.isEmpty(list)) {
            finishDownLoadDTO.setRemarks("没有可导出的数据");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error("没有可导出的数据");
            return;
        }
        Map<Long, StaffVO> staffMap = new HashMap<>(2);
        Map<Long, StoreVO> storeMap = new HashMap<>(2);
        ServerResponseEntity<List<StaffVO>> staffListData = staffFeignClient.getStaffBypByIds(list.stream().map(DistributionStaffExcelVO::getStaffId).distinct().collect((Collectors.toList())));
        if (staffListData.isSuccess() && CollectionUtils.isNotEmpty(staffListData.getData())){
            staffMap = LambdaUtils.toMap(staffListData.getData(), StaffVO::getId);
        }
        ServerResponseEntity<List<StoreVO>> storeListData = storeFeignClient.listBypByStoreIdList(list.stream().map(DistributionStaffExcelVO::getStoreId).collect(Collectors.toList()));
        if (storeListData.isSuccess() && CollectionUtils.isNotEmpty(storeListData.getData())){
            storeMap = LambdaUtils.toMap(storeListData.getData(), StoreVO::getStoreId);
        }
        Map<Long, StoreVO> finalStoreMap = storeMap;
        Map<Long, StaffVO> finalStaffMap = staffMap;
        distributionSalesStatDTO.setDistributionUserType(1);
        list.forEach(distributionStaffExcelVO -> {
            if (null != finalStoreMap.get(distributionStaffExcelVO.getStoreId())){
                distributionStaffExcelVO.setStoreCode(finalStoreMap.get(distributionStaffExcelVO.getStoreId()).getStoreCode());
                distributionStaffExcelVO.setStoreName(finalStoreMap.get(distributionStaffExcelVO.getStoreId()).getName());
            }
            if (null != finalStaffMap.get(distributionStaffExcelVO.getStaffId())){
                distributionStaffExcelVO.setStaffName(finalStaffMap.get(distributionStaffExcelVO.getStaffId()).getStaffName());
                distributionStaffExcelVO.setStaffNo(finalStaffMap.get(distributionStaffExcelVO.getStaffId()).getStaffNo());
                distributionStaffExcelVO.setMobile(finalStaffMap.get(distributionStaffExcelVO.getStaffId()).getMobile());
            }
            distributionSalesStatDTO.setDistributionUserId(distributionStaffExcelVO.getStaffId());
            distributionStaffExcelVO.setOrderNum(orderService.countDistributionOrderNum(distributionSalesStatDTO));
            distributionStaffExcelVO.setRefundNum(orderRefundService.countDistributionRefundNum(distributionSalesStatDTO));
            UserQueryDTO queryDTO = new UserQueryDTO();
            queryDTO.setStartTime(distributionSalesStatDTO.getStartTime());
            queryDTO.setEndTime(distributionSalesStatDTO.getEndTime());
            queryDTO.setStaffId(distributionStaffExcelVO.getStaffId());
            ServerResponseEntity<List<UserApiVO>> userListData = userFeignClient.listUserByStaff(queryDTO);
            if (userListData.isSuccess() && CollectionUtils.isNotEmpty(userListData.getData())){
                distributionStaffExcelVO.setUserNum(userListData.getData().size());
                distributionStaffExcelVO.setWitkeyNum((int) userListData.getData().stream().filter(userApiVO -> Optional.ofNullable(userApiVO.getVeekerAuditStatus()).orElse(0) == 1).count());
            } else {
                distributionStaffExcelVO.setUserNum(0);
                distributionStaffExcelVO.setWitkeyNum(0);
            }
        });

        File file=null;
        try {
            int calCount=list.size();
            log.info("导出数据行数 【{}】",calCount);

            long startTime = System.currentTimeMillis();
            log.info("开始执行导购分销数据生成excel 总条数【{}】",calCount);
            String pathExport= SkqUtils.getExcelFilePath()+"/"+SkqUtils.getExcelName()+".xls";
            EasyExcel.write(pathExport, DistributionStaffExcelVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(list);
            String contentType = "application/vnd.ms-excel";
            log.info("结束执行导购分销数据生成excel，耗时：{}ms   总条数【{}】  excel本地存放目录【{}】", System.currentTimeMillis() - startTime,list.size(),pathExport);

            startTime = System.currentTimeMillis();
            log.info("导出数据到本地excel，开始执行上传excel.....");
            file=new File(pathExport);
            FileInputStream is = new FileInputStream(file);
            MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
                    contentType, is);

            String originalFilename = multipartFile.getOriginalFilename();
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String mimoPath = "excel/" + time + "/" + originalFilename;
            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
            if (responseEntity.isSuccess() && finishDownLoadDTO!=null) {
                log.info("上传文件地址："+responseEntity.getData());
                //下载中心记录
                finishDownLoadDTO.setCalCount(calCount);
                String fileName= DateUtil.format(new Date(),"yyyyMMddHHmmss") +""+DistributionStaffExcelVO.EXCEL_NAME;
                finishDownLoadDTO.setFileName(fileName);
                finishDownLoadDTO.setStatus(1);
                finishDownLoadDTO.setFileUrl(responseEntity.getData());
                finishDownLoadDTO.setRemarks("导出成功");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                // 删除临时文件
                if (file.exists()) {
                    file.delete();
                }
            }
            log.info("导出数据到本地excel，结束执行上传excel，耗时：{}ms", System.currentTimeMillis() - startTime);
        }catch (Exception e){
            log.error("导出导购分销数据信息错误: "+e.getMessage(),e);
            finishDownLoadDTO.setRemarks("导出导购分销数据失败，信息错误："+e.getMessage());
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            // 删除临时文件
            if (file!=null && file.exists()) {
                file.delete();
            }
        }
    }

    private void doStoreExportExcel(List<DistributionStoreStatisticsVO> list, DistributionSalesStatDTO distributionSalesStatDTO,Long downLoadHisId) {
        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(downLoadHisId);
        if (CollectionUtils.isEmpty(list)) {
            finishDownLoadDTO.setRemarks("没有可导出的数据");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error("没有可导出的数据");
            return;
        }
        distributionSalesStatDTO.setDistributionUserType(2);
        List<DistributionStoreExcelVO> distributionStoreExcelVOList = new ArrayList<>();
        list.forEach(distributionStoreStatisticsVO -> {
            DistributionStoreExcelVO vo = new DistributionStoreExcelVO();
            BeanUtils.copyProperties(distributionStoreStatisticsVO, vo);
            distributionSalesStatDTO.setDistributionStoreId(distributionStoreStatisticsVO.getStoreId());
            Long developingSales = orderService.storeDistributionSalesStat(distributionSalesStatDTO);
            vo.setDevelopingSales(developingSales);
            vo.setDistributionSales(Optional.ofNullable(distributionStoreStatisticsVO.getSales()).orElse(0L) - Optional.ofNullable(developingSales).orElse(0L));
            distributionStoreExcelVOList.add(vo);
        });

        File file=null;
        try {
            int calCount=distributionStoreExcelVOList.size();
            log.info("导出数据行数 【{}】",calCount);

            long startTime = System.currentTimeMillis();
            log.info("开始执行导购分销(门店)数据生成excel 总条数【{}】",calCount);
            String pathExport= SkqUtils.getExcelFilePath()+"/"+SkqUtils.getExcelName()+".xls";
            EasyExcel.write(pathExport, DistributionStoreExcelVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(distributionStoreExcelVOList);
            String contentType = "application/vnd.ms-excel";
            log.info("结束执行导购分销(门店)数据生成excel，耗时：{}ms   总条数【{}】  excel本地存放目录【{}】", System.currentTimeMillis() - startTime,distributionStoreExcelVOList.size(),pathExport);

            startTime = System.currentTimeMillis();
            log.info("导出数据到本地excel，开始执行上传excel.....");
            file=new File(pathExport);
            FileInputStream is = new FileInputStream(file);
            MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
                    contentType, is);

            String originalFilename = multipartFile.getOriginalFilename();
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String mimoPath = "excel/" + time + "/" + originalFilename;
            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
            if (responseEntity.isSuccess() && finishDownLoadDTO!=null) {
                log.info("上传文件地址："+responseEntity.getData());
                //下载中心记录
                finishDownLoadDTO.setCalCount(calCount);
                String fileName= DateUtil.format(new Date(),"yyyyMMddHHmmss") +""+DistributionStoreExcelVO.EXCEL_NAME;
                finishDownLoadDTO.setFileName(fileName);
                finishDownLoadDTO.setStatus(1);
                finishDownLoadDTO.setFileUrl(responseEntity.getData());
                finishDownLoadDTO.setRemarks("导出成功");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                // 删除临时文件
                if (file.exists()) {
                    file.delete();
                }
            }
            log.info("导出数据到本地excel，结束执行上传excel，耗时：{}ms", System.currentTimeMillis() - startTime);
        }catch (Exception e){
            log.error("导出导购分销(门店)数据信息错误: "+e.getMessage(),e);
            finishDownLoadDTO.setRemarks("导出导购分销(门店)数据失败，信息错误："+e.getMessage());
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            // 删除临时文件
            if (file!=null && file.exists()) {
                file.delete();
            }
        }
    }


//    private void doStaffExportExcel(PageVO<DistributionStaffExcelVO> objectPageVO, DistributionSalesStatDTO distributionSalesStatDTO) {
//        if (CollectionUtils.isEmpty(objectPageVO.getList())){
//            return;
//        }
//        Map<Long, StaffVO> staffMap = new HashMap<>(2);
//        Map<Long, StoreVO> storeMap = new HashMap<>(2);
//        ServerResponseEntity<List<StaffVO>> staffListData = staffFeignClient.getStaffBypByIds(objectPageVO.getList().stream().map(DistributionStaffExcelVO::getStaffId).distinct().collect((Collectors.toList())));
//        if (staffListData.isSuccess() && CollectionUtils.isNotEmpty(staffListData.getData())){
//            staffMap = LambdaUtils.toMap(staffListData.getData(), StaffVO::getId);
//        }
//        ServerResponseEntity<List<StoreVO>> storeListData = storeFeignClient.listBypByStoreIdList(objectPageVO.getList().stream().map(DistributionStaffExcelVO::getStoreId).collect(Collectors.toList()));
//        if (storeListData.isSuccess() && CollectionUtils.isNotEmpty(storeListData.getData())){
//            storeMap = LambdaUtils.toMap(storeListData.getData(), StoreVO::getStoreId);
//        }
//        Map<Long, StoreVO> finalStoreMap = storeMap;
//        Map<Long, StaffVO> finalStaffMap = staffMap;
//        distributionSalesStatDTO.setDistributionUserType(1);
//        objectPageVO.getList().forEach(distributionStaffExcelVO -> {
//            if (null != finalStoreMap.get(distributionStaffExcelVO.getStoreId())){
//                distributionStaffExcelVO.setStoreCode(finalStoreMap.get(distributionStaffExcelVO.getStoreId()).getStoreCode());
//                distributionStaffExcelVO.setStoreName(finalStoreMap.get(distributionStaffExcelVO.getStoreId()).getName());
//            }
//            if (null != finalStaffMap.get(distributionStaffExcelVO.getStaffId())){
//                distributionStaffExcelVO.setStaffName(finalStaffMap.get(distributionStaffExcelVO.getStaffId()).getStaffName());
//                distributionStaffExcelVO.setStaffNo(finalStaffMap.get(distributionStaffExcelVO.getStaffId()).getStaffNo());
//                distributionStaffExcelVO.setMobile(finalStaffMap.get(distributionStaffExcelVO.getStaffId()).getMobile());
//            }
//            distributionSalesStatDTO.setDistributionUserId(distributionStaffExcelVO.getStaffId());
//            distributionStaffExcelVO.setOrderNum(orderService.countDistributionOrderNum(distributionSalesStatDTO));
//            distributionStaffExcelVO.setRefundNum(orderRefundService.countDistributionRefundNum(distributionSalesStatDTO));
//            UserQueryDTO queryDTO = new UserQueryDTO();
//            queryDTO.setStartTime(distributionSalesStatDTO.getStartTime());
//            queryDTO.setEndTime(distributionSalesStatDTO.getEndTime());
//            queryDTO.setStaffId(distributionStaffExcelVO.getStaffId());
//            ServerResponseEntity<List<UserApiVO>> userListData = userFeignClient.listUserByStaff(queryDTO);
//            if (userListData.isSuccess() && CollectionUtils.isNotEmpty(userListData.getData())){
//                distributionStaffExcelVO.setUserNum(userListData.getData().size());
//                distributionStaffExcelVO.setWitkeyNum((int) userListData.getData().stream().filter(userApiVO -> Optional.ofNullable(userApiVO.getVeekerAuditStatus()).orElse(0) == 1).count());
//            } else {
//                distributionStaffExcelVO.setUserNum(0);
//                distributionStaffExcelVO.setWitkeyNum(0);
//            }
//        });
//        CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
//        downloadRecordDTO.setDownloadTime(new Date());
//        downloadRecordDTO.setFileName(DistributionStaffExcelVO.EXCEL_NAME);
//        downloadRecordDTO.setCalCount(objectPageVO.getList().size());
//        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
//        downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
//        ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
//        Long downLoadHisId=null;
//        if(serverResponseEntity.isSuccess()){
//            downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
//        }
//
//        ExcelUploadDTO excelUploadDTO=new ExcelUploadDTO(downLoadHisId,
//                objectPageVO.getList(),
//                DistributionStaffExcelVO.EXCEL_NAME,
//                DistributionStaffExcelVO.MERGE_ROW_INDEX,
//                DistributionStaffExcelVO.MERGE_COLUMN_INDEX,
//                DistributionStaffExcelVO.class);
//        soldUploadExcelTemplate.syncSend(excelUploadDTO);
//    }




//    private void doStoreExportExcel(PageVO<DistributionStoreStatisticsVO> pageVO, DistributionSalesStatDTO distributionSalesStatDTO) {
//        if (CollectionUtils.isEmpty(pageVO.getList())) {
//            return;
//        }
//        distributionSalesStatDTO.setDistributionUserType(2);
//        List<DistributionStoreExcelVO> distributionStoreExcelVOList = new ArrayList<>();
//        pageVO.getList().forEach(distributionStoreStatisticsVO -> {
//            DistributionStoreExcelVO vo = new DistributionStoreExcelVO();
//            BeanUtils.copyProperties(distributionStoreStatisticsVO, vo);
//            distributionSalesStatDTO.setDistributionStoreId(distributionStoreStatisticsVO.getStoreId());
//            Long developingSales = orderService.storeDistributionSalesStat(distributionSalesStatDTO);
//            vo.setDevelopingSales(developingSales);
//            vo.setDistributionSales(Optional.ofNullable(distributionStoreStatisticsVO.getSales()).orElse(0L) - Optional.ofNullable(developingSales).orElse(0L));
//            distributionStoreExcelVOList.add(vo);
//        });
//        CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
//        downloadRecordDTO.setDownloadTime(new Date());
//        downloadRecordDTO.setFileName(DistributionStoreExcelVO.EXCEL_NAME);
//        downloadRecordDTO.setCalCount(distributionStoreExcelVOList.size());
//        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
//        downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
//        ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
//        Long downLoadHisId=null;
//        if(serverResponseEntity.isSuccess()){
//            downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
//        }
//
//        ExcelUploadDTO excelUploadDTO=new ExcelUploadDTO(downLoadHisId,
//                distributionStoreExcelVOList,
//                DistributionStoreExcelVO.EXCEL_NAME,
//                DistributionStoreExcelVO.MERGE_ROW_INDEX,
//                DistributionStoreExcelVO.MERGE_COLUMN_INDEX,
//                DistributionStoreExcelVO.class);
//        soldUploadExcelTemplate.syncSend(excelUploadDTO);
//    }
}
