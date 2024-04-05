package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.dto.cp.StaffCodeCreateDTO;
import com.mall4j.cloud.api.biz.dto.cp.StaffCodeRefPDTO;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.biz.vo.SoldStaffCodeErrorExcelVO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.dto.cp.CpStaffCodeLogDTO;
import com.mall4j.cloud.biz.dto.cp.ReqStaffCodeLogDTO;
import com.mall4j.cloud.biz.mapper.cp.CpStaffCodeLogMapper;
import com.mall4j.cloud.biz.model.cp.CpErrorCodeLog;
import com.mall4j.cloud.biz.model.cp.CpStaffCodeLog;
import com.mall4j.cloud.biz.service.cp.CpErrorCodeService;
import com.mall4j.cloud.biz.service.cp.CpStaffCodeLogService;
import com.mall4j.cloud.biz.vo.cp.CpStaffCodeLogVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.SystemUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
@Service
@RefreshScope
public class CpStaffCodeLogServiceImpl extends ServiceImpl<CpStaffCodeLogMapper,CpStaffCodeLog> implements CpStaffCodeLogService {

    @Autowired
    private CpStaffCodeLogMapper cpStaffCodeLogMapper;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;
    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private CpErrorCodeService cpErrorCodeService;
    @Value("${mall4cloud.biz.staffCodeErrorPageSize:5000}")
    @Setter
    private Integer staffCodeErrorPageSize;

    @Override
    public PageVO<CpStaffCodeLogVO> listPage(PageDTO pageDTO, ReqStaffCodeLogDTO reqStaffCodeLogDTO) {
        return PageUtil.doPage(pageDTO, () -> cpStaffCodeLogMapper.listPage(reqStaffCodeLogDTO));
    }

    @Override
    public List<CpStaffCodeLogVO> listData(ReqStaffCodeLogDTO reqStaffCodeLogDTO) {
        return cpStaffCodeLogMapper.listPage(reqStaffCodeLogDTO);
    }

    @Override
    public void saveErrorTo(List<CpStaffCodeLogDTO> logDTOList) {
        if(CollectionUtil.isNotEmpty(logDTOList)){
            List<CpStaffCodeLog> logs=mapperFacade.mapAsList(logDTOList,CpStaffCodeLog.class);

            //需要覆盖保存
            this.deleteByStaffIds(logs.stream().map(CpStaffCodeLog::getStaffId).collect(Collectors.toList()));

            Map<String,CpErrorCodeLog> errorCodeLogs=cpErrorCodeService.listData(null);
            if(CollectionUtil.isNotEmpty(errorCodeLogs)){
                logs.forEach(item->{
                    if(StrUtil.isNotBlank(item.getErrorCode()) && errorCodeLogs.containsKey(item.getErrorCode())){
                        item.setLogs(errorCodeLogs.get(item.getErrorCode()).getErrormsg());
                    }
                });
            }
            this.saveBatch(logs);
        }
    }

    @Override
    public void rCreateCode(List<Long> staffIds) {
        if(CollectionUtil.isNotEmpty(staffIds)){
            ReqStaffCodeLogDTO codeLogDTO=new ReqStaffCodeLogDTO();
            codeLogDTO.setNoErrorCodes(ListUtil.of("skqqw002"));
            codeLogDTO.setStaffIds(staffIds);
            List<CpStaffCodeLogVO> logVOS=this.listData(codeLogDTO);
            if(CollectionUtil.isEmpty(logVOS)){
                throw new LuckException("未获取到生成活码失败记录数据");
            }
            staffIds=logVOS.stream().map(CpStaffCodeLogVO::getStaffId).collect(Collectors.toList());
            ServerResponseEntity<List<StaffVO>> responseEntity=staffFeignClient.getStaffByIds(staffIds);
            if(responseEntity.isSuccess() && CollectionUtil.isNotEmpty(responseEntity.getData())){
                List<StaffCodeRefPDTO> refPDTOS=new ArrayList<>();
                responseEntity.getData().stream().forEach(staff -> {
                    refPDTOS.add(new StaffCodeRefPDTO(staff.getId(),staff.getStoreId(),staff.getStaffName(),staff.getStaffNo(),staff.getMobile()));
                });
                applicationContext.publishEvent(new StaffCodeCreateDTO(AuthUserContext.get().getUserId(),
                        AuthUserContext.get().getUsername()+"重新生成活码",2,refPDTOS));
            }

        }
    }

    @Override
    public void staffCodeErrorCreate() {
        int currentPage = 1;
        int pageSize = staffCodeErrorPageSize;
        PageDTO pageDTO=new PageDTO();
        pageDTO.setPageNum(currentPage);
        pageDTO.setPageSize(pageSize,true);
        ReqStaffCodeLogDTO codeLogDTO=new ReqStaffCodeLogDTO();
        codeLogDTO.setNoErrorCodes(ListUtil.of("skqqw002"));
        PageVO<CpStaffCodeLogVO> pageVO=this.listPage(pageDTO,codeLogDTO);
        log.info("活码失败定时重新生成 总条数: {} 总页数：{}",pageVO.getTotal(),pageVO.getPages());
        if(CollectionUtil.isNotEmpty(pageVO.getList())){
            log.info("活码失败定时重新生成--->首次执行 当前页为第【{}】页  单页限制最大条数【{}】 获取到数据总条数【{}】",
                    pageDTO.getPageNum(),pageDTO.getPageSize(),pageVO.getList().size());

            executeStaffCodeErrorCreate(pageVO.getList());

            if(pageVO.getPages()>1){
                for (int i = 2; i <= pageVO.getPages(); i++) {
                    currentPage = currentPage + 1;
                    pageDTO=new PageDTO();
                    pageDTO.setPageNum(currentPage);
                    pageDTO.setPageSize(pageSize,true);

                    pageVO=this.listPage(pageDTO,codeLogDTO);

                    log.info("活码失败定时重新生成--->分页处理中 当前页为第【{}】页  单页限制最大条数【{}】 获取到数据总条数【{}】",
                            pageDTO.getPageNum(),pageDTO.getPageSize(),pageVO.getList().size());

                    executeStaffCodeErrorCreate(pageVO.getList());
                }
            }
        }
    }

    private void executeStaffCodeErrorCreate(List<CpStaffCodeLogVO> cpStaffCodeLogVOS){
        if(CollectionUtil.isNotEmpty(cpStaffCodeLogVOS)){
            log.info("staffCodeErrorCreate 条数: {}",cpStaffCodeLogVOS.size());
            List<Long> staffIds=cpStaffCodeLogVOS.stream().map(CpStaffCodeLogVO::getStaffId).collect(Collectors.toList());

            ServerResponseEntity<List<StaffVO>> responseEntity=staffFeignClient.getStaffByIds(staffIds);
            if(responseEntity.isSuccess() && CollectionUtil.isNotEmpty(responseEntity.getData())){
                List<StaffCodeRefPDTO> refPDTOS=new ArrayList<>();
                responseEntity.getData().stream().forEach(staff -> {
                    refPDTOS.add(new StaffCodeRefPDTO(staff.getId(),staff.getStoreId(),staff.getStaffName(),staff.getStaffNo(),staff.getMobile()));
                });
                applicationContext.publishEvent(new StaffCodeCreateDTO(0L,
                        "定时任务重新生成活码",3,refPDTOS));
            }
        }
    }


    @Override
    public void deleteById(Long id) {
        this.update(new LambdaUpdateWrapper<CpStaffCodeLog>().eq(CpStaffCodeLog::getId,id).set(CpStaffCodeLog::getDelFlag,1));
    }

    @Override
    public void deleteByStaffIds(List<Long> staffIds) {
        this.update(new LambdaUpdateWrapper<CpStaffCodeLog>().in(CpStaffCodeLog::getStaffId,staffIds).set(CpStaffCodeLog::getDelFlag,1));
    }


    /**
     * 导出
     */
    @Async
    @Override
    public void soldStaffCodeLog(ReqStaffCodeLogDTO reqStaffCodeLogDTO,Long downHistoryId) {
        reqStaffCodeLogDTO.setNoErrorCodes(ListUtil.of("skqqw002"));
        List<CpStaffCodeLogVO> cpStaffCodeLogVOs=cpStaffCodeLogMapper.listPage(reqStaffCodeLogDTO);
        if(CollectionUtil.isNotEmpty(cpStaffCodeLogVOs)){
            List<SoldStaffCodeErrorExcelVO> codeErrorExcelVOS=mapperFacade.mapAsList(cpStaffCodeLogVOs,SoldStaffCodeErrorExcelVO.class);

            createLogAndUpload(downHistoryId,codeErrorExcelVOS);
        }
    }

    private void createLogAndUpload(Long downLoadHisId, List<SoldStaffCodeErrorExcelVO> codeErrorExcelVOS){
        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(downLoadHisId);
        File file=null;
        try {

            String pathExport= SystemUtils.getExcelFilePath()+"/"+ SystemUtils.getExcelName()+".xlsx";
            EasyExcel.write(pathExport, SoldStaffCodeErrorExcelVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(codeErrorExcelVOS);
            String contentType = "application/vnd.ms-excel";

            file=new File(pathExport);
            FileInputStream is = new FileInputStream(file);
            MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
                    contentType, is);

            String originalFilename = multipartFile.getOriginalFilename();
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String mimoPath = "excel/" + time + "/" + originalFilename;
            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
            if (responseEntity.isSuccess() && finishDownLoadDTO!=null) {
                //下载中心记录
                finishDownLoadDTO.setCalCount(codeErrorExcelVOS.size());
                finishDownLoadDTO.setFileName("员工活码失败记录"+time);
                finishDownLoadDTO.setStatus(1);
                finishDownLoadDTO.setFileUrl(responseEntity.getData());
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
            // 删除临时文件
            if (file.exists()) {
                file.delete();
            }
        }catch (Exception e){
            log.info("导出活码失败记录信息错误: {} {}",e,e.getMessage());
            finishDownLoadDTO.setRemarks("导出活码失败记录信息错误："+e.getMessage());
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            // 删除临时文件
            if (file!=null && file.exists()) {
                file.delete();
            }
        }
    }
}
