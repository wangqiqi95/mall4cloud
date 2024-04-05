

package com.mall4j.cloud.biz.controller.app.staff;

import com.mall4j.cloud.biz.service.live.LiveRoomService;
import com.mall4j.cloud.biz.vo.LiveRoomGuideVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * @author chaoge
 */
@RestController
@AllArgsConstructor
@RequestMapping("/s/sale/liveRoom")
@Api(tags = "导购端-直播")
public class LiveRoomSaleController {

    private final LiveRoomService liveRoomService;

    /**
     * 分页查询
     *
     * @return 分页数据
     */
    @GetMapping("/page")
    @ApiOperation("直播分享列表查询")
    public ServerResponseEntity<PageVO<LiveRoomGuideVO>> getLiveRoomPage(@Valid PageDTO pageDTO) {
        Long storeId = AuthUserContext.get().getStoreId();
        PageVO<LiveRoomGuideVO> result = liveRoomService.getSaleLivePage(pageDTO, storeId);
        return ServerResponseEntity.success(result);
    }
}
