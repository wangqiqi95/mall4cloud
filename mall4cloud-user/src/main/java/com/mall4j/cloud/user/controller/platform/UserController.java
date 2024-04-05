package com.mall4j.cloud.user.controller.platform;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.PhoneUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.vo.PointDetailVo;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.user.vo.MemberOverviewVO;
import com.mall4j.cloud.api.user.vo.UserManagerVO;
import com.mall4j.cloud.api.user.vo.UserTagApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.dto.ChangeStaffDTO;
import com.mall4j.cloud.user.dto.UpdateScoreDTO;
import com.mall4j.cloud.user.dto.UserDTO;
import com.mall4j.cloud.user.dto.UserManagerDTO;
import com.mall4j.cloud.user.event.SoldUsersEvent;
import com.mall4j.cloud.user.manager.UserExcelManager;
import com.mall4j.cloud.user.model.UserChangeRecord;
import com.mall4j.cloud.user.service.UserChangeRecordService;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.service.UserTagUserService;
import com.mall4j.cloud.user.service.UserWeixinAccountFollowService;
import com.mall4j.cloud.user.vo.UserExcelVO;
import com.mall4j.cloud.user.vo.UserWeixinAccountFollowVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;


/**
 * 用户信息
 * @author cl
 */
@Slf4j
@RestController("platformUserController")
@RequestMapping("/p/user")
@Api(tags = "platform-用户信息")
public class UserController {

    @Value("${mall4cloud.expose.permission:}")
    private Boolean permission;

    @Autowired
    private UserService userService;
    @Autowired
    private UserChangeRecordService userChangeRecordService;
    @Autowired
    private UserTagUserService userTagUserService;
    @Autowired
    private UserExcelManager userExcelManager;
    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    private UserWeixinAccountFollowService userWeixinAccountFollowService;
    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
    private ApplicationContext applicationContext;

    @PostMapping("/user_page")
    @ApiOperation(value = "获取会员管理别表列表", notes = "分页获取会员管理别表列表")
    public ServerResponseEntity<PageVO<UserManagerVO>> userPage(@RequestBody UserManagerDTO userManagerDTO) {
        PageDTO pageDTO =new PageDTO();
        pageDTO.setPageSize(userManagerDTO.getPageSize());
        pageDTO.setPageNum(userManagerDTO.getPageNum());
        PageVO<UserManagerVO> userPage = userService.getUserInfoPage(pageDTO, userManagerDTO);
        if (BooleanUtil.isFalse(permission)){
            for (UserManagerVO userManagerVO : userPage.getList()) {
                userManagerVO.setPhone(PhoneUtil.hideBetween(userManagerVO.getPhone()).toString());
            }
        }
        return ServerResponseEntity.success(userPage);
    }

    @GetMapping("/user_info")
    @ApiOperation(value = "根据用户id获取会员信息", notes = "根据用户id获取会员信息")
    public ServerResponseEntity<UserManagerVO> info(@RequestParam Long userId) {
        UserManagerVO param = userService.getUserInfo(userId);
        List<UserTagApiVO> userTagParams = userTagUserService.getUserTagsByUserId(userId);
        param.setUserTagList(userTagParams);
        return ServerResponseEntity.success(param);
    }

    @GetMapping("/userRelationWxMps")
    @ApiOperation(value = "获取用户关注/取关的公众号列表", notes = "获取用户关注/取关的公众号列表")
    public ServerResponseEntity<List<UserWeixinAccountFollowVo>> info(@RequestParam Long userId, @RequestParam String unionId) {
        return ServerResponseEntity.success(userWeixinAccountFollowService.getUserFollowList(unionId));
    }

    /**
     * 修改
     */
    @PutMapping
    public ServerResponseEntity<Void> update(@RequestBody UserDTO userDTO) {
        userService.updateUserInfo(userDTO);
        return ServerResponseEntity.success();
    }

    /**
     * 变更会员所属导购
     */
    @PutMapping("/changeUserStaff")
    @ApiOperation(value = "变更会员所属导购", notes = "变更会员所属导购")
    public ServerResponseEntity<Void> changeUserStaff(@Valid @RequestBody ChangeStaffDTO changeStaffDTO) {
        log.info("正在修改会员服务导购，参数信息:{}", JSONObject.toJSONString(changeStaffDTO));
        userService.changeUserStaff(changeStaffDTO);
        // 刷新用户缓存
        changeStaffDTO.getUserId().forEach(userId -> {
            userService.removeUserCacheByUserId(userId);
        });
        return ServerResponseEntity.success();
    }

//    @GetMapping("/sold_excel")
//    @ApiOperation(value = "导出excel", notes = "导出用户信息excel")
//    public ServerResponseEntity<Void> userSoldExcel(HttpServletResponse response, UserManagerDTO userManagerDTO){
//        userExcelManager.exportUserInfoExcel(response, userManagerDTO);
//        return ServerResponseEntity.success();
//    }

    @PostMapping("/sold_excel")
    @ApiOperation(value = "导出excel", notes = "导出用户信息excel")
    public ServerResponseEntity userSoldExcel(@RequestBody UserManagerDTO userManagerDTO){
        try {
            CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
            downloadRecordDTO.setDownloadTime(new Date());
            downloadRecordDTO.setFileName(UserExcelVO.EXCEL_NAME);
            downloadRecordDTO.setCalCount(0);
            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
            downloadRecordDTO.setOperatorNo(""+ AuthUserContext.get().getUserId());
            ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
            Long downLoadHisId=null;
            if(serverResponseEntity.isSuccess()){
                downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
            }

            SoldUsersEvent soldUsersEvent=new SoldUsersEvent();
            soldUsersEvent.setUserManagerDTO(userManagerDTO);
            soldUsersEvent.setDownLoadHisId(downLoadHisId);
            applicationContext.publishEvent(soldUsersEvent);

            return ServerResponseEntity.success("操作成功，请转至下载中心下载");
        }catch (Exception e){
            log.error("导出会员信息错误: "+e.getMessage(),e);
            return ServerResponseEntity.showFailMsg("操作失败");
        }
    }

    @GetMapping("/down_model")
    @ApiOperation(value = "导出用户信息导入excel模板", notes = "下载用户信息导入excel模板")
    public ServerResponseEntity<Void> downloadModel(HttpServletResponse response) {
        userExcelManager.downloadModel(response);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "导入文件")
    @PostMapping("/import_excel")
    public ServerResponseEntity<String> importExcel(@RequestParam("excelFile") MultipartFile file){
        if(file == null) {
            throw new LuckException("网络繁忙，请稍后重试");
        }
        String info = userExcelManager.importUserExcel(file);
        return ServerResponseEntity.success(info);
    }

    @GetMapping("/get_user_count_info")
    @ApiOperation(value = "获取今日客户概况", notes = "获取今日客户概况")
    public ServerResponseEntity<MemberOverviewVO> getUserCountInfo() {
        MemberOverviewVO memberOverviewListVO = userService.getUserCountInfo(new Date());
        return ServerResponseEntity.success(memberOverviewListVO);
    }

    @GetMapping("/get_score_detail")
    @ApiOperation(value = "获取用户积分明细")
    public ServerResponseEntity<CrmPageResult<List<PointDetailVo>>> getUserInfo(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam Long userId) {
//        return userService.getScoreDetail(pageNo,pageSize, userId);
        return ServerResponseEntity.success();
    }

    @GetMapping("/get_change_record")
    @ApiOperation(value = "获取用户变更记录")
    public ServerResponseEntity<CrmPageResult<List<UserChangeRecord>>> getUserChangeRecord(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam Long userId) {
        return userChangeRecordService.getUserChangeRecord(pageNo,pageSize, userId);
    }

    @PostMapping("/update_user_score")
    @ApiOperation(value = "修改用户积分")
    public ServerResponseEntity<Void> updateUserScore(@RequestBody UpdateScoreDTO param) {
        return userService.updateUserScore(param);
    }

    @GetMapping("/logout_user")
    @ApiOperation(value = "注销用户")
    public ServerResponseEntity<Void> logoutUser(@RequestParam Long userId) {
        return userService.logoutUser(userId);
    }

    @GetMapping("/delete_user_addr")
    @ApiOperation(value = "清空用户收货地址")
    public ServerResponseEntity<Void> deleteUserAddr(@RequestParam Long userId) {
        return userService.deleteUserAddr(userId);
    }

}
