package com.mall4j.cloud.biz.controller.app.staff;


import com.mall4j.cloud.biz.service.cp.AppGroupInfoService;
import com.mall4j.cloud.biz.vo.cp.AppGroupDetail;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController("StaffGroupDetailController")
@RequestMapping("/s/group/ref")
@Api(tags = "导购-群相关信息")
public class StaffGroupDetailController {

    private final AppGroupInfoService appGroupInfoService;

	@GetMapping("/ua/drainageDetail")
    @ApiOperation(value = "引流页面信息", notes = "引流页面信息")
    public ServerResponseEntity<AppGroupDetail> getById(@RequestParam String state) {
        return ServerResponseEntity.success(appGroupInfoService.getAppGroupDetail(state));
    }

}
