package com.mall4j.cloud.biz.controller.platform.wx;

import com.mall4j.cloud.biz.model.WeixinLinksucai;
import com.mall4j.cloud.biz.service.WeixinLinksucaiService;
import com.mall4j.cloud.biz.vo.WeixinLinksucaiVO;
import com.mall4j.cloud.biz.dto.WeixinLinksucaiDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
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

/**
 * 微信素材链接表
 *
 * @author FrozenWatermelon
 * @date 2022-01-26 22:53:05
 */
//@RestController("multishopWeixinLinksucaiController")
//@RequestMapping("/m/weixin_linksucai")
//@Api(tags = "微信素材链接表")
public class WeixinLinksucaiController {

    @Autowired
    private WeixinLinksucaiService weixinLinksucaiService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取微信素材链接表列表", notes = "分页获取微信素材链接表列表")
	public ServerResponseEntity<PageVO<WeixinLinksucai>> page(@Valid PageDTO pageDTO) {
		PageVO<WeixinLinksucai> weixinLinksucaiPage = weixinLinksucaiService.page(pageDTO);
		return ServerResponseEntity.success(weixinLinksucaiPage);
	}

	@GetMapping
    @ApiOperation(value = "获取微信素材链接表", notes = "根据id获取微信素材链接表")
    public ServerResponseEntity<WeixinLinksucai> getById(@RequestParam String id) {
        return ServerResponseEntity.success(weixinLinksucaiService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存微信素材链接表", notes = "保存微信素材链接表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody WeixinLinksucaiDTO weixinLinksucaiDTO) {
        WeixinLinksucai weixinLinksucai = mapperFacade.map(weixinLinksucaiDTO, WeixinLinksucai.class);
        weixinLinksucai.setId(null);
        weixinLinksucaiService.save(weixinLinksucai);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新微信素材链接表", notes = "更新微信素材链接表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody WeixinLinksucaiDTO weixinLinksucaiDTO) {
        WeixinLinksucai weixinLinksucai = mapperFacade.map(weixinLinksucaiDTO, WeixinLinksucai.class);
        weixinLinksucaiService.update(weixinLinksucai);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除微信素材链接表", notes = "根据微信素材链接表id删除微信素材链接表")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        weixinLinksucaiService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
