

package com.mall4j.cloud.biz.controller.wx.live.app;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.biz.service.live.LiveRoomService;
import com.mall4j.cloud.biz.vo.LiveRoomGuideVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * @author lhd
 * @date 2020-08-05 08:53:17
 */
//@RestController
//@AllArgsConstructor
//@RequestMapping("/ua/live/liveRoom")
//@Api(tags = "app-小程序直播")
public class AppLiveRoomController {

//    private final LiveRoomService liveRoomService;
//
//    @GetMapping("/page")
//    @ApiOperation("分页查询")
//    public ServerResponseEntity<PageVO<LiveRoomGuideVO>> getLiveRoomProdPage(@Valid PageDTO pageDTO
//            , @ApiParam(name = "type", value = "101：直播中，102：未开始，103已结束", required = true) @RequestParam(value = "type") Integer type
//            , @ApiParam(name = "shopId", value = "门店id", required = true) @RequestParam(value = "shopId") Long shopId) {
//        if (shopId == null) {
//            UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
//            if (userInfoInTokenBO != null) {
//                shopId = userInfoInTokenBO.getStoreId();
//            }
//        }
//        PageVO<LiveRoomGuideVO> result = liveRoomService.getAppLivePage(pageDTO, type, shopId);
//        return ServerResponseEntity.success(result);
//    }

}
