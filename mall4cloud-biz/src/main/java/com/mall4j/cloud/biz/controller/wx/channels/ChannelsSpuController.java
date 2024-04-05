package com.mall4j.cloud.biz.controller.wx.channels;

import com.mall4j.cloud.api.biz.dto.channels.response.EcProductResponse;
import com.mall4j.cloud.biz.dto.channels.PriceCheckDTO;
import com.mall4j.cloud.biz.dto.channels.query.ChannelsSpuQueryDTO;
import com.mall4j.cloud.biz.dto.channels.request.AddChannlesSpuRequest;
import com.mall4j.cloud.biz.model.channels.ChannelsSpu;
import com.mall4j.cloud.biz.service.channels.ChannelsSpuService;

import com.mall4j.cloud.biz.vo.channels.ChannelsSpuExtraVO;
import com.mall4j.cloud.biz.vo.channels.ChannelsSpuProductSpuVO;
import com.mall4j.cloud.biz.vo.channels.ChannelsSpuSkuDetailVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 视频号4.0商品
 *
 * @date 2023-02-07 15:01:48
 */
@RestController("multishopChannelsSpuController")
@RequestMapping("/p/channels/spu")
@Api(tags = "视频号4.0商品")
public class ChannelsSpuController {

    @Autowired
    private ChannelsSpuService channelsSpuService;

    @Autowired
    private MapperFacade mapperFacade;

    @GetMapping("/page")
    @ApiOperation(value = "获取视频号4.0商品列表", notes = "分页获取视频号4.0商品列表")
    public ServerResponseEntity<PageVO<ChannelsSpu>> page(@Valid ChannelsSpuQueryDTO dto) {
        PageVO<ChannelsSpu> channelsSpuPage = channelsSpuService.page(dto);
        return ServerResponseEntity.success(channelsSpuPage);
    }

    @GetMapping("/extra/page")
    public ServerResponseEntity<PageVO<ChannelsSpuExtraVO>> extraPage(@Valid ChannelsSpuQueryDTO dto){
        PageVO<ChannelsSpuExtraVO> pageVO = channelsSpuService.extraPage(dto);
        return ServerResponseEntity.success(pageVO);
    }

    @GetMapping
    @ApiOperation(value = "获取视频号4.0商品", notes = "根据id获取视频号4.0商品")
    public ServerResponseEntity<ChannelsSpuSkuDetailVO> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(channelsSpuService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存视频号4.0商品", notes = "保存视频号4.0商品")
    public ServerResponseEntity<Void> add(@Valid @RequestBody AddChannlesSpuRequest addChannlesSpuRequest) {
        channelsSpuService.addChannelsSpu(addChannlesSpuRequest);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新视频号4.0商品", notes = "更新视频号4.0商品")
    public ServerResponseEntity<Void> update(@Valid @RequestBody AddChannlesSpuRequest addChannlesSpuRequest) {
        channelsSpuService.updateChannelsSpu(addChannlesSpuRequest);
        return ServerResponseEntity.success();
    }

    @PostMapping("/save_listing")
    @ApiOperation(value = "视频号4.0 商品保存并上架", notes = "视频号4.0 商品保存并上架")
    public ServerResponseEntity<Void> saveAndListing(@Valid @RequestBody AddChannlesSpuRequest addChannlesSpuRequest){
        channelsSpuService.saveAndListing(addChannlesSpuRequest);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除视频号4.0商品", notes = "根据视频号4.0商品id删除视频号4.0商品")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        // 同步删除对应sku
        channelsSpuService.deleteById(id);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/batch")
    @ApiOperation(value = "批量删除视频号4.0商品", notes = "批量视频号4.0商品id删除视频号4.0商品")
    public ServerResponseEntity<Void> deleteBatch(@RequestBody List<Long> ids) {
        if (Objects.isNull(ids)){
            Assert.faild("参数错误");
        }

        // 同步删除对应sku
        for (Long id : ids) {
            channelsSpuService.deleteById(id);
        }

        return ServerResponseEntity.success();
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "撤回审核", notes = "撤回审核")
    public ServerResponseEntity<Void> auditCancel(@RequestParam Long id) {
        channelsSpuService.auditCancel(id);
        return ServerResponseEntity.success();
    }

    @PostMapping("/cancel/batch")
    @ApiOperation(value = "批量撤回审核", notes = "批量撤回审核")
    public ServerResponseEntity<Void> auditCancelBatch(@RequestBody List<Long> ids) {
        if (Objects.isNull(ids)){
            Assert.faild("参数错误");
        }
        for (Long id : ids) {
             channelsSpuService.auditCancel(id);
        }
        return ServerResponseEntity.success();
    }

    @PostMapping("/listing")
    @ApiOperation(value = "商品上架", notes = "商品上架")
    public ServerResponseEntity<Void> listing(@RequestBody List<Long> ids) {
        channelsSpuService.listing(ids);
        return ServerResponseEntity.success();
    }

    @PostMapping("/delisting")
    @ApiOperation(value = "商品下架", notes = "商品下架")
    public ServerResponseEntity<Void> delisting(@RequestBody List<Long> ids) {
        channelsSpuService.delisting(ids);
        return ServerResponseEntity.success();
    }

    @PostMapping("/price_check")
    @ApiOperation(value = "价格检查", notes = "价格检查")
    public ServerResponseEntity<Void> checkPrice(@RequestBody List<PriceCheckDTO> priceCheckDTO) {
        channelsSpuService.checkPrice(priceCheckDTO);
        return ServerResponseEntity.success();
    }

    @GetMapping("/stock/get")
    @ApiOperation(value = "视频号4.0 获取商品库存", notes = "根据id获取视频号4.0商品库存")
    public ServerResponseEntity<Integer> getStock(@RequestParam Long id,@RequestParam Long skuId) {

        return ServerResponseEntity.success(channelsSpuService.getStock(id, skuId));
    }

    @PostMapping("/stock/update")
    @ApiOperation(value = "视频号4.0 商品库存更新", notes = "快速更新商品库存 内部chanels商品id，sku_id")
    public ServerResponseEntity<Void> updateStock(@RequestParam Long id,
                                                  @RequestParam Long skuId,
                                                  @RequestParam Integer stock,
                                                  @RequestParam Integer type){
        channelsSpuService.updateStock(id, skuId, stock, type);
        return ServerResponseEntity.success();
    }

    @GetMapping("/list")
    public ServerResponseEntity<List<String>> list(){
        return ServerResponseEntity.success(channelsSpuService.listProduct());
    }

    /**
     * 视频号4.0商品详情，调用微信API获取
     * @param id 内部商品ID
     * @param type 获取类型 默认取1。1:获取线上数据, 2:获取草稿数据, 3:同时获取线上和草稿数据（注意：需成功上架后才有线上数据）
     * @return response
     */
    @GetMapping("/get")
    @ApiOperation(value = "视频号4.0商品详情", notes = "视频号4.0商品详情")
    public ServerResponseEntity<EcProductResponse> getEcProduct(@RequestParam Long id,
                                                                @RequestParam Integer type) {
        return ServerResponseEntity.success(channelsSpuService.getEcProductById(id, type));
    }

    @PostMapping("/export")
    @ApiOperation(value = "视频号4.0商品信息导出", notes = "视频号4.0商品信息导出")
    public ServerResponseEntity<Void> export(ChannelsSpuQueryDTO dto, HttpServletResponse response){
        channelsSpuService.export(dto, response);
        return ServerResponseEntity.success();
    }

    @GetMapping("/get_channels_product")
    @ApiOperation(value = "根据spuId获取视频号商品内部商品信息", notes = "根据spuId获取视频号商品内部商品信息")
    public ServerResponseEntity<ChannelsSpuProductSpuVO> getChannelsSpuProductSpuVOBySpuId(@RequestParam Long spuId){
        return ServerResponseEntity.success(channelsSpuService.getChannelsSpuProductSpuVOBySpuId(spuId));
    }


    @PostMapping("/price_check/all")
    @ApiOperation(value = "全局价格检查", notes = "全局价格检查")
    public ServerResponseEntity<Void> checkPrice(@Valid ChannelsSpuQueryDTO dto) {
        channelsSpuService.checkPriceAll(dto);
        return ServerResponseEntity.success();
    }

}
