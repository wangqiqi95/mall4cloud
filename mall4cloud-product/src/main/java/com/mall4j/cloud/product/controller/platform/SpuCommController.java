package com.mall4j.cloud.product.controller.platform;

import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.order.feign.OrderCommFeignClient;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.dto.SpuCommPageDTO;
import com.mall4j.cloud.product.dto.SpuCommReplyDTO;
import com.mall4j.cloud.product.dto.SpuCommShowDTO;
import com.mall4j.cloud.product.model.SpuComm;
import com.mall4j.cloud.product.service.SpuCommService;
import com.mall4j.cloud.product.vo.SpuCommExcelVO;
import com.mall4j.cloud.product.vo.SpuCommVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.validation.Valid;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author hwy
 * @Date 2021/7/19 16:42
 */
@Slf4j
@RequiredArgsConstructor
@RestController("platformSpuCommController")
@RequestMapping("/p/spucomm")
@Api(tags = "platform-spucomm信息")
public class SpuCommController {
    private final OrderCommFeignClient orderCommFeignClient;
    private final SpuCommService spuCommService;
    private final DownloadCenterFeignClient downloadCenterFeignClient;
    private final MinioUploadFeignClient minioUploadFeignClient;
    private final StaffFeignClient staffFeignClient;
    @GetMapping("/page")
    @ApiOperation(value = "评论列表", notes = "评论列表")
    public ServerResponseEntity<PageVO<SpuCommVO>> toTopBySpuId(@Valid PageDTO pageDTO, SpuCommPageDTO request) {
        PageVO<SpuCommVO> spuCommVOPageVO = spuCommService.page(pageDTO,request);
        return ServerResponseEntity.success(spuCommVOPageVO);
    }

    @PostMapping("/showOrHide")
    @ApiOperation(value = "批量展示或者批量隐藏", notes = "批量展示或者批量隐藏")
    public ServerResponseEntity<Void> showOrHide(@Valid @RequestBody SpuCommShowDTO request) {
        request.getIds().forEach(id ->{
            SpuComm comm = new SpuComm();
            comm.setSpuCommId(id);
            comm.setStatus(request.getStatus());
            spuCommService.update(comm);
        });
        return ServerResponseEntity.success();
    }

    @PostMapping("/reply")
    @ApiOperation(value = "商家回复接口", notes = "商家回复接口")
    public ServerResponseEntity<Void> reply(@Valid @RequestBody SpuCommReplyDTO request) {
        SpuComm comm = new SpuComm();
        comm.setSpuCommId(request.getId());

        if(request.getType()==0){
            comm.setReplyContent(request.getContent());
            comm.setReplySts(1);
            comm.setReplyStaffName(AuthUserContext.get().getUsername());
            comm.setReplyTime(new Date());
        }

        if(request.getType()==1){
            comm.setAdditReplyContent(request.getContent());
            comm.setAdditReplySts(1);
            comm.setAdditReplyStaffName(AuthUserContext.get().getUsername());
            comm.setAdditReplyTime(new Date());
        }
        spuCommService.update(comm);
        return ServerResponseEntity.success();
    }

    /**
     * 导出那里还需要再稍微调整下，汪洋这边做了一个统一下载中心的功能，后面所有的导出下载在这个功能上进行下载文件了。你需要在导出的时候调用下汪洋的接口，汪洋生成一笔导出日志，你导出完后把文件上传到oss上，然后再调用下汪洋的接口，把oss的地址给到他
     */
    @GetMapping("/spu_comm_excel")
    @ApiOperation(value = "导出excel", notes = "导出评论excel")
    public ServerResponseEntity spuCommExcel(SpuCommPageDTO request) {
        try {
            List<SpuCommVO> list  = spuCommService.list(request);
            List<SpuCommExcelVO> excelList = new ArrayList<>();
            //product/excel/202203
            if(!CollectionUtils.isEmpty(list)){
                ServerResponseEntity<List<OrderItemVO>> orderItemRes = orderCommFeignClient.listOrderItemByIds(new ArrayList<>(list.stream().map(SpuCommVO::getOrderItemId).collect(Collectors.toList())));
                Map<Long, OrderItemVO> orderItemLangMap = orderItemRes.getData().stream().collect(Collectors.toMap(OrderItemVO::getOrderItemId, o -> o));
                list.forEach(item ->{
                    SpuCommExcelVO spuCommExcelVO = new SpuCommExcelVO();
                    spuCommExcelVO.setProdName(item.getProdName());
                    OrderItemVO orderItemVO = orderItemLangMap.get(item.getOrderItemId());
                    if(orderItemVO!=null) {
                        spuCommExcelVO.setSkuName(orderItemVO.getSkuName());
                    }
                    spuCommExcelVO.setOrderId(item.getOrderId()==null?"":item.getOrderId().toString());
                    spuCommExcelVO.setUserName(item.getUserName());
                    spuCommExcelVO.setMobile(item.getMobile());
                    spuCommExcelVO.setIsAnonymous((item.getIsAnonymous()==null||item.getIsAnonymous()==0)?"否":"是");
                    spuCommExcelVO.setContent(item.getContent());
                    spuCommExcelVO.setEvaluate(item.getEvaluate()==1?"好评":(item.getEvaluate()==2?"中评":"差评"));
                    spuCommExcelVO.setHasImages((item.getHasImages()==null||item.getHasImages()==0)?"是":"否");
                    spuCommExcelVO.setCreateTime(item.getCreateTime());
                    spuCommExcelVO.setReplyContent(item.getReplyContent());
                    spuCommExcelVO.setReplyTime(item.getReplyTime());
                    switch (item.getStatus()){
                        case -1 :spuCommExcelVO.setStatus("删除"); break;
                        case 0 :spuCommExcelVO.setStatus("不显示"); break;
                        case 1 :spuCommExcelVO.setStatus("显示"); break;
                        case 2 :spuCommExcelVO.setStatus("审核不通过"); break;
                        case 3 :spuCommExcelVO.setStatus("待审核"); break;
                        default: ;
                    }
                    excelList.add(spuCommExcelVO);
                });
            }
//            ServerResponseEntity<StaffVO> staffResp = staffFeignClient.getStaffById(AuthUserContext.get().getUserId());
            //创建导出记录
            CalcingDownloadRecordDTO recordDTO = new CalcingDownloadRecordDTO();
            recordDTO.setCalCount(excelList.size());
            recordDTO.setDownloadTime(new Date());
            recordDTO.setOperatorName(AuthUserContext.get().getUsername());
            recordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
//            if(staffResp!=null && staffResp.getData()!=null) {
//                StaffVO staffVO = staffResp.getData();
//                recordDTO.setOperatorName(staffVO.getStaffName());
//                recordDTO.setOperatorNo(staffVO.getStaffNo());
//            }
            ServerResponseEntity<Long> downloadResp = downloadCenterFeignClient.newCalcingTask(recordDTO);
            //上传oss
            String fileName =SpuCommExcelVO.EXCEL_NAME+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String excelUrl = uploadFileToOss(fileName,excelList);
            //将oss会写导出日志
            FinishDownLoadDTO finishDownLoadDTO = new FinishDownLoadDTO();
            finishDownLoadDTO.setId(downloadResp.getData());
            finishDownLoadDTO.setFileName(fileName);
            finishDownLoadDTO.setStatus(excelUrl==null?2:1);
            finishDownLoadDTO.setFileUrl(excelUrl);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            return ServerResponseEntity.success("操作成功，请在下载中心下载");
        }catch (Exception e){
            log.error("评价管理导出excel失败",e.getMessage(),e);
            return ServerResponseEntity.success("操作失败");
        }

    }


    private String uploadFileToOss(String fileName,List<SpuCommExcelVO> excelList){
        try {
//            File excelFile = File.createTempFile(fileName, ExcelTypeEnum.XLSX.getValue());
//            ExcelWriter excelWriter = EasyExcel.write(new FileOutputStream(excelFile))
//                    .registerWriteHandler(new ExcelMergeHandler(SpuCommExcelVO.MERGE_ROW_INDEX, SpuCommExcelVO.MERGE_COLUMN_INDEX))
//                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
//            if (CollUtil.isNotEmpty(excelList)){
//                WriteSheet sheetWriter = EasyExcel.writerSheet("sheet").head(SpuCommExcelVO.class).build();
//                excelWriter.write(excelList,sheetWriter);
//            }
//            excelWriter.finish();
//
//            MultipartFile multipartFile = toMultipartFile(fileName,excelFile);
//            String time=new SimpleDateFormat("yyyyMMdd").format(new Date());
//            String mimoPath = "product/excel/" +time+"/"+ fileName+ExcelTypeEnum.XLSX.getValue();
//            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
//            excelFile.delete();
//            log.info("评价管理导出excel {}",responseEntity.getData());
//            return responseEntity.getData();
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
         MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
         return multipartFile;
   }

}
