package com.mall4j.cloud.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.biz.dto.ExcelUploadDTO;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.biz.service.ExcelUploadService;
import com.mall4j.cloud.common.util.csvExport.hanlder.ExcelMergeHandler;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.mall4j.cloud.common.model.ExcelModel.SHEET_NAME;

/**
 * 短信记录表
 *
 * @author lhd
 * @date 2021-01-04 13:36:52
 */
@Slf4j
@Service
public class ExcelUploadServiceImpl implements ExcelUploadService {

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    public  ExcelWriterBuilder getExcelWriterMerge( ByteArrayOutputStream os, String excelName, int mergeRowIndex, int[] mergeColumnIndex) throws Exception{
        ExcelWriterBuilder build = EasyExcel.write(os)
                // 自定义合并规则
                .registerWriteHandler(new ExcelMergeHandler(mergeRowIndex, mergeColumnIndex))
                // 宽度自适应
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy());
        return build;
    }

    @Override
    public void soleAndUploadExcel( ExcelUploadDTO excelUploadDTO) {
        ExcelWriter excelWriter = null;

//        List list=excelUploadDTO.getList();
        //使用mq 或者 feign List数据为转变为LinkHashMap ，此处解决赋值
        List list= JSON.parseArray(JSON.toJSONString(excelUploadDTO.getList()),excelUploadDTO.getClazz());
        String excelName=excelUploadDTO.getExcelName();
        int mergeRowIndex=excelUploadDTO.getMergeRowIndex();
        int[] mergeColumnIndex=excelUploadDTO.getMergeColumnIndex();
        Class clazz=excelUploadDTO.getClazz();

        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setFileName(excelName);
        finishDownLoadDTO.setId(excelUploadDTO.getDownLoadHisId());
        finishDownLoadDTO.setCalCount(list.size());

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            // 先执行合并策略
            excelWriter = getExcelWriterMerge(os,excelName, mergeRowIndex, mergeColumnIndex).build();
            // 业务代码
            if (CollUtil.isNotEmpty(list)){
                // 进行写入操作
                WriteSheet sheetWriter = EasyExcel.writerSheet(SHEET_NAME).head(clazz).build();

                excelWriter.write(list,sheetWriter);

            }else{
                finishDownLoadDTO.setStatus(2);
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }

        } catch (Exception e) {
            log.error("导出excel失败", e.getMessage(),e);
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
        }
        finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
                // 必须要finish才会写入，不finish只会创建empty的文件
                excelUpload(os,excelUploadDTO,finishDownLoadDTO);
            }
        }
    }

    @Override
    public void soleAndUploadExcelFile(ExcelUploadDTO excelUploadDTO) {

        File file=new File(excelUploadDTO.getFilePath());

        try {
            log.info("导出数据到本地excel，开始执行上传excel.....");
            //下载中心记录
            FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
            finishDownLoadDTO.setId(excelUploadDTO.getDownLoadHisId());
            finishDownLoadDTO.setCalCount(excelUploadDTO.getCalCount());

            FileInputStream is = new FileInputStream(file);

            MultipartFile multipartFile = new MultipartFileDto(excelUploadDTO.getFileName(), excelUploadDTO.getFileName(),
                    excelUploadDTO.getContentType(), is);

            String originalFilename = multipartFile.getOriginalFilename();
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String excelMimoPath = excelUploadDTO.getExcelMimoPath();
            String defaultPath = "excel/" + time + "/" + originalFilename;
            String mimoPath = StrUtil.isNotBlank(excelMimoPath) ? excelMimoPath : defaultPath;
            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
            if (responseEntity.isSuccess() && finishDownLoadDTO!=null) {
                log.info("---ExcelUploadService---" + responseEntity.toString());
                String excelPath = responseEntity.getData();
                //下载中心记录
                String filetime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String fileName=filetime+""+excelUploadDTO.getExcelName();
                finishDownLoadDTO.setFileName(fileName);
                finishDownLoadDTO.setStatus(1);
                finishDownLoadDTO.setFileUrl(excelPath);
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);

                // 会在本地产生临时文件，用完后需要删除
                if (file.exists()) {
                    file.delete();
                }
            }
            log.info("导出数据到本地excel，结束执行上传excel.....");
        }catch (Exception e){
            // 会在本地产生临时文件，用完后需要删除
            if (file!=null && file.exists()) {
                file.delete();
            }
            log.error("导出excel失败", e.getMessage(),e);
        }

    }

    @Override
    public ServerResponseEntity<String> createAnduploadExcel(ExcelUploadDTO excelUploadDTO) {

        ExcelWriter excelWriter = null;

//        List list=excelUploadDTO.getList();
        //使用mq 或者 feign List数据为转变为LinkHashMap ，此处解决赋值
        List list= JSON.parseArray(JSON.toJSONString(excelUploadDTO.getList()),excelUploadDTO.getClazz());
        String excelName=excelUploadDTO.getExcelName();
        int mergeRowIndex=excelUploadDTO.getMergeRowIndex();
        int[] mergeColumnIndex=excelUploadDTO.getMergeColumnIndex();
        Class clazz=excelUploadDTO.getClazz();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            // 先执行合并策略
            excelWriter = getExcelWriterMerge(os,excelName, mergeRowIndex, mergeColumnIndex).build();
            // 业务代码
            if (CollUtil.isNotEmpty(list)){
                // 进行写入操作
                WriteSheet sheetWriter = EasyExcel.writerSheet(SHEET_NAME).head(clazz).build();

                excelWriter.write(list,sheetWriter);

            }

        } catch (Exception e) {
            log.error("导出excel失败", e.getMessage());
        }
        finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
                // 必须要finish才会写入，不finish只会创建empty的文件
                return excelUpload(os,excelUploadDTO,null);
            }
        }
        return null;
    }

    private ServerResponseEntity<String> excelUpload(ByteArrayOutputStream os,ExcelUploadDTO excelUploadDTO,FinishDownLoadDTO finishDownLoadDTO) {
        try {
            String filetime = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String excelFileName = System.currentTimeMillis()+ RandomUtil.getRandomStr(4);
            String excelMimoPath = excelUploadDTO.getExcelMimoPath();
            // 必须要finish才会写入，不finish只会创建empty的文件
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            excelFileName = URLEncoder.encode(excelFileName, "UTF-8");
            if(StrUtil.isNotBlank(excelUploadDTO.getExcelType())){
                excelFileName = excelFileName + excelUploadDTO.getExcelType();
            }else{
                excelFileName = excelFileName + ExcelTypeEnum.XLSX.getValue();
            }
            String contentType = "application/vnd.ms-excel";
            if(StrUtil.isNotBlank(excelUploadDTO.getContentType())){
                contentType=excelUploadDTO.getContentType();
            }
//            String contentType = ContentType.APPLICATION_OCTET_STREAM.toString();
            MultipartFile multipartFile = new MultipartFileDto(excelFileName, excelFileName,
                    contentType, is);
            String originalFilename = multipartFile.getOriginalFilename();
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String defaultPath = "excel/" + time + "/" + originalFilename;
            String mimoPath = StrUtil.isNotBlank(excelMimoPath) ? excelMimoPath : defaultPath;
            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
            if (responseEntity.isSuccess() && finishDownLoadDTO!=null) {
                log.info("---ExcelUploadService---" + responseEntity.toString());
                String excelPath = responseEntity.getData();

                //下载中心记录
                filetime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String fileName=filetime+""+excelUploadDTO.getExcelName();
                finishDownLoadDTO.setFileName(fileName);
                finishDownLoadDTO.setStatus(1);
                finishDownLoadDTO.setFileUrl(excelPath);
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
            return responseEntity;
        } catch (Exception e) {
            log.info(e.getMessage());
            //下载中心记录
            if(finishDownLoadDTO!=null){
                finishDownLoadDTO.setStatus(2);
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
        }
        return null;
    }

}
