package com.mall4j.cloud.biz.controller.wx.channels.platform;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.mall4j.cloud.biz.controller.BaseExportTemplate;
import com.mall4j.cloud.biz.dto.channels.league.*;
import com.mall4j.cloud.biz.service.channels.LeagueItemPlanService;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description 联盟推广计划
 * @Author axin
 * @Date 2023-02-20 15:14
 **/
//@RestController
//@RequestMapping("/p/channels/league/item/plan")
//@Api(tags = "视频号优选联盟商品")
@Deprecated
public class LeagueItemPlanController extends BaseExportTemplate<ItemPlanListPageRespDto,ItemPlanListPageReqDto> {
    @Autowired
    private LeagueItemPlanService leagueItemPlanService;
    @Autowired
    private MapperFacade mapperFacade;

    @PostMapping("/listPage")
//    @ApiOperation(value = "推广计划列表", notes = "优选联盟商品管理")
    public ServerResponseEntity<PageVO<ItemPlanListPageRespDto>> planListPage(@RequestBody  ItemPlanListPageReqDto reqDto){
        return ServerResponseEntity.success(leagueItemPlanService.planListPage(reqDto));
    }

    @PostMapping("/add")
//    @ApiOperation(value = "添加计划", notes = "优选联盟商品管理")
    public ServerResponseEntity<String> addPlan(@RequestBody @Valid AddItemPlanReqDto reqDto){
        return ServerResponseEntity.success(leagueItemPlanService.addPlan(reqDto));
    }

    @PostMapping("/upd")
//    @ApiOperation(value = "编辑计划", notes = "优选联盟商品管理")
    public ServerResponseEntity<Void> updPlan(@RequestBody @Valid UpdItemPlanReqDto reqDto){
        leagueItemPlanService.updPlan(reqDto);
        return ServerResponseEntity.success();
    }

    @GetMapping("/get")
//    @ApiOperation(value = "查询计划详情", notes = "优选联盟商品管理")
    public ServerResponseEntity<ItemPlanRespDto> get(@RequestParam("id")Long id){
        ItemPlanRespDto itemPlanRespDto = leagueItemPlanService.get(id);
        return ServerResponseEntity.success(itemPlanRespDto);
    }

    @PostMapping("/getProductPage")
//    @ApiOperation(value = "查询计划商品详情分页列表", notes = "优选联盟商品管理")
    public ServerResponseEntity<PageVO<ItemDetail>> getProductPage(@RequestBody @Valid ProductPageReqDto reqDto){
        PageVO<ItemDetail> pageVO = leagueItemPlanService.getProductPage(reqDto);
        return ServerResponseEntity.success(pageVO);
    }

    @PostMapping("/disableProduct")
//    @ApiOperation(value = "修改商品状态", notes = "优选联盟商品管理")
    public ServerResponseEntity<Void> updProductStatus(@RequestBody @Valid DisableProductReqDto reqDto){
        leagueItemPlanService.updProductStatus(reqDto);
        return ServerResponseEntity.success();
    }

    @PostMapping("/exportList")
//    @ApiOperation(value = "导出", notes = "优选联盟商品管理")
    public ServerResponseEntity<String> exportList(@RequestBody ItemPlanListPageReqDto reqDto){
        reqDto.setPageSize(5000,true);
        String test = generateFileToDownloadCenter("优选联盟推广计划", reqDto, ItemPlanListExportRespDto.class);
        return ServerResponseEntity.success(test);
    }

    @Override
    public PageVO<ItemPlanListPageRespDto> queryPageExport(ItemPlanListPageReqDto dto) {
        return leagueItemPlanService.planListPage(dto);
    }

    @Override
    public void setData(Class<?> outClazz, PageVO<ItemPlanListPageRespDto> pageReqDtoPageInfo, String pathExport) {
        List<ItemPlanListExportRespDto> itemPlanListExportRespDtos = mapperFacade.mapAsList(pageReqDtoPageInfo.getList(), ItemPlanListExportRespDto.class);
        ExcelWriterBuilder write = EasyExcel.write(pathExport, ItemPlanListExportRespDto.class);
        ExcelWriterSheetBuilder sheet = write.sheet(ExcelModel.SHEET_NAME);
        sheet.doWrite(itemPlanListExportRespDtos);
    }
}
