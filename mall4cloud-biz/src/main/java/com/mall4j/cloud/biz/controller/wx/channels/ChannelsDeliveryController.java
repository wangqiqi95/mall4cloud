package com.mall4j.cloud.biz.controller.wx.channels;

import com.mall4j.cloud.biz.service.channels.EcDeliveryService;
import com.mall4j.cloud.biz.vo.LiveLogisticsVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("channelsDeliveryController")
@RequestMapping("/p/channels/delivery")
@Api(tags = "视频号4.0物流映射管理")
public class ChannelsDeliveryController {
	
	@Autowired
	private EcDeliveryService ecDeliveryService;
	
	@GetMapping("/deliveryList")
	@ApiOperation(value = "查询视频号4.0基本物流列表", notes = "查询基本物流列表")
	public ServerResponseEntity<List<LiveLogisticsVO>> deliveryList(@RequestParam(value = "query", required = false) String query) {
		List<LiveLogisticsVO> list = ecDeliveryService.deliveryList(query);
		return ServerResponseEntity.success(list);
	}
}
