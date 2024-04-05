package com.mall4j.cloud.user.controller.platform;

import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.constant.LevelTypeEnum;
import com.mall4j.cloud.user.dto.UserAdminDTO;
import com.mall4j.cloud.user.dto.UserLevelDTO;
import com.mall4j.cloud.user.service.UserLevelService;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.vo.UserLevelVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 会员等级表
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@RestController("platformUserLevelController")
@RequestMapping("/p/user_level")
@Api(tags = "平台-会员等级表")
public class UserLevelController {

    @Autowired
    private UserLevelService userLevelService;
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    @ApiOperation(value = "获取会员等级表列表", notes = "分页获取会员等级表列表")
    public ServerResponseEntity<List<UserLevelVO>> list(@RequestParam("levelType") Integer levelType) {
        List<UserLevelVO> userLevels = userLevelService.list(levelType);
        return ServerResponseEntity.success(userLevels);
    }

    @GetMapping
    @ApiOperation(value = "获取会员等级表", notes = "根据id获取会员等级表")
    public ServerResponseEntity<UserLevelVO> getById(@RequestParam Long userLevelId) {
        UserLevelVO userLevelVO = userLevelService.getByUserLevelId(userLevelId);
        return ServerResponseEntity.success(userLevelVO);
    }

//    @PostMapping
//    @ApiOperation(value = "保存会员等级表", notes = "保存会员等级表")
//    public ServerResponseEntity<Void> save(@Valid @RequestBody UserLevelDTO userLevelDTO) {
//        userLevelService.save(userLevelDTO);
//        return ServerResponseEntity.success();
//    }

    @PutMapping
    @ApiOperation(value = "更新会员等级表", notes = "更新会员等级表")
    public ServerResponseEntity<Void> saveOrUpdate(@Valid @RequestBody UserLevelDTO userLevelDTO) {
        if(userLevelDTO.getLevelType() == 1 && userLevelDTO.getUserLevelTerms().size() == 0){
            return ServerResponseEntity.showFailMsg("等级期数表不能为空");
        }
        if (userLevelDTO.getLevelType() == 1) {
            //付费会员没有成长值限制
            userLevelDTO.setNeedGrowth(0);
        }
        userLevelService.saveOrUpdate(userLevelDTO);
        userLevelService.removeLevelCache(userLevelDTO.getUserLevelId(),userLevelDTO.getLevelType(),userLevelDTO.getLevel());
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除会员等级表", notes = "根据会员等级表id删除会员等级表")
    public ServerResponseEntity<Void> delete(@RequestParam Long userLevelId) {
        UserLevelVO userLevelVO = userLevelService.getByUserLevelId(userLevelId);
        if (Objects.equals(userLevelVO.getLevelType(), LevelTypeEnum.PAY_USER.value())){
            Integer userNum = userService.countUserByLevel(userLevelVO.getLevel(), userLevelVO.getLevelType());
            if (userNum > 0){
                //如果有用户是该付费会员等级，则不允许删除该等级
                throw new LuckException("存在属于该等级的用户，该等级无法删除！");
            }
        }
        userLevelService.deleteByUserLevelId(userLevelId);
        userLevelService.removeLevelCache(userLevelVO.getUserLevelId(),userLevelVO.getLevelType(),userLevelVO.getLevel());
        return ServerResponseEntity.success();
    }

    @PutMapping("/recruit_status")
    @ApiOperation(value = "付费会员，是否可以招募会员；1可以招募，0停止招募", notes = "付费会员，是否可以招募会员；1可以招募，0停止招募")
    public ServerResponseEntity<Void> updateRecruitStatus(@Valid @RequestBody UserLevelDTO userLevelDTO) {
        Long userLevelId = userLevelDTO.getUserLevelId();
        if (!Objects.equals(userLevelDTO.getLevelType(), LevelTypeEnum.PAY_USER.value())){
            // 非付费会员，不可以使用招募
            throw new LuckException("非付费会员，不可以使用招募！");
        }
        userLevelService.updateRecruitStatus(userLevelDTO);
        userLevelService.removeLevelCache(userLevelId,userLevelDTO.getLevelType(),userLevelDTO.getLevel());
        return ServerResponseEntity.success();
    }

    @PutMapping("/update_user_level")
    @ApiOperation(value = "新增/更新用户会员等级", notes = "新增/更新用户会员等级")
    public ServerResponseEntity<Void> updateUserLevel() {
        userLevelService.updateUserLevel();
        userLevelService.removeLevelListCache(LevelTypeEnum.ORDINARY_USER.value());
        return ServerResponseEntity.success();
    }

    @PutMapping("/update_growth")
    @ApiOperation(value = "批量修改会员成长值", notes = "批量修改会员成长值")
    public ServerResponseEntity<Boolean> batchUserGrowth(@RequestBody @Valid UserAdminDTO userAdminDTO) {
        userLevelService.batchUpdateGrowth(userAdminDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("/batch_user_score")
    @ApiOperation(value = "批量修改会员积分", notes = "批量修改会员积分")
    public ServerResponseEntity<Boolean> batchUserScore(@RequestBody @Valid UserAdminDTO userAdminDTO) {
        userLevelService.batchUserScore(userAdminDTO);
        return ServerResponseEntity.success();
    }
}
