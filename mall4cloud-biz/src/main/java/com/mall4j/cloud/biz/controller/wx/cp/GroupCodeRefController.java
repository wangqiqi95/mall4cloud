package com.mall4j.cloud.biz.controller.wx.cp;


import com.mall4j.cloud.biz.service.cp.GroupCodeRefService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 群活码表
 *
 * @author hwy
 * @date 2022-02-16 15:17:19
 */
@Slf4j
@RequiredArgsConstructor
@RestController("GroupCodeRefController")
@RequestMapping("/p/cp/group/code/ref")
@Api(tags = "群活码关联群")
public class GroupCodeRefController {

    private final GroupCodeRefService refService;


    @GetMapping("/enable")
    @ApiOperation(value = "关联群启用", notes = "关联群启用")
    public ServerResponseEntity<Void> enableRefGroup(@RequestParam Long id) {
        refService.updateStatus(id,1);
        return ServerResponseEntity.success();
    }

    @GetMapping("/disable")
    @ApiOperation(value = "关联群禁用", notes = "关联群禁用")
    public ServerResponseEntity<Void> disableRefGroup(@RequestParam Long id) {
        refService.updateStatus(id,0);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "根据id删除关联的群", notes = "根据id删除关联的群")
    public ServerResponseEntity<Void> deleteById(@RequestParam Long id) {
        refService.deleteById(id);
        return ServerResponseEntity.success();
    }

//    @DeleteMapping
//    @ApiOperation(value = "删除关联的群", notes = "删除关联的群")
//    public ServerResponseEntity<Void> deleteGroup(@RequestParam String id) {
//        refService.deleteByCodeId(id);
//        return ServerResponseEntity.success();
//    }

//    @GetMapping("/getGroupList")
//    @ApiOperation(value = "查询关联的群信息", notes = "查询关联的群信息")
//    public ServerResponseEntity<PageVO<CpCustGroup>> getSelectedGroupList(@Valid  PageDTO pageDTO, GetSelectedGroupListDTO groupCodeDTO) {
//        PageVO<CpCustGroup> page  = groupService.getSelectGroupList(pageDTO,groupCodeDTO);
//        return ServerResponseEntity.success(page);
//    }

}
