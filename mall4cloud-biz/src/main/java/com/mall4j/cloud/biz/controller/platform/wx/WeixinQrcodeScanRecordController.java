package com.mall4j.cloud.biz.controller.platform.wx;

import com.mall4j.cloud.biz.model.WeixinQrcodeScanRecord;
import com.mall4j.cloud.biz.service.WeixinQrcodeScanRecordService;
import com.mall4j.cloud.biz.vo.WeixinQrcodeScanRecordVO;
import com.mall4j.cloud.biz.dto.WeixinQrcodeScanRecordDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * 微信二维码扫码记录表
 *
 * @author FrozenWatermelon
 * @date 2022-01-25 18:13:49
 */
//@RestController("platformWeixinQrcodeScanRecordController")
//@RequestMapping("/p/weixin/qrscanrec")
//@Api(tags = "微信二维码扫码记录表")
public class WeixinQrcodeScanRecordController {

    @Autowired
    private WeixinQrcodeScanRecordService weixinQrcodeScanRecordService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取微信二维码扫码记录表列表", notes = "分页获取微信二维码扫码记录表列表")
	public ServerResponseEntity<PageVO<WeixinQrcodeScanRecord>> page(@Valid PageDTO pageDTO) {
		PageVO<WeixinQrcodeScanRecord> weixinQrcodeScanRecordPage = weixinQrcodeScanRecordService.page(pageDTO);
		return ServerResponseEntity.success(weixinQrcodeScanRecordPage);
	}

	@GetMapping
    @ApiOperation(value = "获取微信二维码扫码记录表", notes = "根据id获取微信二维码扫码记录表")
    public ServerResponseEntity<WeixinQrcodeScanRecord> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(weixinQrcodeScanRecordService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存微信二维码扫码记录表", notes = "保存微信二维码扫码记录表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody WeixinQrcodeScanRecordDTO weixinQrcodeScanRecordDTO) {
        WeixinQrcodeScanRecord weixinQrcodeScanRecord = mapperFacade.map(weixinQrcodeScanRecordDTO, WeixinQrcodeScanRecord.class);
        weixinQrcodeScanRecord.setId(null);
        weixinQrcodeScanRecordService.save(weixinQrcodeScanRecord);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新微信二维码扫码记录表", notes = "更新微信二维码扫码记录表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody WeixinQrcodeScanRecordDTO weixinQrcodeScanRecordDTO) {
        WeixinQrcodeScanRecord weixinQrcodeScanRecord = mapperFacade.map(weixinQrcodeScanRecordDTO, WeixinQrcodeScanRecord.class);
        weixinQrcodeScanRecordService.update(weixinQrcodeScanRecord);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除微信二维码扫码记录表", notes = "根据微信二维码扫码记录表id删除微信二维码扫码记录表")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        weixinQrcodeScanRecordService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
