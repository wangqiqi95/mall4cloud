package com.mall4j.cloud.biz.controller.platform.wx;

import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.dto.WeixinAutoMsgPutDTO;
import com.mall4j.cloud.biz.dto.WeixinSubscribeOpenDTO;
import com.mall4j.cloud.biz.model.WeixinConfig;
import com.mall4j.cloud.biz.service.WeixinAutoMsgService;
import com.mall4j.cloud.biz.service.WeixinConfigService;
import com.mall4j.cloud.biz.vo.WeixinAutoMsgVO;

import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 微信消息自动回复
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 17:52:24
 */
@RestController("platformWeixinAutoMsgController")
@RequestMapping("/p/weixin/automsg")
@Api(tags = "微信消息自动回复")
public class WeixinAutoMsgController {

    @Autowired
    private WeixinAutoMsgService weixinAutoMsgService;

    @Autowired
	private MapperFacade mapperFacade;

    @Autowired
    private WeixinConfigService weixinConfigService;

    @GetMapping("/getAutoMsgOpen")
    @ApiOperation(value = "获取消息自动回复开关", notes = "获取消息自动回复开关")
    public ServerResponseEntity<Integer> getAutoMsgOpen(@RequestParam String appId) {
        WeixinConfig weixinConfig=weixinConfigService.getWeixinConfigByKey(appId, WechatConstants.WxConfigConstants.MP_AUTO_MSG_OPEN);
        return ServerResponseEntity.success(weixinConfig!=null?Integer.parseInt(weixinConfig.getParamValue()):2);
    }

    @PutMapping("/updateAutoMsgOpen")
    @ApiOperation(value = "更新消息自动回复开关", notes = "更新消息自动回复开关")
    public ServerResponseEntity<Integer> updateAutoMsgOpen(@RequestBody WeixinSubscribeOpenDTO openDTO) {
        try {
            weixinConfigService.saveOrUpdateWeixinConfig(openDTO.getAppId(), WechatConstants.WxConfigConstants.MP_AUTO_MSG_OPEN,openDTO.getOpenState());
            return ServerResponseEntity.success(Integer.parseInt(openDTO.getOpenState()));
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }
    }

    @GetMapping("/getWeixinAutoMsg")
    @ApiOperation(value = "获取消息自动回复内容", notes = "获取消息自动回复内容")
    public ServerResponseEntity<WeixinAutoMsgVO> getWeixinAutoMsg(@RequestParam String appId, @RequestParam String msgType) {
        return weixinAutoMsgService.getWeixinAutoMsgVO(appId,msgType);
    }

    @PostMapping("/putWeixinAutoMsg")
    @ApiOperation(value = "保存消息自动回复内容", notes = "保存消息自动回复内容")
    public ServerResponseEntity<Void> putWeixinAutoMsg(@RequestBody WeixinAutoMsgPutDTO autoMsgPutDTO) {
        try {
            weixinAutoMsgService.saveWeixinAutoMsg(autoMsgPutDTO);
            return ServerResponseEntity.success();
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }

    }

//	@GetMapping("/page")
//	@ApiOperation(value = "获取微信消息自动回复列表", notes = "分页获取微信消息自动回复列表")
//	public ServerResponseEntity<PageVO<WeixinAutoMsg>> page(@Valid PageDTO pageDTO) {
//		PageVO<WeixinAutoMsg> weixinAutoMsgPage = weixinAutoMsgService.page(pageDTO);
//		return ServerResponseEntity.success(weixinAutoMsgPage);
//	}
//
//	@GetMapping
//    @ApiOperation(value = "获取微信消息自动回复", notes = "根据id获取微信消息自动回复")
//    public ServerResponseEntity<WeixinAutoMsg> getById(@RequestParam Long id) {
//        return ServerResponseEntity.success(weixinAutoMsgService.getById(id));
//    }
//
//    @PostMapping
//    @ApiOperation(value = "保存微信消息自动回复", notes = "保存微信消息自动回复")
//    public ServerResponseEntity<Void> save(@Valid @RequestBody WeixinAutoMsgDTO weixinAutoMsgDTO) {
//        WeixinAutoMsg weixinAutoMsg = mapperFacade.map(weixinAutoMsgDTO, WeixinAutoMsg.class);
//        weixinAutoMsg.setId(null);
//        weixinAutoMsgService.save(weixinAutoMsg);
//        return ServerResponseEntity.success();
//    }
//
//    @PutMapping
//    @ApiOperation(value = "更新微信消息自动回复", notes = "更新微信消息自动回复")
//    public ServerResponseEntity<Void> update(@Valid @RequestBody WeixinAutoMsgDTO weixinAutoMsgDTO) {
//        WeixinAutoMsg weixinAutoMsg = mapperFacade.map(weixinAutoMsgDTO, WeixinAutoMsg.class);
//        weixinAutoMsgService.update(weixinAutoMsg);
//        return ServerResponseEntity.success();
//    }
//
//    @DeleteMapping
//    @ApiOperation(value = "删除微信消息自动回复", notes = "根据微信消息自动回复id删除微信消息自动回复")
//    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
//        weixinAutoMsgService.deleteById(id);
//        return ServerResponseEntity.success();
//    }
}
