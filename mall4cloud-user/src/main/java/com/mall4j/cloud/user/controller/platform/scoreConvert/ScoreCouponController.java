package com.mall4j.cloud.user.controller.platform.scoreConvert;


import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.user.vo.SoldScoreCouponLogVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.constant.scoreConvert.ScoreConvertStatusEnum;
import com.mall4j.cloud.user.dto.scoreConvert.*;
import com.mall4j.cloud.user.service.scoreConvert.ScoreBarterService;
import com.mall4j.cloud.user.service.scoreConvert.ScoreCouponService;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreCouponListVO;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreCouponLogVO;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreCouponVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 积分换券
 * @author shijing
 */
@RestController("platformScoreCouponController")
@RequestMapping("/p/score/coupon")
@Api(tags = "platform-积分换券")
@Slf4j
public class ScoreCouponController {
    @Resource
    private ScoreBarterService scoreBarterService;

    @Resource
    private ScoreCouponService scoreCouponService;
    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @PostMapping("/list")
    @ApiOperation(value = "积分换券列表")
    public ServerResponseEntity<PageInfo<ScoreCouponListVO>> list(@RequestBody ScoreConvertListDTO param){
        return scoreCouponService.list(param);
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增换券活动")
    public ServerResponseEntity<Void> add(@RequestBody ScoreCouponSaveDTO param){
        return scoreCouponService.save(param);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改换券活动")
    public ServerResponseEntity<Void> update(@RequestBody ScoreCouponUpdateDTO param){
        return scoreCouponService.update(param);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "查看详情")
    public ServerResponseEntity<ScoreCouponVO> detail(@RequestParam Long id){
        return scoreCouponService.selectDetail(id);
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

    @PostMapping("/importPhoneNum")
    @ApiOperation(value = "导入禁用人员")
    public ServerResponseEntity<List<String>> importPhoneNum(MultipartFile file){
        return scoreCouponService.importPhoneNum(file);
    }

    @PostMapping("/phoneNumList")
    @ApiOperation(value = "禁用人员列表")
    public ServerResponseEntity<PageInfo<String>> phoneNumList(@RequestBody PhoneNumListDTO param){
        return scoreCouponService.phoneNumList(param);
    }

    @GetMapping("/deletePhone")
    @ApiOperation(value = "删除禁用人员")
    public ServerResponseEntity<Void> deletePhone(@RequestParam Long id, @RequestParam String phoneNum){
        return scoreCouponService.deletePhone(id,phoneNum);
    }

    @PostMapping("/deleteBatchPhone")
    @ApiOperation(value = "批量删除禁用人员")
    public ServerResponseEntity<Void> deleteBatchPhone(@RequestBody DeletePhoneDTO param){
        return scoreCouponService.deleteBatchPhone(param.getConvertId(), param.getPhoneNums());
    }

    @GetMapping("/addInventory")
    @ApiOperation(value = "增加库存")
    public ServerResponseEntity<Void> addInventory(@RequestParam Long id, @RequestParam Long num){
        return scoreBarterService.addInventory(id,num);
    }

    @PostMapping("/logList")
    @ApiOperation(value = "兑换记录列表")
    public ServerResponseEntity<PageInfo<ScoreCouponLogVO>> logList(@RequestBody ScoreBarterLogListDTO param){
        return scoreCouponService.logList(param);
    }

    @PostMapping("/addLogistics")
    @ApiOperation(value = "添加物流信息")
    public ServerResponseEntity<Void> addLogistics(@RequestBody ScoreLogDTO param){
        return scoreCouponService.addLogistics(param);
    }

    @PostMapping("/confirmExprot")
    public ServerResponseEntity<Void> confirmExprot(@RequestBody ScoreLogConfirmDTO scoreLogConfirmDTO){
        return scoreCouponService.confirmExprot(scoreLogConfirmDTO.getIds());
    }

    @PostMapping("/importLog")
    @ApiOperation(value = "导入兑换记录")
    public ServerResponseEntity<Map<String,Integer>> importLog(@RequestParam("file") MultipartFile file) {
        return scoreCouponService.importLog(file);
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出兑换记录")
    public ServerResponseEntity<String> export(@RequestBody ScoreBarterLogListDTO param, HttpServletResponse response){
        try {
            CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
            downloadRecordDTO.setDownloadTime(new Date());
            downloadRecordDTO.setFileName("积分换券活动ID"+param.getConvertId()+"-"+ SoldScoreCouponLogVO.EXCEL_NAME);
            downloadRecordDTO.setCalCount(0);
            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
            downloadRecordDTO.setOperatorNo(""+ AuthUserContext.get().getUserId());
            ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
            Long downLoadHisId=null;
            if(serverResponseEntity.isSuccess()){
                downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
            }

            param.setDownLoadHisId(downLoadHisId);
            scoreCouponService.export(param,response);

            return ServerResponseEntity.success("操作成功，请转至下载中心下载");
        }catch (Exception e){
            log.error("导出兑换记录错误: "+e.getMessage(),e);
            return ServerResponseEntity.showFailMsg("操作失败");
        }
    }
}
