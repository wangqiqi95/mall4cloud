package com.mall4j.cloud.platform.controller.platform;

import com.mall4j.cloud.api.platform.vo.TzTagDetailVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.dto.TzTagDTO;
import com.mall4j.cloud.platform.dto.TzTagQueryParamDTO;
import com.mall4j.cloud.platform.dto.TzTagStoreDTO;
import com.mall4j.cloud.platform.model.TzTag;
import com.mall4j.cloud.platform.service.TzTagService;
import com.mall4j.cloud.platform.service.TzTagStaffService;
import com.mall4j.cloud.platform.service.TzTagStoreService;
import com.mall4j.cloud.platform.vo.TzTagSimpleVO;
import com.mall4j.cloud.platform.vo.TzTagStaffVO;
import com.mall4j.cloud.platform.vo.TzTagStoreVO;
import com.mall4j.cloud.platform.vo.TzTagVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 门店标签
 *
 * @author gmq
 * @date 2022-09-13 12:00:57
 */
@RestController("platformTzTagController")
@RequestMapping("/p/store/tag")
@Api(tags = "门店标签")
public class TzTagController {

    @Autowired
    private TzTagService tzTagService;
    @Autowired
    private TzTagStaffService tzTagStaffService;
    @Autowired
    private TzTagStoreService tzTagStoreService;

    @Autowired
	private MapperFacade mapperFacade;

	@PostMapping("/page")
	@ApiOperation(value = "获取标签列表", notes = "获取标签列表")
	public ServerResponseEntity<PageVO<TzTagVO>> page(@Valid PageDTO pageDTO, @RequestBody TzTagQueryParamDTO paramDTO) {
		PageVO<TzTagVO> tzTagPage = tzTagService.page(pageDTO,paramDTO);
		return ServerResponseEntity.success(tzTagPage);
	}

    @PostMapping("/listTagByStaffId")
    @ApiOperation(value = "获取员工标签列表", notes = "获取员工标签列表")
    public ServerResponseEntity<List<TzTagDetailVO>> listTagByStaffId(@RequestParam Long staffId) {
        List<TzTagDetailVO> tzTagDetailVOS = tzTagService.listTagByStaffId(staffId);
        return ServerResponseEntity.success(tzTagDetailVOS);
    }

    @PostMapping("/listTagByStoreId")
    @ApiOperation(value = "获取门店标签列表", notes = "获取门店标签列表")
    public ServerResponseEntity<List<TzTagSimpleVO>> listTagByStoreId(@RequestParam Long storeId) {
        List<TzTagSimpleVO> tzTagSimpleVOS = tzTagStoreService.listTagByStoreId(storeId);
        return ServerResponseEntity.success(tzTagSimpleVOS);
    }

    @PostMapping("/pageStaff")
    @ApiOperation(value = "获取标签-员工列表", notes = "获取标签-员工列表")
    public ServerResponseEntity<PageVO<TzTagStaffVO>> pageStaff(@Valid PageDTO pageDTO, @RequestParam Long tagId) {
        PageVO<TzTagStaffVO> tzTagStaffVOPageVO = tzTagStaffService.pageStaff(pageDTO,tagId);
        return ServerResponseEntity.success(tzTagStaffVOPageVO);
    }

    @PostMapping("/pageStore")
    @ApiOperation(value = "获取标签-门店列表", notes = "获取标签-门店")
    public ServerResponseEntity<PageVO<TzTagStoreVO>> pageStore(@Valid PageDTO pageDTO, @RequestParam Long tagId) {
        PageVO<TzTagStoreVO> tzTagStoreVOPageVO = tzTagStoreService.pageStore(pageDTO,tagId);
        return ServerResponseEntity.success(tzTagStoreVOPageVO);
    }

	@GetMapping
    @ApiOperation(value = "获取标签明细", notes = "获取标签明细")
    public ServerResponseEntity<TzTagVO> getByTagId(@RequestParam Long tagId) {
        return ServerResponseEntity.success(tzTagService.getByTagId(tagId));
    }

    @PostMapping
    @ApiOperation(value = "保存标签", notes = "保存标签")
    public ServerResponseEntity<Void> save(@Valid @RequestBody TzTagDTO tzTagDTO) {
        tzTagService.saveTo(tzTagDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("saveStoreTags")
    @ApiOperation(value = "门店管理-保存门店标签", notes = "门店管理-保存门店标签")
    public ServerResponseEntity<Void> saveStoreTags(@Valid @RequestBody TzTagStoreDTO tzTagStoreDTO) {
        tzTagStoreService.saveStoreTags(tzTagStoreDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新标签", notes = "更新标签")
    public ServerResponseEntity<Void> update(@Valid @RequestBody TzTagDTO tzTagDTO) {
        if(tzTagService.getByTagId(tzTagDTO.getTagId())==null){
            throw new LuckException("实体未找到");
        }
        tzTagService.updateTo(tzTagDTO);
        return ServerResponseEntity.success();
    }

    @GetMapping("/updateTagStatus")
    @ApiOperation(value = "变更标签状态", notes = "变更标签状态")
    public ServerResponseEntity<Void> updateTagStatus(@RequestParam(value = "tagId",required = true) Long tagId,
                                                      @RequestParam(value = "status",required = true) Integer status) {
        tzTagService.updateTagStatus(tagId,status);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除标签", notes = "删除标签")
    public ServerResponseEntity<Void> delete(@RequestParam Long tagId) {
        tzTagService.deleteById(tagId);
        return ServerResponseEntity.success();
    }
}
