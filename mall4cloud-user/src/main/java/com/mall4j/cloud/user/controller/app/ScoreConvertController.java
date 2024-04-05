package com.mall4j.cloud.user.controller.app;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.service.scoreConvert.ScoreCouponService;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreConvertAppListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ty
 * @ClassName ScoreConvertController
 * @description
 * @date 2022/10/13 13:56
 */
@RestController("appScoreConvertController")
@RequestMapping("/user_score_convert")
@Api(tags = "app-积分清零活动")
public class ScoreConvertController {

    @Autowired
    private ScoreCouponService scoreCouponService;

    @GetMapping("/selectScoreConvertList")
    @ApiOperation(value = "查询当前会员积分所能匹配的活动")
    public ServerResponseEntity<ScoreConvertAppListVO> selectScoreConvertList(@RequestParam Long userScore, @RequestParam(value = "convertType", defaultValue = "0", required = false) Integer convertType, @RequestParam Long shopId) {
        return scoreCouponService.selectScoreConvertList(userScore, convertType, shopId);
    }

}
