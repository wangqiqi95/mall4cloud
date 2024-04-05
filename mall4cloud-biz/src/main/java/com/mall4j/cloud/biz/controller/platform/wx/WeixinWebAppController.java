package com.mall4j.cloud.biz.controller.platform.wx;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.WeixinOpenAccountDTO;
import com.mall4j.cloud.biz.dto.WeixinWebAppCrmTypeDTO;
import com.mall4j.cloud.biz.model.WeixinOpenAccount;
import com.mall4j.cloud.biz.model.WeixinWebApp;
import com.mall4j.cloud.biz.service.WeixinOpenAccountService;
import com.mall4j.cloud.biz.service.WeixinWebAppService;
import com.mall4j.cloud.biz.vo.WeixinWebAppVO;
import com.mall4j.cloud.biz.dto.WeixinWebAppDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.error.YAMLException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * 微信公众号
 *
 * @author FrozenWatermelon
 * @date 2021-12-29 10:13:40
 */
@Slf4j
@RestController("platformWeixinWebAppController")
@RequestMapping("/p/weixin/webapp")
@Api(tags = "微信公众号管理")
public class WeixinWebAppController {

    @Autowired
    private WeixinWebAppService weixinWebAppService;

    @Autowired
    private WeixinOpenAccountService weixinOpenAccountService;

    @Autowired
	private MapperFacade mapperFacade;

    @Autowired
    private WxConfig wxConfig;

	@GetMapping("/page")
	@ApiOperation(value = "微信公众号管理列表", notes = "分页微信公众号管理列表")
	public ServerResponseEntity<PageVO<WeixinWebAppVO>> page(@Valid PageDTO pageDTO) {
		PageVO<WeixinWebAppVO> weixinWebAppPage = weixinWebAppService.page(pageDTO);
		return ServerResponseEntity.success(weixinWebAppPage);
	}

    @PutMapping("/updateCrmType")
    @ApiOperation(value = "更新微信公众号crmType", notes = "更新微信公众号crmType")
    public ServerResponseEntity<Void> updateCrmType(@Valid @RequestBody WeixinWebAppCrmTypeDTO weixinWebAppDTO) {
        if(weixinWebAppService.getById(weixinWebAppDTO.getId())==null){
            return ServerResponseEntity.showFailMsg("公众号信息未找到");
        }
        WeixinWebApp weixinWebApp =new WeixinWebApp();
        weixinWebApp.setId(weixinWebAppDTO.getId());
        weixinWebApp.setCrmType(weixinWebAppDTO.getCrmType());
        weixinWebAppService.update(weixinWebApp);
        return ServerResponseEntity.success();
    }

//	@GetMapping
//    @ApiOperation(value = "微信公众号详情", notes = "根据id微信公众号管理")
//    public ServerResponseEntity<WeixinWebApp> getById(@RequestParam Long id) {
//        return ServerResponseEntity.success(weixinWebAppService.getById(id));
//    }

//    @PostMapping
//    @ApiOperation(value = "保存微信公众号", notes = "保存微信公众号")
//    public ServerResponseEntity<Void> save(@Valid @RequestBody WeixinWebAppDTO weixinWebAppDTO) {
//        WeixinWebApp weixinWebApp = mapperFacade.map(weixinWebAppDTO, WeixinWebApp.class);
//        Long id = RandomUtil.getUniqueNum();
//        weixinWebApp.setId(String.valueOf(id));
//        weixinWebAppService.save(weixinWebApp);
//        return ServerResponseEntity.success();
//    }
//
    @PutMapping
    @ApiOperation(value = "更新微信公众号", notes = "更新微信公众号")
    public ServerResponseEntity<Void> update(@Valid @RequestBody WeixinWebAppDTO weixinWebAppDTO) {
	    if(Objects.isNull(weixinWebAppService.getById(weixinWebAppDTO.getId()))){
	        throw new LuckException("操作失败，信息未找到");
        }
        WeixinWebApp weixinWebApp = new WeixinWebApp();
        weixinWebApp.setId(weixinWebAppDTO.getId());
        weixinWebApp.setWeixinAppSecret(weixinWebAppDTO.getWeixinAppSecret());
        weixinWebApp.setUpdateBy(AuthUserContext.get().getUsername());
        weixinWebApp.setUpdateTime(new Date());
        weixinWebAppService.update(weixinWebApp);
        return ServerResponseEntity.success();
    }
//
//    @DeleteMapping
//    @ApiOperation(value = "删除微信公众号", notes = "根据微信公众号id删除微信公众号")
//    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
//        weixinWebAppService.deleteById(id);
//        return ServerResponseEntity.success();
//    }

}
