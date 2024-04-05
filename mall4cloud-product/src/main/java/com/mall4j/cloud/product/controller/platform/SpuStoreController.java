package com.mall4j.cloud.product.controller.platform;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.model.SpuStore;
import com.mall4j.cloud.product.service.SpuStoreService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * spu信息(SpuStore)表控制层
 *
 * @author makejava
 * @since 2022-02-21 21:28:45
 */
@RestController
@RequestMapping("/p/spuStore")
public class SpuStoreController {
    /**
     * 服务对象
     */
    @Resource
    private SpuStoreService spuStoreService;

    /**
     * 分页查询所有数据
     *
     * @param page     分页对象
     * @param spuStore 查询实体
     * @return 所有数据
     */
    @GetMapping
    public ServerResponseEntity selectAll(Page<SpuStore> page, SpuStore spuStore) {
        return ServerResponseEntity.success(this.spuStoreService.page(page, new QueryWrapper<>(spuStore)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ServerResponseEntity selectOne(@PathVariable Serializable id) {
        return ServerResponseEntity.success(this.spuStoreService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param spuStore 实体对象
     * @return 新增结果
     */
    @PostMapping
    public ServerResponseEntity insert(@RequestBody SpuStore spuStore) {
        return ServerResponseEntity.success(this.spuStoreService.save(spuStore));
    }

    /**
     * 修改数据
     *
     * @param spuStore 实体对象
     * @return 修改结果
     */
    @PutMapping
    public ServerResponseEntity update(@RequestBody SpuStore spuStore) {
        return ServerResponseEntity.success(this.spuStoreService.updateById(spuStore));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public ServerResponseEntity delete(@RequestParam("idList") List<Long> idList) {
        return ServerResponseEntity.success(this.spuStoreService.removeByIds(idList));
    }
}

