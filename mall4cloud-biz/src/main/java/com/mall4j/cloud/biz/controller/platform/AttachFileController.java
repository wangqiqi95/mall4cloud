package com.mall4j.cloud.biz.controller.platform;

import com.mall4j.cloud.biz.service.AttachFileService;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author lth
 * @Date 2021/6/1 15:45
 */
@RestController("platformAttachFileController")
@RequestMapping("/p/attach_file")
@Api(tags = "platform-上传文件记录表")
public class AttachFileController {

    @Autowired
    private AttachFileService attachFileService;

    @DeleteMapping("/delete_by_ids")
    @ApiOperation(value = "根据文件id列表批量删除文件记录", notes = "根据文件id列表批量删除文件记录")
    public ServerResponseEntity<Void> deleteByIds(@RequestBody List<Long> ids) {
        Long shopId = Constant.PLATFORM_SHOP_ID;
        attachFileService.deleteByIdsAndShopId(ids, shopId);
        return ServerResponseEntity.success();
    }

    @PutMapping("/batch_move")
    @ApiOperation(value = "根据文件id列表与分组id批量移动文件", notes = "根据文件id列表与分组id批量移动文件")
    public ServerResponseEntity<Void> batchMove(@RequestBody List<Long> ids, @RequestParam(value = "groupId", required = false, defaultValue = "") Long groupId) {
        Long shopId = Constant.PLATFORM_SHOP_ID;
        attachFileService.batchMoveByShopIdAndIdsAndGroupId(shopId, ids, groupId);
        return ServerResponseEntity.success();
    }
}
