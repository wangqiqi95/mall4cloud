package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.biz.dto.cp.StaffCodeCreateDTO;
import com.mall4j.cloud.api.biz.dto.cp.StaffCodeRefPDTO;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.constant.OriginType;
import com.mall4j.cloud.biz.dto.cp.*;
import com.mall4j.cloud.biz.mapper.cp.CpStaffCodeRefMapper;
import com.mall4j.cloud.biz.mapper.cp.StaffCodeMapper;
import com.mall4j.cloud.biz.model.cp.CpStaffCodeRef;
import com.mall4j.cloud.biz.model.cp.CpWelcomeAttachment;
import com.mall4j.cloud.biz.model.cp.StaffCode;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.vo.cp.AttachMentVO;
import com.mall4j.cloud.biz.vo.cp.StaffCodeVO;
import com.mall4j.cloud.biz.vo.cp.WelcomeDetailVO;
import com.mall4j.cloud.biz.wx.cp.constant.CodeType;
import com.mall4j.cloud.biz.wx.cp.constant.FlagEunm;
import com.mall4j.cloud.biz.wx.cp.constant.StaffCodeOriginEumn;
import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.RandomUtil;
import com.mall4j.cloud.common.util.csvExport.hanlder.ExcelMergeHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpBaseResp;
import me.chanjar.weixin.cp.bean.external.WxCpContactWayInfo;
import me.chanjar.weixin.cp.bean.external.WxCpContactWayResult;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 员工活码表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class StaffCodeServiceImpl implements StaffCodeService {
    private final static String  UPDATE="update";
    private final static String  CREATE="create";

    private final WelcomeService welcomeService;
    private final MapperFacade mapperFacade;
    private final StaffCodeMapper staffCodeMapper;
    private final CpStaffCodeRefMapper staffCodeRefMapper;
    private final StaffCodeRefService staffCodeRefService;
    private final WelcomeAttachmentService welcomeAttachmentService;
    private  final StaffFeignClient staffFeignClient;
    private final MinioUploadFeignClient minioUploadFeignClient;
    private final DownloadCenterFeignClient downloadCenterFeignClient;
    private final CpStaffCodeLogService cpStaffCodeLogService;

    @Override
    public PageVO<StaffCodeVO> page(PageDTO pageDTO, StaffCodePageDTO request) {
        PageVO<StaffCodeVO> pageVO= PageUtil.doPage(pageDTO, () -> staffCodeMapper.list(request));
        pageVO.getList().stream().forEach(staffCodeVO -> {
            if(StrUtil.isNotBlank(staffCodeVO.getSlogan())){
                staffCodeVO.setSlogan(staffCodeVO.getSlogan().replace("${staffName}","${导购姓名}")
                        .replace("${userName}","${客户昵称}")
                        .replace("${storeName}","${门店名称}")
                        .replace("${orgName}","${门店简称}"));
            }
        });
        return pageVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(StaffCode staffCode, AttachmentExtDTO attachMent, List<StaffCodeRefDTO> staffList) {

        if(StrUtil.isNotBlank(staffCode.getSlogan())){
            staffCode.setSlogan(staffCode.getSlogan().replace("${导购姓名}","${staffName}")
                    .replace("${客户昵称}","${userName}")
                    .replace("${门店名称}","${storeName}")
                    .replace("${门店简称}","${orgName}"));
        }

        List<StaffCodeExcelDTO>  excelData = new ArrayList<>();
        List<CpStaffCodeLogDTO> codeLogDTOS=new ArrayList<>();
        //批量单人|单人
        if(CodeType.BATCH.getCode()==staffCode.getCodeType()||CodeType.SINGLE.getCode()==staffCode.getCodeType()){
            for(StaffCodeRefDTO staffCodeRefDTO : staffList ){
                CpStaffCodeRef staffCodeRef = new CpStaffCodeRef();
                staffCodeRef.setStaffId(staffCodeRefDTO.getStaffId());
                staffCode.setState(String.valueOf(RandomUtil.getUniqueNum()));
                StaffCodeExcelDTO staffCodeExcelDTO = createOrUpdateStaffCode(staffCode, Lists.newArrayList(staffCodeRef), WxCpContactWayInfo.TYPE.SINGLE, CREATE);
                if(staffCodeExcelDTO==null) {
                    staffCodeMapper.save(staffCode);
                    //保存活码的员工信息
                    staffCodeRef.setCodeId(staffCode.getId());
                    staffCodeRefService.save(staffCodeRef);
                    //保存附件
                    welcomeAttachmentService.save(new CpWelcomeAttachment(attachMent, OriginType.STAFF_CODE, staffCode.getId()));
                }else{
                    excelData.add(staffCodeExcelDTO);
                    //活码失败记录
                    CpStaffCodeLogDTO cpStaffCodeLogDTO=initCpStaffCodeLogDTO(staffCodeRefDTO, AuthUserContext.get().getUsername()+"添加活码");
                    cpStaffCodeLogDTO.setLogs(staffCodeExcelDTO.getErrorMessage());
                    cpStaffCodeLogDTO.setWxerror(staffCodeExcelDTO.getErrorMessage());
                    cpStaffCodeLogDTO.setErrorCode(staffCodeExcelDTO.getErrorCode());
                    cpStaffCodeLogDTO.setAttachmentExt(JSONObject.toJSONString(attachMent));//日志
                    codeLogDTOS.add(cpStaffCodeLogDTO);
                }
            }
        }
        //批量多人
        if(CodeType.DOUBLE.getCode()==staffCode.getCodeType()){
            staffCode.setState(String.valueOf(RandomUtil.getUniqueNum()));
//            List<StaffCodeRef> list = new ArrayList<>();
            for (StaffCodeRefDTO staffCodeRefDTO : staffList) {
                CpStaffCodeRef staffCodeRef = new CpStaffCodeRef();
                staffCodeRef.setStaffId(staffCodeRefDTO.getStaffId());
                StaffCodeExcelDTO staffCodeExcelDTO = createOrUpdateStaffCode(staffCode, Lists.newArrayList(staffCodeRef), WxCpContactWayInfo.TYPE.MULTI, CREATE);
                if(staffCodeExcelDTO==null) {
                    staffCodeMapper.save(staffCode);
                    //保存活码的员工信息
                    staffCodeRef.setCodeId(staffCode.getId());
                    staffCodeRefService.save(staffCodeRef);
                    //保存附件
                    welcomeAttachmentService.save(new CpWelcomeAttachment(attachMent, OriginType.STAFF_CODE, staffCode.getId()));
                }else{
                    excelData.add(staffCodeExcelDTO);
                    //活码失败记录
                    CpStaffCodeLogDTO cpStaffCodeLogDTO=initCpStaffCodeLogDTO(staffCodeRefDTO, AuthUserContext.get().getUsername()+"添加活码");
                    cpStaffCodeLogDTO.setLogs(staffCodeExcelDTO.getErrorMessage());
                    cpStaffCodeLogDTO.setWxerror(staffCodeExcelDTO.getErrorMessage());
                    cpStaffCodeLogDTO.setErrorCode(staffCodeExcelDTO.getErrorCode());
                    cpStaffCodeLogDTO.setAttachmentExt(JSONObject.toJSONString(attachMent));//日志
                    codeLogDTOS.add(cpStaffCodeLogDTO);
                }
            }

//            for (StaffCodeRefDTO staffCodeRefDTO : staffList) {
//                StaffCodeRef staffCodeRef = new StaffCodeRef();
//                staffCodeRef.setStaffId(staffCodeRefDTO.getStaffId());
//                list.add(staffCodeRef);
//            }
//            StaffCodeExcelDTO staffCodeExcelDTO = createOrUpdateStaffCode(staffCode, list, WxCpContactWayInfo.TYPE.MULTI, CREATE);
//            if(staffCodeExcelDTO==null) {
//                staffCodeMapper.save(staffCode);
//                list.forEach(item -> {
//                    item.setCodeId(staffCode.getId());
//                    staffCodeRefService.save(item);
//                });
//                //保存附件
//                welcomeAttachmentService.save(new WelcomeAttachment(attachMent, OriginType.STAFF_CODE, staffCode.getId()));
//            }else{
//                excelData.add(staffCodeExcelDTO);
//                //活码失败记录
//                Map<Long,StaffCodeRefDTO> staffCodeRefMap=staffList.stream().collect(Collectors.toMap(StaffCodeRefDTO::getStaffId,a -> a,(k1, k2)->k1));
//                StaffCodeRefDTO staffCodeRefDTO=staffCodeRefMap.get(staffCodeExcelDTO.getStaffId());
//                CpStaffCodeLogDTO cpStaffCodeLogDTO=initCpStaffCodeLogDTO(staffCodeRefDTO,AuthUserContext.get().getUsername()+"添加活码");
//                cpStaffCodeLogDTO.setLogs(staffCodeExcelDTO.getErrorMessage());
//                cpStaffCodeLogDTO.setWxerror(staffCodeExcelDTO.getErrorMessage());
//                cpStaffCodeLogDTO.setErrorCode(staffCodeExcelDTO.getErrorCode());
//                cpStaffCodeLogDTO.setAttachmentExt(JSONObject.toJSONString(attachMent));//日志
//                codeLogDTOS.add(cpStaffCodeLogDTO);
//            }
        }
        if(CollectionUtil.isNotEmpty(excelData)){
            uploadErrorStaffCodeExcel(excelData);
        }
        if(CollectionUtil.isNotEmpty(codeLogDTOS)){
            cpStaffCodeLogService.saveErrorTo(codeLogDTOS);
        }
    }

    private void uploadErrorStaffCodeExcel(List<StaffCodeExcelDTO> excelData) {
        try {
            CalcingDownloadRecordDTO recordDTO = new CalcingDownloadRecordDTO();
            recordDTO.setCalCount(excelData.size());
            recordDTO.setDownloadTime(new Date());
            recordDTO.setOperatorName(Objects.nonNull(AuthUserContext.get())? AuthUserContext.get().getUsername():"admin");
            recordDTO.setOperatorNo(Objects.nonNull(AuthUserContext.get())? AuthUserContext.get().getUserId()+"":"1");
            ServerResponseEntity<Long> downloadResp = downloadCenterFeignClient.newCalcingTask(recordDTO);
            //上传oss
            String fileName = StaffCodeExcelDTO.EXCEL_NAME + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String excelUrl = uploadFileToOss(fileName, excelData);
            //将oss会写导出日志
            FinishDownLoadDTO finishDownLoadDTO = new FinishDownLoadDTO();
            finishDownLoadDTO.setId(downloadResp.getData());
            finishDownLoadDTO.setFileName(fileName);
            finishDownLoadDTO.setStatus(excelUrl == null ? 2 : 1);
            finishDownLoadDTO.setFileUrl(excelUrl);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
        }catch (Exception e){
            log.error("",e);
        }
    }

    private String uploadFileToOss(String fileName,List<StaffCodeExcelDTO> excelList){
        try {
            File excelFile = File.createTempFile(fileName, ExcelTypeEnum.XLSX.getValue());
            ExcelWriter excelWriter = EasyExcel.write(new FileOutputStream(excelFile))
                    .registerWriteHandler(new ExcelMergeHandler(StaffCodeExcelDTO.MERGE_ROW_INDEX, StaffCodeExcelDTO.MERGE_COLUMN_INDEX))
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
            if (CollUtil.isNotEmpty(excelList)){
                WriteSheet sheetWriter = EasyExcel.writerSheet("sheet").head(StaffCodeExcelDTO.class).build();
                excelWriter.write(excelList,sheetWriter);
            }
            excelWriter.finish();

            MultipartFile multipartFile = toMultipartFile(fileName,excelFile);
            String time=new SimpleDateFormat("yyyyMMdd").format(new Date());
            String mimoPath = "product/excel/" +time+"/"+ fileName+ ExcelTypeEnum.XLSX.getValue();
            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
            excelFile.delete();
            log.info("活码异常导出excel {}",responseEntity.getData());
            return responseEntity.getData();
        }catch (Exception e){
            log.error("",e);
        }
        return null;

    }

    public static MultipartFile toMultipartFile(String fieldName, File file) throws Exception {
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        String contentType = new MimetypesFileTypeMap().getContentType(file);
        FileItem fileItem = diskFileItemFactory.createItem(fieldName, contentType, false, file.getName());
        try (
                InputStream inputStream = new ByteArrayInputStream(FileCopyUtils.copyToByteArray(file));
                OutputStream outputStream = fileItem.getOutputStream()
        ) {
            FileCopyUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            throw e;
        }
        return new CommonsMultipartFile(fileItem);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(StaffCode staffCode, AttachmentExtDTO attachMent, List<StaffCodeRefDTO> staffList) {

        if(StrUtil.isNotBlank(staffCode.getSlogan())){
            staffCode.setSlogan(staffCode.getSlogan().replace("${导购姓名}","${staffName}")
                    .replace("${客户昵称}","${userName}")
                    .replace("${门店名称}","${storeName}")
                    .replace("${门店简称}","${orgName}"));
        }

        //可以先删除 再添加
        welcomeAttachmentService.deleteByWelId(staffCode.getId(), OriginType.STAFF_CODE.getCode());
        staffCodeRefService.deleteByCodeId(staffCode.getId());
        List<CpStaffCodeRef> list = new ArrayList<>();
        for (StaffCodeRefDTO staffCodeRefDTO : staffList) {
            CpStaffCodeRef staffCodeRef = new CpStaffCodeRef();
            staffCodeRef.setStaffId(staffCodeRefDTO.getStaffId());
            list.add(staffCodeRef);
        }
        //更新企业微信的配置
        StaffCodeExcelDTO staffCodeExcelDTO = createOrUpdateStaffCode(staffCode, list, staffCode.getCodeType() == 3 ? WxCpContactWayInfo.TYPE.MULTI : WxCpContactWayInfo.TYPE.SINGLE, UPDATE);
        if(staffCodeExcelDTO==null) {
            staffCode.setUpdateTime(new Date());
            staffCodeMapper.update(staffCode);
            //更新员工和附件
            list.forEach(item -> {
                item.setCodeId(staffCode.getId());
                staffCodeRefService.save(item);
            });
            welcomeAttachmentService.save(new CpWelcomeAttachment(attachMent, OriginType.STAFF_CODE, staffCode.getId()));
        }

    }

    /**
     * 外部触发生成员工活动（员工批量导入、同步中台员工）
     */
    @Async
    @Override
    public void syncStaffCodeSUP(StaffCodeCreateDTO staffCodeCreateDTO) {
        Integer fromType=staffCodeCreateDTO.getFromType();//0批量导入员工 1中台同步员工
        List<StaffCodeRefPDTO> refPDTOS=staffCodeCreateDTO.getDtos();
        String time=DateUtil.format(new Date(),"yyyyMMdd");
        String codeName="员工活码";
        if(Objects.nonNull(fromType)){
            if(fromType==0) codeName="导入";
            if(fromType==1) codeName="同步";
            if(fromType==2) codeName="手动-活码失败重新生成";
            if(fromType==3) codeName="定时任务-活码失败重新生成";
        }
        codeName=codeName+time;
        log.info("外部触发生成员工活动》》》》》》》》》》》》》》》》》》》》》员工数:{} 来源:{}",refPDTOS.size(),codeName);
        if(CollectionUtil.isNotEmpty(refPDTOS)){
            Long startTime=System.currentTimeMillis();
            log.info("开始执行外部触发生成员工活动》》》》》》》》》》》》》》》》》》》》》员工数:{} 来源:{}",refPDTOS.size(),codeName);
            List<StaffCodeRefDTO> staffList=mapperFacade.mapAsList(refPDTOS, StaffCodeRefDTO.class);

            StaffCode staffCode = new StaffCode();
            staffCode.setCodeName(codeName);
            staffCode.setAuthType(1);
            staffCode.setCodeType(CodeType.BATCH.getCode());
            staffCode.setCreateBy(staffCodeCreateDTO.getCreateById());
            staffCode.setCreateName(staffCodeCreateDTO.getCreateByName());
            staffCode.setStatus(StatusType.YX.getCode());
            staffCode.setFlag(FlagEunm.USE.getCode());
            staffCode.setCreateTime(new Date());
            staffCode.setUpdateTime(staffCode.getCreateTime());
            staffCode.setOrigin(StaffCodeOriginEumn.BACK.getCode());

            List<StaffCodeExcelDTO>  excelData = new ArrayList<>();
            List<CpStaffCodeLogDTO> codeLogDTOS=new ArrayList<>();

            staffList.stream().forEach(staffCodeRefPDTO -> {
                //活码失败记录
                CpStaffCodeLogDTO cpStaffCodeLogDTO=initCpStaffCodeLogDTO(staffCodeRefPDTO,staffCodeCreateDTO.getCreateByName());

                CpStaffCodeRef codeRef=staffCodeRefMapper.getStaffCodeRefByStaffId(staffCodeRefPDTO.getStaffId());
                if(Objects.nonNull(codeRef)){
                    log.info("外部触发生成员工活动-失败 员工id【{}】 姓名【{}】 活码id【{}】",
                            staffCodeRefPDTO.getStaffId(),staffCodeRefPDTO.getStaffName(),codeRef.getCodeId());
                    cpStaffCodeLogDTO.setErrorCode("skqqw002");
                    cpStaffCodeLogDTO.setLogs("活码已存在");
                    codeLogDTOS.add(cpStaffCodeLogDTO);
                    return;
                }
                StaffCodeDTO staffCodeDTO=getStaffCodeDTO(staffCodeRefPDTO.getStoreId());
                if(Objects.isNull(staffCodeDTO)){
                    log.info("外部触发生成员工活动-失败 未获取到欢迎语配置",
                            staffCodeRefPDTO.getStaffId(),staffCodeRefPDTO.getStaffName(),codeRef.getCodeId());
                    cpStaffCodeLogDTO.setErrorCode("skqqw003");
                    cpStaffCodeLogDTO.setLogs("未获取到欢迎语配置");
                    codeLogDTOS.add(cpStaffCodeLogDTO);
                    return;
                }
                AttachmentExtDTO attachMent = AttachMentVO.getAttachMent(staffCodeDTO);
                cpStaffCodeLogDTO.setAttachmentExt(JSONObject.toJSONString(attachMent));//日志
                staffCode.setSlogan(staffCodeDTO.getSlogan());
                attachMent.setLocalUrl(staffCodeDTO.getLocalUrl());
                CpStaffCodeRef staffCodeRef = new CpStaffCodeRef();
                staffCodeRef.setStaffId(staffCodeRefPDTO.getStaffId());
                staffCode.setState(String.valueOf(RandomUtil.getUniqueNum()));
                StaffCodeExcelDTO staffCodeExcelDTO = createOrUpdateStaffCode(staffCode, Lists.newArrayList(staffCodeRef), WxCpContactWayInfo.TYPE.SINGLE, CREATE);
                if(staffCodeExcelDTO==null) {
                    staffCodeMapper.save(staffCode);
                    //保存活码的员工信息
                    staffCodeRef.setCodeId(staffCode.getId());
                    staffCodeRefService.save(staffCodeRef);
                    //保存附件
                    CpWelcomeAttachment attachment= new CpWelcomeAttachment(attachMent, OriginType.STAFF_CODE, staffCode.getId());
                    attachment.setBaseWelId(staffCodeDTO.getBaseWelId());
                    welcomeAttachmentService.save(attachment);
                    log.info("外部触发生成员工活动-成功 员工id【{}】 姓名【{}】 活码地址【{}】",
                            staffCodeRefPDTO.getStaffId(),staffCodeRefPDTO.getStaffName(),staffCode.getQrCode());
                }else{
                    excelData.add(staffCodeExcelDTO);
                    cpStaffCodeLogDTO.setLogs(staffCodeExcelDTO.getErrorMessage());
                    cpStaffCodeLogDTO.setWxerror(staffCodeExcelDTO.getErrorMessage());
                    cpStaffCodeLogDTO.setErrorCode(staffCodeExcelDTO.getErrorCode());
                    codeLogDTOS.add(cpStaffCodeLogDTO);
                }

            });

            log.info("结束执行外部触发生成员工活动》》》》》》》》》》》》》》》》》》》》》耗时：{}ms",System.currentTimeMillis() - startTime);
            //错误日志文件
            if(CollectionUtil.isNotEmpty(excelData)){
                uploadErrorStaffCodeExcel(excelData);
            }
            if(CollectionUtil.isNotEmpty(codeLogDTOS)){
                cpStaffCodeLogService.saveErrorTo(codeLogDTOS);
            }
        }
    }

    private CpStaffCodeLogDTO initCpStaffCodeLogDTO(StaffCodeRefDTO staffCodeRefPDTO, String createBy){
        CpStaffCodeLogDTO cpStaffCodeLogDTO=new CpStaffCodeLogDTO();
        cpStaffCodeLogDTO.setStaffId(staffCodeRefPDTO.getStaffId());
        if(StrUtil.isBlank(staffCodeRefPDTO.getStaffName()) || StrUtil.isBlank(staffCodeRefPDTO.getStaffNo()) || StrUtil.isBlank(staffCodeRefPDTO.getStaffPhone())){
            ServerResponseEntity<StaffVO> responseEntity=staffFeignClient.getStaffById(staffCodeRefPDTO.getStaffId());
            if(responseEntity.isSuccess() && Objects.nonNull(responseEntity.getData())){
                staffCodeRefPDTO.setStaffName(responseEntity.getData().getStaffName());
                staffCodeRefPDTO.setStaffNo(responseEntity.getData().getStaffNo());
                staffCodeRefPDTO.setStaffPhone(responseEntity.getData().getMobile());
            }
        }
        cpStaffCodeLogDTO.setStaffName(staffCodeRefPDTO.getStaffName());
        cpStaffCodeLogDTO.setStaffNo(staffCodeRefPDTO.getStaffNo());
        cpStaffCodeLogDTO.setStaffPhone(staffCodeRefPDTO.getStaffPhone());
        cpStaffCodeLogDTO.setStatus(0);
        cpStaffCodeLogDTO.setDelFlag(0);
        cpStaffCodeLogDTO.setErrorCode("skqqw000");
        cpStaffCodeLogDTO.setLogs("未知");
        cpStaffCodeLogDTO.setCreateBy(createBy);
        cpStaffCodeLogDTO.setCreateTime(new Date());
        return cpStaffCodeLogDTO;
    }

    //根据员工门店获取欢迎语配置（包含全部门店）
    private StaffCodeDTO getStaffCodeDTO(Long storeId){
        WelcomeDetailVO welcomeDetailVO=welcomeService.getWelcomeDetailSimpleByWeight(storeId);
        if(Objects.isNull(welcomeDetailVO)){
            return null;
        }
        StaffCodeDTO staffCodeDTO=new StaffCodeDTO();
        staffCodeDTO.setSlogan(welcomeDetailVO.getWelcome().getSlogan());
        staffCodeDTO.setBaseWelId(welcomeDetailVO.getAttachment().getWelId());
        AttachmentExtDTO attachmentExtDTO=JSONObject.parseObject(welcomeDetailVO.getAttachment().getData(), AttachmentExtDTO.class) ;
        staffCodeDTO.setLocalUrl(attachmentExtDTO.getLocalUrl());
        if(AttachMentVO.IMAGE.equals(welcomeDetailVO.getAttachment().getType())){
            staffCodeDTO.setMsgType(AttachMentVO.IMAGE);
            staffCodeDTO.setPicUrl(attachmentExtDTO.getAttachment().getImage().getPicUrl());
            staffCodeDTO.setMediaId(attachmentExtDTO.getAttachment().getImage().getMediaId());
        }
        if(AttachMentVO.MINIPROGRAM.equals(welcomeDetailVO.getAttachment().getType())){
            staffCodeDTO.setMsgType(AttachMentVO.MINIPROGRAM);
            staffCodeDTO.setAppId(attachmentExtDTO.getAttachment().getMiniProgram().getAppid());
            staffCodeDTO.setPage(attachmentExtDTO.getAttachment().getMiniProgram().getPage());
            staffCodeDTO.setMiniProgramTitle(attachmentExtDTO.getAttachment().getMiniProgram().getTitle());
            staffCodeDTO.setMediaId(attachmentExtDTO.getAttachment().getMiniProgram().getPicMediaId());
        }
        return staffCodeDTO;
    }

    /**
     * 创建qrode
     * @param staffCode
     * @param staffCodeRefs
     */
    private StaffCodeExcelDTO createOrUpdateStaffCode(StaffCode staffCode, List<CpStaffCodeRef> staffCodeRefs, WxCpContactWayInfo.TYPE type, String oper) {
        //try {
            StaffCodeExcelDTO staffCodeExcelDTO = new StaffCodeExcelDTO();
            for(CpStaffCodeRef staffCodeRef : staffCodeRefs){
                ServerResponseEntity<StaffVO> respone = staffFeignClient.getStaffById(staffCodeRef.getStaffId());
                StaffVO staffVO = respone.getData();
                staffCodeRef.setStaffName(staffVO.getStaffName());
                staffCodeRef.setUserId(staffVO.getQiWeiUserId());
                staffCodeExcelDTO.setStaffId(staffCodeRef.getStaffId());
                staffCodeExcelDTO.setStaffNo(staffVO.getStaffNo());
                staffCodeExcelDTO.setStaffName(staffVO.getStaffName());
            }
            WxCpContactWayInfo.ContactWay contactWay =  new WxCpContactWayInfo.ContactWay();
            contactWay.setType(type);
            contactWay.setIsTemp(false);
            contactWay.setScene(WxCpContactWayInfo.SCENE.QRCODE);
            contactWay.setSkipVerify(staffCode.getAuthType()==1?false:true);
            contactWay.setUsers(staffCodeRefs.parallelStream().map(item->item.getUserId()).collect(Collectors.toList()));
            contactWay.setState(staffCode.getState());
            WxCpContactWayInfo cpContactWayInfo =  new WxCpContactWayInfo();
            cpContactWayInfo.setContactWay(contactWay);

            if(CollectionUtil.isEmpty(contactWay.getUsers())){
                staffCodeExcelDTO.setErrorMessage("员工未绑定企业微信id");
                staffCodeExcelDTO.setErrorCode("skqqw001");
                return staffCodeExcelDTO;
            }
            try {
                if (oper.equals(CREATE)) {
                    WxCpContactWayResult result = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().addContactWay(cpContactWayInfo);
                    if (result.getErrcode() == 0) {
                        staffCode.setQrCode(result.getQrCode());
                        staffCode.setConfigId(result.getConfigId());
                    } else {
                        staffCodeExcelDTO.setErrorMessage(result.getErrmsg());
                        staffCodeExcelDTO.setErrorCode(result.getErrcode().toString());
                        return staffCodeExcelDTO;
                        //throw new WxErrorException(result.getErrmsg());
                    }
                } else {
                    StaffCode staffCodeDTO = this.getById(staffCode.getId());
                    cpContactWayInfo.getContactWay().setConfigId(staffCodeDTO.getConfigId());
                    WxCpBaseResp resp = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().updateContactWay(cpContactWayInfo);
                    if (!resp.success()) {
                        staffCodeExcelDTO.setErrorMessage(resp.getErrmsg());
                        staffCodeExcelDTO.setErrorCode(resp.getErrcode().toString());
                        return staffCodeExcelDTO;
                        //throw new WxErrorException(resp.getErrmsg());
                    }
                }
            }catch (WxErrorException e){
                staffCodeExcelDTO.setErrorMessage("ErrorCode:"+e.getError().getErrorCode()+" message:"+e.getMessage());
                staffCodeExcelDTO.setErrorCode(String.valueOf(e.getError().getErrorCode()));
                log.error("",e);
                return staffCodeExcelDTO;
            }
       /* }catch (WxErrorException e){
            throw new RuntimeException("错误代码："+e.getError().getErrorCode()+" 提示消息："+e.getMessage());
        }*/
        return null;
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long id) {
        try {
            StaffCode staffCode = staffCodeMapper.getById(id);
            welcomeAttachmentService.deleteByWelId(id, OriginType.STAFF_CODE.getCode());
            staffCodeRefService.deleteByCodeId(id);
            staffCodeMapper.deleteById(id);
            WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().deleteContactWay(staffCode.getConfigId());
        }catch (WxErrorException e){
            throw new RuntimeException("错误代码："+e.getError().getErrorCode()+" 提示消息："+e.getMessage());
        }
    }


    @Override
    public StaffCode getById(Long id) {
        StaffCode staffCode=staffCodeMapper.getById(id);
        if(StrUtil.isNotBlank(staffCode.getSlogan())){
            staffCode.setSlogan(staffCode.getSlogan().replace("staffName","导购姓名")
                    .replace("userName","客户昵称")
                    .replace("storeName","门店名称")
                    .replace("orgName","门店简称"));
        }
        return staffCode;
    }

    @Override
    public StaffCode selectByStaffIdAndState(Long staffId, String state) {
        return staffCodeMapper.selectByStaffIdAndState(staffId,state);
    }

    @Override
    public List<StaffCode> selectByStaffId(Long staffId) {
        return staffCodeMapper.selectByStaffId(staffId);
    }
}
