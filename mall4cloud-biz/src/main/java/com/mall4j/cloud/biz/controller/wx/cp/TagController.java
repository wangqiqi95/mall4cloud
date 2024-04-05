package com.mall4j.cloud.biz.controller.wx.cp;


import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.biz.dto.cp.TagGroupDTO;
import com.mall4j.cloud.biz.dto.cp.TagGroupEnableDTO;
import com.mall4j.cloud.biz.dto.cp.TagSortDTO;
import com.mall4j.cloud.biz.model.cp.Tag;
import com.mall4j.cloud.biz.model.cp.TagGroup;
import com.mall4j.cloud.biz.service.cp.GroupTagRefService;
import com.mall4j.cloud.biz.service.cp.TagGroupService;
import com.mall4j.cloud.biz.service.cp.TagService;
import com.mall4j.cloud.biz.vo.cp.TagGroupVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 群标签配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Slf4j
@RequiredArgsConstructor
@RestController("platformTagController")
@RequestMapping("/p/cp/tag")
@Api(tags = "群标签配置表")
public class TagController {

    private final TagService tagService;
    private final TagGroupService tagGroupService;
    private final GroupTagRefService groupTagRefService;
	private final MapperFacade mapperFacade;

	@GetMapping("/index")
	@ApiOperation(value = "获取群标签配置表首页", notes = "获取群标签配置表首页")
	public ServerResponseEntity<List<TagGroupVO>> index() {
		return ServerResponseEntity.success(tagGroupService.list(null));
	}

    @GetMapping("/allTags")
    @ApiOperation(value = "获取群标签值列表", notes = "获取群标签值列表")
    public ServerResponseEntity<List<Tag>> allTags() {
        return ServerResponseEntity.success(tagService.list());
    }

	@GetMapping
    @ApiOperation(value = "获取群标签配置表", notes = "根据id获取群标签配置表")
    public ServerResponseEntity<TagGroupVO> getById(@RequestParam Long id) {
        List<TagGroupVO> list  = tagGroupService.list(id);
        return ServerResponseEntity.success(list.get(0));
    }

    @PostMapping
    @ApiOperation(value = "保存群标签配置表", notes = "保存群标签配置表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody TagGroupDTO request) {
        List<TagGroupVO> list = tagGroupService.list(null);
        TagGroup tagGroup = mapperFacade.map(request, TagGroup.class);
        tagGroup.setStatus(StatusType.YX.getCode());
        tagGroup.setFlag(StatusType.WX.getCode());
        tagGroup.setSort(list.size()+1);
        tagGroupService.addAndUpdate(tagGroup,request.getTags());
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新群标签配置表", notes = "更新群标签配置表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody TagGroupDTO request) {
        TagGroup tagGroup = mapperFacade.map(request, TagGroup.class);
        tagGroupService.addAndUpdate(tagGroup,request.getTags());
        return ServerResponseEntity.success();
    }

    @PostMapping("/enableGroup")
    @ApiOperation(value = "标签组启用接口", notes = "标签组启用接口")
    public ServerResponseEntity<Void> enableTagGroup(@Valid @RequestBody TagGroupEnableDTO request) {
        TagGroup tagGroup = tagGroupService.getById(request.getId());
        tagGroup.setStatus(request.getStatus());
        tagGroupService.update(tagGroup);
        return ServerResponseEntity.success();
    }

    @PostMapping("/enableTag")
    @ApiOperation(value = "标签启用接口", notes = "标签启用接口")
    public ServerResponseEntity<Void> enableTag(@Valid @RequestBody TagGroupEnableDTO request) {
        Tag tag = tagService.getById(request.getId());
        tag.setStatus(request.getStatus());
        tagService.update(tag);
        return ServerResponseEntity.success();
    }


    @PostMapping("/sort")
    @ApiOperation(value = "标签排序接口", notes = "标签排序接口")
    public ServerResponseEntity<Void> sortTag(@Valid @RequestBody TagSortDTO request) {
        TagGroup tagGroup = tagGroupService.getById(request.getId());
        int offset = 0;
        int front = tagGroup.getSort()>request.getSort().intValue()?0:1;
        tagGroupService.sort(request.getId(),request.getSort(),tagGroup.getSort(),front);
        tagGroup.setSort(request.getSort());
        tagGroupService.update(tagGroup);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除标签", notes = "删除标签")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        tagService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
