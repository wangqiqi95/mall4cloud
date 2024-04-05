package com.mall4j.cloud.user.controller.platform;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.UserAdminDTO;
import com.mall4j.cloud.user.dto.UserTagUserDTO;
import com.mall4j.cloud.user.service.UserTagUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户和标签关联表
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@RestController("platformUserTagUserController")
@RequestMapping("/p/user_tag_user")
@Api(tags = "platform-用户和标签关联表")
public class UserTagUserController {

    @Autowired
    private UserTagUserService userTagUserService;

    @PutMapping("/update_user_Tag")
    @ApiOperation(value = "平台批量修改会员标签", notes = "平台批量修改会员标签")
    public ServerResponseEntity<Boolean> batchUpdateUserTag(@RequestBody UserAdminDTO userAdminDTO) {
        return ServerResponseEntity.success(userTagUserService.batchUpdateUserTag(userAdminDTO));
    }

    @DeleteMapping("/delete_user_tag")
    @ApiOperation(value = "删除会员的某个标签", notes = "删除会员的某个标签")
    public ServerResponseEntity<Boolean> deleteUserTag(@RequestBody UserTagUserDTO userTagUserDTO) {
        return ServerResponseEntity.success(userTagUserService.removeByUserIdAndTagId(userTagUserDTO.getUserId(),userTagUserDTO.getUserTagId()));
    }

}
