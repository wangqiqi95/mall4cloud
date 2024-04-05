package com.mall4j.cloud.biz.controller.platform.wx;

import cn.hutool.core.net.URLDecoder;
import com.mall4j.cloud.biz.dto.WeixinStoreSubscribePutDTO;
import com.mall4j.cloud.biz.dto.WeixinSubscribeOpenDTO;
import com.mall4j.cloud.biz.model.WeixinConfig;
import com.mall4j.cloud.biz.model.WeixinStoreSubscribe;
import com.mall4j.cloud.biz.service.WeixinConfigService;
import com.mall4j.cloud.biz.service.WeixinStoreSubscribeService;
import com.mall4j.cloud.biz.vo.WeixinStoreSubscribeVO;
import com.mall4j.cloud.biz.dto.WeixinStoreSubscribeDTO;

import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.nio.charset.Charset;

/**
 * 微信门店回复内容
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 16:43:04
 */
@RestController("platformWeixinStoreSubscribeController")
@RequestMapping("/p/weixin/subscribe/store")
@Api(tags = "微信关注自动回复")
public class WeixinStoreSubscribeController {

    @Autowired
    private WeixinStoreSubscribeService weixinStoreSubscribeService;

    @Autowired
	private MapperFacade mapperFacade;

    @Autowired
    private WeixinConfigService weixinConfigService;

	@GetMapping("/storeSubscribePage")
	@ApiOperation(value = "门店-获取门店列表", notes = "门店-获取门店列表")
	public ServerResponseEntity<PageVO<WeixinStoreSubscribeVO>> storeSubscribePage(@Valid PageDTO pageDTO,
                                                                                   @RequestParam(defaultValue = "",required = true) String appId,
                                                                                   @RequestParam(defaultValue = "",required = false) String msgType,
                                                                                   @RequestParam(defaultValue = "",required = true) String storeName) {
        if(StringUtils.isNotEmpty(msgType))  msgType= URLDecoder.decode(msgType, Charset.forName("UTF-8"));
        if(StringUtils.isNotEmpty(storeName))  storeName=URLDecoder.decode(storeName, Charset.forName("UTF-8"));
		PageVO<WeixinStoreSubscribeVO> weixinStoreSubscribePage = weixinStoreSubscribeService.page(pageDTO,appId,storeName,msgType);
		return ServerResponseEntity.success(weixinStoreSubscribePage);
	}

    @GetMapping("/getStoreSubOpen")
    @ApiOperation(value = "获取门店关注回复开关", notes = "获取门店关注回复开关")
    public ServerResponseEntity<Integer> getStoreSubOpen(@RequestParam String appId) {
        WeixinConfig weixinConfig=weixinConfigService.getWeixinConfigByKey(appId, WechatConstants.WxConfigConstants.MP_SUBSCRIBE_STORE_OPEN);
        return ServerResponseEntity.success(weixinConfig!=null?Integer.parseInt(weixinConfig.getParamValue()):2);
    }

    @PutMapping("/updateStoreSubOpen")
    @ApiOperation(value = "更新门店关注回复开关", notes = "更新门店关注回复开关")
    public ServerResponseEntity<Integer> updateStoreSubOpen(@RequestBody WeixinSubscribeOpenDTO openDTO) {
        try {
            weixinConfigService.saveOrUpdateWeixinConfig(openDTO.getAppId(), WechatConstants.WxConfigConstants.MP_SUBSCRIBE_STORE_OPEN,openDTO.getOpenState());
            return ServerResponseEntity.success(Integer.parseInt(openDTO.getOpenState()));
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }
    }

	@GetMapping("getStoreSubscribe")
    @ApiOperation(value = "门店-获取详情", notes = "门店-获取详情")
    public ServerResponseEntity<WeixinStoreSubscribeVO> getStoreSubscribe(@RequestParam(defaultValue = "",required = true) String id) {
        return ServerResponseEntity.success(weixinStoreSubscribeService.getStoreSubscribeById(id));
    }

    @PostMapping("saveStoreSubscribe")
    @ApiOperation(value = "门店-保存门店与回复", notes = "门店-保存门店与回复")
    public ServerResponseEntity<Void> saveStoreSubscribe(@Valid @RequestBody WeixinStoreSubscribePutDTO storeSubscribePutDTO) {
        return weixinStoreSubscribeService.saveWeixinStoreSubscribe(storeSubscribePutDTO);
    }

    @PutMapping("updateStoreSubscribe")
    @ApiOperation(value = "门店-更新门店", notes = "门店-更新门店")
    public ServerResponseEntity<Void> updateStoreSubscribe(@Valid @RequestBody WeixinStoreSubscribeDTO weixinStoreSubscribeDTO) {
        return weixinStoreSubscribeService.updateWeixinStoreSubscribe(weixinStoreSubscribeDTO);
    }

    @DeleteMapping("deleteStoreSubscribe")
    @ApiOperation(value = "门店-删除门店", notes = "门店-删除门店")
    public ServerResponseEntity<Void> deleteStoreSubscribe(@RequestParam(defaultValue = "",required = true) String id) {
        WeixinStoreSubscribe weixinStoreSubscribe=weixinStoreSubscribeService.getById(id);
        if(weixinStoreSubscribe==null){
            return ServerResponseEntity.showFailMsg("操作失败，未找到门店");
        }
        weixinStoreSubscribeService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
