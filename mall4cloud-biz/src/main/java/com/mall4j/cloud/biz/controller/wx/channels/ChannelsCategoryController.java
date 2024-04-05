package com.mall4j.cloud.biz.controller.wx.channels;

import com.mall4j.cloud.api.biz.dto.channels.EcCategoryAuditResult;
import com.mall4j.cloud.api.biz.dto.channels.EcCats;
import com.mall4j.cloud.api.biz.dto.channels.EcFile;
import com.mall4j.cloud.biz.wx.wx.channels.EcCategoryApi;
import com.mall4j.cloud.biz.dto.channels.ChannelsCategoryDTO;
import com.mall4j.cloud.biz.dto.channels.query.ChannelsCategoryQueryDTO;
import com.mall4j.cloud.biz.model.channels.ChannelsCategory;
import com.mall4j.cloud.biz.service.channels.ChannelsCategoryService;
import com.mall4j.cloud.biz.vo.channels.ChannelsCategoryDetailVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * 视频号4.0类目
 *
 * @date 2023-02-07 15:01:48
 */
@RestController("multishopChannelsCategoryController")
@RequestMapping("/p/channels/category")
@Api(tags = "视频号4.0类目")
public class ChannelsCategoryController {

    @Autowired
    private ChannelsCategoryService channelsCategoryService;
    @Autowired
    private EcCategoryApi ecCategoryApi;
    @Autowired
    private MapperFacade mapperFacade;

    @GetMapping("/all")
    @ApiOperation(value = "获取视频号4.0全部类目", notes = "获取视频号4.0全部类目")
    public ServerResponseEntity<List<EcCats>> all() {
        return ServerResponseEntity.success(channelsCategoryService.allList());
    }

    @GetMapping("/page")
    @ApiOperation(value = "获取视频号4.0 类目列表", notes = "分页获取视频号4.0 类目列表")
    public ServerResponseEntity<PageVO<ChannelsCategory>> page(@Valid ChannelsCategoryQueryDTO dto) {
        PageVO<ChannelsCategory> channelsCategoryPage = channelsCategoryService.page(dto);
        return ServerResponseEntity.success(channelsCategoryPage);
    }

    @GetMapping
    @ApiOperation(value = "获取视频号4.0 获取提交审核内容", notes = "获取提交审核内容")
    public ServerResponseEntity<ChannelsCategory> get(@RequestParam Long id){
        return ServerResponseEntity.success(channelsCategoryService.getById(id));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取视频号4.0 类目详情", notes = "分页获取视频号4.0 类目详情")
    public ServerResponseEntity<ChannelsCategoryDetailVO> detail(@RequestParam Long id) {
        return ServerResponseEntity.success(channelsCategoryService.detail(id));
    }

    @PostMapping
    @ApiOperation(value = "视频号4.0 申请类目", notes = "申请视频号4.0 申请类目")
    public ServerResponseEntity<Void> apply(@Valid @RequestBody ChannelsCategoryDTO channelsCategoryDTO) {
        return channelsCategoryService.applyOrReApply(channelsCategoryDTO, true);
    }

    @PostMapping("/re")
    @ApiOperation(value = "视频号4.0 重新提交申请类目", notes = "申请视频号4.0 重新提交申请类目")
    public ServerResponseEntity<Void> reApply(@Valid @RequestBody ChannelsCategoryDTO channelsCategoryDTO) {
        return channelsCategoryService.applyOrReApply(channelsCategoryDTO, false);
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "视频号4.0 取消审核", notes = "视频号4.0 取消审核")
    public ServerResponseEntity<Void> cancel(@RequestParam Long id){
        channelsCategoryService.cancel(id);
        return ServerResponseEntity.success();
    }

    @PostMapping("/qualification/upload")
    @ApiOperation(value = "视频号4.0 上传资质图片", notes = "视频号4.0 上传资质图片")
    public ServerResponseEntity<EcFile> uploadQualification(MultipartFile file){
        EcFile ecFile = channelsCategoryService.uploadQualification(file);
        return ServerResponseEntity.success(ecFile);
    }

    @GetMapping("/audit/get")
    @ApiOperation(value = "视频号4.0 获取类目审核结果", notes = "视频号4.0 获取类目审核结果")
    public ServerResponseEntity<EcCategoryAuditResult> getAudit(@RequestParam Long id){
        return ServerResponseEntity.success(channelsCategoryService.getAudit(id));
    }

}
