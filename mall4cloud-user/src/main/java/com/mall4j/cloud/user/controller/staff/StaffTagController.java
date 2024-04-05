package com.mall4j.cloud.user.controller.staff;

import com.mall4j.cloud.api.user.vo.TagVO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.api.user.vo.UserTagApiVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.dto.*;
import com.mall4j.cloud.user.service.TagGroupService;
import com.mall4j.cloud.user.service.TagService;
import com.mall4j.cloud.user.service.UserTagRelationService;
import com.mall4j.cloud.user.service.UserTagStaffService;
import com.mall4j.cloud.user.vo.StaffGetVisibleUserTagVO;
import com.mall4j.cloud.user.vo.StaffTagVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ZengFanChang
 * @Date 2022/02/13
 */
@RestController("staffStaffTagController")
@RequestMapping("/s/staff_tag")
@Api(tags = "导购标签")
public class StaffTagController {

    @Autowired
    private UserTagStaffService tagStaffService;

    @Autowired
    private TagService tagService;

    @Autowired
    private TagGroupService tagGroupService;

    @Autowired
    private UserTagRelationService userTagRelationService;

    @ApiOperation("导购分组标签列表")
    @GetMapping("/listStaffTag")
    public ServerResponseEntity<List<StaffTagVo>> listStaffTag(UserTagStaffDTO dto) {
        dto.setStaffId(AuthUserContext.get().getUserId());
        return ServerResponseEntity.success(tagStaffService.listStaffTag(dto));
    }


    @ApiOperation("添加导购分组标签")
    @PostMapping("/addStaffTag")
    public ServerResponseEntity<Void> addStaffTag(@RequestBody UserTagStaffDTO dto) {
        dto.setStaffId(AuthUserContext.get().getUserId());
        tagStaffService.addStaffTag(dto);
        return ServerResponseEntity.success();
    }


    @ApiOperation("删除导购分组标签")
    @PostMapping("/deleteStaffTag")
    public ServerResponseEntity<Void> deleteStaffTag(@RequestBody UserTagStaffDTO dto) {
        dto.setStaffId(AuthUserContext.get().getUserId());
        tagStaffService.deleteStaffTag(dto);
        return ServerResponseEntity.success();
    }


    @ApiOperation("分组标签会员列表")
    @GetMapping("/getUserByTag")
    public ServerResponseEntity<List<UserApiVO>> getUserByTag(UserTagStaffDTO dto) {
        dto.setStaffId(AuthUserContext.get().getUserId());
        return ServerResponseEntity.success(tagStaffService.getUserByTag(dto));
    }


    @ApiOperation("导购获取系统标签列表")
    @GetMapping("/listSysStaffTag")
    public ServerResponseEntity<List<StaffTagVo>> listSysStaffTag() {
        return ServerResponseEntity.success(tagStaffService.listSysTagByStaff(AuthUserContext.get().getUserId()));
    }


    @ApiOperation("添加/删除分组标签会员")
    @PostMapping("/saveStaffTagUser")
    public ServerResponseEntity<Void> saveStaffTagUser(@RequestBody UserTagStaffDTO dto) {
        dto.setStaffId(AuthUserContext.get().getUserId());
        tagStaffService.saveStaffTagUser(dto);
        return ServerResponseEntity.success();
    }


    @ApiOperation("威客/会员详情标签列表")
    @GetMapping("/listStaffUserTag")
    public ServerResponseEntity<List<UserTagApiVO>> listStaffUserTag(UserTagStaffDTO dto) {
        dto.setStaffId(AuthUserContext.get().getUserId());
        return ServerResponseEntity.success(tagStaffService.listStaffUserTag(dto));
    }


    @ApiOperation("详情添加标签列表")
    @GetMapping("/listStaffTagByUser")
    public ServerResponseEntity<List<StaffTagVo>> listStaffTagByUser(UserTagStaffDTO dto) {
        dto.setStaffId(AuthUserContext.get().getUserId());
        return ServerResponseEntity.success(tagStaffService.listStaffTagByUser(dto));
    }


    @ApiOperation("详情保存标签")
    @PostMapping("/saveUserTag")
    public ServerResponseEntity<Void> saveUserTag(@RequestBody UserTagStaffDTO dto) {
        dto.setStaffId(AuthUserContext.get().getUserId());
        tagStaffService.saveUserTag(dto);
        return ServerResponseEntity.success();
    }

    /**
     * 导购获取可对用户进行打标的标签信息【标签所在组状态包含导购可用】
     * 支持根据标签名模糊筛选
     * @param queryTagGroupGradeDTO
     * @return
     */
    @GetMapping("/staffGetFirstGrade")
    @ApiOperation(value = "导购获取可对用户进行打标的标签信息-初始分类组", notes = "导购获取可对用户进行打标的标签信息-初始分类组")
    public ServerResponseEntity staffGetFirstGrade(QueryTagGroupGradeDTO queryTagGroupGradeDTO) {
        List<Integer> authFlagArray = new ArrayList<>();
        authFlagArray.add(2);
        queryTagGroupGradeDTO.setAuthFlagArray(authFlagArray);
        return tagGroupService.getFirstGrade(queryTagGroupGradeDTO);
    }

    @GetMapping("/staffGetSecondGrade")
    @ApiOperation(value = "导购获取可对用户进行打标的标签信息-标签分类组", notes = "导购获取可对用户进行打标的标签信息-标签分类组")
    public ServerResponseEntity staffGetSecondGrade(QueryTagGroupGradeDTO queryTagGroupGradeDTO) {
        List<Integer> authFlagArray = new ArrayList<>();
        authFlagArray.add(2);
        queryTagGroupGradeDTO.setAuthFlagArray(authFlagArray);
        return tagGroupService.getSecondGrade(queryTagGroupGradeDTO);
    }

    @GetMapping("/staffGetTagPage")
    @ApiOperation(value = "导购获取可对用户进行打标的标签信息-标签信息", notes = "导购获取可对用户进行打标的标签信息-标签信息")
    public ServerResponseEntity<PageVO<TagVO>> staffGetTagPage(QueryTagPageDTO queryTagPageDTO) {
        queryTagPageDTO.setStaff(true);
        return tagService.getTagPageVO(queryTagPageDTO);
    }

    /**
     * 导购查看单个会员标签【跟会员存在关联且标签所在组状态包含导购可见】
     * @param staffGetVisibleUserTagDTO
     * @return
     */
    @PostMapping("/staffGetVisibleUserTag")
    @ApiOperation(value = "导购查看单个会员标签", notes = "导购查看单个会员标签")
    public ServerResponseEntity<List<StaffGetVisibleUserTagVO>> staffGetVisibleUserTag(@RequestBody StaffGetVisibleUserTagDTO staffGetVisibleUserTagDTO) {
        return ServerResponseEntity.success(userTagRelationService.staffGetVisibleUserTag(staffGetVisibleUserTagDTO));
    }

    /**
     * 导购对单个会员进行打标,如果该标签已经打过标那就进行取消打标
     * @param markingTagDTO
     * @return
     */
    @PostMapping("/staffSaveUserTag")
    @ApiOperation(value = "导购对会员进行打标", notes = "导购对会员进行打标")
    public ServerResponseEntity staffSaveUserTag(@RequestBody MarkingTagDTO markingTagDTO) {
        return userTagRelationService.staffSaveUserTag(markingTagDTO);
    }

}
