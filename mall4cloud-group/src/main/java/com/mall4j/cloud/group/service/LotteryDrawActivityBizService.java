package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.*;
import com.mall4j.cloud.group.model.LotteryDrawActivityAwardRecord;
import com.mall4j.cloud.group.model.LotteryDrawActivityShop;
import com.mall4j.cloud.group.model.OpenScreenAdShop;
import com.mall4j.cloud.group.vo.*;
import com.mall4j.cloud.group.vo.app.DrawAwardVO;
import com.mall4j.cloud.group.vo.app.DrawPrizeVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface LotteryDrawActivityBizService {
    ServerResponseEntity<Integer> saveOrUpdateLotteryDrawActivity(LotteryDrawActivityDTO param);

    ServerResponseEntity<Void> saveOrUpdateLotteryDrawActivityPrize(LotteryDrawActivityPrizeAddDTO param);

    ServerResponseEntity<Void> saveOrUpdateLotteryDrawActivityGame(LotteryDrawActivityGameDTO param);

    ServerResponseEntity<LotteryDrawActivityVO> detail(Integer id);

    ServerResponseEntity<PageVO<LotteryDrawListVO>> page(LotteryDrawPageDTO param);

    ServerResponseEntity<Void> enable(Integer id);

    ServerResponseEntity<Void> disable(Integer id);

    ServerResponseEntity<Void> delete(Integer id);

    ServerResponseEntity<List<LotteryDrawActivityShop>> getActivityShop(Integer activityId);

    ServerResponseEntity<Void> addActivityShop(ModifyShopDTO param);

    ServerResponseEntity<Void> deleteActivityShop(Integer activityId, Integer shopId);

    ServerResponseEntity<Void> deleteAllShop(Integer activityId);

    ServerResponseEntity<LotteryDrawActivityCensusVO> census(Integer id);

    ServerResponseEntity<Void> stockChange(LotteryPrizeStockChangeDTO param);

    ServerResponseEntity<List<LotteryStockChangeLogVO>> stockChangeLog(Integer id);

    ServerResponseEntity<PageVO<LotteryAwardRecordVO>> awardRecord(LotteryAwardRecordListDTO param);

    ServerResponseEntity<DrawPrizeVO> draw(Integer id,Long storeId);

    ServerResponseEntity<List<DrawAwardVO>> drawAward(Integer id);

    ServerResponseEntity<Integer> drawScore(Integer id);

    ServerResponseEntity<List<DrawAwardVO>> myAward(Integer id);

    ServerResponseEntity<Void> updateRecord(LotteryDrawActivityAwardRecord lotteryDrawActivityAwardRecord);

    ServerResponseEntity<Void> updateLogistics(LotteryDrawActivityAwardRecord lotteryDrawActivityAwardRecord);

    ServerResponseEntity<Void> share(Integer id);

    void awardRecordExport(HttpServletResponse response, LotteryAwardRecordListDTO param);
}
