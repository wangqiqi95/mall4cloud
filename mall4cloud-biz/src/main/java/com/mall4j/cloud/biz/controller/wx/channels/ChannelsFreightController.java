package com.mall4j.cloud.biz.controller.wx.channels;

import com.mall4j.cloud.biz.dto.channels.ChannelsFreightDTO;
import com.mall4j.cloud.biz.dto.channels.ChannelsFreightUpdateDTO;
import com.mall4j.cloud.biz.service.channels.ChannelsFreightService;
import com.mall4j.cloud.biz.vo.channels.ChannelsFreightVO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("channelsFreightController")
@RequestMapping("/p/channels/freight")
@Api(tags = "视频号4.0运费模板管理")
public class ChannelsFreightController {
	
	@Autowired
	private ChannelsFreightService channelsFreightService;
	
	@GetMapping("/list")
	@ApiOperation(value = "查询视频号4.0运费模板列表", notes = "查询视频号4.0运费模板列表")
	public ServerResponseEntity<PageVO<ChannelsFreightVO>> list(@Valid PageDTO pageDTO, @RequestParam(value = "templateName", required = false) String templateName) {
		PageVO<ChannelsFreightVO> pageVO = channelsFreightService.list(pageDTO,templateName);
		return ServerResponseEntity.success(pageVO);
	}
	
	@PostMapping("/save")
	@ApiOperation(value = "新增视频号4.0运费模板", notes = "新增视频号4.0运费模板")
	public ServerResponseEntity<Void> save(@Valid @RequestBody ChannelsFreightDTO channelsFreightDTO) {
		channelsFreightService.save(channelsFreightDTO);
		return ServerResponseEntity.success();
	}
	
	@PutMapping("/update")
	@ApiOperation(value = "更新视频号4.0运费模板", notes = "更新视频号4.0运费模板")
	public ServerResponseEntity<Void> update(@Valid @RequestBody ChannelsFreightUpdateDTO channelsFreightUpdateDTO) {
		channelsFreightService.update(channelsFreightUpdateDTO);
		return ServerResponseEntity.success();
	}
	
	@DeleteMapping("/delete/{transportId}")
	@ApiOperation(value = "删除视频号4.0运费模板", notes = "删除视频号4.0运费模板")
	public ServerResponseEntity<Void> delete(@PathVariable Long transportId) {
		channelsFreightService.delete(transportId);
		return ServerResponseEntity.success();
	}
	
}
