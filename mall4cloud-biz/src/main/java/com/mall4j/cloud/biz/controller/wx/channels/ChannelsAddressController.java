package com.mall4j.cloud.biz.controller.wx.channels;

import com.mall4j.cloud.biz.dto.channels.ChannelsAddressDTO;
import com.mall4j.cloud.biz.dto.channels.ChannelsAddressPageDTO;
import com.mall4j.cloud.biz.service.channels.ChannelsAddressService;
import com.mall4j.cloud.api.biz.vo.ChannelsAddressVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
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
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("channelsAddressController")
@RequestMapping("/p/channels/address")
@Api(tags = "视频号4.0地址管理")
public class ChannelsAddressController {
	
	@Autowired
	private ChannelsAddressService channelsAddressService;
	
	@GetMapping("/page")
	@ApiOperation(value = "查询视频号4.0退货地址列表", notes = "查询视频号4.0退货地址列表")
	public ServerResponseEntity<PageVO<ChannelsAddressVO>> page(@Valid PageDTO pageDTO, ChannelsAddressPageDTO channelsAddressPageDTO) {
		PageVO<ChannelsAddressVO> pageVO = channelsAddressService.page(pageDTO, channelsAddressPageDTO);
		return ServerResponseEntity.success(pageVO);
	}
	
	
	@PostMapping("/save")
	@ApiOperation(value = "新增视频号4.0退货地址", notes = "新增视频号4.0退货地址")
	public ServerResponseEntity<Void> save(@RequestBody ChannelsAddressDTO channelsAddressDTO) {
		channelsAddressService.save(channelsAddressDTO);
		return ServerResponseEntity.success();
	}
	
	@PutMapping("/update")
	@ApiOperation(value = "更新视频号4.0退货地址", notes = "更新视频号4.0退货地址")
	public ServerResponseEntity<Void> update(@RequestBody ChannelsAddressDTO channelsAddressDTO) {
		channelsAddressService.update(channelsAddressDTO);
		return ServerResponseEntity.success();
	}
	
	@DeleteMapping("/delete/{addressId}")
	@ApiOperation(value = "删除视频号4.0退货地址", notes = "删除视频号4.0退货地址")
	public ServerResponseEntity<Void> delete(@PathVariable String addressId) {
		channelsAddressService.delete(addressId);
		return ServerResponseEntity.success();
	}
}
