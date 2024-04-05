package com.mall4j.cloud.biz.controller.app;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.mall4j.cloud.biz.service.NotifyLogService;
import com.mall4j.cloud.biz.vo.NotifyLogVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 *
 *
 * @author lhd
 * @date 2020-05-12 08:21:24
 */
@RestController("appNotifyLogController")
@RequestMapping("/notify_log")
@Api(tags = "消息通知")
public class NotifyLogController {

    @Autowired
    private NotifyLogService notifyLogService;


    /**
     * 查询用户未读消息数量
     */
    @GetMapping("/unread_count" )
    @ApiOperation(value = "查询用户未读消息数量", notes = "查询用户未读消息数量")
    public ServerResponseEntity<Integer> getNotifyCount() {
        Long userId = AuthUserContext.get().getUserId();
        return ServerResponseEntity.success(notifyLogService.countUnreadBySendTypeAndRemindType(userId, Constant.MSG_TYPE));
    }


    /**
     * 查询未读消息列表
     */
    @GetMapping("/unread_count_list")
    @ApiOperation(value = "查询消息列表", notes = "查询消息列表")
    @ApiImplicitParam(name = "status", value = "状态 0未读 1已读 不传查询全部按照已读未读排序", dataType = "Integer")
    public ServerResponseEntity<PageVO<NotifyLogVO>> getUnReadCountList(@Valid PageDTO page, Integer status) {
        Long userId = AuthUserContext.get().getUserId();
//        status = Objects.isNull(status) ? StatusEnum.DISABLE.value() : status;
        PageVO<NotifyLogVO> notifyLogPageVO = notifyLogService.pageBySendTypeAndRemindType(page, userId, Constant.MSG_TYPE, status);
        List<NotifyLogVO> records = notifyLogPageVO.getList();
        List<NotifyLogVO> updateList = new ArrayList<>();
        for (NotifyLogVO record : records) {
            if (!Objects.equals(StatusEnum.DISABLE.value(), record.getStatus())) {
                continue;
            }
            NotifyLogVO notifyLogVO = new NotifyLogVO();
            notifyLogVO.setLogId(record.getLogId());
            notifyLogVO.setStatus(StatusEnum.ENABLE.value());
            updateList.add(notifyLogVO);
        }
        if(CollectionUtils.isNotEmpty(updateList)) {
            notifyLogService.updateBatchById(updateList);
        }
        return ServerResponseEntity.success(notifyLogPageVO);
    }



}
