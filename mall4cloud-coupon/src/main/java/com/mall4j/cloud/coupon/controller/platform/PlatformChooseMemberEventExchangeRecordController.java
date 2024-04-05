package com.mall4j.cloud.coupon.controller.platform;


import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.dto.ChooseMemberEventExchangeRecordPageDTO;
import com.mall4j.cloud.coupon.dto.ChooseMemberEventRecordConfirmDTO;
import com.mall4j.cloud.coupon.dto.ChooseMemberEventRecordDTO;
import com.mall4j.cloud.coupon.service.ChooseMemberEventExchangeRecordService;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventExchangeRecordExcelVO;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventExchangeRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 *  高价值会员活动兑换记录
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
@RestController
@RequestMapping("/p/choose/member/event/exchange/record")
@Api(tags = "高价值会员活动兑换记录API")
@Slf4j
public class PlatformChooseMemberEventExchangeRecordController {

    @Autowired
    private ChooseMemberEventExchangeRecordService chooseMemberEventExchangeRecordService;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;


    @PostMapping("/recordList")
    @ApiOperation("查询活动兑换记录")
    public ServerResponseEntity<PageInfo<ChooseMemberEventExchangeRecordVO>> recordList(@RequestBody ChooseMemberEventExchangeRecordPageDTO pageDTO) {
        return chooseMemberEventExchangeRecordService.recordList(pageDTO);
    }

    @PostMapping("/addLogistics")
    @ApiOperation(value = "添加物流信息")
    public ServerResponseEntity<Void> addLogistics(@RequestBody ChooseMemberEventRecordDTO recordDTO){
        return chooseMemberEventExchangeRecordService.addLogistics(recordDTO);
    }

    @PostMapping("/confirmExport")
    @ApiOperation(value = "记录信息确认")
    public ServerResponseEntity<Void> confirmExport(@RequestBody ChooseMemberEventRecordConfirmDTO recordConfirmDTO){
        return chooseMemberEventExchangeRecordService.confirmExport(recordConfirmDTO.getIds());
    }

    @PostMapping("/importRecord")
    @ApiOperation(value = "导入兑换记录")
    public ServerResponseEntity<Map<String,Integer>> importRecord(@RequestParam("file") MultipartFile file) {
        return chooseMemberEventExchangeRecordService.importRecord(file);
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出兑换记录")
    public ServerResponseEntity<String> export(@RequestBody ChooseMemberEventExchangeRecordPageDTO param, HttpServletResponse response){

        try {
            CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
            downloadRecordDTO.setDownloadTime(new Date());
            downloadRecordDTO.setFileName("高价值会员兑换活动ID"+param.getEventId()+"-"+ ChooseMemberEventExchangeRecordExcelVO.EXCEL_NAME);
            downloadRecordDTO.setCalCount(0);
            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
            downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
            ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
            Long downLoadHisId=null;
            if(serverResponseEntity.isSuccess()){
                downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
            }

            param.setDownLoadHisId(downLoadHisId);
            chooseMemberEventExchangeRecordService.export(param,response);

            return ServerResponseEntity.success("操作成功，请转至下载中心下载");
        }catch (Exception e){
            log.error("导出兑换记录错误: "+e.getMessage(),e);
            return ServerResponseEntity.showFailMsg("操作失败");
        }
    }
}

