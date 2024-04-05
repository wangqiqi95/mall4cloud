package com.mall4j.cloud.group.controller.ad.admin;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.AddPopUpAdDTO;
import com.mall4j.cloud.group.dto.PopUpAdUserOperatePageDTO;
import com.mall4j.cloud.group.service.PopUpAdUserOperateService;
import com.mall4j.cloud.group.vo.OperateStatisticsVO;
import com.mall4j.cloud.group.vo.PopUpAdUserOperateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/p/pop/up/ad/user/operate")
@Api(tags = "管理端弹窗广告用户操作相关接口")
@Slf4j
public class PlatformPopUpAdUserOperateController {

    @Autowired
    private PopUpAdUserOperateService popUpAdUserOperateService;


    @GetMapping("/ad/statistics")
    @ApiOperation("查看广告用户操作统计")
    public ServerResponseEntity<OperateStatisticsVO> add(@RequestParam("adId") Long adId){
        return popUpAdUserOperateService.operateStatisticsByAdId(adId);
    }


    @GetMapping("/ad/record")
    @ApiOperation("查看广告用户操作明细分页数据")
    public ServerResponseEntity<PageVO<PopUpAdUserOperateVO>> add(PopUpAdUserOperatePageDTO popUpAdUserOperatePageDTO){
        return popUpAdUserOperateService.operateRecordByAdId(popUpAdUserOperatePageDTO);
    }

    @PostMapping("/export/to/download/center")
    @ApiOperation("下载广告用户行为数据")
    public ServerResponseEntity exportMarkingUser(@RequestParam Long adId, HttpServletResponse response){
        return popUpAdUserOperateService.downloadData(adId, response);
    }


}
