package com.mall4j.cloud.biz.controller.wx.cp;

import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.constant.OriginType;
import com.mall4j.cloud.biz.dto.cp.*;
import com.mall4j.cloud.biz.model.cp.CpWelcomeAttachment;
import com.mall4j.cloud.biz.service.cp.DistributionMomentsCommentsService;
import com.mall4j.cloud.biz.service.cp.DistributionMomentsSendRecordService;
import com.mall4j.cloud.biz.service.cp.DistributionMomentsService;
import com.mall4j.cloud.biz.service.cp.WelcomeAttachmentService;
import com.mall4j.cloud.biz.task.CpMomentsTask;
import com.mall4j.cloud.biz.vo.cp.DistributionMomentsCommentsVO;
import com.mall4j.cloud.biz.vo.cp.DistributionMomentsVO;
import com.mall4j.cloud.biz.vo.cp.MomentSendRecordExcelVO;
import com.mall4j.cloud.biz.vo.cp.MomentSendRecordPageVO;
import com.mall4j.cloud.biz.wx.cp.utils.ZipUtils;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 分销推广-朋友圈
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
@Slf4j
@RestController("CPplatformDistributionMomentsController")
@RequestMapping("/p/distribution_moments")
@Api(tags = "平台端-分销推广-朋友圈")
public class DistributionMomentsController {

    @Autowired
    private DistributionMomentsService distributionMomentsService;
    @Autowired
    DistributionMomentsSendRecordService distributionMomentsSendRecordService;
    @Autowired
    WelcomeAttachmentService attachmentService;
    @Autowired
    CpMomentsTask cpMomentsTask;

    @PostMapping("/uploadTempImage")
    @ApiOperation(value = "上传朋友圈附件资源", notes = "上传朋友圈附件资源")
    public ServerResponseEntity<WxMediaUploadResult> uploadTempImage(@Valid @RequestBody UploadImageDTO request) throws IOException, WxErrorException {
        String type  = request.getType();
        if(StringUtils.isEmpty(type)){
            type = "image";
        }
        try {
            File tempFile = ZipUtils.creatTempImageFile("tmpImg_",request.getImagePath());
//            WxMediaUploadResult tempPath = WxCpConfiguration.getWxCpService(WxCpConfiguration.CP_CONNECT_AGENT_ID).getMediaService().upload(type,tempFile);
            WxMediaUploadResult tempPath = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().uploadAttachment(type,1,tempFile);
            tempFile.delete();
            return ServerResponseEntity.success(tempPath);
        }catch (WxErrorException e){
            log.info("上传图片到企业微信临时素材库 失败 {} {}",e,e.getMessage());
            if(e.getError().getErrorCode()== 41080){
                throw new LuckException("附件资源大小超过限制");
            }
            else if(e.getError().getErrorCode()== 41081){
                throw new LuckException("附件资源的图片分辨率超过限制");
            }
            else if(e.getError().getErrorCode()== 41082){
                throw new LuckException("附件资源的视频时长超过限制");
            }else{

                throw new LuckException(e.getMessage());
            }
        }
    }

	@GetMapping("/page")
	@ApiOperation(value = "获取分销推广-朋友圈列表", notes = "分页获取分销推广-朋友圈列表")
	public ServerResponseEntity<PageVO<DistributionMomentsVO>> page(@Valid PageDTO pageDTO, DistributionMomentsDTO dto) {
		PageVO<DistributionMomentsVO> distributionMomentsPage = distributionMomentsService.page(pageDTO, dto);
		return ServerResponseEntity.success(distributionMomentsPage);
	}

	@GetMapping
    @ApiOperation(value = "获取分销推广-朋友圈", notes = "根据id获取分销推广-朋友圈")
    public ServerResponseEntity<DistributionMomentsDTO> getById(@RequestParam Long id) {
        DistributionMomentsDTO momentsDTO = distributionMomentsService.getMomentsById(id);
        Assert.isNull(momentsDTO,"朋友圈数据为空，请检查id是否正确！");
        List<CpWelcomeAttachment> attachments = attachmentService.listByWelId(id, OriginType.MOMENTS_CONFIG.getCode());
        momentsDTO.setAttachment(attachments);
        return ServerResponseEntity.success(momentsDTO);
    }

    @PostMapping
    @ApiOperation(value = "保存分销推广-朋友圈", notes = "保存分销推广-朋友圈")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionMomentsDTO dto) {
        distributionMomentsService.save(dto);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新分销推广-朋友圈", notes = "更新分销推广-朋友圈")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionMomentsDTO dto) {
        distributionMomentsService.update(dto);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除分销推广-朋友圈", notes = "根据分销推广-朋友圈id删除分销推广-朋友圈")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        distributionMomentsService.deleteById(id);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "批量更新朋友圈状态", notes = "批量更新朋友圈状态")
    @PostMapping("/updateStatusBatch")
    public ServerResponseEntity<Void> updateStatusBatch(@RequestBody DistributionMomentsDTO dto){
	    distributionMomentsService.updateStatusBatch(dto.getIds(), dto.getStatus());
	    return ServerResponseEntity.success();
    }

    @ApiOperation(value = "朋友圈置顶", notes = "朋友圈置顶")
    @PostMapping("/momentsTop")
    public ServerResponseEntity<Void> momentsTop(@RequestBody DistributionMomentsDTO dto){
	    distributionMomentsService.momentsTop(dto.getId(), dto.getTop());
	    return ServerResponseEntity.success();
    }


    @GetMapping("/sendRecord/page")
    @ApiOperation(value = "获取朋友圈 员工发送记录表列表", notes = "分页获取朋友圈 员工发送记录表列表")
    public ServerResponseEntity<MomentSendRecordPageVO> page(@Valid PageDTO pageDTO, MomentsSendRecordPageDTO request) {
        MomentSendRecordPageVO pageVO = distributionMomentsSendRecordService.page(pageDTO,request);
        return ServerResponseEntity.success(pageVO);
    }

    @GetMapping("/sendRecord/sold_excel")
    @ApiOperation(value = "员工发送记录表列表导出", notes = "员工发送记录表列表导出")
    public void orderSoldExcel(HttpServletResponse response, PageDTO pageDTO, MomentsSendRecordPageDTO request) throws IOException {

        List<MomentSendRecordExcelVO> excelVOList =  distributionMomentsSendRecordService.orderSoldExcelList(pageDTO,request);
//        if(CollUtil.isEmpty(excelVOList)){
//            throw new LuckException("暂无数据可导出");
//        }
        //导出
        ExcelUtil.soleExcel(response, excelVOList,
                MomentSendRecordExcelVO.EXCEL_NAME,
                MomentSendRecordExcelVO.MERGE_ROW_INDEX,
                MomentSendRecordExcelVO.MERGE_COLUMN_INDEX,
                MomentSendRecordExcelVO.class);
    }

    @GetMapping("/ua/sendRecord/send")
    @ApiOperation(value = "测试发送朋友圈", notes = "测试发送朋友圈")
    public ServerResponseEntity<Void> send(@Valid MomentsSendDTO request) {
//        distributionMomentsService.send(request);
        return ServerResponseEntity.success();
    }

    @GetMapping("/taskTimeOut")
    @ApiOperation(value = "定时任务处理超时未发送的朋友圈", notes = "定时任务处理超时未发送的朋友圈")
    public ServerResponseEntity<Void> taskTimeOut() {
        distributionMomentsService.taskTimeOut();
        return ServerResponseEntity.success();
    }

    @GetMapping("/ua/getMomentTaskResult")
    @ApiOperation(value = "获取朋友圈任务创建结果", notes = "获取朋友圈任务创建结果")
    public ServerResponseEntity<Void> getMomentTaskResult() {
        cpMomentsTask.getMomentTaskResult();
        return ServerResponseEntity.success();
    }
    @GetMapping("/ua/getMomentComments")
    @ApiOperation(value = "获取朋友圈互动数据", notes = "获取朋友圈互动数据")
    public ServerResponseEntity<Void> getMomentComments() {
        cpMomentsTask.getMomentComments();
        return ServerResponseEntity.success();
    }

    @GetMapping("/ua/getMomentPublishStatus")
    @ApiOperation(value = "获取员工朋友圈发表状态", notes = "获取员工朋友圈发表状态")
    public ServerResponseEntity<Void> getMomentPublishStatus() {
        cpMomentsTask.getMomentPublishStatus();
        return ServerResponseEntity.success();
    }


    @Autowired
    private DistributionMomentsCommentsService distributionMomentsCommentsService;

    @GetMapping("/comments/page")
    @ApiOperation(value = "获取朋友圈互动明细数据列表", notes = "分页获取朋友圈互动明细数据列表")
    public ServerResponseEntity<PageVO<DistributionMomentsCommentsVO>> page(@Valid PageDTO pageDTO, DistributionMomentsCommentsPageDTO dto) {
        PageVO<DistributionMomentsCommentsVO> distributionMomentsCommentsPage = distributionMomentsCommentsService.page(pageDTO,dto);
        return ServerResponseEntity.success(distributionMomentsCommentsPage);
    }

}
