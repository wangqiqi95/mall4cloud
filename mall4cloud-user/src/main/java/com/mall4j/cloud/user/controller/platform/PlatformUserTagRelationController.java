package com.mall4j.cloud.user.controller.platform;


import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.annotation.ParameterValid;
import com.mall4j.cloud.user.dto.*;
import com.mall4j.cloud.user.service.UserTagRelationService;
import com.mall4j.cloud.user.vo.MarkingUserPageVO;
import com.mall4j.cloud.user.vo.UserTagListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/p/user/tag/relation")
@Api(tags = "platform-会员与标签关系API")
public class PlatformUserTagRelationController {

    @Autowired
    private UserTagRelationService userTagRelationService;

//    @ParameterValid
    @PostMapping("/import")
    @ApiOperation("标签会员导入")
    public ServerResponseEntity importAdd(@Valid ExportUserTagRelationDTO exportUserTagRelationDTO, BindingResult result){
        return userTagRelationService.importAdd(exportUserTagRelationDTO);
    }

    @ParameterValid
    @PostMapping("/import/remove")
    @ApiOperation("根据导入文件删除标签会员关系")
    public ServerResponseEntity importRemove(@Valid ExportUserTagRelationDTO exportUserTagRelationDTO, BindingResult result){
        return userTagRelationService.importRemove(exportUserTagRelationDTO);
    }

    @PostMapping("/remove/by/tag")
    @ApiOperation("根据标签ID删除标签会员关系")
    public ServerResponseEntity removeByTag(Long tagId){
        return userTagRelationService.removeByTag(tagId);
    }


    @ParameterValid
    @GetMapping("/get/marking/user/page/by/tag")
    @ApiOperation("获取标签相关的会员信息")
    public ServerResponseEntity<PageVO<MarkingUserPageVO>> getMarkingUserByTagPage(@Valid QueryMarkingUserPageDTO pageDTO,
                                                                                   BindingResult result){
        return userTagRelationService.getMarkingUserByTagPage(pageDTO);
    }



    @ParameterValid
    @PostMapping("/remove/batch/by/select")
    @ApiOperation("批量删除标签会员关系")
    public ServerResponseEntity removeBySelectList(@Valid @RequestBody RemoveUserTagRelationBatchDTO removeBatchDTO){
        return userTagRelationService.removeBySelectList(removeBatchDTO);
    }

    @ParameterValid
    @PostMapping("/remove/relation/by/id")
    @ApiOperation("根据用户标签关联关系ID删除标签会员关系")
    public ServerResponseEntity removeByUserTagRelationId(@RequestBody CancelTagDTO cancelTagDTO){
        return userTagRelationService.removeByUserTagRelationId(cancelTagDTO.getUserTagRelationId());
    }


    @ParameterValid
    @PostMapping("/export/to/download/center")
    @ApiOperation("下载标签会员数据")
    public ServerResponseEntity exportMarkingUser(@RequestParam("tagId") Long tagId, HttpServletResponse response){
        return userTagRelationService.exportMarkingUser(tagId, response);
    }

    @ParameterValid
    @PostMapping("/marking/tag/to/user")
    @ApiOperation("会员打标")
    public ServerResponseEntity marking(@Valid @RequestBody MarkingTagDTO markingTagDTO, BindingResult result){
        return userTagRelationService.marking(markingTagDTO);
    }

    @ParameterValid
    @PostMapping("/cancel/marking/tag/to/user")
    @ApiOperation("取消会员打标")
    public ServerResponseEntity cancelMarking(Long relationId){
        return userTagRelationService.cancelMarking(relationId);
    }


    @ParameterValid
    @GetMapping("/get/tag/list/by/vip")
    @ApiOperation("获取会员相关的标签列表")
    public ServerResponseEntity<List<UserTagListVO>> getTheTagByVipCode(String vipCode){
        return userTagRelationService.getTheTagByVipCode(vipCode);
    }
}
