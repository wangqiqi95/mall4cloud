package com.mall4j.cloud.group.controller.group.app;

import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.service.GroupActivityService;
import com.mall4j.cloud.group.service.GroupOrderService;
import com.mall4j.cloud.group.service.GroupTeamService;
import com.mall4j.cloud.group.vo.GroupOrderVO;
import com.mall4j.cloud.group.vo.app.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * 拼团团队表
 *
 * @author YXF
 * @date 2021-03-20 10:39:32
 */
@RestController("appGroupTeamController")
@RequestMapping("/group_team")
@Api(tags = "app-拼团团队表")
public class GroupTeamController {

    @Autowired
    private GroupTeamService groupTeamService;

    @Autowired
    private GroupActivityService groupActivityService;

    @Autowired
    private GroupOrderService groupOrderService;

    @Autowired
	private MapperFacade mapperFacade;
    @Autowired
    private SpuFeignClient spuFeignClient;

    @SuppressWarnings("checkstyle:LineLength")
    @GetMapping("/info")
    @ApiOperation(value = "拼团详情", notes = "拼团详情,至少携带一个参数,查看拼团单（团队）信息")
    public ServerResponseEntity<AppGroupTeamInfoVO> teamInfo(@RequestParam Long storeId,
            @ApiParam(value = "拼团团队ID")  Long groupTeamId,
            @ApiParam(value = "订单id") Long orderId, @ApiParam(value = "商品id") Long spuId) {
        // 通过订单编号获取团ID
        if (Objects.nonNull(orderId)) {
            GroupOrderVO groupOrder = groupOrderService.getByOrderId(orderId);
            if (Objects.nonNull(groupOrder) && Objects.nonNull(groupOrder.getGroupTeamId())) {
                groupTeamId = groupOrder.getGroupTeamId();
            }
        }

        AppGroupTeamVO groupTeam = groupTeamService.getAppGroupTeam(groupTeamId);

        // 获取参团的用户列表
        List<AppGroupUserVO> groupUserList = groupOrderService.listApiGroupUserDto(groupTeamId);
        // 获取活动信息
        AppGroupActivityVO appGroupActivityVoInfo = groupActivityService.getAppGroupActivityByGroupActivityId(groupTeam.getGroupActivityId(),
                storeId);
        if(Objects.isNull(appGroupActivityVoInfo)){
            throw new LuckException("很遗憾，拼团条件不满足");
        }

        GroupOrderVO groupOrder = groupOrderService.getUserGroupOrderByGroupTeamId(groupTeamId);

        ServerResponseEntity<SpuVO> spuResponse = spuFeignClient.getById(spuId);
        if (!spuResponse.isSuccess()) {
            return ServerResponseEntity.transform(spuResponse);
        }
        SpuVO spuVO = spuResponse.getData();
        appGroupActivityVoInfo.setJoinGroupTeamId(groupTeamId);

        // 拼装拼团详情信息
        AppGroupTeamInfoVO infoVo = new AppGroupTeamInfoVO();
        infoVo.setGroupTeam(groupTeam);
        infoVo.setGroupUserList(groupUserList);
        infoVo.setGroupActivity(appGroupActivityVoInfo);
        infoVo.setSpuName(spuVO.getName());
        infoVo.setPriceFee(spuVO.getPriceFee());
        infoVo.setMainImgUrl(spuVO.getMainImgUrl());
        infoVo.setSellingPoint(spuVO.getSellingPoint());
        if (groupOrder != null) {
            infoVo.setOrderId(groupOrder.getOrderId());
        }
        return ServerResponseEntity.success(infoVo);
    }

    @GetMapping("/join_users")
    @ApiOperation(value = "参团的用户列表", notes = "参团的用户列表")
    public ServerResponseEntity<List<AppGroupUserVO>> joinGroupUsers(
            @ApiParam(value = "拼团团队ID", required = true) @RequestParam("groupTeamId") Long groupTeamId) {
        // 获取参团的用户列表
        List<AppGroupUserVO> groupUserList = groupOrderService.listApiGroupUserDto(groupTeamId);
        return ServerResponseEntity.success(groupUserList);
    }
}
