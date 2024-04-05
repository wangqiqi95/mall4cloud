package com.mall4j.cloud.product.controller.platform;


import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.dto.SpuTagDTO;
import com.mall4j.cloud.product.dto.SpuTagReferenceDTO;
import com.mall4j.cloud.product.model.SpuTag;
import com.mall4j.cloud.product.service.SpuService;
import com.mall4j.cloud.product.service.SpuTagReferenceService;
import com.mall4j.cloud.product.service.SpuTagService;
import com.mall4j.cloud.product.vo.SpuPageVO;
import com.mall4j.cloud.product.vo.SpuTagVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品分组标签关联信息
 *
 * @author lhd
 * @date 2021-02-20 14:28:10
 */
@RestController("platformSpuTagReferenceController")
@RequestMapping("/p/spu_tag_reference")
@Api(tags = "platform-商品分组关联")
public class SpuTagReferenceController {

    @Autowired
    private SpuTagReferenceService spuTagReferenceService;

    @Autowired
    private SpuService spuService;

    @Autowired
    private SpuTagService spuTagService;

    @Autowired
    private MapperFacade mapperFacade;

    @GetMapping("/page_spu_by_tag")
    @ApiOperation(value = "获取当前分组的所有商品", notes = "获取当前分组的所有商品")
    public ServerResponseEntity<PageVO<SpuPageVO>> pageProdByTag(@Valid PageDTO pageDTO, SpuTagReferenceDTO spuTagReferenceDTO) {
        return ServerResponseEntity.success(spuTagReferenceService.pageSpuListByTagId(pageDTO, spuTagReferenceDTO));
    }

    @PostMapping("/add_prod_for_tag")
    @ApiOperation(value = "向分组添加商品", notes = "向分组添加商品")
    public ServerResponseEntity<Boolean> addProdForTag(@RequestBody @Valid SpuTagDTO spuTagDTO) {
        SpuTagVO spuTagVO = spuTagService.getById(spuTagDTO.getId());
        if (Objects.isNull(spuTagVO)) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        if (!Objects.equals(spuTagVO.getShopId(), Constant.PLATFORM_SHOP_ID)) {
            // 您无权进行操作
            throw new LuckException("您无权进行操作");
        }
        List<SpuDTO> spuList = spuTagDTO.getSpuList();
        if (CollectionUtil.isEmpty(spuList)) {
            // 请选择需要加入分组的商品
            throw new LuckException("请选择需要加入分组的商品");
        }
        List<Long> ids = spuTagReferenceService.spuIdsByTagId(spuTagDTO.getId());
        List<Long> curIds = spuList.stream().map(SpuDTO::getSpuId).collect(Collectors.toList());
        for (Long curId : curIds) {
            if (ids.contains(curId)) {
                spuList.removeIf(spu -> spu.getSpuId().equals(curId));
            }
        }
        spuTagDTO.setShopId(spuTagVO.getShopId());
        SpuTag spuTag = mapperFacade.map(spuTagDTO, SpuTag.class);
        spuTagReferenceService.addProdForTag(spuTag, spuList);
        return ServerResponseEntity.success();
    }

    @PutMapping("/update_prod_seq")
    @ApiOperation(value = "修改分组中的商品排序号", notes = "修改分组中的商品排序号")
    public ServerResponseEntity<Boolean> updateProdSeq(@RequestBody @Valid List<SpuTagReferenceDTO> spuTagList) {

        if (CollectionUtil.isEmpty(spuTagList)) {
            // 请选择需要修改排序的分组商品
            throw new LuckException("请选择需要修改排序的分组商品");
        }
        spuTagReferenceService.updateProdSeq(spuTagList);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/delete_prod")
    @ApiOperation(value = "删除分组中的商品", notes = "删除分组中的商品")
    public ServerResponseEntity<Void> delete(@RequestBody SpuTagDTO spuTagDTO) {
        SpuTagVO spuTagVO = spuTagService.getById(spuTagDTO.getId());
        if (Objects.isNull(spuTagDTO)) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        List<Long> spuIds = spuTagDTO.getSpuIds();
        if (CollectionUtil.isEmpty(spuIds)) {
            // 请选择需要删除分组中的商品
            throw new LuckException("请选择需要删除分组中的商品");
        }
        if (Objects.isNull(spuTagDTO.getId())) {
            // 缺少分组id
            throw new LuckException("缺少分组id");
        }
        spuTagReferenceService.removeByProdId(spuTagDTO.getId(), spuIds);
        return ServerResponseEntity.success();
    }
}
