package com.mall4j.cloud.user.controller.platform.scoreConvert;


import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.user.vo.SoldScoreBannerShopVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.constant.scoreConvert.ScoreConvertStatusEnum;
import com.mall4j.cloud.user.dto.scoreConvert.BannerSaveOrUpdateDTO;
import com.mall4j.cloud.user.dto.scoreConvert.BannerShopDTO;
import com.mall4j.cloud.user.dto.scoreConvert.ScoreConvertListDTO;
import com.mall4j.cloud.user.service.scoreConvert.ScoreBannerService;
import com.mall4j.cloud.user.vo.scoreConvert.BannerDetailVO;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreBannerListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 积分banner
 * @author shijing
 */
@RestController("platformScoreBannerController")
@RequestMapping("/p/score/banner")
@Api(tags = "platform-积分banner")
@Slf4j
public class ScoreBannerController {
    @Resource
    private ScoreBannerService scoreBannerService;
    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @PostMapping("/list")
    @ApiOperation(value = "积分banner列表")
    public ServerResponseEntity<PageInfo<ScoreBannerListVO>> list(@RequestBody ScoreConvertListDTO param){
        return scoreBannerService.list(param);
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增banner活动")
    public ServerResponseEntity<Void> add(@RequestBody BannerSaveOrUpdateDTO param){
        return scoreBannerService.save(param);
    }

    @PostMapping("/selectActivityShop")
    @ApiOperation(value = "查询门店参与的banner活动")
    public ServerResponseEntity<List<BannerShopDTO>> selectActivityShop(@RequestBody BannerSaveOrUpdateDTO param){
        return scoreBannerService.selectActivityShop(param);
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出兑换记录")
    public ServerResponseEntity<String> export(@RequestBody List<BannerShopDTO> list, HttpServletResponse response){
        try {
            CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
            downloadRecordDTO.setDownloadTime(new Date());
            downloadRecordDTO.setFileName("重复门店活动-"+ SoldScoreBannerShopVO.EXCEL_NAME);
            downloadRecordDTO.setCalCount(0);
            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
            downloadRecordDTO.setOperatorNo(""+ AuthUserContext.get().getUserId());
            ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
            Long downLoadHisId=null;
            if(serverResponseEntity.isSuccess()){
                downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
            }

            scoreBannerService.export(list,downLoadHisId,response);

            return ServerResponseEntity.success("操作成功，请转至下载中心下载");
        }catch (Exception e){
            log.error("导出重复门店数据错误: "+e.getMessage(),e);
            return ServerResponseEntity.showFailMsg("操作失败");
        }
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改banner活动")
    public ServerResponseEntity<Void> update(@RequestBody BannerSaveOrUpdateDTO param){
        return scoreBannerService.update(param);
    }

    @PostMapping("/updateWeight")
    @ApiOperation(value = "修改banner活动权重")
    public ServerResponseEntity<Void> updateWeight(@RequestBody BannerSaveOrUpdateDTO param){
        return scoreBannerService.updateWeight(param);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "查看详情")
    public ServerResponseEntity<BannerDetailVO> detail(@RequestParam Long id){
        return scoreBannerService.selectDetail(id);
    }

    @DeleteMapping
    @ApiOperation(value = "删除活动")
    public ServerResponseEntity<Void> delete(@RequestParam Long id){
        return scoreBannerService.delete(id);
    }

    @GetMapping("/enable")
    @ApiOperation(value = "启用活动")
    public ServerResponseEntity<Void> enable(@RequestParam Long id){
        return scoreBannerService.updateStatus(id, ScoreConvertStatusEnum.ENABLE.value());
    }

    @GetMapping("/disable")
    @ApiOperation(value = "禁用活动")
    public ServerResponseEntity<Void> disable(@RequestParam Long id){
        return scoreBannerService.updateStatus(id, ScoreConvertStatusEnum.DISABLE.value());
    }

}
