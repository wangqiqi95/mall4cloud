package com.mall4j.cloud.user.controller.platform.scoreConvert;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.constant.scoreConvert.ScoreConvertStatusEnum;
import com.mall4j.cloud.user.dto.scoreConvert.*;
import com.mall4j.cloud.user.service.scoreConvert.ScoreBarterService;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreBarterListVO;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreBarterLogVO;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreBarterVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


/**
 * 积分换物
 * @author shijing
 */
@RestController("platformScoreConvertController")
@RequestMapping("/p/score/barter")
@Api(tags = "platform-积分换物")
public class ScoreBarterController {

    @Resource
    private ScoreBarterService scoreBarterService;

    @PostMapping("/list")
    @ApiOperation(value = "积分换物列表")
    public ServerResponseEntity<PageInfo<ScoreBarterListVO>> list(@RequestBody ScoreConvertListDTO param){
        return scoreBarterService.list(param);
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增积分换物")
    public ServerResponseEntity<Void> add(@RequestBody ScoreBarterSaveDTO param){
        return scoreBarterService.save(param);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改积分换物")
    public ServerResponseEntity<Void> update(@RequestBody ScoreBarterUpdateDTO param){
        return scoreBarterService.update(param);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "查看详情")
    public ServerResponseEntity<ScoreBarterVO> detail(@RequestParam Long id){
        return scoreBarterService.selectDetail(id);
    }

    @DeleteMapping
    @ApiOperation(value = "删除活动")
    public ServerResponseEntity<Void> delete(@RequestParam Long id){
        return scoreBarterService.deleteConvert(id);
    }

    @GetMapping("/enable")
    @ApiOperation(value = "启用活动")
    public ServerResponseEntity<Void> enable(@RequestParam Long id){
        return scoreBarterService.updateConvertStatus(id, ScoreConvertStatusEnum.ENABLE.value());
    }

    @GetMapping("/disable")
    @ApiOperation(value = "禁用活动")
    public ServerResponseEntity<Void> disable(@RequestParam Long id){
        return scoreBarterService.updateConvertStatus(id, ScoreConvertStatusEnum.DISABLE.value());
    }

    @GetMapping("/addInventory")
    @ApiOperation(value = "增加库存")
    public ServerResponseEntity<Void> addInventory(@RequestParam Long id, @RequestParam Long num){
        return scoreBarterService.addInventory(id,num);
    }

    @PostMapping("/logList")
    @ApiOperation(value = "兑换记录列表")
    public ServerResponseEntity<PageInfo<ScoreBarterLogVO>> logList(@RequestBody ScoreBarterLogListDTO param){
        return scoreBarterService.logList(param);
    }

    @PostMapping("/addCourierCode")
    @ApiOperation(value = "添加物流编号")
    public ServerResponseEntity<Void> addCourierCode(@RequestBody AddCourierCodeDTO param){
        return scoreBarterService.addCourierCode(param.getId(),param.getCourierCode(),param.getNote());
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出兑换记录")
    public void export(@RequestBody ScoreBarterLogListDTO param, HttpServletResponse response){
        scoreBarterService.export(param,response);
    }

}
