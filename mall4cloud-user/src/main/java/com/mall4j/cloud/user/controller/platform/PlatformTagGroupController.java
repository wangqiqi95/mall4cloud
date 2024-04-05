package com.mall4j.cloud.user.controller.platform;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.annotation.ParameterValid;
import com.mall4j.cloud.user.dto.AddTagGroupDTO;
import com.mall4j.cloud.user.dto.EditParentTagGroupDTO;
import com.mall4j.cloud.user.dto.EditTagGroupDTO;
import com.mall4j.cloud.user.dto.QueryTagGroupGradeDTO;
import com.mall4j.cloud.user.service.TagGroupService;
import com.mall4j.cloud.user.vo.TagGroupVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/p/tag/group")
@Api(tags = "platform-标签组相关API")
public class PlatformTagGroupController {

    @Autowired
    private TagGroupService tagGroupService;


    @ParameterValid
    @GetMapping("/first/grade")
    @ApiOperation("一级分类")
    public ServerResponseEntity getFirstGrade(@Valid QueryTagGroupGradeDTO queryTagGroupGradeDTO, BindingResult result){
        return tagGroupService.getFirstGrade(queryTagGroupGradeDTO);
    }

    @ParameterValid
    @GetMapping("/second/grade")
    @ApiOperation("二级分类")
    public ServerResponseEntity<List<TagGroupVO>> getSecondGrade(@Valid QueryTagGroupGradeDTO queryTagGroupGradeDTO, BindingResult result){
        return tagGroupService.getSecondGrade(queryTagGroupGradeDTO);
    }

    @ParameterValid
    @PostMapping("/add/grade")
    @ApiOperation("添加分类与标签组")
    public ServerResponseEntity addTagGroup(@Valid @RequestBody AddTagGroupDTO addTagGroupDTO, BindingResult result){
        return tagGroupService.addTagGroup(addTagGroupDTO);
    }

    @ParameterValid
    @PostMapping("/edit/grade")
    @ApiOperation("修改分类与标签组")
    public ServerResponseEntity updateTagGroup(@Valid @RequestBody EditTagGroupDTO editTagGroupDTO, BindingResult result){
        return tagGroupService.updateTagGroup(editTagGroupDTO);
    }

    @ParameterValid
    @PostMapping("/edit/first/grade")
    @ApiOperation("修改分类与标签组")
    public ServerResponseEntity updateParentTagGroup(@Valid @RequestBody EditParentTagGroupDTO editParentTagGroupDTO, BindingResult result){
        return tagGroupService.updateParentTagGroup(editParentTagGroupDTO);
    }
}
