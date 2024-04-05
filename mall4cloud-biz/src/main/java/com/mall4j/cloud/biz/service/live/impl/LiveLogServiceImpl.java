
package com.mall4j.cloud.biz.service.live.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.mapper.live.LiveLogMapper;
import com.mall4j.cloud.biz.model.live.LiveInterfaceType;
import com.mall4j.cloud.biz.model.live.LiveLog;
import com.mall4j.cloud.biz.dto.live.LiveUsableNumParam;
import com.mall4j.cloud.biz.service.live.LiveLogService;
import com.mall4j.cloud.common.exception.LuckException;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 *
 * @author lhd
 * @date 2020-08-12 16:05:26
 */
@Service
@AllArgsConstructor
public class LiveLogServiceImpl extends ServiceImpl<LiveLogMapper, LiveLog> implements LiveLogService {

    private final LiveLogMapper liveLogMapper;

    /**
     * 校验商家和平台可用次数并保存日志
     * @param shopId 店铺id
     * @param type 校验类型
     */
    @Override
    public void checkNumsAndSaveLog(Long shopId, LiveInterfaceType type, String desc) {
        List<LiveLog> liveLogs = list(new LambdaQueryWrapper<LiveLog>()
                .eq(LiveLog::getType,type.value())
                .le(LiveLog::getCreateTime, DateUtil.endOfDay(new Date()))
                .ge(LiveLog::getCreateTime,DateUtil.beginOfDay(new Date())));
        int platformNum = 0;
        int shopNum = 0;
        LiveLog liveLogDb = null;
        if(CollectionUtils.isNotEmpty(liveLogs)) {
            for (LiveLog liveLog : liveLogs) {
                if (Objects.equals(liveLog.getShopId(),shopId)) {
                    shopNum = liveLog.getNum();
                    liveLogDb = liveLog;
                }
                platformNum += liveLog.getNum();
            }
        }
        String message = "可用次数已达上限";
        String platformMsg = "平台今日";
        String shopMsg = "店铺今日";
        // 如果次数大于平台每日上限则抛出异常
        if(platformNum + 1 > type.getNumLimit()){
            throw new LuckException(platformMsg + desc+message);
        }

        // 校验商家今日可用次数
        if(shopNum + 1 > type.getShopNumLimit()){
            throw new LuckException(shopMsg + desc+message);
        }

        // 保存or修改日志
        if(Objects.isNull(liveLogDb)){
            LiveLog liveLog = new LiveLog();
            liveLog.setCreateTime(new Date());
            liveLog.setNum(1);
            liveLog.setShopId(shopId);
            liveLog.setType(type.value());
            save(liveLog);
        }else{
            liveLogDb.setNum(liveLogDb.getNum() + 1);
            updateById(liveLogDb);
        }
    }

    /**
     * 返回商家和平台可用次数
     * @param shopId 店铺id
     * @param type 校验类型
     */
    @Override
    public LiveUsableNumParam getLiveNum(Long shopId, LiveInterfaceType type) {
        List<LiveLog> liveLogs = list(new LambdaQueryWrapper<LiveLog>()
                .eq(LiveLog::getType,type.value())
                .le(LiveLog::getCreateTime, DateUtil.endOfDay(new Date()))
                .ge(LiveLog::getCreateTime,DateUtil.beginOfDay(new Date())));
        LiveUsableNumParam liveUsableNumParam = new LiveUsableNumParam();
        int platformNum = 0;
        int shopNum = 0;
        if(CollectionUtils.isNotEmpty(liveLogs)){
            for (LiveLog liveLog : liveLogs) {
                if (Objects.equals(liveLog.getShopId(), shopId)) {
                    shopNum += liveLog.getNum();
                }
                platformNum += liveLog.getNum();
            }
        }
        liveUsableNumParam.setShopNum(type.getShopNumLimit() - shopNum);
        liveUsableNumParam.setTotalNum(type.getNumLimit() - platformNum);
        return liveUsableNumParam;
    }

}
