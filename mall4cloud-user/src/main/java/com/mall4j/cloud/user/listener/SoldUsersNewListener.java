package com.mall4j.cloud.user.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.user.vo.UserManagerVO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.CompressUtil;
import com.mall4j.cloud.common.util.FileUtil;
import com.mall4j.cloud.common.util.SystemUtils;
import com.mall4j.cloud.user.dto.UserManagerDTO;
import com.mall4j.cloud.user.event.SoldUsersEvent;
import com.mall4j.cloud.user.mapper.UserMapper;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.vo.UserExcelVO;
import lombok.Setter;
import ma.glasnost.orika.MapperFacade;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 会员数据导出
 */
@Component("SoldUsersListener")
@RefreshScope
//@AllArgsConstructor
public class SoldUsersNewListener {

    @Autowired
    private UserService userService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    private static final Logger log = LoggerFactory.getLogger(SoldUsersNewListener.class);

    @Value("${scrm.user.exportUserpageSize:20000}")
    @Setter
    private Integer exportUserpageSize=20000;

    @Resource
    private UserMapper userMapper;

    @Async
    @EventListener(SoldUsersEvent.class)
    public void soldUsersEvent(SoldUsersEvent event) {

        log.info("--会员数据导出来了 预警----");

        int currentPage = 1;
        int pageSize = exportUserpageSize;

        Long downLoadHisId= event.getDownLoadHisId();
        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(downLoadHisId);

        UserManagerDTO userManagerDTO=event.getUserManagerDTO();

        Long totalCount = userMapper.countUserPageByParam(userManagerDTO);
        totalCount=totalCount!=null?totalCount:0;
        log.info("会员数据导出，数据总条数【{}】",totalCount!=null?totalCount:0);
        if (totalCount==null || totalCount == 0) {
            finishDownLoadDTO.setRemarks("无会员数据导出");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error("无会员数据导出");
            return;
        }
        long startTime = System.currentTimeMillis();
        log.info("会员数据导出，开始执行导出任务--------->>>>>");

        //本次导出总条数
        finishDownLoadDTO.setCalCount(totalCount.intValue());
        //计算本次导出总页数
        int totalPage = (totalCount.intValue() + pageSize -1) / pageSize;
        log.info("会员数据导出，数据总条数【{}】 总页数【{}】",totalCount!=null?totalCount:0,totalPage);

        // 多线程分页查询数据
//        PageDTO pageIndex = new PageDTO();
//        pageIndex.setPageSize(pageSize,true);
//        pageIndex.setPageNum(currentPage);
        // 分页查询数据
        PageAdapter pageAdapter=new PageAdapter(currentPage,pageSize);

        //存放全部导出excel文件
        List<String> filePaths=new ArrayList<>();
        int seq=1;//excel表格序号
        //先导出第一页数据，生成第一个excel
        List<UserManagerVO> userInfoList = userService.getUserInfoList(pageAdapter, userManagerDTO);
        List<UserExcelVO> list = mapperFacade.mapAsList(userInfoList, UserExcelVO.class);
        String filePath=createExcelFile(list,seq);
        if(StrUtil.isNotBlank(filePath)){
            filePaths.add(filePath);
        }

        //存在多页数据，循环处理
        if(totalPage>1){
            for (int i = 2; i <= totalPage; i++) {
                currentPage = currentPage + 1;
                seq++;
//                pageIndex.setPageNum(currentPage);
//                pageIndex.setPageSize(pageSize,true);
                pageAdapter=new PageAdapter(currentPage,pageSize);
                List<UserManagerVO> userInfos = userService.getUserInfoList(pageAdapter, userManagerDTO);
                log.info("会员数据导出--->循环处理中 当前页为第【{}】页  单页限制最大条数【{}】 获取到数据总条数【{}】",pageAdapter.getBegin(),pageAdapter.getSize(),userInfos.size());
                if(CollectionUtil.isNotEmpty(userInfos)){
                    List<UserExcelVO> backList = mapperFacade.mapAsList(userInfos, UserExcelVO.class);
                    String exlcePath=createExcelFile(backList,seq);
                    if(StrUtil.isNotBlank(exlcePath)){
                        filePaths.add(exlcePath);
                    }
                }
            }
        }

        if(CollectionUtil.isNotEmpty(filePaths)){
            try {

                log.info("会员数据导出，导出excel文件总数 【{}】",filePaths.size());

                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String zipFilePathExport="/opt/skechers/temp/tentacle/"+time+"/";

                File copyFile=new File(zipFilePathExport);
                copyFile.mkdirs();

                List<File> fromFileList=new ArrayList<>();
                List<File> backFileList=new ArrayList<>();

                filePaths.forEach(item->{
                    File file=new File(item);
                    fromFileList.add(file);
                    log.info("会员数据导出，单个excel文件信息 文件名【{}】 文件大小【{}】",file.getName(),cn.hutool.core.io.FileUtil.size(file));
                });
                //文件存放统一目录
                FileUtil.copyCodeToFile(fromFileList,zipFilePathExport,backFileList);
                //压缩统一文件目录
                String zipPath= CompressUtil.zipFile(copyFile,"zip");

                if(new File(zipPath).isFile()){

                    FileInputStream fileInputStream = new FileInputStream(zipPath);

                    String zipFileName="soldMember_"+AuthUserContext.get().getUserId()+"_"+time+".zip";
                    MultipartFile multipartFile = new MultipartFileDto(zipFileName, zipFileName,
                            ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

                    String originalFilename = multipartFile.getOriginalFilename();
                    String mimoPath = "excel/" + time + "/" + originalFilename;
                    ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
                    if(responseEntity.isSuccess()){
                        log.info("---ExcelUploadService---" + responseEntity.toString());
                        //下载中心记录
                        String fileName= DateUtil.format(new Date(),"yyyyMMddHHmmss") +""+ UserExcelVO.EXCEL_NAME;
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
                log.info("会员数据导出，excel生成zip失败",e);
                //下载中心记录
                if(finishDownLoadDTO!=null){
                    finishDownLoadDTO.setStatus(2);
                    finishDownLoadDTO.setRemarks("会员数据导出，excel生成zip失败");
                    downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                }
            }

        }else{
            if(finishDownLoadDTO!=null){
                finishDownLoadDTO.setStatus(2);
                finishDownLoadDTO.setRemarks("会员数据导出，没有可导出的文件");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
        }

        log.info("会员数据导出，结束执行任务------>>耗时：{}ms", System.currentTimeMillis() - startTime);

    }

    private String createExcelFile(List<UserExcelVO> userExcelVOList,int seq){
        String file=null;
        try {
            int calCount=userExcelVOList.size();
            long startTime = System.currentTimeMillis();
            log.info("开始执行会员数据数据生成excel 总条数【{}】",calCount);
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String pathExport= SystemUtils.getExcelFilePath()+"/"+time+"_"+ SystemUtils.getExcelName()+"_"+seq+".xls";
            EasyExcel.write(pathExport, UserExcelVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(userExcelVOList);
            String contentType = "application/vnd.ms-excel";
            log.info("结束执行会员数据数据生成excel，耗时：{}ms   总条数【{}】  excel本地存放目录【{}】", System.currentTimeMillis() - startTime,userExcelVOList.size(),pathExport);
            return pathExport;//返回文件生成路径
        }catch (Exception e){
            e.printStackTrace();
        }
        return file;
    }

}
