

package com.mall4j.cloud.biz.controller.app;

import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.biz.service.live.LiveRoomService;
import com.mall4j.cloud.biz.vo.LiveRoomGuideVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;


@RestController("veekerLiveRoomController")
@AllArgsConstructor
@RequestMapping("/sale/liveRoom")
@Api(tags = "微客端-直播")
public class AppLiveRoomController {

    private final LiveRoomService liveRoomService;

    @Autowired
    private UserFeignClient userFeignClient;

    /**
     * 分页查询
     *
     * @return 分页数据
     */
    @GetMapping("/page")
    @ApiOperation("直播分享列表查询")
    public ServerResponseEntity<PageVO<LiveRoomGuideVO>> getLiveRoomPage(@RequestParam(required = false) Long storeId,
                                                                         @Valid PageDTO pageDTO) {
        if (storeId == null) {
            ServerResponseEntity<UserApiVO> userResp = userFeignClient.getInsiderUserData(AuthUserContext.get().getUserId());
            if (userResp.isSuccess()) {
                storeId = userResp.getData().getStoreId();
            }
        }
        PageVO<LiveRoomGuideVO> result = liveRoomService.getSaleLivePage(pageDTO, storeId);
        return ServerResponseEntity.success(result);
    }

}
