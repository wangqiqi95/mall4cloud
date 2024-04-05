

package com.mall4j.cloud.biz.controller.wx.live.base;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall4j.cloud.biz.dto.live.PageParam;
import com.mall4j.cloud.biz.model.live.LiveInterfaceType;
import com.mall4j.cloud.biz.model.live.LiveUser;
import com.mall4j.cloud.biz.dto.live.LiveUsableNumParam;
import com.mall4j.cloud.biz.dto.live.ReturnLiveParam;
import com.mall4j.cloud.biz.service.live.LiveLogService;
import com.mall4j.cloud.biz.service.live.LiveUserService;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lhd
 * @date 2020-11-20 17:49:56
 */
@RestController
@AllArgsConstructor
@RequestMapping("/mp/multishop/live/liveUser")
@Api(tags = "商家端直播间用户接口")
@Slf4j
public class LiveUserController {

    private final LiveUserService liveUserService;
    private final LiveLogService liveLogService;

    @GetMapping("/page")
//    @PreAuthorize("@pms.hasPermission('live:liveUser:page')")
    @ApiOperation(value = "分页查找直播间用户信息")
    public ResponseEntity<IPage<LiveUser>> getLiveUserPage(PageParam<LiveUser> page, LiveUser liveUser) {
        PageParam<LiveUser> liveUserPage = liveUserService.page(page, new LambdaQueryWrapper<LiveUser>()
                .like(StrUtil.isNotBlank(liveUser.getAnchorWechat()), LiveUser::getAnchorWechat, liveUser.getAnchorWechat())
                .like(StrUtil.isNotBlank(liveUser.getNickName()), LiveUser::getNickName, liveUser.getNickName())
                .like(StrUtil.isNotBlank(liveUser.getRoles()), LiveUser::getRoles, liveUser.getRoles())
                .orderByDesc(LiveUser::getUpdateTime)
        );
        for (LiveUser record : liveUserPage.getRecords()) {
            int[] roles = StrUtil.splitToInt(record.getRoles(), StrUtil.COMMA);
            StringBuilder roleStr = new StringBuilder();
            Map<Integer, String> roleMap = new HashMap<>(16);
            roleMap.put(0, "超级管理员");
            roleMap.put(1, "管理员");
            roleMap.put(2, "主播");
            roleMap.put(3, "运营者");
            for (int i = 0; i < roles.length; i++) {
                if (i > 0) {
                    roleStr.append(",");
                }
                roleStr.append(roleMap.get(roles[i]));
            }
            log.info("record.getRoles = {} , roleStr = {}", record.getRoles(), roleStr.toString());
            //roleStr.deleteCharAt(roleStr.length() - 1);
            record.setRoles(roleStr.toString());
        }
        return ResponseEntity.ok(liveUserPage);
    }

    @GetMapping("/info/{liveUserId}")
    @ApiOperation(value = "通过id查询直播间用户")
    @ApiImplicitParam(name = "liveUserId", value = "直播间用户id", required = true, dataType = "Long")
    public ResponseEntity<LiveUser> getById(@PathVariable("liveUserId") Long liveUserId) {
        LiveUser liveUser = liveUserService.getById(liveUserId);
        return ResponseEntity.ok(liveUser);
    }

    @PostMapping
//    @PreAuthorize("@pms.hasPermission('live:liveUser:save')")
    @ApiOperation(value = "新增直播间用户")
    public ResponseEntity<ReturnLiveParam> save(@RequestBody @Valid LiveUser liveUser) throws WxErrorException {
        return ResponseEntity.ok(liveUserService.saveInfo(liveUser));
    }

    @PutMapping
    @ApiOperation(value = "修改直播间用户")
    public ResponseEntity<Boolean> updateById(@RequestBody @Valid LiveUser liveUser) {
        return ResponseEntity.ok(liveUserService.updateById(liveUser));
    }

    @DeleteMapping("/{liveUserId}")
//    @PreAuthorize("@pms.hasPermission('live:liveUser:delete')")
    @ApiOperation(value = "通过id删除直播间用户")
    @ApiImplicitParam(name = "liveUserId", value = "直播间用户id", required = true, dataType = "Long")
    public ResponseEntity<Boolean> removeById(@PathVariable Long liveUserId) throws WxErrorException {
        return ResponseEntity.ok(liveUserService.removeAndWeChatById(liveUserId));
    }

    @GetMapping("/getAddProdNum")
    @ApiOperation(value = "查询商家今日剩余可用添加直播商品、可删除商品库商品次数")
    public ResponseEntity<LiveUsableNumParam> getAddRoomNum() {
        LiveUsableNumParam liveNum = liveLogService.getLiveNum(AuthUserContext.get().getStoreId(), LiveInterfaceType.ADD_LIVE_ROLE);
        LiveUsableNumParam deleteLiveNum = liveLogService.getLiveNum(   AuthUserContext.get().getStoreId(), LiveInterfaceType.REMOVE_LIVE_ROLE);
        liveNum.setDeleteShopNum(deleteLiveNum.getShopNum());
        liveNum.setDeleteTotalNum(deleteLiveNum.getTotalNum());
        return ResponseEntity.ok(liveNum);
    }
}
