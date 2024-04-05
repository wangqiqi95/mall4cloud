package com.mall4j.cloud.biz.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.biz.dto.WeixinShortlinkRecordItemDTO;
import com.mall4j.cloud.biz.model.WeixinShortlink;
import com.mall4j.cloud.biz.service.WeixinShortlinkService;
import com.mall4j.cloud.biz.vo.WeixinShortlinkRecordItemVo;
import com.mall4j.cloud.biz.vo.WeixinShortlinkVO;
import com.mall4j.cloud.biz.dto.WeixinShortlinkDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 
 *
 * @author gmq
 * @date 2022-06-09 14:31:51
 */
@RestController("appWeixinShortlinkController")
@RequestMapping("/ua/ma/wxshortlink")
@Api(tags = "app-小程序短链工具")
public class WeixinShortlinkController {

    @Autowired
    private WeixinShortlinkService weixinShortlinkService;


	@GetMapping("/getShortLink")
    @ApiOperation(value = "获取小程序短链", notes = "根据shortKey获取")
    public ServerResponseEntity<WeixinShortlinkVO> getShortLink(@RequestParam String shortkey) {
        return ServerResponseEntity.success(weixinShortlinkService.getShortkey(shortkey));
    }



}
