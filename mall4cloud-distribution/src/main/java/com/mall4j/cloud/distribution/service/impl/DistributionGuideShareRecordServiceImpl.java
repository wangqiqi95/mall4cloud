package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.mall4j.cloud.api.biz.dto.ExcelUploadDTO;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.distribution.vo.DistributionGuideShareExcelVO;
import com.mall4j.cloud.api.order.vo.OrderExcelVO;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.CompressUtil;
import com.mall4j.cloud.common.util.FileUtil;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.distribution.dto.DistributionGuideShareRecordDTO;
import com.mall4j.cloud.distribution.mapper.DistributionGuideShareRecordMapper;
import com.mall4j.cloud.distribution.model.DistributionGuideShareRecord;
import com.mall4j.cloud.distribution.service.DistributionGuideShareRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 分销数据-导购分销效果
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@Slf4j
@Service
public class DistributionGuideShareRecordServiceImpl implements DistributionGuideShareRecordService {

    @Autowired
    private DistributionGuideShareRecordMapper distributionGuideShareRecordMapper;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Autowired
    private OnsMQTemplate soldUploadExcelTemplate;
    @Autowired
    MinioUploadFeignClient minioUploadFeignClient;

    @Override
    public PageVO<DistributionGuideShareRecord> page(PageDTO pageDTO, DistributionGuideShareRecordDTO dto) {
        return PageUtil.doPage(pageDTO, () -> distributionGuideShareRecordMapper.list(dto));
    }

    @Override
    public DistributionGuideShareRecord getById(Long id) {
        return distributionGuideShareRecordMapper.getById(id);
    }

    @Override
    public void save(DistributionGuideShareRecord distributionGuideShareRecord) {
        distributionGuideShareRecordMapper.save(distributionGuideShareRecord);
    }

    @Override
    public void update(DistributionGuideShareRecord distributionGuideShareRecord) {
        distributionGuideShareRecordMapper.update(distributionGuideShareRecord);
    }

    @Override
    public void deleteById(Long id) {
        distributionGuideShareRecordMapper.deleteById(id);
    }

    @Override
    public DistributionGuideShareRecord getByGuideAndActivityAndDate(Long guideId, Integer activityType, Date date) {
        return distributionGuideShareRecordMapper.getByGuideAndActivityAndDate(guideId, activityType, date);
    }

    @Override
    public Integer countStaffNum(DistributionGuideShareRecordDTO dto) {
        return distributionGuideShareRecordMapper.countStaffNum(dto);
    }

    @Override
    public void exportGuideShareRecord(DistributionGuideShareRecordDTO dto) {
        if(dto.getQueryStartDate()==null || dto.getQueryEndDate() == null){
            Assert.faild("导出数据时间不允许为空。");
        }
        if(dto.getQueryStartDate()!=null && dto.getQueryEndDate() != null){
            if(DateUtil.betweenDay(dto.getQueryStartDate(),dto.getQueryEndDate(),false)>32){
                Assert.faild("导出数据时间条件不允许选择超过31天。");
            }
        }

        //存放全部导出excel文件
        List<String> filePaths=new ArrayList<>();

        CalcingDownloadRecordDTO downloadRecordDTO = new CalcingDownloadRecordDTO();
        downloadRecordDTO.setDownloadTime(new Date());
        downloadRecordDTO.setFileName(DistributionGuideShareExcelVO.EXCEL_NAME);
        downloadRecordDTO.setCalCount(0);
        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
        downloadRecordDTO.setOperatorNo("" + AuthUserContext.get().getUserId());
        ServerResponseEntity serverResponseEntity = downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
        Long downLoadHisId = null;
        if (serverResponseEntity.isSuccess()) {
            downLoadHisId = Long.parseLong(serverResponseEntity.getData().toString());
        }

        int totalPage = 1;
        int currentPage = 1;

        int pageSize = 50000;

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageNum(currentPage);
        pageDTO.setPageSize(pageSize,true);
        PageVO<DistributionGuideShareRecord> pageVO = PageUtil.doPage(pageDTO, () -> distributionGuideShareRecordMapper.list(dto));
        totalPage = pageVO.getTotal().intValue() / pageSize + 1;
        String excelFileName = doExportExcel(pageVO,currentPage);
        if(StrUtil.isNotEmpty(excelFileName)){
            filePaths.add(excelFileName);
        }

        /**
         * 根据总条数拆分要导出的数据， 拆分excel。
         */
        for (int i = 2; i <= totalPage; i++) {
            PageDTO param = new PageDTO();
            param.setPageNum(i);
            param.setPageSize(pageSize,true);
            PageVO<DistributionGuideShareRecord> pageStoreStatistics = PageUtil.doPage(param, () -> distributionGuideShareRecordMapper.list(dto));
            String excelFileName2 = doExportExcel(pageStoreStatistics,i);
            if(StrUtil.isNotEmpty(excelFileName2)){
                filePaths.add(excelFileName2);
            }
        }


        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(downLoadHisId);
        finishDownLoadDTO.setCalCount(pageVO.getTotal().intValue());
        //将需要导出的文件压缩成一个压缩包上传
        if(CollectionUtil.isNotEmpty(filePaths)){
            try {

                log.info("导购分享数据导出，导出excel文件总数 【{}】",filePaths.size());

                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String zipFilePathExport="/opt/skechers/temp/tentacle/"+time+"/";

                File copyFile=new File(zipFilePathExport);
                copyFile.mkdirs();

                List<File> fromFileList=new ArrayList<>();
                List<File> backFileList=new ArrayList<>();

                filePaths.forEach(item->{
                    File file=new File(item);
                    fromFileList.add(file);
                    log.info("导购分享数据导出，单个excel文件信息 文件名【{}】 文件大小【{}】",file.getName(),cn.hutool.core.io.FileUtil.size(file));
                });
                //文件存放统一目录
                FileUtil.copyCodeToFile(fromFileList,zipFilePathExport,backFileList);
                //压缩统一文件目录
                String zipPath= CompressUtil.zipFile(copyFile,"zip");

                if(new File(zipPath).isFile()){

                    FileInputStream fileInputStream = new FileInputStream(zipPath);

                    String zipFileName="shareRecord_"+AuthUserContext.get().getUserId()+"_"+time+".zip";
                    MultipartFile multipartFile = new MultipartFileDto(zipFileName, zipFileName,
                            ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

                    String originalFilename = multipartFile.getOriginalFilename();
                    String mimoPath = "excel/" + time + "/" + originalFilename;
                    ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
                    if(responseEntity.isSuccess()){
                        log.info("---导购分享数据导出---" + responseEntity.toString());
                        //下载中心记录
                        String fileName= DateUtil.format(new Date(),"yyyyMMddHHmmss") +""+ DistributionGuideShareExcelVO.EXCEL_NAME;
                        finishDownLoadDTO.setFileName(fileName);
                        finishDownLoadDTO.setStatus(1);
                        finishDownLoadDTO.setFileUrl(responseEntity.getData());
                        finishDownLoadDTO.setRemarks("导出成功");
                        downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                    }
                    //删除本地临时文件
                    cn.hutool.core.io.FileUtil.del(zipPath);
                    cn.hutool.core.io.FileUtil.del(copyFile);
                }
            }catch (Exception e){
                log.info("导购分享数据导出，excel生成zip失败",e);
                //下载中心记录
                if(finishDownLoadDTO!=null){
                    finishDownLoadDTO.setStatus(2);
                    finishDownLoadDTO.setRemarks("导购分享数据导出，excel生成zip失败");
                    downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                }
            }

        }else{
            if(finishDownLoadDTO!=null){
                finishDownLoadDTO.setStatus(2);
                finishDownLoadDTO.setRemarks("订单数据导出，没有可导出的文件");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
        }

    }

    private String doExportExcel(PageVO<DistributionGuideShareRecord> pageVO,Integer currentPage) {
        if (CollectionUtils.isEmpty(pageVO.getList())) {
            Assert.faild("查询数据为空，不执行导出操作。");
        }
        List<DistributionGuideShareExcelVO> distributionGuideShareExcelVOList = new ArrayList<>();
        pageVO.getList().forEach(guideShareRecord -> {
            DistributionGuideShareExcelVO vo = new DistributionGuideShareExcelVO();
            BeanUtils.copyProperties(guideShareRecord, vo);
            vo.setType(buildType(guideShareRecord.getActivityType()));
            distributionGuideShareExcelVOList.add(vo);
        });

        File file=null;
        try {
            int calCount=distributionGuideShareExcelVOList.size();
            log.info("导出数据行数 【{}】",calCount);

            long startTime = System.currentTimeMillis();
            log.info("开始执行导购分享数据生成excel 总条数【{}】",calCount);
            String pathExport= SkqUtils.getExcelFilePath()+"/"+SkqUtils.getExcelName()+"：("+currentPage+")"+".xls";
            EasyExcel.write(pathExport, DistributionGuideShareExcelVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(distributionGuideShareExcelVOList);
            String contentType = "application/vnd.ms-excel";
            log.info("结束执行导购分享数据生成excel，耗时：{}ms   总条数【{}】  excel本地存放目录【{}】", System.currentTimeMillis() - startTime,distributionGuideShareExcelVOList.size(),pathExport);
            return pathExport;
        }catch (Exception e){
            log.error("导出导购分享数据信息错误: "+e.getMessage(),e);
            // 删除临时文件
            if (file!=null && file.exists()) {
                file.delete();
            }
        }
        return "";
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
