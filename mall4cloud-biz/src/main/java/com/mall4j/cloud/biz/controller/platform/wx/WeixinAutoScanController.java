package com.mall4j.cloud.biz.controller.platform.wx;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.URLDecoder;
import com.mall4j.cloud.api.biz.vo.WeixinQrcodeScanRecordLogVO;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.model.WeixinWebApp;
import com.mall4j.cloud.biz.service.WeixinWebAppService;
import com.mall4j.cloud.biz.wx.wx.api.WechatOpenApi;
import com.mall4j.cloud.biz.wx.wx.constant.WxQRSceneSrcType;
import com.mall4j.cloud.biz.service.WeixinAutoScanService;
import com.mall4j.cloud.biz.service.WeixinQrcodeScanRecordService;
import com.mall4j.cloud.biz.vo.WeixinAutoScanVO;
import com.mall4j.cloud.biz.dto.WeixinAutoScanDTO;
import com.mall4j.cloud.biz.vo.WeixinQrcodeScanRecordVO;
import com.mall4j.cloud.common.bean.WxMp;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

/**
 * 微信扫码回复
 *
 * @author FrozenWatermelon
 * @date 2022-01-25 16:46:42
 */
@Slf4j
@RestController("platformWeixinAutoScanController")
@RequestMapping("/p/weixin/autoscan")
@Api(tags = "微信扫码自动回复")
public class WeixinAutoScanController {

    @Autowired
    private WeixinAutoScanService weixinAutoScanService;

    @Autowired
    private WeixinQrcodeScanRecordService weixinQrcodeScanRecordService;
    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private WeixinWebAppService weixinWebAppService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取微信扫码回复分页列表", notes = "获取微信扫码回复分页列表")
	public ServerResponseEntity<PageVO<WeixinAutoScanVO>> page(@Valid PageDTO pageDTO,
                                                               @ApiParam(value = "appId", required = false)
                                                               @RequestParam(value = "appId",required = false,defaultValue = "") String appId,
                                                               @ApiParam(value = "规则名称",required = false)
                                                               @RequestParam(value = "name",required = false,defaultValue = "") String name,
                                                               @ApiParam(value = "用户交互类型: 0全部 1:已关注用户 2：未关注用户",required = false)
                                                               @RequestParam(value = "type",required = false,defaultValue = "") Integer type,
                                                               @ApiParam(value = "开始时间(yyyy-MM-dd HH:mm:ss)", required = false)
                                                               @RequestParam(value = "startDate",required = false,defaultValue = "") String startTime,
                                                               @ApiParam(value = "结束时间(yyyy-MM-dd HH:mm:ss)", required = false)
                                                               @RequestParam(value = "endDate",required = false,defaultValue = "") String endTime) {
        if(StringUtils.isNotEmpty(startTime)) startTime= URLDecoder.decode(startTime, Charset.forName("UTF-8"));
        if(StringUtils.isNotEmpty(endTime))  endTime=URLDecoder.decode(endTime, Charset.forName("UTF-8"));
        if(StringUtils.isNotEmpty(name))  name=URLDecoder.decode(name, Charset.forName("UTF-8"));
		PageVO<WeixinAutoScanVO> weixinAutoScanPage = weixinAutoScanService.page(pageDTO,appId,name,type,startTime,endTime);
		return ServerResponseEntity.success(weixinAutoScanPage);
	}

    @GetMapping("/scanRecPages")
    @ApiOperation(value = "扫码回复(数据详情)分页列表", notes = "扫码回复(数据详情)分页列表")
    public ServerResponseEntity<PageVO<WeixinQrcodeScanRecordVO>> scanRecPages(@Valid PageDTO pageDTO,
                                                                               @ApiParam(value = "openid", required = false)
                                                                               @RequestParam(value = "openid",required = false,defaultValue = "") String openid,
                                                                               @ApiParam(value = "autoScanId 扫码回复规则id", required = true)
                                                                                   @RequestParam(value = "autoScanId",required = false,defaultValue = "") String autoScanId,
                                                                               @ApiParam(value = "二维码id", required = false)
                                                                                   @RequestParam(value = "qrcodeId",required = false,defaultValue = "") String qrcodeId,
                                                                               @ApiParam(value = "开始时间(yyyy-MM-dd HH:mm:ss)", required = false)
                                                               @RequestParam(value = "startDate",required = false,defaultValue = "") String startTime,
                                                                               @ApiParam(value = "结束时间(yyyy-MM-dd HH:mm:ss)", required = false)
                                                               @RequestParam(value = "endDate",required = false,defaultValue = "") String endTime) {
        if(StringUtils.isNotEmpty(startTime)) startTime= URLDecoder.decode(startTime, Charset.forName("UTF-8"));
        if(StringUtils.isNotEmpty(endTime))  endTime=URLDecoder.decode(endTime, Charset.forName("UTF-8"));
        String isScanSubscribe="0";
        Integer sceneSrc= WxQRSceneSrcType.scan_auto_msg.value();
        PageVO<WeixinQrcodeScanRecordVO> weixinQrcodeScanRecordVOPageVO = weixinQrcodeScanRecordService.pageList(pageDTO,isScanSubscribe,qrcodeId,autoScanId,sceneSrc,openid,startTime,endTime);
        return ServerResponseEntity.success(weixinQrcodeScanRecordVOPageVO);
    }

    @GetMapping("/soldScanRecs")
    @ApiOperation(value = "导出扫码回复(数据详情)", notes = "导出扫码回复(数据详情)")
    public void soldScanRecs(
                                @ApiParam(value = "openid", required = false)
                               @RequestParam(value = "openid",required = false,defaultValue = "") String openid,
                                @ApiParam(value = "autoScanId 扫码回复规则id", required = true)
                                @RequestParam(value = "autoScanId",required = false,defaultValue = "") String autoScanId,
                               @ApiParam(value = "二维码id", required = false)
                               @RequestParam(value = "qrcodeId",required = false,defaultValue = "") String qrcodeId,
                               @ApiParam(value = "开始时间(yyyy-MM-dd HH:mm:ss)", required = false)
                               @RequestParam(value = "startDate",required = false,defaultValue = "") String startTime,
                               @ApiParam(value = "结束时间(yyyy-MM-dd HH:mm:ss)", required = false)
                               @RequestParam(value = "endDate",required = false,defaultValue = "") String endTime,
                               HttpServletResponse response) {

        if(StringUtils.isNotEmpty(startTime)) startTime= URLDecoder.decode(startTime, Charset.forName("UTF-8"));
        if(StringUtils.isNotEmpty(endTime))  endTime=URLDecoder.decode(endTime, Charset.forName("UTF-8"));
        String isScanSubscribe="0";
        Integer sceneSrc= WxQRSceneSrcType.scan_auto_msg.value();

        List<WeixinQrcodeScanRecordLogVO>  result=weixinQrcodeScanRecordService.soldScanRecs(isScanSubscribe,qrcodeId,autoScanId,sceneSrc,openid,startTime,endTime,null,response);
        String fileName="taskDetailLog-"+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        try {
            ExcelUtil.exportExcel(WeixinQrcodeScanRecordLogVO.class, result, fileName, response);
        } catch (IOException e) {
            log.info("数据导出失败 {}：{}", e, e.getMessage());
            throw new LuckException("数据导出失败，请重试！");
        }

//        try {
//            if(StringUtils.isNotEmpty(startTime)) startTime= URLDecoder.decode(startTime, Charset.forName("UTF-8"));
//            if(StringUtils.isNotEmpty(endTime))  endTime=URLDecoder.decode(endTime, Charset.forName("UTF-8"));
//            String isScanSubscribe="0";
//            Integer sceneSrc= WxQRSceneSrcType.scan_auto_msg.value();
//
//            weixinQrcodeScanRecordService.soldScanRecs(isScanSubscribe,qrcodeId,autoScanId,sceneSrc,openid,startTime,endTime,downLoadHisId,response);

//            CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
//            downloadRecordDTO.setDownloadTime(new Date());
//            downloadRecordDTO.setCalCount(0);
//            downloadRecordDTO.setFileName(WeixinQrcodeScanRecordLogVO.EXCEL_NAME);
//            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
//            downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
//            ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
//            Long downLoadHisId=null;
//            if(serverResponseEntity.isSuccess()){
//                downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
//            }
//            weixinQrcodeScanRecordService.soldScanRecs(isScanSubscribe,qrcodeId,autoScanId,sceneSrc,openid,startTime,endTime,downLoadHisId,response);
//            return ServerResponseEntity.success("操作成功，请转至下载中心下载");
//        }catch (Exception e){
//            log.error("导出失败",e,e.getMessage());
//            e.printStackTrace();
//            return ServerResponseEntity.showFailMsg("操作失败");
//        }

    }

	@GetMapping("getWeixinAutoScan")
    @ApiOperation(value = "获取微信扫码回复详情", notes = "获取微信扫码回复详情")
    public ServerResponseEntity<WeixinAutoScanVO> getWeixinAutoScan(@RequestParam String id) {
        return weixinAutoScanService.getWeixinAutoScanVO(id);
    }

    @PostMapping("saveAutoScan")
    @ApiOperation(value = "保存微信扫码回复", notes = "保存微信扫码回复")
    public ServerResponseEntity<Void> saveAutoScan(@Valid @RequestBody WeixinAutoScanDTO weixinAutoScanDTO) {
        weixinAutoScanService.saveWeixinAutoScan(weixinAutoScanDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("updateAutoScan")
    @ApiOperation(value = "更新微信扫码回复", notes = "更新微信扫码回复")
    public ServerResponseEntity<Void> updateAutoScan(@Valid @RequestBody WeixinAutoScanDTO weixinAutoScanDTO) {
        weixinAutoScanService.saveWeixinAutoScan(weixinAutoScanDTO);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除微信扫码回复", notes = "根据微信扫码回复id删除微信扫码回复")
    public ServerResponseEntity<Void> delete(@RequestParam String id) {
        weixinAutoScanService.deleteById(id);
        return ServerResponseEntity.success();
    }

    @GetMapping("sendTextMsg")
    @ApiOperation(value = "测试发送公众号消息", notes = "测试发送公众号消息")
    public ServerResponseEntity<WeixinAutoScanVO> getWeixinAutoScan(@RequestParam String templateContent,String fromUser,String toUser) throws WxErrorException {
        WeixinWebApp weixinWebApp=weixinWebAppService.queryByAppid(toUser);
        WxMp wxMp = new WxMp();
        wxMp.setAppId(weixinWebApp.getWeixinAppId());
        wxMp.setSecret(weixinWebApp.getWeixinAppSecret());
        templateContent="欢迎访问\n<a href=\"https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Service_Center_messages.html#7\">客服消息api1</a>";
        String accessToken = wxConfig.getWxMpService(wxMp).getAccessToken(true);
        WechatOpenApi.sendTextMsg(templateContent,fromUser,accessToken);
        return ServerResponseEntity.success();
    }
}
