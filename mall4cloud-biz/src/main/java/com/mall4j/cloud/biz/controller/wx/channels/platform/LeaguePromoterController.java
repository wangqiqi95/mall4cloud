package com.mall4j.cloud.biz.controller.wx.channels.platform;

import com.mall4j.cloud.biz.controller.BaseExportTemplate;
import com.mall4j.cloud.biz.dto.channels.league.*;
import com.mall4j.cloud.biz.service.channels.LeaguePromoterService;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Description 优选联盟
 * @Author axin
 * @Date 2023-02-13 15:48
 **/
@RestController
@RequestMapping("/p/channels/league/promoter")
@Api(tags = "视频号优选联盟达人")
public class LeaguePromoterController extends BaseExportTemplate<PromoterListRespDto,PromoterListReqDto> {

    @Resource
    private LeaguePromoterService leaguePromoterService;

    @PostMapping("/list")
    @ApiOperation(value = "达人列表", notes = "优选联盟达人管理")
    public ServerResponseEntity<PageVO<PromoterListRespDto>> promoterList(@RequestBody PromoterListReqDto reqDto){
        return ServerResponseEntity.success(leaguePromoterService.promoterList(reqDto));
    }

    @PostMapping("/exportList")
    @ApiOperation(value = "导出达人列表", notes = "优选联盟达人管理")
    public ServerResponseEntity<String> exportList(@RequestBody @Valid PromoterListReqDto reqDto){
        reqDto.setPageSize(5000,true);
        return ServerResponseEntity.success(generateFileToDownloadCenter("优选联盟达人", reqDto, PromoterListExportRespDto.class));
    }


    @GetMapping("/detail")
    @ApiOperation(value = "达人详情", notes = "优选联盟达人管理")
    public ServerResponseEntity<PromoterDetailRespDto> promoterDetail(@RequestParam(name = "id") Long id){
        return ServerResponseEntity.success(leaguePromoterService.promoterDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增达人", notes = "优选联盟达人管理")
    public ServerResponseEntity<Void> promoterAdd(@RequestBody @Valid PromoterAddReqDto reqDto){
        leaguePromoterService.promoterAdd(reqDto);
        return ServerResponseEntity.success();
    }

    @PostMapping("/upd")
    @ApiOperation(value = "编辑达人", notes = "优选联盟达人管理")
    public ServerResponseEntity<Void> promoterUpd(@RequestBody @Valid PromoterUpdReqDto reqDto){
        leaguePromoterService.promoterUpd(reqDto);
        return ServerResponseEntity.success();
    }

    //删除达人
    @PostMapping("/del")
    @ApiOperation(value = "删除达人", notes = "优选联盟达人管理")
    public ServerResponseEntity<Void> promoterDel(@RequestBody @Valid PromoterDelReqDto reqDto){
        leaguePromoterService.promoterDel(reqDto);
        return ServerResponseEntity.success();
    }

    @Override
    public PageVO<PromoterListRespDto> queryPageExport(PromoterListReqDto reqDto) {
        return leaguePromoterService.promoterList(reqDto);
    }
}
