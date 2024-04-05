package com.mall4j.cloud.product.controller.app;

import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.constant.SpuType;
import com.mall4j.cloud.common.product.vo.CategoryVO;
import com.mall4j.cloud.common.product.vo.app.CategoryAppVO;
import com.mall4j.cloud.product.dto.SpuBrowseLogDTO;
import com.mall4j.cloud.product.model.SpuBrowseLog;
import com.mall4j.cloud.product.service.CategoryService;
import com.mall4j.cloud.product.service.SpuBrowseLogService;
import com.mall4j.cloud.product.vo.SpuBrowseLogVO;
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
import java.util.Objects;

/**
 * 商品浏览记录表
 *
 * @author YXF
 * @date 2021-03-19 14:28:14
 */
@RestController("appSpuBrowseLogController")
@RequestMapping("/spu_browse_log")
@Api(tags = "商品浏览记录表")
public class SpuBrowseLogController {

    @Autowired
    private SpuBrowseLogService spuBrowseLogService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    @ApiOperation(value = "获取商品浏览记录表列表", notes = "分页获取商品浏览记录表列表")
    public ServerResponseEntity<PageVO<SpuBrowseLogVO>> page(@Valid PageDTO pageDTO) {
        PageVO<SpuBrowseLogVO> spuBrowseLogPage = spuBrowseLogService.page(pageDTO);
        return ServerResponseEntity.success(spuBrowseLogPage);
    }

    @PostMapping
    @ApiOperation(value = "保存商品浏览记录表", notes = "保存商品浏览记录表")
    public ServerResponseEntity<Void> save(@RequestBody SpuBrowseLogDTO spuBrowseLogDTO) {
        if (Objects.isNull(spuBrowseLogDTO.getSpuId())) {
            throw new LuckException("商品id不能为空");
        }
        if (Objects.isNull(spuBrowseLogDTO.getCategoryId())) {
            throw new LuckException("分类id不能为空");
        }
        if (Objects.isNull(spuBrowseLogDTO.getSpuType())) {
            spuBrowseLogDTO.setSpuType(SpuType.NORMAL.value());
        }
        Long userId = AuthUserContext.get().getUserId();
        spuBrowseLogDTO.setUserId(userId);
        SpuBrowseLog spuBrowseLog = spuBrowseLogService.getCurrentLogBySpuIdAndUserId(spuBrowseLogDTO);
        // 商品记录已存在，则更新商品记录
        if (Objects.nonNull(spuBrowseLog) && Objects.equals(spuBrowseLog.getSpuType(), spuBrowseLogDTO.getSpuType())) {
            spuBrowseLog.setStatus(StatusEnum.ENABLE.value());
            spuBrowseLogService.update(spuBrowseLog);
            return ServerResponseEntity.success();
        }
        SpuBrowseLog spuBrowse = mapperFacade.map(spuBrowseLogDTO, SpuBrowseLog.class);
        CategoryVO category = categoryService.getById(spuBrowse.getCategoryId());
        spuBrowse.setCategoryId(category.getPrimaryCategoryId());
        spuBrowse.setUserId(userId);
        spuBrowseLogService.save(spuBrowse);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "删除商品浏览记录表(单个删除传id，批量删除传数组)", notes = "根据商品浏览记录表id删除商品浏览记录表")
    public ServerResponseEntity<Void> delete(@RequestBody SpuBrowseLogDTO spuBrowseLogDTO) {
        spuBrowseLogService.delete(spuBrowseLogDTO);
        return ServerResponseEntity.success();
    }
}
