package com.mall4j.cloud.order.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.CompressUtil;
import com.mall4j.cloud.common.util.FileUtil;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.common.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * 分文件表格导出工具
 *
 * @author Zhang Fan
 * @date 2022/8/15 18:05
 */
@Slf4j
public final class SubFileExcelExporter {

    public static <I, O, EXCELT> void export(String businessLogTips, String excelName, Function<I, List<O>> selectExportDataFunc, I selectExportDataParam, Function<List<O>, List<EXCELT>> handleExportDataFunc, Class<EXCELT> exportExcelDataTypeClass) {
        export(businessLogTips, excelName, 2000, selectExportDataFunc, selectExportDataParam, handleExportDataFunc, exportExcelDataTypeClass);
    }

    public static <I, O, EXCELT> void export(String businessLogTips, String excelName, int pageSize, Function<I, List<O>> selectExportDataFunc, I selectExportDataParam, Function<List<O>, List<EXCELT>> handleExportDataFunc, Class<EXCELT> exportExcelDataTypeClass) {
        // 存放全部导出excel文件
        List<String> filePaths = new ArrayList<>();

        CalcingDownloadRecordDTO downloadRecordDTO = new CalcingDownloadRecordDTO();
        downloadRecordDTO.setDownloadTime(new Date());
        downloadRecordDTO.setFileName(excelName);
        downloadRecordDTO.setCalCount(0);
        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
        downloadRecordDTO.setOperatorNo("" + AuthUserContext.get().getUserId());
        ServerResponseEntity serverResponseEntity = SpringContextUtils.getBean(DownloadCenterFeignClient.class).newCalcingTask(downloadRecordDTO);
        Long downLoadHisId = null;
        if (serverResponseEntity.isSuccess()) {
            downLoadHisId = Long.parseLong(serverResponseEntity.getData().toString());
        }

        int currentPage = 1;

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageNum(currentPage);
        pageDTO.setPageSize(pageSize, true);
        PageVO<O> pageVO = PageUtil.doPage(pageDTO, () -> selectExportDataFunc.apply(selectExportDataParam));
        int totalPage = pageVO.getTotal().intValue() / pageSize + 1;
        String excelFileName = doExportExcel(businessLogTips, pageVO, currentPage, handleExportDataFunc, exportExcelDataTypeClass);
        if (StrUtil.isNotEmpty(excelFileName)) {
            filePaths.add(excelFileName);
        }

        /**
         * 根据总条数拆分要导出的数据， 拆分excel。
         */
        for (currentPage = 2; currentPage <= totalPage; currentPage++) {
            PageDTO param = new PageDTO();
            param.setPageNum(currentPage);
            param.setPageSize(pageSize, true);
            pageVO = PageUtil.doPage(param, () -> selectExportDataFunc.apply(selectExportDataParam));
            excelFileName = doExportExcel(businessLogTips, pageVO, currentPage, handleExportDataFunc, exportExcelDataTypeClass);
            if (StrUtil.isNotEmpty(excelFileName)) {
                filePaths.add(excelFileName);
            }
        }

        FinishDownLoadDTO finishDownLoadDTO = new FinishDownLoadDTO();
        finishDownLoadDTO.setId(downLoadHisId);
        finishDownLoadDTO.setCalCount(pageVO.getTotal().intValue());
        // 将需要导出的文件压缩成一个压缩包上传
        if (CollectionUtil.isNotEmpty(filePaths)) {
            try {
                log.info("{}导出，导出excel文件总数 【{}】", businessLogTips, filePaths.size());

                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String zipFilePathExport = "/opt/skechers/temp/tentacle/" + time + "/";

                File copyFile = new File(zipFilePathExport);
                copyFile.mkdirs();

                List<File> fromFileList = new ArrayList<>();
                List<File> backFileList = new ArrayList<>();

                filePaths.forEach(item -> {
                    File file = new File(item);
                    fromFileList.add(file);
                    log.info("{}导出，单个excel文件信息 文件名【{}】 文件大小【{}】", businessLogTips, file.getName(), cn.hutool.core.io.FileUtil.size(file));
                });
                // 文件存放统一目录
                FileUtil.copyCodeToFile(fromFileList, zipFilePathExport, backFileList);
                // 压缩统一文件目录
                String zipPath = CompressUtil.zipFile(copyFile, "zip");

                if (new File(zipPath).isFile()) {

                    FileInputStream fileInputStream = new FileInputStream(zipPath);

                    String zipFileName = "exportRecord_" + AuthUserContext.get().getUserId() + "_" + time + ".zip";
                    MultipartFile multipartFile = new MultipartFileDto(zipFileName, zipFileName,
                            ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

                    String originalFilename = multipartFile.getOriginalFilename();
                    String mimoPath = "excel/" + time + "/" + originalFilename;
                    ServerResponseEntity<String> responseEntity = SpringContextUtils.getBean(MinioUploadFeignClient.class).minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
                    if (responseEntity.isSuccess()) {
                        log.info("---{}导出---{}", businessLogTips, responseEntity.toString());
                        // 下载中心记录
                        String fileName = DateUtil.format(new Date(), "yyyyMMddHHmmss") + "" + excelName;
                        finishDownLoadDTO.setFileName(fileName);
                        finishDownLoadDTO.setStatus(1);
                        finishDownLoadDTO.setFileUrl(responseEntity.getData());
                        finishDownLoadDTO.setRemarks("导出成功");
                        SpringContextUtils.getBean(DownloadCenterFeignClient.class).finishDownLoad(finishDownLoadDTO);
                    }
                    // 删除本地临时文件
                    cn.hutool.core.io.FileUtil.del(zipPath);
                    cn.hutool.core.io.FileUtil.del(copyFile);
                }
            } catch (Exception e) {
                log.info("{}导出，excel生成zip失败", businessLogTips, e);
                //下载中心记录
                if (finishDownLoadDTO != null) {
                    finishDownLoadDTO.setStatus(2);
                    finishDownLoadDTO.setRemarks(businessLogTips + "导出，excel生成zip失败");
                    SpringContextUtils.getBean(DownloadCenterFeignClient.class).finishDownLoad(finishDownLoadDTO);
                }
            }

        } else {
            if (finishDownLoadDTO != null) {
                finishDownLoadDTO.setStatus(2);
                finishDownLoadDTO.setRemarks(businessLogTips + "导出，没有可导出的文件");
                SpringContextUtils.getBean(DownloadCenterFeignClient.class).finishDownLoad(finishDownLoadDTO);
            }
        }
    }

    private static <O, EXCELT> String doExportExcel(String businessLogTips, PageVO<O> pageVO, int currentPage, Function<List<O>, List<EXCELT>> handleExportDataFunc, Class<EXCELT> exportExcelDataTypeClass) {
        List<EXCELT> exportDataList = handleExportDataFunc.apply(pageVO.getList());

        File file = null;
        try {
            int calCount = exportDataList.size();
            log.info(businessLogTips + "导出数据行数 【{}】", calCount);

            long startTime = System.currentTimeMillis();
            log.info("开始执行{}生成excel 总条数【{}】", businessLogTips, calCount);
            String pathExport = SkqUtils.getExcelFilePath() + "/" + SkqUtils.getExcelName() + "：(" + currentPage + ")" + ".xls";
            EasyExcel.write(pathExport, exportExcelDataTypeClass).sheet(ExcelModel.SHEET_NAME).doWrite(exportDataList);
            String contentType = "application/vnd.ms-excel";
            log.info("结束执行{}生成excel，耗时：{}ms   总条数【{}】  excel本地存放目录【{}】", businessLogTips, System.currentTimeMillis() - startTime, calCount, pathExport);
            return pathExport;
        } catch (Exception e) {
            log.error("导出{}错误: " + e.getMessage(), businessLogTips, e);
            // 删除临时文件
            if (file != null && file.exists()) {
                file.delete();
            }
        }
        return "";
    }
}
