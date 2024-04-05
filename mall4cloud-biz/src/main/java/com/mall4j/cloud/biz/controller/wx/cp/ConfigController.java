package com.mall4j.cloud.biz.controller.wx.cp;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.biz.wx.cp.constant.FlagEunm;
import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.biz.dto.cp.ConfigDTO;
import com.mall4j.cloud.biz.model.cp.Config;
import com.mall4j.cloud.biz.service.cp.ConfigService;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

/**
 * 企业微信配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@RequiredArgsConstructor
@RestController("platformConfigController")
@RequestMapping("/p/cp/config")
@Api(tags = "企业微信配置表")
public class ConfigController {

    private final ConfigService configService;
	private final  MapperFacade mapperFacade;

	@GetMapping("/index")
	@ApiOperation(value = "获取企业微信配置信息", notes = "获取企业微信配置信息")
	public ServerResponseEntity<Config> index() {
		return ServerResponseEntity.success(configService.getConfig());
	}


    @PostMapping
    @ApiOperation(value = "保存企业微信配置表", notes = "保存企业微信配置表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody ConfigDTO configDTO) {
	    if(StrUtil.isEmpty(configDTO.getCpId())){
            throw new LuckException("企业ID不能为空");
        }
        Config config=configService.getByCpId(configDTO.getCpId());
	    if(Objects.isNull(config)){
            config = mapperFacade.map(configDTO, Config.class);
            config.setStatus(StatusType.YX.getCode());
            config.setFlag(FlagEunm.USE.getCode());
            config.setCreateTime(new Date());
            config.setCreateBy(AuthUserContext.get().getUsername());
            configService.save(config);
        }else{
            config = mapperFacade.map(configDTO, Config.class);
            config.setUpdateTime(config.getCreateTime());
            config.setUpdateBy(AuthUserContext.get().getUsername());
            configService.update(config);
        }
        return ServerResponseEntity.success();
    }

}
