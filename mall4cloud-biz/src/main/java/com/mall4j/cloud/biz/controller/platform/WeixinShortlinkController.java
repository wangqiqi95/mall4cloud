package com.mall4j.cloud.biz.controller.platform;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.biz.dto.*;
import com.mall4j.cloud.biz.model.WeixinShortlink;
import com.mall4j.cloud.biz.service.WeixinShortlinkService;
import com.mall4j.cloud.biz.vo.WeixinShortlinkRecordItemVo;
import com.mall4j.cloud.biz.vo.WeixinShortlinkRecordVo;
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
import java.util.Date;
import java.util.List;

/**
 * 
 *
 * @author gmq
 * @date 2022-06-09 14:31:51
 */
@RestController("platformWeixinShortlinkController")
@RequestMapping("/p/weixin_shortlink")
@Api(tags = "platform-小程序短链工具")
public class WeixinShortlinkController {

    @Autowired
    private WeixinShortlinkService weixinShortlinkService;

    @Autowired
	private MapperFacade mapperFacade;

    @PostMapping("/shortLink")
    @ApiOperation(value = "短链生成", notes = "短链生成")
    public ServerResponseEntity<String> shortLink(@Valid @RequestBody WeixinShortlinkDTO weixinShortlinkDTO) {
        WeixinShortlink weixinShortlink = mapperFacade.map(weixinShortlinkDTO, WeixinShortlink.class);
        weixinShortlink.setId(null);
        return weixinShortlinkService.saveTo(weixinShortlink);
    }

    @PostMapping("/shortLinkRecord")
    @ApiOperation(value = "查询短链列表", notes = "查询短链列表")
    public ServerResponseEntity<PageVO<WeixinShortlinkRecordVo>> selectShortLinkRecordList(@Valid @RequestBody WeixinShortlinkRecordDTO weixinShortlinkRecordDTO) {
        return ServerResponseEntity.success(weixinShortlinkService.selectShortLinkRecordList(weixinShortlinkRecordDTO));
    }

    @PostMapping("/shortLinkRecordItem")
    @ApiOperation(value = "查询短链列表详情", notes = "查询短链列表详情")
    public ServerResponseEntity<PageVO<WeixinShortlinkRecordItemVo>> selectShortLinkRecordItemList(@Valid @RequestBody WeixinShortlinkRecordItemDTO weixinShortlinkRecordItemDTO) {
        return ServerResponseEntity.success(weixinShortlinkService.selectShortLinkRecordItemList(weixinShortlinkRecordItemDTO));
    }

    @PostMapping("/shortLinkRecordItemExcel")
    @ApiOperation(value = "短链列表详情导出", notes = "短链列表详情导出")
    public ServerResponseEntity<String> shortLinkRecordItemExcel(@RequestBody WeixinShortlinkRecordItemPageDTO param){
        return ServerResponseEntity.success(weixinShortlinkService.shortLinkRecordItemExcel(param));
    }

}
