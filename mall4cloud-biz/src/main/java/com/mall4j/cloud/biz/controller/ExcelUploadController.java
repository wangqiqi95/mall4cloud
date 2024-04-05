package com.mall4j.cloud.biz.controller;

import com.mall4j.cloud.api.biz.dto.ExcelUploadDTO;
import com.mall4j.cloud.api.biz.vo.WeixinQrcodeScanRecordLogVO;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.biz.service.ExcelUploadService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Date 2021年12月30日, 0030 15:18
 * @Created by eury
 */
@Slf4j
@RestController
@RequestMapping("/p/excel")
@Api(tags = "excelupload")
public class ExcelUploadController {

    @Resource
    private ExcelUploadService excelUploadService;

//    @Autowired
//    private ExcelUploadFeignClient excelUploadFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

//    @Autowired
//    private OnsMQTemplate soldUploadExcelTemplate;

    @GetMapping("/upload/sold_excel")
    @ApiOperation(value = "导出excel-上传测试", notes = "导出excel-上传测试")
    public ServerResponseEntity<String> orderSoldExcel(HttpServletResponse response){
        try {
            CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
            downloadRecordDTO.setDownloadTime(new Date());
            downloadRecordDTO.setFileName(WeixinQrcodeScanRecordLogVO.EXCEL_NAME);
            downloadRecordDTO.setCalCount(0);
            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
            downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
            ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
            Long downLoadHisId=null;
            if(serverResponseEntity.isSuccess()){
                downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
            }


            List<WeixinQrcodeScanRecordLogVO> list = new ArrayList<>();
            WeixinQrcodeScanRecordLogVO weixinQrcodeScanRecordLogVO=new WeixinQrcodeScanRecordLogVO();
//            weixinQrcodeScanRecordLogVO.setHeadimgurl("headimageurl");
            weixinQrcodeScanRecordLogVO.setOpenid("90909090090");


            for(int i=0;i<10000;i++){
//                weixinQrcodeScanRecordLogVO.setNickName("nicildkjafojdafi--->"+i);
                list.add(weixinQrcodeScanRecordLogVO);
            }

            ExcelUploadDTO excelUploadDTO=new ExcelUploadDTO(downLoadHisId,
                    list,
                    WeixinQrcodeScanRecordLogVO.EXCEL_NAME,
                    WeixinQrcodeScanRecordLogVO.MERGE_ROW_INDEX,
                    WeixinQrcodeScanRecordLogVO.MERGE_COLUMN_INDEX,
                    WeixinQrcodeScanRecordLogVO.class);

//            excelUploadService.soleAndUploadExcel(excelUploadDTO);

//            excelUploadFeignClient.soleAndUploadExcel(excelUploadDTO);

//            soldUploadExcelTemplate.syncSend(excelUploadDTO);

            return ServerResponseEntity.success("操作成功，请转至下载中心下载");
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("导出失败");
        }

    }


}
