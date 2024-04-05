package com.mall4j.cloud.biz.controller.app.staff;


import com.mall4j.cloud.api.biz.dto.cp.CpUserGroupDTO;
import com.mall4j.cloud.api.biz.vo.UserJoinCustGroupVO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.biz.service.cp.GroupCustInfoService;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 客群配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Slf4j
@RequiredArgsConstructor
@RestController("staffCustGroupController")
@RequestMapping("/s/cp/cust_group")
@Api(tags = "导购-客群配置表")
public class CustGroupController {

    private final GroupCustInfoService groupCustInfoService;

    @PostMapping("/pageGroupByUser")
    @ApiOperation(value = "获取所在群聊列表", notes = "分页获取所在群聊列表")
    public ServerResponseEntity<PageVO<UserJoinCustGroupVO>> pageGroupByUser(@RequestBody CpUserGroupDTO dto) {
        PageVO<UserJoinCustGroupVO> pageVO = groupCustInfoService.appPageGroupByUser(dto);
        return ServerResponseEntity.success(pageVO);
    }

}
