package com.mall4j.cloud.user.controller.platform;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.ReqScoreTimeDiscountActivityDTO;
import com.mall4j.cloud.user.dto.ScoreTimeDiscountActivityDTO;
import com.mall4j.cloud.user.service.ScoreTimeDiscountActivityService;
import com.mall4j.cloud.user.vo.ScoreTimeDiscountActivityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 积分限时折扣
 *
 * @author gmq
 * @date 2022-07-11 15:11:39
 */
@RestController("multishopScoreTimeDiscountActivityController")
@RequestMapping("/p/score/time/discount/activity")
@Api(tags = "积分限时折扣")
public class ScoreTimeDiscountActivityController {

    @Autowired
    private ScoreTimeDiscountActivityService scoreTimeDiscountActivityService;



	@GetMapping("/page")
	@ApiOperation(value = "获取积分限时折扣列表", notes = "分页获取积分限时折扣列表")
	public ServerResponseEntity<PageVO<ScoreTimeDiscountActivityVO>> page(@Valid ReqScoreTimeDiscountActivityDTO pageDTO) {
		PageVO<ScoreTimeDiscountActivityVO> scoreTimeDiscountActivityPage = scoreTimeDiscountActivityService.pageData(pageDTO);
		return ServerResponseEntity.success(scoreTimeDiscountActivityPage);
	}

	@GetMapping
    @ApiOperation(value = "获取积分限时折扣", notes = "根据id获取积分限时折扣")
    public ServerResponseEntity<ScoreTimeDiscountActivityVO> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(scoreTimeDiscountActivityService.getDetailById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存积分限时折扣", notes = "保存积分限时折扣")
    public ServerResponseEntity<Void> save(@Valid @RequestBody ScoreTimeDiscountActivityDTO scoreTimeDiscountActivityDTO) {
        scoreTimeDiscountActivityService.saveTo(scoreTimeDiscountActivityDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新积分限时折扣", notes = "更新积分限时折扣")
    public ServerResponseEntity<Void> update(@Valid @RequestBody ScoreTimeDiscountActivityDTO scoreTimeDiscountActivityDTO) {
        if(Objects.isNull(scoreTimeDiscountActivityDTO.getId())){
            return ServerResponseEntity.showFailMsg("参数id不能为空");
        }
        if(Objects.isNull(scoreTimeDiscountActivityService.getById(scoreTimeDiscountActivityDTO.getId()))){
            return ServerResponseEntity.showFailMsg("未获取到实体数据");
        }
        scoreTimeDiscountActivityService.updateTo(scoreTimeDiscountActivityDTO);
        return ServerResponseEntity.success();
    }

    @GetMapping("/openStatus")
    @ApiOperation(value = "启用", notes = "启用")
    public ServerResponseEntity<Void> openStatus(@RequestParam Long id) {
        return scoreTimeDiscountActivityService.openOrClose(id,1);
    }

    @GetMapping("/closeStatus")
    @ApiOperation(value = "禁用", notes = "openStatus")
    public ServerResponseEntity<Void> closeStatus(@RequestParam Long id) {
        return scoreTimeDiscountActivityService.openOrClose(id,0);
    }

    @DeleteMapping
    @ApiOperation(value = "删除积分限时折扣", notes = "根据积分限时折扣id删除积分限时折扣")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        scoreTimeDiscountActivityService.deleteToById(id);
        return ServerResponseEntity.success();
    }
}
