package com.mall4j.cloud.user.controller.app;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.user.dto.UserActionSaveNotifyDTO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.dto.UserActionSaveDTO;
import com.mall4j.cloud.user.model.UserAction;
import com.mall4j.cloud.user.model.UserExtension;
import com.mall4j.cloud.user.service.UserActionService;
import com.mall4j.cloud.user.service.UserExtensionService;
import com.mall4j.cloud.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController("userActionController")
@RequestMapping("/user_action")
@Api(tags = "会员用户行为记录")
public class UserActionController {

    @Autowired
    private UserActionService userActionService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private UserService userService;
    @Autowired
    private UserExtensionService userExtensionService;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private OnsMQTemplate userActionSaveNotifyTemplate;

    @PostMapping("/save")
    @ApiOperation(value = "保存", notes = "保存")
    public ServerResponseEntity<Void> save(@Valid @RequestBody UserActionSaveDTO userActionSaveDTO) {
        Long userId = AuthUserContext.get().getUserId();
        UserApiVO user = userService.getByUserId(userId);
        if (Objects.isNull(user)) {
            throw new LuckException("用户不存在");
        }
        String userName = user.getNickName();
        List<UserAction> userActionList = new ArrayList<>();
        if (userActionSaveDTO.getOrderId() != null) {
            ServerResponseEntity<List<OrderItemVO>> orderItemResp = orderFeignClient.listOrderItemAndLangByOrderId(userActionSaveDTO.getOrderId());
            if (orderItemResp.isSuccess()) {
                userActionList = orderItemResp.getData().stream().map(orderItemVO -> {
                    UserAction userAction = mapperFacade.map(userActionSaveDTO, UserAction.class);
                    userAction.setUserId(userId);
                    userAction.setUserName(userName);
                    userAction.setProductId(orderItemVO.getSpuId());
                    userAction.setProductName(orderItemVO.getSpuName());
                    return userAction;
                }).collect(Collectors.toList());
            }
        } else {
            UserAction userAction = mapperFacade.map(userActionSaveDTO, UserAction.class);
            userAction.setUserId(userId);
            userAction.setUserName(userName);
            userActionList.add(userAction);
        }
        if (!CollectionUtils.isEmpty(userActionList)) {
            userActionService.saveBatch(userActionList);
            // 更新用户最近消费和最近访问时间
            UserExtension extension = userExtensionService.getByUserId(userId);
            if (Objects.nonNull(extension)) {
                extension.setRecentVisitTime(new Date());
                if (userActionSaveDTO.getOrderId() != null) {
                    extension.setRecentConsumerTime(new Date());
                }
                userExtensionService.update(extension);
            }
            // 分销相关统计通知
            if (StrUtil.isNotBlank(userActionSaveDTO.getTentacleNo())) {
                userActionList.stream().forEach(userAction -> {
                    UserActionSaveNotifyDTO userActionSaveNotifyDTO = mapperFacade.map(userAction, UserActionSaveNotifyDTO.class);
                    userActionSaveNotifyDTO.setStoreId(AuthUserContext.get().getStoreId());
                    userActionSaveNotifyTemplate.syncSend(userActionSaveNotifyDTO);
                });
            }
        }
        return ServerResponseEntity.success();
    }

}
