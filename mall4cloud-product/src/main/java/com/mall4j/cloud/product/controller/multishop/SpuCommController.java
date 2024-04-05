package com.mall4j.cloud.product.controller.multishop;

import com.mall4j.cloud.product.model.SpuComm;
import com.mall4j.cloud.product.service.SpuCommService;
import com.mall4j.cloud.product.vo.SpuCommVO;
import com.mall4j.cloud.product.dto.SpuCommDTO;
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
 * 商品评论
 *
 * @author YXF
 * @date 2021-01-11 13:47:33
 */
@RestController("multishopSpuCommController")
@RequestMapping("/m/spu_comm")
@Api(tags = "商品评论")
public class SpuCommController {

    @Autowired
    private SpuCommService spuCommService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取商品评论列表", notes = "分页获取商品评论列表")
	public ServerResponseEntity<PageVO<SpuCommVO>> page(@Valid PageDTO pageDTO) {
		PageVO<SpuCommVO> spuCommPage = spuCommService.page(pageDTO, AuthUserContext.get().getTenantId());
		return ServerResponseEntity.success(spuCommPage);
	}

	@GetMapping
    @ApiOperation(value = "获取商品评论", notes = "根据spuCommId获取商品评论")
    public ServerResponseEntity<SpuCommVO> getBySpuCommId(@RequestParam Long spuCommId) {
        return ServerResponseEntity.success(spuCommService.getBySpuCommId(spuCommId));
    }

    @PutMapping
    @ApiOperation(value = "更新商品评论", notes = "更新商品评论")
    public ServerResponseEntity<Void> update(@Valid @RequestBody SpuCommDTO spuCommDTO) {
        SpuComm spuComm = mapperFacade.map(spuCommDTO, SpuComm.class);
        spuCommService.update(spuComm);
        return ServerResponseEntity.success();
    }
}
