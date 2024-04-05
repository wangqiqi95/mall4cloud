package com.mall4j.cloud.user.controller.platform;


import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.annotation.ParameterValid;
import com.mall4j.cloud.user.dto.QuerySendRecordPageDTO;
import com.mall4j.cloud.user.service.GroupSonTaskSendRecordService;
import com.mall4j.cloud.user.vo.StaffSendRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/p/group/push/son/task/send/record")
@Api(tags = "platform-管理后台触达明细相关API")
public class PlatformGroupSonTaskSendRecordController {

    @Autowired
    private GroupSonTaskSendRecordService groupSonTaskSendRecordService;

    @ParameterValid
    @GetMapping("/get/by/task")
    @ApiOperation("查看触达明细分页数据")
    public ServerResponseEntity<PageVO<StaffSendRecordVO>> getPage(@Valid QuerySendRecordPageDTO pageDTO, BindingResult result){
        return groupSonTaskSendRecordService.getTheSendRecordBySonTaskAndStaff(pageDTO);

    }


}
