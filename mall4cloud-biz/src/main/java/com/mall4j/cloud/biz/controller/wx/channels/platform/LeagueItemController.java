package com.mall4j.cloud.biz.controller.wx.channels.platform;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.mall4j.cloud.api.biz.constant.channels.PromotionType;
import com.mall4j.cloud.api.biz.dto.channels.response.league.item.BatchAddResp;
import com.mall4j.cloud.biz.constant.LeagueItemStatus;
import com.mall4j.cloud.biz.controller.BaseExportTemplate;
import com.mall4j.cloud.biz.dto.channels.league.*;
import com.mall4j.cloud.biz.service.channels.LeagueItemService;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 联盟推广商品
 * @Author axin
 * @Date 2023-02-20 15:14
 **/
@RestController
@RequestMapping("/p/channels/league/item")
@Api(tags = "视频号优选联盟商品")
public class LeagueItemController extends BaseExportTemplate<ItemListPageRespDto,ItemListPageReqDto> {

    @Autowired
    private LeagueItemService leagueItemService;

    @Autowired
    private MapperFacade mapperFacade;

    @PostMapping("/listPage")
    @ApiOperation(value = "推广商品列表", notes = "优选联盟商品管理")
    public ServerResponseEntity<PageVO<ItemListPageRespDto>> listPage(@RequestBody @Valid  ItemListPageReqDto reqDto){
        return ServerResponseEntity.success(leagueItemService.listPage(reqDto));
    }

    @PostMapping("/allowPromotionListPageByType")
    @ApiOperation(value = "查询可推广商品列表", notes = "优选联盟商品管理")
    public ServerResponseEntity<PageVO<ItemAllowPromotionListPageRespDto>> allowPromotionListPageByType(@RequestBody @Valid ItemAllowPromotionListPageReqDto reqDto){
        return ServerResponseEntity.success(leagueItemService.allowPromotionListPageByType(reqDto));
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加商品", notes = "优选联盟商品管理")
    public ServerResponseEntity<List<BatchAddResp.ResultInfo>> add(@RequestBody @Valid AddItemReqDto reqDto){
        return ServerResponseEntity.success(leagueItemService.add(reqDto));
    }

    @PostMapping("/upd")
    @ApiOperation(value = "编辑商品", notes = "优选联盟商品管理")
    public ServerResponseEntity<Void> upd(@RequestBody @Valid UpdItemReqDto reqDto){
        leagueItemService.upd(reqDto);
        return ServerResponseEntity.success();
    }

    @GetMapping("/get")
    @ApiOperation(value = "查询商品详情", notes = "优选联盟商品管理")
    public ServerResponseEntity<ItemListDetailRespDto> get(@RequestParam("id")Long id){
        return ServerResponseEntity.success(leagueItemService.get(id));
    }


    @PostMapping("/updProductStatus")
    @ApiOperation(value = "修改商品状态", notes = "优选联盟商品管理")
    public ServerResponseEntity<Void> updProductStatus(@RequestBody @Valid DisableProductReqDto reqDto){
        leagueItemService.updProductStatus(reqDto);
        return ServerResponseEntity.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除联盟商品", notes = "优选联盟商品管理")
    public ServerResponseEntity<Void> delete(@RequestBody @Valid DeleteItem reqDto){
        leagueItemService.delete(reqDto);
        return ServerResponseEntity.success();
    }

    @PostMapping("/exportList")
    @ApiOperation(value = "导出", notes = "优选联盟商品管理")
    public ServerResponseEntity<String> exportList(@RequestBody @Valid ItemListPageReqDto reqDto){
        reqDto.setPageSize(5000,true);
        String test = generateFileToDownloadCenter("优选联盟商品", reqDto, ItemListExportRespDto.class);
        return ServerResponseEntity.success(test);
    }

    @Override
    public PageVO<ItemListPageRespDto> queryPageExport(ItemListPageReqDto dto) {
        return leagueItemService.listPage(dto);
    }

    @Override
    public void setData(Class<?> outClazz, PageVO<ItemListPageRespDto> pageReqDtoPageInfo, String pathExport) {
        List<ItemListExportRespDto> itemListExportRespDtos = mapperFacade.mapAsList(pageReqDtoPageInfo.getList(), ItemListExportRespDto.class);
        if(CollectionUtils.isNotEmpty(itemListExportRespDtos)){
            itemListExportRespDtos.forEach(item -> {
                item.setStatusName(LeagueItemStatus.getDesc(item.getStatus()));
                item.setTypeName(PromotionType.getDesc(item.getType()));
                item.setFinderNames(CollectionUtils.isNotEmpty(item.getFinder())?item.getFinder().stream().map(ItemListFinderRespDto::getFinderName).collect(Collectors.joining(",")):"");
            });
        }
        ExcelWriterBuilder write = EasyExcel.write(pathExport, ItemListExportRespDto.class);
        ExcelWriterSheetBuilder sheet = write.sheet(ExcelModel.SHEET_NAME);
        sheet.doWrite(itemListExportRespDtos);
    }
}
