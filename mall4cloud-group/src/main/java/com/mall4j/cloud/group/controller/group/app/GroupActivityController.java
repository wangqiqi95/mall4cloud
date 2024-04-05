package com.mall4j.cloud.group.controller.group.app;


import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.constant.GroupActivityStatusEnum;
import com.mall4j.cloud.group.dto.AppGroupActivityDTO;
import com.mall4j.cloud.group.service.GroupActivityService;
import com.mall4j.cloud.group.service.GroupTeamService;
import com.mall4j.cloud.group.vo.app.AppGroupActivityVO;
import com.mall4j.cloud.group.vo.app.AppGroupTeamVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


/**
 * 拼团活动表
 *
 * @author YXF
 * @date 2021-03-20 10:39:31
 */
@RestController("appGroupActivityController")
@RequestMapping("/ua/group_activity")
@Api(tags = "app-拼团活动表")
public class GroupActivityController {

    @Autowired
    private GroupActivityService groupActivityService;

    @Autowired
    private GroupTeamService groupTeamService;

    @GetMapping("/info")
    @ApiOperation(value = "拼团活动详情", notes = "获取对应商品的拼团活动信息")
    public ServerResponseEntity<AppGroupActivityVO> activityInfo(@RequestParam("spuId") Long spuId,
                                                                 @RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId) {

        // 查询拼团活动信息
        AppGroupActivityVO groupActivity = groupActivityService.getAppGroupActivityInfo(spuId, storeId);

        // 前端看到这个状态码的时候，不用渲染活动页面了
        if (groupActivity == null || !Objects.equals(groupActivity.getStatus(), GroupActivityStatusEnum.ENABLE.value())) {
            return ServerResponseEntity.fail(ResponseEnum.ACTIVITY_END);
        }

        long now = System.currentTimeMillis();
        int activityStatus = 2;
        // 活动还没开始
        if (groupActivity.getStartTime().getTime() > now) {
            // 距离活动开始还有
            groupActivity.setStartIn((groupActivity.getStartTime().getTime() - now) / 1000);
            activityStatus = 1;
        }
        // 活动还没结束
        if (groupActivity.getEndTime().getTime() > now) {
            // 距离活动开始还有
            groupActivity.setExpiresIn((groupActivity.getEndTime().getTime() - now) / 1000);
        }else{
            activityStatus = 3;
        }
        groupActivity.setActivityStatus(activityStatus);
        return ServerResponseEntity.success(groupActivity);
    }

    @GetMapping("/join_group_list")
    @ApiOperation(value = "可加入的团列表", notes = "只显示最近n个团列表(默认10)")
    public ServerResponseEntity<List<AppGroupTeamVO>> joinGroupList(
            @ApiParam(value = "拼团活动ID", required = true) @RequestParam("groupActivityId") Long groupActivityId,
            @ApiParam(value = "显示数量（默认10）") @RequestParam(value = "showSize", defaultValue = "10") Integer showSize) {
        List<AppGroupTeamVO> list = groupTeamService.listJoinGroup(groupActivityId, showSize);
        return ServerResponseEntity.success(list);
    }

    @PostMapping("/group/list")
    @ApiOperation(value = "根据活动Id查询门店的活动及详情", notes = "根据活动id及门店参数筛选")
    public ServerResponseEntity<List<AppGroupActivityVO>> groupList(@RequestParam(value = "storeId", defaultValue = "1") Long storeId, @RequestBody AppGroupActivityDTO appGroupActivityDTO) {
        List<AppGroupActivityVO> list = groupActivityService.groupListByStoreIdAndActivityId(storeId, appGroupActivityDTO);
        return ServerResponseEntity.success(list);
    }
}
