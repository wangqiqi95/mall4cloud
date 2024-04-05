package com.mall4j.cloud.biz.controller.platform.wx;

import cn.hutool.core.net.URLDecoder;
import com.mall4j.cloud.biz.dto.WeixinQrcodeCheckDTO;
import com.mall4j.cloud.biz.dto.WeixinQrcodePutDTO;
import com.mall4j.cloud.biz.dto.WxCpMsgDTO;
import com.mall4j.cloud.biz.model.WeixinQrcode;
import com.mall4j.cloud.biz.service.WechatEventHandler;
import com.mall4j.cloud.biz.service.WeixinQrcodeService;
import com.mall4j.cloud.biz.vo.WeixinQrcodeVO;
import com.mall4j.cloud.biz.dto.WeixinQrcodeDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.SpringContextUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * 微信二维码
 *
 * @author FrozenWatermelon
 * @date 2022-01-28 22:25:17
 */
@RestController("platformWeixinQrcodeController")
@RequestMapping("/p/weixin/qrcode")
@Api(tags = "微信二维码管理")
public class WeixinQrcodeController {

    @Autowired
    private WeixinQrcodeService weixinQrcodeService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取微信二维码列表", notes = "分页获取微信二维码列表")
	public ServerResponseEntity<PageVO<WeixinQrcodeVO>> page(@Valid PageDTO pageDTO,
                                                           @ApiParam(value = "作业名称",required = false)
                                                           @RequestParam(value = "actionInfo",required = false,defaultValue = "") String actionInfo,
                                                             @ApiParam(value = "门店id(多个用英文逗号分隔开)",required = false)
                                                           @RequestParam(value = "storeId",required = false,defaultValue = "") String storeId,
                                                           @ApiParam(value = "开始时间(yyyy-MM-dd HH:mm:ss)", required = false)
                                                           @RequestParam(value = "startDate",required = false,defaultValue = "") String startTime,
                                                           @ApiParam(value = "结束时间(yyyy-MM-dd HH:mm:ss)", required = false)
                                                           @RequestParam(value = "endDate",required = false,defaultValue = "") String endTime) {
        if(StringUtils.isNotEmpty(startTime)) startTime= URLDecoder.decode(startTime, Charset.forName("UTF-8"));
        if(StringUtils.isNotEmpty(endTime))  endTime=URLDecoder.decode(endTime, Charset.forName("UTF-8"));
        if(StringUtils.isNotEmpty(actionInfo))  actionInfo=URLDecoder.decode(actionInfo, Charset.forName("UTF-8"));
        if(StringUtils.isNotEmpty(storeId))  storeId=URLDecoder.decode(storeId, Charset.forName("UTF-8"));
		PageVO<WeixinQrcodeVO> weixinQrcodePage = weixinQrcodeService.page(pageDTO,actionInfo,storeId,startTime,endTime);
		return ServerResponseEntity.success(weixinQrcodePage);
	}

//	@GetMapping
//    @ApiOperation(value = "获取微信二维码", notes = "根据id获取微信二维码")
//    public ServerResponseEntity<WeixinQrcode> getById(@RequestParam String id) {
//        return ServerResponseEntity.success(weixinQrcodeService.getById(id));
//    }

    @GetMapping("/generateQrcode")
    @ApiOperation(value = "生成微信二维码（调试用）", notes = "生成微信二维码（调试用）")
    public ServerResponseEntity<WeixinQrcode> generateQrcode(@RequestParam String id) {
        WeixinQrcode weixinQrcode=weixinQrcodeService.getById(id);
        if(weixinQrcode==null){
            return ServerResponseEntity.showFailMsg("操作失败");
        }
        return weixinQrcodeService.generateQrcode(weixinQrcode);
    }

    @GetMapping("/downQrcode")
    @ApiOperation(value = "单个下载二维码", notes = "单个下载二维码")
    public void downQrcode(@ApiParam(value = "多个下载id(英文逗号分隔开)",required = true)
                               @RequestParam("id") String id,
                           @ApiParam(value = "尺寸大小",required = true)
                               @RequestParam(value = "qrcodeSize",defaultValue = "300") Integer qrcodeSize,
                           HttpServletRequest request, HttpServletResponse response) {
         weixinQrcodeService.downQrcode(id,qrcodeSize,request,response);
    }

    @GetMapping("/downQrcodeAll")
    @ApiOperation(value = "批量下载二维码", notes = "批量下载二维码")
    public void downQrcodeAll(@ApiParam(value = "多个下载id(英文逗号分隔开)",required = true)
                                  @RequestParam("ids") String ids,
                              @ApiParam(value = "尺寸大小",required = true)
                              @RequestParam(value = "qrcodeSize",defaultValue = "300") Integer qrcodeSize,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        weixinQrcodeService.downQrcodeAll(ids,qrcodeSize,request,response);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存微信二维码", notes = "保存微信二维码")
    public ServerResponseEntity<Void> save(@Valid @RequestBody WeixinQrcodePutDTO weixinQrcodePutDTO) {
        weixinQrcodeService.saveWeixinQrcode(weixinQrcodePutDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/sendPostMsgEvent")
    @ApiOperation(value = "sendPostMsgEvent", notes = "sendPostMsgEvent")
    public ServerResponseEntity<Void> save(@Valid @RequestBody WxCpMsgDTO msgDTO) throws Exception{
        WechatEventHandler handler = SpringContextUtils.getBean("wx_scan_event_handler", WechatEventHandler.class);
        handler.exec(msgDTO);
        return ServerResponseEntity.success();
    }

//    @PutMapping("/checkStatus")
//    @ApiOperation(value = "微信二维码审核", notes = "微信二维码审核")
//    public ServerResponseEntity<Void> checkStatus(@RequestBody WeixinQrcodeCheckDTO checkDTO) {
//        WeixinQrcode weixinQrcode=weixinQrcodeService.getById(checkDTO.getId());
//        if(weixinQrcode==null){
//            return ServerResponseEntity.showFailMsg("操作失败");
//        }
//        weixinQrcode.setUpdateTime(new Date());
//        weixinQrcode.setStatus(checkDTO.getStatus());
//        weixinQrcodeService.update(weixinQrcode);
//        return ServerResponseEntity.success();
//    }

//    @PutMapping
//    @ApiOperation(value = "更新微信二维码", notes = "更新微信二维码")
//    public ServerResponseEntity<Void> update(@Valid @RequestBody WeixinQrcodeDTO weixinQrcodeDTO) {
//        WeixinQrcode weixinQrcode = mapperFacade.map(weixinQrcodeDTO, WeixinQrcode.class);
//        weixinQrcodeService.update(weixinQrcode);
//        return ServerResponseEntity.success();
//    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除微信二维码", notes = "根据微信二维码id删除微信二维码")
    public ServerResponseEntity<Void> delete(@RequestParam String id) {
        weixinQrcodeService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
