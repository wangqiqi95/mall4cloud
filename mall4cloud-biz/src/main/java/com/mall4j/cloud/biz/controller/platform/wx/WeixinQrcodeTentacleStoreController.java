package com.mall4j.cloud.biz.controller.platform.wx;

import com.mall4j.cloud.biz.dto.WeixinQrcodeTentacleStoreDTO;
import com.mall4j.cloud.biz.model.WeixinQrcodeTentacleStore;
import com.mall4j.cloud.biz.service.WeixinQrcodeTentacleStoreService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 微信触点门店二维码表
 *
 * @author FrozenWatermelon
 * @date 2022-03-09 16:05:09
 */
//@RestController("multishopWeixinQrcodeTentacleStoreController")
//@RequestMapping("/p/weixin/qrcode/tentaclestore")
//@Api(tags = "微信触点门店二维码表")
public class WeixinQrcodeTentacleStoreController {

    @Autowired
    private WeixinQrcodeTentacleStoreService weixinQrcodeTentacleStoreService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取微信触点门店二维码表列表", notes = "分页获取微信触点门店二维码表列表")
	public ServerResponseEntity<PageVO<WeixinQrcodeTentacleStore>> page(@Valid PageDTO pageDTO) {
		PageVO<WeixinQrcodeTentacleStore> weixinQrcodeTentacleStorePage = weixinQrcodeTentacleStoreService.page(pageDTO);
		return ServerResponseEntity.success(weixinQrcodeTentacleStorePage);
	}

	@GetMapping
    @ApiOperation(value = "获取微信触点门店二维码表", notes = "根据id获取微信触点门店二维码表")
    public ServerResponseEntity<WeixinQrcodeTentacleStore> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(weixinQrcodeTentacleStoreService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存微信触点门店二维码表", notes = "保存微信触点门店二维码表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody WeixinQrcodeTentacleStoreDTO weixinQrcodeTentacleStoreDTO) {
        WeixinQrcodeTentacleStore weixinQrcodeTentacleStore = mapperFacade.map(weixinQrcodeTentacleStoreDTO, WeixinQrcodeTentacleStore.class);
        weixinQrcodeTentacleStore.setId(null);
        weixinQrcodeTentacleStoreService.save(weixinQrcodeTentacleStore);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新微信触点门店二维码表", notes = "更新微信触点门店二维码表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody WeixinQrcodeTentacleStoreDTO weixinQrcodeTentacleStoreDTO) {
        WeixinQrcodeTentacleStore weixinQrcodeTentacleStore = mapperFacade.map(weixinQrcodeTentacleStoreDTO, WeixinQrcodeTentacleStore.class);
        weixinQrcodeTentacleStoreService.update(weixinQrcodeTentacleStore);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除微信触点门店二维码表", notes = "根据微信触点门店二维码表id删除微信触点门店二维码表")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        weixinQrcodeTentacleStoreService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
