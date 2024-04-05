package com.mall4j.cloud.flow.controller.multishop;

import com.mall4j.cloud.api.product.dto.ProdEffectDTO;
import com.mall4j.cloud.api.product.vo.ProdEffectRespVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.flow.dto.ProductAnalyseDTO;
import com.mall4j.cloud.flow.model.ProductAnalyse;
import com.mall4j.cloud.flow.service.ProductAnalyseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 流量分析—商品分析
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
@RestController("multishopProductAnalyseController")
@RequestMapping("/m/product_analyse")
@Api(tags = "流量分析—商品分析")
public class ProductAnalyseController {

    @Autowired
    private ProductAnalyseService productAnalyseService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取流量分析—商品分析列表", notes = "分页获取流量分析—商品分析列表")
	public ServerResponseEntity<PageVO<ProductAnalyse>> page(@Valid PageDTO pageDTO) {
		PageVO<ProductAnalyse> productAnalysePage = productAnalyseService.page(pageDTO);
		return ServerResponseEntity.success(productAnalysePage);
	}

	@GetMapping
    @ApiOperation(value = "获取流量分析—商品分析", notes = "根据productAnalyseId获取流量分析—商品分析")
    public ServerResponseEntity<ProductAnalyse> getByProductAnalyseId(@RequestParam Long productAnalyseId) {
        return ServerResponseEntity.success(productAnalyseService.getByProductAnalyseId(productAnalyseId));
    }

    @PostMapping
    @ApiOperation(value = "保存流量分析—商品分析", notes = "保存流量分析—商品分析")
    public ServerResponseEntity<Void> save(@Valid @RequestBody ProductAnalyseDTO productAnalyseDTO) {
        ProductAnalyse productAnalyse = mapperFacade.map(productAnalyseDTO, ProductAnalyse.class);
        productAnalyse.setProductAnalyseId(null);
        productAnalyseService.save(productAnalyse);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新流量分析—商品分析", notes = "更新流量分析—商品分析")
    public ServerResponseEntity<Void> update(@Valid @RequestBody ProductAnalyseDTO productAnalyseDTO) {
        ProductAnalyse productAnalyse = mapperFacade.map(productAnalyseDTO, ProductAnalyse.class);
        productAnalyseService.update(productAnalyse);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除流量分析—商品分析", notes = "根据流量分析—商品分析id删除流量分析—商品分析")
    public ServerResponseEntity<Void> delete(@RequestParam Long productAnalyseId) {
        productAnalyseService.deleteById(productAnalyseId);
        return ServerResponseEntity.success();
    }

    @GetMapping("/get_product_effect")
    @ApiOperation(value = "获取商家端商品洞察数据", notes = "获取商品洞察数据")
    public ServerResponseEntity<PageVO<ProdEffectRespVO>> getProductEffect(@Valid PageDTO pageDTO, ProdEffectDTO prodEffectDTO){
        prodEffectDTO.setShopId(AuthUserContext.get().getTenantId());
        prodEffectDTO.setLang(I18nMessage.getLang());
        return ServerResponseEntity.success(productAnalyseService.getProductEffect(pageDTO,prodEffectDTO));
    }
}
