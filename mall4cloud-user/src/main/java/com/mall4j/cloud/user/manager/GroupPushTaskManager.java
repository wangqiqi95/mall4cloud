package com.mall4j.cloud.user.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.cp.externalcontact.AttachmentExtApiDTO;
import com.mall4j.cloud.api.biz.dto.cp.media.UploadUrlMediaDTO;
import com.mall4j.cloud.api.biz.feign.WeixinCpExternalContactFeignClient;
import com.mall4j.cloud.api.biz.feign.WeixinCpMediaFeignClient;
import com.mall4j.cloud.api.biz.feign.WxCpCustNotifyFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinUploadMediaResultVO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.SystemUtils;
import com.mall4j.cloud.user.bo.ExportGroupPushTaskStatisticsBO;
import com.mall4j.cloud.user.bo.ExportSonTaskStaffPageBO;
import com.mall4j.cloud.user.bo.ExportStaffSendRecordBO;
import com.mall4j.cloud.user.mapper.GroupPushTaskMapper;
import com.mall4j.cloud.user.model.GroupPushTask;
import com.mall4j.cloud.user.vo.GroupPushTaskVO;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.bean.external.WxCpMsgTemplateAddResult;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Slf4j
@Service
public class GroupPushTaskManager {

    @Autowired
    private GroupPushTaskMapper groupPushTaskMapper;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Autowired
    private WeixinCpMediaFeignClient weixinCpMediaFeignClient;

    @Autowired
    private WeixinCpExternalContactFeignClient weixinCpExternalContactFeignClient;

    @Autowired
    private WxCpCustNotifyFeignClient wxCpCustNotifyFeignClient;

    @Autowired
    private StaffFeignClient staffFeignClient;


    public GroupPushTaskVO getByPushTaskID(Long pushTaskId){
        GroupPushTask groupPushTask = groupPushTaskMapper.selectById(pushTaskId);

        GroupPushTaskVO groupPushTaskVO = new GroupPushTaskVO();

        BeanUtils.copyProperties(groupPushTask, groupPushTaskVO);

        return groupPushTaskVO;
    }

    public Boolean checkDownloadData(List downLoadDataList, FinishDownLoadDTO finishDownLoadDTO){
        if (CollectionUtil.isEmpty(downLoadDataList)){
            finishDownLoadDTO.setRemarks("无相关推送明细数据导出");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error("无相关推送明细数据导出");
            return true;
        }
        return false;
    }


    public String createExcelFile(List<ExportGroupPushTaskStatisticsBO> statisticsBOListExcelVOList, List<ExportSonTaskStaffPageBO> exportSonTaskStaffPageBOList,
                                  List<ExportStaffSendRecordBO> sendRecordBOList, String  fileName) {
        String file = null;
        try {
            String pathExport = SystemUtils.getExcelFilePath() + "/" + fileName + ".xlsx";
            File realFile = new File(pathExport);
            OutputStream outputStream = new FileOutputStream(realFile);
            ExcelWriter excelWriter = EasyExcel.write(outputStream).build();
            //生成第一个sheet保存任务统计数据
            WriteSheet groupPushTaskStatisticSheet = EasyExcel.writerSheet(0, ExportGroupPushTaskStatisticsBO.SHEET_NAME)
                    .head(ExportGroupPushTaskStatisticsBO.class).build();
            excelWriter.write(statisticsBOListExcelVOList, groupPushTaskStatisticSheet);
            //生成第二个sheet保存导购任务数据
            WriteSheet sonTaskStaffPageSheet = EasyExcel.writerSheet(1, ExportSonTaskStaffPageBO.SHEET_NAME)
                    .head(ExportSonTaskStaffPageBO.class).build();
            excelWriter.write(exportSonTaskStaffPageBOList, sonTaskStaffPageSheet);
            //生成第三个sheet保存导购触达明细数据
            WriteSheet exportStaffSendRecordSheet = EasyExcel.writerSheet(2, ExportStaffSendRecordBO.SHEET_NAME)
                    .head(ExportStaffSendRecordBO.class).build();
            excelWriter.write(sendRecordBOList, exportStaffSendRecordSheet);
            //关闭流
            excelWriter.finish();

            return pathExport;//返回文件生成路径
        } catch (Exception e) {
            log.error("会员礼物活动会员名单导出异常，message is:{}", e);
        }
        return file;
    }

    public WeixinUploadMediaResultVO uploadByUrlFile(UploadUrlMediaDTO uploadUrlMediaDTO){
        log.info("UPLOAD URL FILE PARAM IS:{}", JSONObject.toJSONString(uploadUrlMediaDTO));
        ServerResponseEntity<WeixinUploadMediaResultVO> response = weixinCpMediaFeignClient.uploadByUrlFile(uploadUrlMediaDTO);

        if (response.isSuccess()){
            return response.getData();
        }else {
            throw new LuckException(response.getMsg());
        }
    }

    public WeixinUploadMediaResultVO uploadEventCodeFile(UploadUrlMediaDTO uploadUrlMediaDTO){
        log.info("UPLOAD MERGE FILE PARAM IS:{}", JSONObject.toJSONString(uploadUrlMediaDTO));
        ServerResponseEntity<WeixinUploadMediaResultVO> response = weixinCpMediaFeignClient.uploadEventCodeFile(uploadUrlMediaDTO);

        if (response.isSuccess()){
            return response.getData();
        }else {
            throw new LuckException(response.getMsg());
        }
    }


    public WxCpMsgTemplateAddResult addExternalContactMsgTemplate(String wxCpMsgTemplate){
        ServerResponseEntity<WxCpMsgTemplateAddResult> responseEntity = weixinCpExternalContactFeignClient.addExternalContactMsgTemplate(wxCpMsgTemplate);
        if (responseEntity.isSuccess()){
            return responseEntity.getData();
        }else {
            throw new LuckException(responseEntity.getMsg());
        }
    }


    public String defineStaffMiniProgram(Attachment attachment, Long staffId, Long staffStoreId){

        AttachmentExtApiDTO attachmentExtApiDTO = new AttachmentExtApiDTO(attachment, "", staffId ,staffStoreId);

        ServerResponseEntity<String> response = wxCpCustNotifyFeignClient.defineStaffMiniProgram(attachmentExtApiDTO);

        if (response.isFail()){
            throw new LuckException("CREATE TENTACLE IS FAIL , MESSAGE IS :{}",response.getMsg());
        }else {
            return response.getData();
        }

    }


    public StaffVO getStaffById(Long staffId){
        ServerResponseEntity<StaffVO> response = staffFeignClient.getStaffById(staffId);
        if (response.isSuccess()){
            return response.getData();
        }
        return null;
    }


}
