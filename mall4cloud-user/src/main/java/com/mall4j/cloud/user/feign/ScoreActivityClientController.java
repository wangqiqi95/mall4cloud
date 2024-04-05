package com.mall4j.cloud.user.feign;

import cn.hutool.core.bean.BeanUtil;
import com.mall4j.cloud.api.user.feign.ScoreActivityClient;
import com.mall4j.cloud.api.user.vo.ScoreConvertVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.model.scoreConvert.ScoreConvert;
import com.mall4j.cloud.user.service.scoreConvert.ScoreCouponService;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreCouponVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author shijing
 * @date 2022-03-23 14:14:36
 */
@RestController
public class ScoreActivityClientController implements ScoreActivityClient {

    @Autowired
    private ScoreCouponService scoreCouponService;

    @Override
    public ServerResponseEntity<List<Long>> getShops(Long activityId) {
        return scoreCouponService.getShops(activityId);
    }

    @Override
    public ServerResponseEntity<List<ScoreConvertVO>> checkScoreConvertByCoupon(Long couponId) {
        return scoreCouponService.checkScoreConvertByCoupon(couponId);
    }

    @Override
    public ServerResponseEntity<ScoreConvertVO> getConvert(Long convertId) {
        return scoreCouponService.getScoreConvertVO(convertId);
    }

    @Override
    public ServerResponseEntity<List<Long>> getCouponIdListByConvertId(Long convertId) {
        return scoreCouponService.getCouponIdListByConvertId(convertId);
    }

    @Override
    public ServerResponseEntity<ScoreConvertVO> getScoreConvertAndCouponList(Long convertId) {
        return scoreCouponService.getScoreConvertAndCouponList(convertId);
    }
}
