package com.mall4j.cloud.user.service.scoreConvert;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.user.vo.ScoreConvertVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.scoreConvert.*;
import com.mall4j.cloud.user.model.scoreConvert.ScoreConvert;
import com.mall4j.cloud.user.model.scoreConvert.ScoreCouponLog;
import com.mall4j.cloud.user.vo.scoreConvert.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 积分兑换
 *
 * @author shijing
 */

public interface ScoreCouponService extends IService<ScoreConvert> {

    /**
     * 积分换券列表
     * @param param 查询信息
     */
    ServerResponseEntity<PageInfo<ScoreCouponListVO>> list(ScoreConvertListDTO param);

    /**
     * 保存换券活动
     * @param param 新增信息
     */
    ServerResponseEntity<Void> save(ScoreCouponSaveDTO param);

    /**
     * 修改换券活动
     * @param param 修改信息
     */
    ServerResponseEntity<Void> update(ScoreCouponUpdateDTO param);

    /**
     * 换券活动详情
     * @param convertId 主键id
     * @return ScoreConvert 积分兑换详情
     */
    ServerResponseEntity<ScoreCouponVO> selectDetail(Long convertId);

    /**
     * 获取禁用手机号
     * @param file 导入文件
     */
    ServerResponseEntity<List<String>> importPhoneNum(MultipartFile file);

    /**
     * 禁用人员名单
     * @param param 主键id
     */
    ServerResponseEntity<PageInfo<String>> phoneNumList(PhoneNumListDTO param);

    /**
     * 删除禁用人员
     */
    ServerResponseEntity<Void> deletePhone(Long id,String phoneNum);


    /**
     * 批量删除禁用人员
     */
    ServerResponseEntity<Void> deleteBatchPhone(Long id,List<String> phoneNum);

    /**
     * 兑换记录列表
     * @param param 兑换列表搜索参数
     */
    ServerResponseEntity<PageInfo<ScoreCouponLogVO>> logList(ScoreBarterLogListDTO param);

    ServerResponseEntity<Void> addLogistics(@RequestBody ScoreLogDTO param);

    ServerResponseEntity<Void> confirmExprot(List<Long> ids);

    /**
     * 导出兑换记录
     * @param param 兑换列表搜索参数
     */
    void export(ScoreBarterLogListDTO param, HttpServletResponse response);

    ServerResponseEntity<Map<String,Integer>> importLog(@RequestParam("file") MultipartFile file);

    /**
     * 积分商城首页（商城小程序）
     * @param shopId 当前门店id
     */
    ServerResponseEntity<ScoreMallHomeVO> listForApp(Long shopId);

    ServerResponseEntity<List<ScoreBannerUrlDTO>> bannerData(Long shopId,Integer type);

    /**
     * 积分兑换详情页（商城小程序）
     * @param id 积分活动id
     */
    ServerResponseEntity<ScoreCouponAppDetailVO> detailForApp(Long id);

    ServerResponseEntity<Boolean> activityFlag(Long id,Long storeId);

    /**
     * 积分兑换（商城小程序）
     * @param id 积分活动id
     */
    ServerResponseEntity<Void> userConvert(Long id,Long storeId,String userAddr,String phone,
                                           String userName);
    /**
     * 积分兑换记录（商城小程序）
     * @param
     */
    ServerResponseEntity<PageInfo<UserCouponLogVO>> userConvertLog(Integer pageNo,Integer pageSize, Short type);

    /**
     * 开启积分上新提醒（商城小程序）
     * @param
     */
    ServerResponseEntity<Void> enableNotice(String formId);

    /**
     * 关闭积分上新提醒（商城小程序）
     * @param
     */
    ServerResponseEntity<Void> disableNotice(String formId);

    /**
     * 开启积分券过期提醒（商城小程序）
     * @param
     */
    ServerResponseEntity<Void> enableCouponNotice(String formId,Long activityId);

    /**
     * 关闭积分券过期提醒（商城小程序）
     * @param
     */
    ServerResponseEntity<Void> disableCouponNotice(String formId,Long activityId);

    /**
     * 根据活动id获取门店
     * @param
     */
    ServerResponseEntity<List<Long>> getShops(Long activityId);

    /**
     * 积分清零活动列表查询（商城小程序）
     * @param userScore     用户个人所剩积分
     * @param convertType   兑换活动种类
     * @param shopId   用户所在门店ID
     * @return
     */
    ServerResponseEntity<ScoreConvertAppListVO> selectScoreConvertList(Long userScore, Integer convertType, Long shopId);


    ServerResponseEntity<List<ScoreConvertVO>> checkScoreConvertByCoupon(Long couponId);

    ServerResponseEntity<ScoreConvertVO> getScoreConvertVO(Long convertId);

    ServerResponseEntity<Integer> isToShop(Long couponId);

    ServerResponseEntity<List<Long>> getCouponIdListByConvertId(Long convertId);

    ServerResponseEntity<ScoreConvertVO> getScoreConvertAndCouponList(Long convertId);
}
