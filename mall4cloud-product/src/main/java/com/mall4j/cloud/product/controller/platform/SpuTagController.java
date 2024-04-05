package com.mall4j.cloud.product.controller.platform;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.feign.SearchSpuFeignClient;
import com.mall4j.cloud.api.product.feign.ProductFeignClient;
import com.mall4j.cloud.api.vo.EsPageVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.constant.SpuType;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.vo.search.SpuAdminVO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.dto.SpuTagDTO;
import com.mall4j.cloud.product.model.SpuTag;
import com.mall4j.cloud.product.service.SpuTagReferenceService;
import com.mall4j.cloud.product.service.SpuTagService;
import com.mall4j.cloud.product.vo.SpuCommVO;
import com.mall4j.cloud.product.vo.SpuTagVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * 商品分组表
 *
 * @author lhd
 * @date 2021-02-20 14:28:10
 */
@RestController("platformSpuTagController")
@RequestMapping("/p/spu_tag")
@Api(tags = "platform-商品分组表")
public class SpuTagController {

    @Autowired
    private SpuTagService spuTagService;

    @Autowired
    private SpuTagReferenceService spuTagReferenceService;

    @Autowired
	private MapperFacade mapperFacade;

    @Autowired
    private ProductFeignClient productFeignClient;

	@GetMapping("/page")
	@ApiOperation(value = "获取商品分组表列表", notes = "分页获取商品分组表列表")
	public ServerResponseEntity<PageVO<SpuTagVO>> page(@Valid PageDTO pageDTO,SpuTagDTO spuTagDTO) {
        spuTagDTO.setShopId(Constant.PLATFORM_SHOP_ID);
		PageVO<SpuTagVO> spuTagPage = spuTagService.pageByTitle(pageDTO,spuTagDTO);
		return ServerResponseEntity.success(spuTagPage);
	}

    @GetMapping("/info/{id}")
    @ApiOperation(value = "获取商品分组", notes = "根据id获取商品分组")
    public ServerResponseEntity<SpuTagVO> getById(@PathVariable("id") Long id) {
        return ServerResponseEntity.success(spuTagService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存商品分组表", notes = "保存商品分组表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody SpuTagDTO spuTagDTO) {
        spuTagDTO.setShopId(Constant.PLATFORM_SHOP_ID);
        // 查看是否相同的标签
        List<SpuTagVO> list = spuTagService.listByTitle(spuTagDTO);
        if (CollectionUtil.isNotEmpty(list)) {
            // 分组名称已存在，不能添加相同的分组
            throw new LuckException("分组名称已存在，不能添加相同的分组");
        }
        SpuTag spuTag = mapperFacade.map(spuTagDTO, SpuTag.class);
        spuTag.setId(null);
        spuTag.setProdCount(0L);
        spuTagService.save(spuTag);
        spuTagService.removeCacheByShopId(AuthUserContext.get().getTenantId());
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新商品分组表", notes = "更新商品分组表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody SpuTagDTO spuTagDTO) {
        SpuTag spuTag = mapperFacade.map(spuTagDTO, SpuTag.class);
        spuTagDTO.setShopId(Constant.PLATFORM_SHOP_ID);
        // 查看是否相同的标签
        List<SpuTagVO> list = spuTagService.listByTitle(spuTagDTO);
        if (CollectionUtil.isNotEmpty(list)) {
            // 分组名称已存在，不能添加相同的分组
            throw new LuckException("分组名称已存在，不能添加相同的分组");
        }
        spuTagService.update(spuTag);
        spuTagService.removeCacheByShopId(AuthUserContext.get().getTenantId());
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除商品分组表", notes = "根据商品分组表id删除商品分组表")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        SpuTagVO spuTagVO = spuTagService.getById(id);
        if (spuTagVO.getIsDefault() != 0) {
            // 默认标签不能删除
            throw new LuckException("默认标签不能删除");
        }
        // 校验分组是否已经使用过
        int count = spuTagReferenceService.countByStatusAndTagId(StatusEnum.ENABLE.value(),id);
        if (count > 0) {
            throw new LuckException("该分组还存在商品，无法进行删除操作");
        }
        spuTagService.deleteById(id);
        spuTagService.removeCacheByShopId(AuthUserContext.get().getTenantId());
        return ServerResponseEntity.success();
    }

    /**
     * 获取没有参与当前分组的商品
     */
    @GetMapping("/get_not_tag_prod_page")
    @ApiOperation(value = "获取非本分组的商品列表", notes = "根据tagId,获取非本分组的商品列表")
    public ServerResponseEntity<PageVO<SpuCommonVO>> getNotTagProdPage(@Valid PageDTO pageDTO, ProductSearchDTO productSearchDTO) {
        List<Long> spuIds = spuTagReferenceService.spuIdsByTagId(productSearchDTO.getTagId());
        if (CollUtil.isNotEmpty(spuIds)) {
            productSearchDTO.setSpuIdsExclude(spuIds);
        }
        productSearchDTO.setSpuStatus(StatusEnum.ENABLE.value());
        productSearchDTO.setSpuType(SpuType.NORMAL.value());
        productSearchDTO.setTagId(null);
        ServerResponseEntity<PageVO<SpuCommonVO>> responseEntity = productFeignClient.commonSearch(productSearchDTO);
        return ServerResponseEntity.success(responseEntity.getData());
    }
}
