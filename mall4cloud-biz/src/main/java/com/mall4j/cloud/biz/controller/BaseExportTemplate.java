package com.mall4j.cloud.biz.controller;

/**
 * @Description 导出文件
 * @Author axin
 * @Date 2022-12-27 15:24
 **/

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.excel.EasyExcel;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.CompressUtil;
import com.mall4j.cloud.common.util.FileUtil;
import com.mall4j.cloud.common.util.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.http.entity.ContentType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
public abstract class BaseExportTemplate<RESP,REQ extends PageDTO> implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private static String pathExport;


    /**
     * 分页查询方法,循环调用当前方法
     * @param reqObj 请求入参对象
     * @return
     */
    public abstract PageVO<RESP> queryPageExport(REQ reqObj);

    /**
     * 生成Excel文件到下载中心
     * @param reqObj 查询入参 reqObj.pageSize 设置单个文件条数
     * @param fileName 文件名
     * @param outClazz 导出的EXCEL文件对象
     * @return 成功失败提示
     */
    public String generateFileToDownloadCenter(String fileName,REQ reqObj,Class<?> outClazz) {
        try {
            CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
            downloadRecordDTO.setDownloadTime(new Date());
            downloadRecordDTO.setFileName(fileName);
            downloadRecordDTO.setCalCount(0);
            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
            downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
            DownloadCenterFeignClient downloadCenterFeignClient=applicationContext.getBean(DownloadCenterFeignClient.class);
            ServerResponseEntity<Long> serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
            Long downloadCenterId=null;
            if(serverResponseEntity.isSuccess()){
                downloadCenterId=Long.parseLong(serverResponseEntity.getData().toString());
            }
            templateMethod(downloadCenterId,fileName,reqObj, outClazz);
            return "操作成功，请转至下载中心下载";
        }catch (Exception e){
            log.error(fileName+": "+e.getMessage(),e);
            return "操作失败";
        }
    }

    /**
     * 将数据设置到Excel,需要个性化数据可以重写此方法
     * @param outClazz
     * @param pageReqDtoPageInfo
     * @param pathExport
     */
    public void setData(Class<?> outClazz, PageVO<RESP> pageReqDtoPageInfo, String pathExport) {
        MapperFacade mapperFacade=applicationContext.getBean(MapperFacade.class);
        List<?> classes = mapperFacade.mapAsList(pageReqDtoPageInfo.getList(), outClazz);
        EasyExcel.write(pathExport, outClazz).sheet(ExcelModel.SHEET_NAME).doWrite(classes);
    }

    private void templateMethod(Long downloadCenterId,String fileName,REQ reqObj,Class<?> outClazz){
        ThreadUtil.execAsync(()->{
            FinishDownLoadDTO finishDownLoadDTO = new FinishDownLoadDTO();
            try {
                PageVO<RESP> pageReqDtoPageInfo;
                int pageNum=0;
                List<String> localFileUrls = Lists.newArrayList();
                do{
                    reqObj.setPageNum(++pageNum);
                    pageReqDtoPageInfo = queryPageExport(reqObj);
                    pathExport = SystemUtils.getExcelFilePath() + "/"+fileName+"_" + SystemUtils.getExcelName() + ".xls";

                    setData(outClazz, pageReqDtoPageInfo, pathExport);

                    localFileUrls.add(pathExport);
                }while (pageNum<pageReqDtoPageInfo.getPages());

                String fileUrl = getUploadUrl(localFileUrls);
                finishDownLoadDTO.setFileUrl(fileUrl);
                finishDownLoadDTO.setCalCount(Math.toIntExact(pageReqDtoPageInfo.getTotal()));
                finishDownLoadDTO.setStatus(1);
                finishDownLoadDTO.setRemarks("导出成功");
            } catch (Exception e) {
                finishDownLoadDTO.setStatus(2);
                finishDownLoadDTO.setRemarks("导出失败");
                log.error(fileName+"导出失败",e);
            }finally {
                finishDownLoadDTO.setId(downloadCenterId);
                finishDownLoadDTO.setFileName(fileName);
                DownloadCenterFeignClient downloadCenterFeignClient=applicationContext.getBean(DownloadCenterFeignClient.class);
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
        });
    }


    /**
     * 获取批量上传压缩包url(支持单个非压缩包)
     *
     * @param localFileUrls
     * @return
     * @throws Exception
     */
    private String getUploadUrl(List<String> localFileUrls) throws Exception {
        String pathExport;
        String contentType;

        if (localFileUrls.size() == 1) {
            pathExport = localFileUrls.get(0);
            contentType = "application/vnd.ms-excel";
        } else {
            String time = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
            String zipFilePathExport = CompressUtil.ZIP_FILE_PATH_EXPORT + time + "/";
            List<File> fromFileList = Lists.newArrayList();
            File copyFile = new File(zipFilePathExport);
            localFileUrls.forEach(item -> {
                File file = new File(item);
                fromFileList.add(file);
                log.info("单个excel文件信息 文件名【{}】 文件大小【{}】", file.getName(), cn.hutool.core.io.FileUtil.size(file));
            });
            boolean mkdirs = copyFile.mkdirs();
            //文件存放统一目录
            FileUtil.copyCodeToFile(fromFileList, zipFilePathExport, Lists.newArrayList());
            //压缩统一文件目录
            pathExport = CompressUtil.zipFile(copyFile, "zip");
            contentType = ContentType.APPLICATION_OCTET_STREAM.toString();
        }
        return upload(pathExport, contentType);
    }

    private String upload(String pathExport, String contentType) {
        File file = null;
        try {
            file = new File(pathExport);
            FileInputStream is = new FileInputStream(file);
            MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(), contentType, is);

            String originalFilename = multipartFile.getOriginalFilename();
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String mimoPath = "excel/" + time + "/" + originalFilename;
            MinioUploadFeignClient minioUploadFeignClient=applicationContext.getBean(MinioUploadFeignClient.class);
            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
            if (responseEntity.isSuccess()) {
                log.info("上传文件地址：" + responseEntity.getData());
                // 删除临时文件
                if (file.exists()) {
                    file.delete();
                }
            }
            return responseEntity.getData();
        } catch (Exception e) {
            // 删除临时文件
            if (file != null && file.exists()) {
                file.delete();
            }
            return null;
        }
    }

    @Override
    public  void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
