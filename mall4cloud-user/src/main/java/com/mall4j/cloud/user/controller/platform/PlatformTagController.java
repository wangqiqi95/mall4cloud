package com.mall4j.cloud.user.controller.platform;

import com.mall4j.cloud.api.user.vo.TagVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.annotation.ParameterValid;
import com.mall4j.cloud.user.dto.AddTagDTO;
import com.mall4j.cloud.user.dto.EditTagDTO;
import com.mall4j.cloud.user.dto.QueryTagPageDTO;
import com.mall4j.cloud.user.dto.RemoveTagDTO;
import com.mall4j.cloud.user.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/p/tag")
@Api(tags = "platform-管理后台会员标签API")
public class PlatformTagController {

    @Autowired
    private TagService tagService;


    @ParameterValid
    @PostMapping("/add")
    @ApiOperation("新增标签")
    public ServerResponseEntity addTag(@Valid @RequestBody AddTagDTO addTagDTO, BindingResult result){
        return tagService.addTag(addTagDTO);
    }

    @ParameterValid
    @GetMapping("/page")
    @ApiOperation("查询分页标签")
    public ServerResponseEntity<PageVO<TagVO>> getTagPageVO(@Valid QueryTagPageDTO queryTagPageDTO, BindingResult result){
        return tagService.getTagPageVO(queryTagPageDTO);
    }

    @ParameterValid
    @PostMapping("/edit")
    @ApiOperation("修改标签")
    ServerResponseEntity editTag(@Valid @RequestBody EditTagDTO editTagDTO, BindingResult result){
        return tagService.editTag(editTagDTO);
    }


    @GetMapping("/get/by/{id}")
    @ApiOperation("根据ID查询标签")
    ServerResponseEntity<TagVO> getTag(@PathVariable("id") Long tagId){
        return tagService.getTag(tagId);
    }


    @PostMapping("/remove/group/tag")
    @ApiOperation("删除标签")
    ServerResponseEntity removeTag(@RequestBody RemoveTagDTO removeTagDTO){
        return tagService.removeTag(removeTagDTO.getTagId(), removeTagDTO.getGroupId());
    }

}
