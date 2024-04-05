package com.mall4j.cloud.biz.controller.app;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.URLDecoder;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.biz.dto.WeixinQrcodeTentacleDTO;
import com.mall4j.cloud.biz.model.WeixinQrcodeTentacle;
import com.mall4j.cloud.biz.service.WeixinQrcodeTentacleService;
import com.mall4j.cloud.biz.service.WeixinQrcodeTentacleStoreService;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleStoreVO;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.NumberTo64;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * 微信触点二维码
 *
 * @author FrozenWatermelon
 * @date 2022-03-09 16:04:27
 */
@RestController("appWeixinQrcodeTentacleController")
@RequestMapping("/wx/ma/qrcode/tentacle")
@Api(tags = "小程序微信触点二维码扫描")
public class WeixinQrcodeTentacleController {

    @Autowired
    private WeixinQrcodeTentacleStoreService weixinQrcodeTentacleStoreService;

    @PostMapping("/saveQrcodeTentacleStoreItem")
    @ApiOperation(value = "新增触点列表详情记录", notes = "新增触点列表详情记录")
    public ServerResponseEntity<String> saveQrcodeTentacleStoreItem(@RequestParam("tentacleStoreId") String tentacleStoreId, @RequestParam("code") String code) throws WxErrorException {
        return weixinQrcodeTentacleStoreService.saveQrcodeTentacleStoreItem(tentacleStoreId, code);
    }

    @GetMapping("/getQrcodeTentacleStoreScene")
    @ApiOperation(value = "获取微信触点二维码信息", notes = "获取微信触点二维码信息")
    public ServerResponseEntity<WeixinQrcodeTentacleStoreVO> getQrcodeTentacleStoreScene(@RequestParam("tentacleStoreId") String tentacleStoreId){
        return weixinQrcodeTentacleStoreService.getQrcodeTentacleStoreScene(tentacleStoreId);
    }

}
