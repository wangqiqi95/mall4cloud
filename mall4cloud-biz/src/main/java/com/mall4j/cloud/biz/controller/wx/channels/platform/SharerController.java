package com.mall4j.cloud.biz.controller.wx.channels.platform;

import com.mall4j.cloud.biz.controller.BaseExportTemplate;
import com.mall4j.cloud.biz.dto.channels.sharer.*;
import com.mall4j.cloud.biz.service.channels.ChannelsSharerService;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description 分享员管理
 * @Author axin
 * @Date 2023-02-22 16:54
 **/
@RestController
@RequestMapping("/p/channels/sharer")
@Api(tags = "视频号分享员管理")
public class SharerController extends BaseExportTemplate<SharerPageListRespDto,SharerPageListReqDto> {

    @Autowired
    private ChannelsSharerService channelsSharerService;

    @PostMapping("/pageList")
    @ApiOperation(value = "分享员列表", notes = "视频号分享员")
    public ServerResponseEntity<PageVO<SharerPageListRespDto>> pageList(@RequestBody SharerPageListReqDto reqDto){
        PageVO<SharerPageListRespDto> pageVO = channelsSharerService.pageList(reqDto);
        return ServerResponseEntity.success(pageVO);
    }

    @PostMapping("/bind")
    @ApiOperation(value = "绑定分享员", notes = "视频号分享员")
    public ServerResponseEntity<Void> bind(@RequestBody @Valid SharerBindReqDto reqDto){
        channelsSharerService.bind(reqDto);
        return ServerResponseEntity.success();
    }

    @PostMapping("/batchBind")
    @ApiOperation(value = "批量绑定分享员", notes = "视频号分享员")
    public ServerResponseEntity<Void> batchBind(@RequestBody @Valid List<SharerBindReqDto> reqDtos){
        channelsSharerService.batchBind(reqDtos);
        return ServerResponseEntity.success();
    }

    @PostMapping("/rebind")
    @ApiOperation(value = "重新申请绑定分享员", notes = "视频号分享员")
    public ServerResponseEntity<Void> rebind(@RequestBody SharerReBindReqDto reqDto){
        channelsSharerService.rebind(reqDto);
        return ServerResponseEntity.success();
    }

    @PostMapping("/unbind")
    @ApiOperation(value = "解绑分享员", notes = "视频号分享员")
    public ServerResponseEntity<Void> unbind(@RequestBody SharerUnBindReqDto reqDto){
        channelsSharerService.unbind(reqDto);
        return ServerResponseEntity.success();
    }

    @GetMapping("/getQrImgBase64")
    @ApiOperation(value = "获取绑定二维码Base64", notes = "视频号分享员")
    public ServerResponseEntity<String> getQrImgBase64(@RequestParam("id") Long id){
        return ServerResponseEntity.success(channelsSharerService.getQrImgBase64(id));
    }

    @PostMapping("/exportList")
    @ApiOperation(value = "导出分享员列表", notes = "视频号分享员")
    public ServerResponseEntity<String> exportList(@RequestBody SharerPageListReqDto reqDto){
        reqDto.setPageSize(5000,true);
        return ServerResponseEntity.success(generateFileToDownloadCenter("视频号分享员", reqDto, SharerPageListExportRespDto.class));
    }

    @PostMapping("/exportQRCode")
    @ApiOperation(value = "导出分享员邀请码", notes = "视频号分享员")
    public ServerResponseEntity<String> exportQRCode(@RequestBody SharerQrCodeImgListReqDto reqDto){
        reqDto.setPageSize(5000,true);
        reqDto.setPageNum(1);
        return channelsSharerService.exportQRCode(reqDto);
    }

    @Override
    public PageVO<SharerPageListRespDto> queryPageExport(SharerPageListReqDto reqDto) {
        return channelsSharerService.pageList(reqDto);
    }
}
