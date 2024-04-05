package com.mall4j.cloud.biz.controller.wx.channels;

import com.mall4j.cloud.api.biz.dto.channels.request.EcPageRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcAllBrandResponse;
import com.mall4j.cloud.biz.dto.channels.ChannelsBrandDTO;
import com.mall4j.cloud.biz.dto.channels.ChannelsBrandPageDTO;
import com.mall4j.cloud.biz.service.channels.ChannelsBrandService;
import com.mall4j.cloud.biz.service.channels.EcBrandService;
import com.mall4j.cloud.biz.vo.channels.ChannelsBasicBrandVO;
import com.mall4j.cloud.biz.vo.channels.ChannelsBrandOneVO;
import com.mall4j.cloud.biz.vo.channels.ChannelsBrandVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 视频号4.0商品
 *
 * @date 2023-02-07 15:01:48
 */
@RestController("multishopChannelsBrandController")
@RequestMapping("/p/channels/brand")
@Api(tags = "视频号4.0品牌")
public class ChannelsBrandController {
    @Autowired
    EcBrandService ecBrandService;
    @Autowired
    private ChannelsBrandService channelsBrandService;

    @GetMapping("/all")
    @ApiOperation(value = "获取视频号4.0全部品牌", notes = "获取视频号4.0全部品牌")
    public ServerResponseEntity<EcAllBrandResponse> all(EcPageRequest ecPageRequest) {
        return ServerResponseEntity.success(ecBrandService.all(ecPageRequest));
    }
    @GetMapping("/basicBrandList")
    @ApiOperation(value = "视频号4.0获取品牌库列表", notes = "视频号4.0获取品牌库列表")
    public ServerResponseEntity<List<ChannelsBasicBrandVO>> basicBrandList(@RequestParam(value = "query", required = true) String query) {
        List<ChannelsBasicBrandVO> list = channelsBrandService.basicBrandList(query);
        return ServerResponseEntity.success(list);
    }
    
    @GetMapping("/brandList")
    @ApiOperation(value = "查询视频号4.0审核成功的品牌列表", notes = "查询视频号4.0审核成功的品牌列表")
    public ServerResponseEntity<List<ChannelsBrandVO>> brandList() {
        List<ChannelsBrandVO> list = channelsBrandService.brandList();
        return ServerResponseEntity.success(list);
    }
    
    @GetMapping("/page")
    @ApiOperation(value = "查询视频号4.0品牌资质列表", notes = "查询视频号4.0品牌列表")
    public ServerResponseEntity<List<ChannelsBrandVO>> page(ChannelsBrandPageDTO param) {
        List<ChannelsBrandVO> result = channelsBrandService.page(param);
        return ServerResponseEntity.success(result);
    }
    
    @PostMapping("/save")
    @ApiOperation(value = "视频号4.0添加品牌资质", notes = "视频号4.0添加品牌")
    public ServerResponseEntity<Void> save(@Valid @RequestBody ChannelsBrandDTO channelsBrandDTO) {
        channelsBrandService.save(channelsBrandDTO);
        return ServerResponseEntity.success();
    }
    
    @GetMapping("/oneBrand")
    @ApiOperation(value = "获取视频号4.0单个品牌资质信息", notes = "获取视频号4.0单个品牌信息")
    public ServerResponseEntity<ChannelsBrandOneVO> getByBrandId(@RequestParam(value = "brandId" ,required = true) String brandId) {
        return ServerResponseEntity.success(channelsBrandService.getByBrandId(brandId));
    }
    
    @PutMapping("/updateBrand")
    @ApiOperation(value = "视频号4.0更新品牌资质", notes = "视频号4.0更新品牌资质")
    public ServerResponseEntity<Void> updateBrand(@Valid @RequestBody ChannelsBrandDTO channelsBrandDTO) {
        channelsBrandService.updateBrand(channelsBrandDTO);
        return ServerResponseEntity.success();
    }
    
    @DeleteMapping("/cancelBrand/{brandId}")
    @ApiOperation(value = "视频号4.0撤回品牌资质审核", notes = "视频号4.0撤回品牌资质审核")
    public ServerResponseEntity<Void> cancelBrand(@PathVariable String brandId) {
        channelsBrandService.cancelBrand(brandId);
        return ServerResponseEntity.success();
    }
}
