package com.mall4j.cloud.group.controller.draw.app;


import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.model.LotteryDrawActivityAwardRecord;
import com.mall4j.cloud.group.service.LotteryDrawActivityBizService;
import com.mall4j.cloud.group.vo.LotteryDrawActivityVO;
import com.mall4j.cloud.group.vo.app.DrawAwardVO;
import com.mall4j.cloud.group.vo.app.DrawPrizeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/lottery_draw")
@Api(tags = "app-抽奖活动")
public class LotteryDrawAppController {
    @Resource
    private LotteryDrawActivityBizService lotteryDrawActivityBizService;

    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据id查询抽奖活动详情", notes = "根据id查询抽奖活动详情")
    public ServerResponseEntity<LotteryDrawActivityVO> info(@PathVariable Integer id){
        return lotteryDrawActivityBizService.detail(id);
    }

    @PostMapping("/draw/{id}")
    @ApiOperation(value = "抽奖", notes = "抽奖")
    public ServerResponseEntity<DrawPrizeVO> draw(@PathVariable Integer id,
                                                  @RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId){
        return lotteryDrawActivityBizService.draw(id,storeId);
    }

    @PostMapping("/award/{id}")
    @ApiOperation(value = "该活动中奖记录", notes = "该活动中奖记录")
    public ServerResponseEntity<List<DrawAwardVO>> drawAward(@PathVariable Integer id){
        return lotteryDrawActivityBizService.drawAward(id);
    }

    @PostMapping("/draw/score/{id}")
    @ApiOperation(value = "抽奖消耗积分多少", notes = "抽奖消耗积分多少")
    public ServerResponseEntity<Integer> drawScore(@PathVariable Integer id){
        return lotteryDrawActivityBizService.drawScore(id);
    }

    @PostMapping("/my/award/{id}")
    @ApiOperation(value = "查看我的奖品", notes = "查看我的奖品")
    public ServerResponseEntity<List<DrawAwardVO>> myAward(@PathVariable Integer id){
        return lotteryDrawActivityBizService.myAward(id);
    }

    @PostMapping("/my/award/updateRecord")
    @ApiOperation(value = "实物奖品填写发货地址", notes = "实物奖品填写发货地址")
    public ServerResponseEntity<Void> updateRecord(@RequestBody LotteryDrawActivityAwardRecord lotteryDrawActivityAwardRecord){
        lotteryDrawActivityBizService.updateRecord(lotteryDrawActivityAwardRecord);
        return ServerResponseEntity.success();
    }

    @PostMapping("/share/{id}")
    @ApiOperation(value = "抽奖分享", notes = "抽奖分享")
    public ServerResponseEntity<Void> share(@PathVariable Integer id){
        return lotteryDrawActivityBizService.share(id);
    }
}
