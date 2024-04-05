package com.mall4j.cloud.user.controller.app;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.api.platform.vo.ScoreExpireConfigVO;
import com.mall4j.cloud.common.constant.ConfigNameConstant;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.user.constant.ScoreIoTypeEnum;
import com.mall4j.cloud.user.constant.ScoreLogTypeEnum;
import com.mall4j.cloud.user.model.UserExtension;
import com.mall4j.cloud.user.model.UserScoreGetLog;
import com.mall4j.cloud.user.model.UserScoreLog;
import com.mall4j.cloud.user.service.UserExtensionService;
import com.mall4j.cloud.user.service.UserLevelService;
import com.mall4j.cloud.user.service.UserScoreGetLogService;
import com.mall4j.cloud.user.service.UserScoreLogService;
import com.mall4j.cloud.user.vo.ScoreCompleteConfigVO;
import com.mall4j.cloud.user.vo.ScoreDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * 积分中心接口
 *
 * @author LHD
 * @date 2019-3-18 10:27:46
 */
@RestController
@RequestMapping("/score")
@Api(tags = "app-积分中心接口")
public class UserScoreController {

    @Autowired
    private ConfigFeignClient configFeignClient;

    @Autowired
    private UserScoreLogService scoreLogService;

    @Autowired
    private UserScoreGetLogService userScoreGetLogService;

    @Autowired
    private UserExtensionService userExtensionService;

    @Autowired
    private UserLevelService userLevelService;

    @GetMapping("/update_user_score")
    @ApiOperation(value = "积分签到", notes = "积分签到")
    public ServerResponseEntity<String> updateUserScore() {
        Long userId = AuthUserContext.get().getUserId();
        UserExtension extension = userExtensionService.getByUserId(userId);
        String scoreConfigValue = configFeignClient.getConfig(ConfigNameConstant.SCORE_CONFIG).getData();
        ScoreCompleteConfigVO scoreParam = Json.parseObject(scoreConfigValue, ScoreCompleteConfigVO.class);
        //签到，计算连续签到日期
        if (isSignIn(userId,DateUtil.date())) {
            // 今天已经签到过了，请明天再试!
            return ServerResponseEntity.success("今天已经签到过了，请明天再试!");
        }
        //根据逗号分隔
        String[] signInScore = scoreParam.getSignInScoreString().split(StrUtil.COMMA);
        int count = Math.min(extension.getSignDay(),signInScore.length - 1);
        long score = Long.parseLong(signInScore[count]);
        // 查询昨天有没有签到,没有则初始化为1
        if(isSignIn(userId,DateUtil.offsetDay(DateUtil.date(),-1))) {
            extension.setSignDay(extension.getSignDay() + 1);
        }else{
            extension.setSignDay(1);
        }
//        String remarks = "签到第" + count + "天获取的积分";
        updateUserScoreBySign(score, ScoreIoTypeEnum.INCOME.value(),extension);
        // 领取积分成功
        return ServerResponseEntity.success("领取积分成功");
    }

    @GetMapping("/score_info")
    @ApiOperation(value = "查看积分中心信息", notes = "查看积分中心信息")
    public ServerResponseEntity<ScoreDataVO> scoreInfo() {
        ScoreDataVO scoreDataVO = new ScoreDataVO();
        Long userId = AuthUserContext.get().getUserId();
        String scoreParam = configFeignClient.getConfig(ConfigNameConstant.SCORE_CONFIG).getData();
        ScoreCompleteConfigVO scoreParamConfigVO = Json.parseObject(scoreParam, ScoreCompleteConfigVO.class);
        String scoreExpireParam = configFeignClient.getConfig(ConfigNameConstant.SCORE_EXPIRE).getData();
        //计算过期时间
        ScoreExpireConfigVO scoreExpireParamConfigVO = Json.parseObject(scoreExpireParam, ScoreExpireConfigVO.class);
        Integer expireYear = Objects.isNull(scoreExpireParamConfigVO) ? 0 : scoreExpireParamConfigVO.getExpireYear();
        ArrayList<Integer> signInScores = new ArrayList<>();
        for (String s : scoreParamConfigVO.getSignInScoreString().trim().split(StrUtil.COMMA)) {
            Integer signInScore = Integer.valueOf(s);
            signInScores.add(signInScore);
        }
        UserExtension extension = userExtensionService.getByUserId(userId);
        // 查询连续签到天数大于0并且没有连续签到初始化为0
        boolean isContinuousSign = !isSignIn(userId,DateUtil.offsetDay(DateUtil.date(),-1)) && !isSignIn(userId,DateUtil.date());
        if(isContinuousSign && extension.getSignDay() != 0) {
            extension.setSignDay(0);
            userExtensionService.update(extension);
        }
        UserScoreGetLog scoreLog = userScoreGetLogService.getLogByUserId(userId);
        scoreDataVO.setExpireScore(Objects.nonNull(scoreLog) ? scoreLog.getUsableScore() : 0);
        scoreDataVO.setExpireYear(expireYear);
        scoreDataVO.setScoreExpireSwitch(scoreExpireParamConfigVO.getScoreExpireSwitch());
        scoreDataVO.setIsRegister(1);
        scoreDataVO.setScoreList(signInScores);
        scoreDataVO.setRegisterScore(scoreParamConfigVO.getRegisterScore());
        scoreDataVO.setShoppingScoreSwitch(scoreParamConfigVO.getShoppingScoreSwitch());
        scoreDataVO.setShoppingGetScore(scoreParamConfigVO.getShoppingGetScore());
        scoreDataVO.setIsSignIn(isSignIn(userId,DateUtil.date()) ? 1 : 0);
        scoreDataVO.setLevelType(extension.getLevelType());
        Long score = extension.getScore();
        scoreDataVO.setScore(score);
        scoreDataVO.setLevelName(userLevelService.getOneByTypeAndLevel(extension.getLevelType(), extension.getLevel()).getLevelName());
        int count = extension.getSignDay() <= 0 ? 1 : extension.getSignDay();
        // 如果不为第一天签到或者大于等于第七天的签到，+1
        if (extension.getSignDay() != 0 && !isSignIn(userId,DateUtil.date())) {
            count++;
        }
        // 连续签到天数
        scoreDataVO.setSignInCount(count);
        //根据逗号分隔
        String[] signInScore = scoreParamConfigVO.getSignInScoreString().split(StrUtil.COMMA);
        // 当天签到可领的积分
        int curScore;
        if (scoreDataVO.getIsSignIn() == 1) {
            count = Math.min(count,signInScore.length - 1);
            curScore = Integer.parseInt(signInScore[count]);
        }else{
            count = Math.min(count,signInScore.length);
            curScore = Integer.parseInt(signInScore[count - 1]);
        }
        scoreDataVO.setCurScore(curScore);
        return ServerResponseEntity.success(scoreDataVO);
    }

    private void updateUserScoreBySign(Long score, Integer ioType, UserExtension extension) {
        if (score == null) {
            return;
        }
        UserScoreLog scoreLog = new UserScoreLog();
        scoreLog.setSource(ScoreLogTypeEnum.SIGN_IN.value());
        scoreLog.setCreateTime(new Date());
        scoreLog.setIoType(ScoreIoTypeEnum.INCOME.value());
        //添加一条日志和修改用户积分
        scoreLog.setUserId(extension.getUserId());
        scoreLog.setScore(score);
        scoreLog.setBizId(1L);
        scoreLogService.save(scoreLog);
        if (ioType == 1) {
            extension.setScore(extension.getScore() + score);
            // 添加积分明细
            UserScoreGetLog addDetail = new UserScoreGetLog();
            addDetail.setCreateTime(new Date());
            addDetail.setStatus(1);
            addDetail.setUserId(extension.getUserId());
            addDetail.setUsableScore(score);
            userScoreGetLogService.save(addDetail);
        } else {
            extension.setScore(extension.getScore() - score);
        }
        userExtensionService.update(extension);
    }

    /**
     * 获取数据库的签到记录记录
     *
     * @param userId
     * @return
     */
    private Integer getCount(Long userId) {
        Integer count = scoreLogService.getConsecutiveDays(userId);
        //如果没有记录就是第一天签到
        if (count == null) {
            count = 0;
        }
        return count;
    }

    /**
     * 是否已经签到
     *
     * @param userId
     * @return
     */
    private boolean isSignIn(Long userId,Date date) {
        Integer count = scoreLogService.countByUserIdAndDateTimeAndType(
                ScoreLogTypeEnum.SIGN_IN.value(), userId, DateUtil.beginOfDay(date), DateUtil.endOfDay(date));
        return Objects.nonNull(count) && count > 0;
    }

//
//    @GetMapping("/scoreInfo")
//    @ApiOperation(value = "查看积分中心信息", notes = "查看积分中心信息")
//    public ResponseEntity<ScoreDataVO> scoreInfo() {
//        ScoreDataVO ScoreDataVO = new ScoreDataVO();
//        String userId = SecurityUtils.getUser().getUserId();
//        ScoreConfigParam scoreParam = sysConfigService.getSysConfigObject(Constant.SCORE_CONFIG, ScoreConfigParam.class);
//        // 0.计算过期时间
//        ScoreExpireParam scoreExpireParam = sysConfigService.getSysConfigObject(Constant.SCORE_EXPIRE, ScoreExpireParam.class);
//        if (Objects.isNull(scoreParam)) {
//            return ResponseEntity.ok().build();
//        }
//        Integer year = Objects.isNull(scoreExpireParam) ? 0 : scoreExpireParam.getExpireYear();
//        ArrayList<Integer> signInScores = new ArrayList<>();
//        for (String s : scoreParam.getSignInScoreString().trim().split(StrUtil.COMMA)) {
//            Integer signInScore = Integer.valueOf(s);
//            signInScores.add(signInScore);
//        }
//        UserExtension userExtension = userExtensionService.getOne(new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, userId));
//        if (userExtension == null) {
//            return ResponseEntity.ok().build();
//        }
//        UserScoreGetLog userScoreDetail = userScoreGetLogService.getOne(new LambdaQueryWrapper<UserScoreGetLog>().eq(UserScoreGetLog::getUserId, userId)
//                .eq(UserScoreGetLog::getStatus, -1).orderByDesc(UserScoreGetLog::getExpireTime).last("limit 1"));
//        ScoreDataVO.setIsRegister(1);
//        ScoreDataVO.setExpireScore(Objects.nonNull(userScoreDetail) ? userScoreDetail.getUsableScore() : 0);
//        ScoreDataVO.setExpireYear(year);
//        ScoreDataVO.setGrowth(userExtension.getGrowth());
//        ScoreDataVO.setScore(userExtension.getScore());
//        ScoreDataVO.setScoreList(signInScores);
//        ScoreDataVO.setRegisterScore(scoreParam.getRegisterScore());
//        ScoreDataVO.setShopScore(scoreParam.getShopGetScore());
//        ScoreDataVO.setIsSignIn(isSignIn(userId) ? 1 : 0);
//        ScoreDataVO.setLevelType(userExtension.getLevelType());
//        UserLevel userLevel = userLevelService.getOne(new LambdaQueryWrapper<UserLevel>()
//                .eq(UserLevel::getLevel, userExtension.getLevel())
//                .eq(UserLevel::getLevelType, userExtension.getLevelType())
//        );
//        ScoreDataVO.setLevelName(userLevel.getLevelName());
//        //计算签到天数
//        Integer count = scoreLogService.getConsecutiveDays(userId);
////        count = Math.min(7,count);
//        // 如果不为第一天签到或者大于等于第七天的签到，+1
////        if(count >= 0 && count < 7 && !isSignIn(userId)){
////            count++;
////        }
//        if (count >= 0 && !isSignIn(userId)) {
//            count++;
//        }
//        ScoreDataVO.setSignInCount(count);
//        return ResponseEntity.ok(ScoreDataVO);
//    }
//
//
//    /**
//     * 分页查询积分明细
//     *
//     * @param page 分页对象
//     * @return 分页数据
//     */
//    @GetMapping("/page")
//    @ApiOperation(value = "查询积分明细", notes = "查询积分明细")
//    public ResponseEntity<IPage<UserScoreLog>> getScoreLogPage(PageParam<UserScoreLog> page) {
//        String userId = SecurityUtils.getUser().getUserId();
//        return ResponseEntity.ok(scoreLogService.page(page, new LambdaQueryWrapper<UserScoreLog>()
//                .eq(UserScoreLog::getUserId, userId).orderByDesc(UserScoreLog::getCreateTime)));
//    }
//
//
//    /**
//     * 等级页展示
//     *
//     * @return 等级页展示
//     */
//    @GetMapping("/getLevelShow")
//    @ApiOperation(value = "等级页展示", notes = "等级页展示")
//    public ResponseEntity<String> getLevelShow() {
//        String config = sysConfigService.getSysConfigObject(Constant.LEVEL_SHOW, String.class);
//        if (Objects.isNull(config)) {
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.ok(config);
//    }
//
//    /**
//     * 积分常见问题
//     *
//     * @return 积分常见问题
//     */
//    @GetMapping("/getScoreQuestion")
//    @ApiOperation(value = "积分常见问题", notes = "积分常见问题")
//    public ResponseEntity<SysConfig> getScoreQuestion() {
//        SysConfig config = sysConfigService.getOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getParamKey, Constant.SCORE_QUESTION));
//        if (Objects.isNull(config)) {
//            return ResponseEntity.ok(new SysConfig());
//        }
//        return ResponseEntity.ok(config);
//    }
    /**
     * 配置信息
     */
    @GetMapping("/score_config_info/{id}")
    @ApiOperation(value = "查看积分成长值配置信息", notes = "常见问题：SCORE_QUESTION；等级页展示：LEVEL_SHOW；成长值说明：MEMBER_GROWTH_DESCRIPTION")
    public ServerResponseEntity<String> info(@PathVariable("id") String key){
        String config = configFeignClient.getConfig(key).getData();
        if(Objects.isNull(config)){
            return ServerResponseEntity.success(null);
        }
        return ServerResponseEntity.success(config);
    }

}
