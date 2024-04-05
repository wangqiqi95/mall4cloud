package com.mall4j.cloud.group.controller.group.admin;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.constant.GroupActivityStatusEnum;
import com.mall4j.cloud.group.dto.GroupActivityDTO;
import com.mall4j.cloud.group.service.GroupActivityService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.vo.GroupActivityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

/**
 * 拼团活动表
 *
 * @author YXF
 * @date 2021-03-19 11:26:37
 */
@RestController("adminGroupActivityController")
@RequestMapping("/mp/group_activity")
@Api(tags = "admin-拼团活动表")
public class GroupActivityController {

    @Autowired
    private GroupActivityService groupActivityService;

    /**
     * 分页查询
     *
     * @param pageDTO             分页对象
     * @param groupActivityDTO 拼团活动表
     * @return 分页数据
     */
    @GetMapping("/page")
    public ServerResponseEntity<PageVO<GroupActivityVO>> getGroupActivityPage(PageDTO pageDTO, GroupActivityDTO groupActivityDTO) {
        Long shopId = AuthUserContext.get().getTenantId();
        groupActivityDTO.setShopId(shopId);
        return ServerResponseEntity.success(groupActivityService.page(pageDTO, groupActivityDTO));
    }

    /**
     * 平台管理分页查询
     *
     * @param pageDTO             分页对象
     * @param groupActivityDTO 拼团活动表
     * @return 分页数据
     */
    @GetMapping("/platform_page")
    public ServerResponseEntity<PageVO<GroupActivityVO>> platformPage(PageDTO pageDTO, GroupActivityDTO groupActivityDTO) {
        if (!Objects.equals(Constant.PLATFORM_SHOP_ID, AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        return ServerResponseEntity.success(groupActivityService.platformPage(pageDTO, groupActivityDTO));
    }

    /**
     * 通过id查询拼团活动表
     *
     * @param groupActivityId id
     * @return 单个数据
     */
    @GetMapping
    public ServerResponseEntity<GroupActivityVO> getById(@RequestParam("groupActivityId") Long groupActivityId) {
        return ServerResponseEntity.success(groupActivityService.getGroupActivityInfo(groupActivityId));
    }

    /**
     * 新增拼团活动表
     *
     * @param groupActivityDTO 拼团活动表
     * @return 是否新增成功
     */
    @PostMapping
    public ServerResponseEntity<Void> save(@RequestBody @Valid GroupActivityDTO groupActivityDTO) {
        if (Objects.isNull(groupActivityDTO.getSpuId())) {
            throw new LuckException("请选择商品");
        }
        if (CollUtil.isEmpty(groupActivityDTO.getGroupSkuList())) {
            throw new LuckException("sku列表不能为空");
        }
        Long shopId = AuthUserContext.get().getTenantId();
        groupActivityDTO.setShopId(shopId);
        groupActivityService.save(groupActivityDTO);
        groupActivityService.removeCache(groupActivityDTO.getSpuId());
        return ServerResponseEntity.success();
    }

    /**
     * 修改拼团活动表
     *
     * @param groupActivityDTO 拼团活动表
     * @return 是否修改成功
     */
    @PutMapping
    public ServerResponseEntity<Void> updateById(@RequestBody GroupActivityDTO groupActivityDTO) {
        if (CollUtil.isEmpty(groupActivityDTO.getGroupSkuList())) {
            throw new LuckException("sku列表不能为空");
        }
        groupActivityService.update(groupActivityDTO);
        groupActivityService.removeCache(groupActivityDTO.getSpuId());
        return ServerResponseEntity.success();
    }

    /**
     * 通过id启用拼团活动
     *
     * @param groupActivityDTO
     * @return 是否启用拼团活动
     */
    @PutMapping("/active")
    public ServerResponseEntity<Boolean> activeGroupActivity(@RequestBody GroupActivityDTO groupActivityDTO) {
        GroupActivityVO groupActivity = groupActivityService.getByGroupActivityId(groupActivityDTO.getGroupActivityId());
        Date currentDate = new Date();
        if (groupActivity == null) {
            throw new LuckException("未找到此活动，请稍后重试");
        } else if (groupActivity.getEndTime().getTime() < currentDate.getTime()) {
            throw new LuckException("您不能启用一个结束时间小于当前时间的活动");
        } else if (!Objects.equals(groupActivity.getShopId(), AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        } else if (!Objects.equals(groupActivity.getStatus(), GroupActivityStatusEnum.DISABLE.value())) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        // 启用状态
        groupActivityService.activeGroupActivity(groupActivityDTO.getGroupActivityId(), groupActivity.getSpuId());
        groupActivityService.removeCache(groupActivity.getSpuId());
        return ServerResponseEntity.success(true);
    }

    /**
     * 通过id删除拼团活动表
     *
     * @param groupActivityId id
     * @return 通过id删除拼团活动表
     */
    @DeleteMapping
    public ServerResponseEntity<Boolean> removeById(@RequestParam("groupActivityId") Long groupActivityId) {
        // 查看活动是否为失效状态或者活动结束
        GroupActivityVO groupActivity = groupActivityService.getByGroupActivityId(groupActivityId);
        if (Objects.isNull(groupActivity)) {
            throw new LuckException("未找到此活动，请稍后重试");
        }
        if (Objects.equals(groupActivity.getStatus(), GroupActivityStatusEnum.ENABLE.value())) {
            throw new LuckException("该活动启用中，不能删除");
        }
        if (Objects.equals(groupActivity.getStatus(), GroupActivityStatusEnum.EXPIRED.value())) {
            groupActivityService.updateStatus(groupActivityId, GroupActivityStatusEnum.DELETE);
        } else {
            groupActivityService.deleteGroupActivity(groupActivityId, groupActivity.getSpuId());
        }
        groupActivityService.removeCache(groupActivity.getSpuId());
        return ServerResponseEntity.success(true);
    }

    /**
     * 失效进行中的拼团活动
     */
    @PutMapping("/invalid")
    public ServerResponseEntity<Boolean> invalidActivity(@RequestBody GroupActivityDTO groupActivityDTO) {
        // 查看活动
        GroupActivityVO groupActivity = groupActivityService.getByGroupActivityId(groupActivityDTO.getGroupActivityId());
        if (groupActivity == null) {
            throw new LuckException("未找到此活动，请稍后重试");
        } else if (!Objects.equals(groupActivity.getShopId(), AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        } if (!Objects.equals(groupActivity.getStatus(), GroupActivityStatusEnum.ENABLE.value())) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        groupActivityService.invalidGroupActivity(groupActivity.getGroupActivityId(), groupActivity.getSpuId());
        groupActivityService.removeCache(groupActivity.getSpuId());
        return ServerResponseEntity.success(true);
    }

    @PostMapping("/offline")
    @ApiOperation(value = "下线团购活动", notes = "下线团购活动")
    public ServerResponseEntity<Void> offline(@RequestBody OfflineHandleEventDTO offlineHandleEventDto) {
        if (!Objects.equals(Constant.PLATFORM_SHOP_ID, AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        GroupActivityVO groupActivityVO = groupActivityService.getByGroupActivityId(offlineHandleEventDto.getHandleId());
        if (Objects.isNull(groupActivityVO)) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        groupActivityService.offline(offlineHandleEventDto);
        groupActivityService.removeCache(groupActivityVO.getSpuId());
        return ServerResponseEntity.success();
    }

    @GetMapping("/get_offline_handle_event/{groupActivityId}")
    @ApiOperation(value = "获取最新下线的事件", notes = "获取最新下线的事件")
    public ServerResponseEntity<OfflineHandleEventVO> getOfflineHandleEvent(@PathVariable Long groupActivityId) {
        return ServerResponseEntity.success(groupActivityService.getOfflineHandleEvent(groupActivityId));
    }

    @PostMapping("/audit")
    @ApiOperation(value = "审核活动", notes = "审核活动")
    public ServerResponseEntity<Void> audit(@RequestBody OfflineHandleEventDTO offlineHandleEventDto) {
        if (!Objects.equals(Constant.PLATFORM_SHOP_ID, AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        groupActivityService.audit(offlineHandleEventDto);
        return ServerResponseEntity.success();
    }

    @PostMapping("/audit_apply")
    @ApiOperation(value = "违规活动提交审核", notes = "违规活动提交审核")
    public ServerResponseEntity<Void> auditApply(@RequestBody OfflineHandleEventDTO offlineHandleEventDto) {
        groupActivityService.auditApply(offlineHandleEventDto);
        return ServerResponseEntity.success();
    }

}
