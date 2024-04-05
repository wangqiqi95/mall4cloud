package com.mall4j.cloud.biz.controller.platform.wx;

import com.mall4j.cloud.biz.dto.WeixinSubscribeOpenDTO;
import com.mall4j.cloud.biz.model.WeixinConfig;
import com.mall4j.cloud.biz.service.WeixinConfigService;
import com.mall4j.cloud.biz.service.WeixinSubscribeService;
import com.mall4j.cloud.biz.dto.WeixinSubscribePutDTO;

import com.mall4j.cloud.biz.vo.WeixinSubscribeVO;
import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 微信关注回复
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 17:21:59
 */
@RestController("platformWeixinSubscribeController")
@RequestMapping("/p/weixin/subscribe")
@Api(tags = "微信关注自动回复")
public class WeixinSubscribeController {

    @Autowired
    private WeixinSubscribeService weixinSubscribeService;

    @Autowired
    private WeixinConfigService weixinConfigService;

    @Autowired
	private MapperFacade mapperFacade;

    @GetMapping("/getDefSubOpen")
    @ApiOperation(value = "获取默认关注回复开关", notes = "获取默认关注回复开关")
    public ServerResponseEntity<Integer> getDefSubOpen(@RequestParam String appId) {
        WeixinConfig weixinConfig=weixinConfigService.getWeixinConfigByKey(appId, WechatConstants.WxConfigConstants.MP_SUBSCRIBE_OPEN);
        return ServerResponseEntity.success(weixinConfig!=null?Integer.parseInt(weixinConfig.getParamValue()):2);
    }

    @PutMapping("/updateDefSubOpen")
    @ApiOperation(value = "更默认新关注回复开关", notes = "更默认新关注回复开关")
    public ServerResponseEntity<Integer> updateDefSubOpen(@RequestBody WeixinSubscribeOpenDTO openDTO) {
        try {
            weixinConfigService.saveOrUpdateWeixinConfig(openDTO.getAppId(), WechatConstants.WxConfigConstants.MP_SUBSCRIBE_OPEN,openDTO.getOpenState());
            return ServerResponseEntity.success(Integer.parseInt(openDTO.getOpenState()));
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }

    }


    @GetMapping("/getWeixinSubscribe")
    @ApiOperation(value = "获取默认回复内容", notes = "获取默认回复内容")
    public ServerResponseEntity<WeixinSubscribeVO> getWeixinSubscribe(@RequestParam String appId,
                                                                      @ApiParam(value = "回复内容标识： 0默认回复 1门店回复",required = true)
                                                                      @RequestParam Integer replyType,
                                                                      @ApiParam(value = "消息内容",required = true)
                                                                      @RequestParam String msgType) {
        return weixinSubscribeService.getWeixinSubscribeVO(appId,replyType,msgType);
    }

    @PostMapping("/putWeixinSubscribe")
    @ApiOperation(value = "保存默认回复内容", notes = "保存默认回复内容")
    public ServerResponseEntity<Void> putWeixinSubscribe(@RequestBody WeixinSubscribePutDTO subscribeDTO) {
        try {
            weixinSubscribeService.saveWeixinSubscribe(subscribeDTO);
            return ServerResponseEntity.success();
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }

    }

//	@GetMapping("/page")
//	@ApiOperation(value = "获取微信关注回复列表", notes = "分页获取微信关注回复列表")
//	public ServerResponseEntity<PageVO<WeixinSubscribe>> page(@Valid PageDTO pageDTO) {
//		PageVO<WeixinSubscribe> weixinSubscribePage = weixinSubscribeService.page(pageDTO);
//		return ServerResponseEntity.success(weixinSubscribePage);
//	}
//
//	@GetMapping
//    @ApiOperation(value = "获取微信关注回复", notes = "根据id获取微信关注回复")
//    public ServerResponseEntity<WeixinSubscribe> getById(@RequestParam Long id) {
//        return ServerResponseEntity.success(weixinSubscribeService.getById(id));
//    }
//
//    @PostMapping
//    @ApiOperation(value = "保存微信关注回复", notes = "保存微信关注回复")
//    public ServerResponseEntity<Void> save(@Valid @RequestBody WeixinSubscribeDTO weixinSubscribeDTO) {
//        WeixinSubscribe weixinSubscribe = mapperFacade.map(weixinSubscribeDTO, WeixinSubscribe.class);
//        weixinSubscribe.setId(null);
//        weixinSubscribeService.save(weixinSubscribe);
//        return ServerResponseEntity.success();
//    }
//
//    @PutMapping
//    @ApiOperation(value = "更新微信关注回复", notes = "更新微信关注回复")
//    public ServerResponseEntity<Void> update(@Valid @RequestBody WeixinSubscribeDTO weixinSubscribeDTO) {
//        WeixinSubscribe weixinSubscribe = mapperFacade.map(weixinSubscribeDTO, WeixinSubscribe.class);
//        weixinSubscribeService.update(weixinSubscribe);
//        return ServerResponseEntity.success();
//    }
//
//    @DeleteMapping
//    @ApiOperation(value = "删除微信关注回复", notes = "根据微信关注回复id删除微信关注回复")
//    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
//        weixinSubscribeService.deleteById(id);
//        return ServerResponseEntity.success();
//    }
}
