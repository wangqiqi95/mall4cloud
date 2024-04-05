package com.mall4j.cloud.group.controller.sign.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall4j.cloud.api.order.vo.OrderExcelVO;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.dto.SignActivityDTO;
import com.mall4j.cloud.group.dto.SignActivityPageDTO;
import com.mall4j.cloud.group.dto.SignDetailDTO;
import com.mall4j.cloud.group.dto.SignGatherDTO;
import com.mall4j.cloud.group.service.SignActivityBizService;
import com.mall4j.cloud.group.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/mp/sign_activity")
@Api(tags = "admin-签到活动接口")
@Slf4j
public class SignActivityController {
    @Resource
    private SignActivityBizService signActivityBizService;
    @Autowired
    DownloadCenterFeignClient downloadCenterFeignClient;
    /**
     * 新增签到活动
     *
     * @param param 签到活动入参
     * @return 是否新增成功
     */
    @PutMapping
    @ApiOperation(value = "saveOrUpdate",notes = "新增或修改签到活动")
    public ServerResponseEntity<Void> saveOrUpdate(@RequestBody @Valid SignActivityDTO param) {
        Long userId = AuthUserContext.get().getUserId();
        String username = AuthUserContext.get().getUsername();
        Integer id = param.getId();

        if (null == id){
            param.setCreateUserId(userId);
            param.setCreateUserName(username);
        }else {
            param.setUpdateUserId(userId);
            param.setUpdateUserName(username);
        }
        return signActivityBizService.saveOrUpdateSignActivity(param);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "detail",notes = "查询签到活动详情")
    public ServerResponseEntity<SignActivityVO> detail(@PathVariable Integer id){
        return signActivityBizService.detail(id);
    }

    @PostMapping
    @ApiOperation(value = "page",notes = "查询签到活动列表")
    public ServerResponseEntity<PageVO<SignActivityListVO>> page(@RequestBody SignActivityPageDTO param){
        return signActivityBizService.page(param);
    }

    @PostMapping("/enable/{id}")
    public ServerResponseEntity<Void> enable(@PathVariable Integer id){
        return signActivityBizService.enable(id);
    }

    @PostMapping("/disable/{id}")
    public ServerResponseEntity<Void> disable(@PathVariable Integer id){
        return signActivityBizService.disable(id);
    }

    @DeleteMapping("/{id}")
    public ServerResponseEntity<Void> delete(@PathVariable Integer id){
        return signActivityBizService.delete(id);
    }

    @PostMapping("/signGather")
    public ServerResponseEntity<SignGatherVO> signGather(@RequestBody SignGatherDTO param){
        return signActivityBizService.signGather(param);
    }
    @PostMapping("/normal_detail")
    public ServerResponseEntity<IPage<SignDetailVO>> signNormalDetail(@RequestBody SignDetailDTO param) {
        return signActivityBizService.signDetail(param);
    }

    @GetMapping("/normal_detail/export")
    public ServerResponseEntity signNormalDetailExport(HttpServletResponse response, SignDetailDTO param) {
        try{
            CalcingDownloadRecordDTO downloadRecordDTO = new CalcingDownloadRecordDTO();
            downloadRecordDTO.setDownloadTime(new Date());
            downloadRecordDTO.setFileName(SignDetailExcelVO.EXCEL_NAME);
            downloadRecordDTO.setCalCount(0);
            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
            downloadRecordDTO.setOperatorNo("" + AuthUserContext.get().getUserId());
            ServerResponseEntity serverResponseEntity = downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
            Long downLoadHisId = null;
            if (serverResponseEntity.isSuccess()) {
                downLoadHisId = Long.parseLong(serverResponseEntity.getData().toString());
            }
            signActivityBizService.signNormalDetailExport(response,param,downLoadHisId);
            return ServerResponseEntity.success("操作成功，请转至下载中心下载");
        }catch (Exception e){
            log.error("导出签到活动信息错误: "+e.getMessage(),e);
            return ServerResponseEntity.showFailMsg("操作失败");
        }
    }
}
