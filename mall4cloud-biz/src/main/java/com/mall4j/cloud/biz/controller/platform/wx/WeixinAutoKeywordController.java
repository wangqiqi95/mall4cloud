package com.mall4j.cloud.biz.controller.platform.wx;

import cn.hutool.core.net.URLDecoder;
import com.mall4j.cloud.biz.dto.WeixinAutoKeywordPutDTO;
import com.mall4j.cloud.biz.dto.WeixinAutoKeywordWorkDTO;
import com.mall4j.cloud.biz.model.WeixinAutoKeyword;
import com.mall4j.cloud.biz.service.WeixinAutoKeywordService;
import com.mall4j.cloud.biz.vo.WeixinAutoKeywordVO;
import com.mall4j.cloud.biz.dto.WeixinAutoKeywordDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import net.sf.jsqlparser.util.validation.metadata.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * 微信关键字表
 *
 * @author FrozenWatermelon
 * @date 2022-01-19 16:06:52
 */
@RestController("platformWeixinAutoKeywordController")
@RequestMapping("/p/weixin/keyword")
@Api(tags = "微信关键词回复")
public class WeixinAutoKeywordController {

    @Autowired
    private WeixinAutoKeywordService weixinAutoKeywordService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取微信关键字分页列表", notes = "获取微信关键字分页列表")
	public ServerResponseEntity<PageVO<WeixinAutoKeywordVO>> page(@Valid PageDTO pageDTO,
                                                                @RequestParam(defaultValue = "",required = true) String appId,
                                                                @RequestParam(defaultValue = "",required = false) String name,
                                                                @RequestParam(defaultValue = "",required = false) String keyword,
                                                                @RequestParam(defaultValue = "",required = false) Integer isWork) {
        name=URLDecoder.decode(name, Charset.forName("UTF-8"));
        keyword=URLDecoder.decode(keyword, Charset.forName("UTF-8"));
		PageVO<WeixinAutoKeywordVO> weixinAutoKeywordPage = weixinAutoKeywordService.page(pageDTO,appId,name,keyword,isWork);
		return ServerResponseEntity.success(weixinAutoKeywordPage);
	}

	@GetMapping("getDetail")
    @ApiOperation(value = "获取微信关键字详情", notes = "获取微信关键字详情")
    public ServerResponseEntity<WeixinAutoKeywordVO> getDetail(@RequestParam String id) {
        return weixinAutoKeywordService.getWeixinAutoKeywordVO(id);
    }

    @PostMapping("saveKeyword")
    @ApiOperation(value = "保存微信关键字", notes = "保存微信关键字")
    public ServerResponseEntity<Void> saveKeyword(@Valid @RequestBody WeixinAutoKeywordPutDTO autoKeywordPutDTO) {
        return weixinAutoKeywordService.saveWeixinAutoKeyword(autoKeywordPutDTO);
    }

    @PutMapping("updateKeyword")
    @ApiOperation(value = "更新微信关键字", notes = "更新微信关键字")
    public ServerResponseEntity<Void> updateKeyword(@Valid @RequestBody WeixinAutoKeywordPutDTO autoKeywordPutDTO) {
        WeixinAutoKeyword autoKeyword=weixinAutoKeywordService.getById(autoKeywordPutDTO.getId());
        if(autoKeyword==null){
            return ServerResponseEntity.showFailMsg("关键词实体未找到");
        }
        return weixinAutoKeywordService.saveWeixinAutoKeyword(autoKeywordPutDTO);
    }

    @PutMapping("updateKeywordWork")
    @ApiOperation(value = "关键字启用/禁用", notes = "关键字启用/禁用")
    public ServerResponseEntity<Void> updateKeywordWork(@Valid @RequestBody WeixinAutoKeywordWorkDTO workDTO) {
        WeixinAutoKeyword autoKeyword=weixinAutoKeywordService.getById(workDTO.getId());
        if(autoKeyword==null){
            return ServerResponseEntity.showFailMsg("未找到对应实体");
        }
        autoKeyword=new WeixinAutoKeyword();
        autoKeyword.setId(workDTO.getId());
        autoKeyword.setIsWork(workDTO.getIsWork());
        autoKeyword.setUpdateBy(String.valueOf(AuthUserContext.get().getUserId()));
        autoKeyword.setUpdateTime(new Date());
        weixinAutoKeywordService.update(autoKeyword);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("deleteKeyword")
    @ApiOperation(value = "删除微信关键字", notes = "根据微信关键字表id删除微信关键字")
    public ServerResponseEntity<Void> deleteKeyword(@RequestParam String id) {
        weixinAutoKeywordService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
