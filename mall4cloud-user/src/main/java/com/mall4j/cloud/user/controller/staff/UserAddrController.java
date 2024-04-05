package com.mall4j.cloud.user.controller.staff;

import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.vo.UserAddrVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.dto.UserAddrDTO;
import com.mall4j.cloud.user.model.UserAddr;
import com.mall4j.cloud.user.service.UserAddrService;
import com.mall4j.cloud.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户地址
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:50:02
 */
@RestController("staffUserAddrController")
@RequestMapping("/s/user_addr")
@Api(tags = "staff-导购用户地址")
public class UserAddrController {

    @Autowired
    private UserAddrService userAddrService;

    @Autowired
	private MapperFacade mapperFacade;

    @Autowired
    private StaffFeignClient staffFeignClient;

    @Autowired
    private UserService userService;

    private static final Integer MAX_USER_ADDR = 10;

	@GetMapping("/list")
	@ApiOperation(value = "获取用户地址列表", notes = "获取用户地址列表")
	public ServerResponseEntity<List<UserAddrVO>> list(@RequestParam(value = "userId", required = false) Long userId) {
        ServerResponseEntity<StaffVO> staffData = staffFeignClient.getStaffById(AuthUserContext.get().getUserId());
        if (!staffData.isSuccess() || null == staffData.getData()){
            throw new LuckException("当前导购不存在");
        }
        if (null == userId){
            UserApiVO userApiVO = userService.getUserByMobile(staffData.getData().getMobile());
            if (null == userApiVO){
                throw new LuckException("当前导购未注册会员");
            }
            userId = userApiVO.getUserId();
        }
        List<UserAddrVO> userAddrPage = userAddrService.list(userId);
		return ServerResponseEntity.success(userAddrPage);
	}

	@GetMapping
    @ApiOperation(value = "获取用户地址", notes = "根据addrId获取用户地址")
    public ServerResponseEntity<UserAddrVO> getByAddrId(@RequestParam Long addrId, @RequestParam(value = "userId", required = false) Long userId) {
        ServerResponseEntity<StaffVO> staffData = staffFeignClient.getStaffById(AuthUserContext.get().getUserId());
        if (!staffData.isSuccess() || null == staffData.getData()){
            throw new LuckException("当前导购不存在");
        }
        if (null == userId){
            UserApiVO userApiVO = userService.getUserByMobile(staffData.getData().getMobile());
            if (null == userApiVO){
                throw new LuckException("当前导购未注册会员");
            }
            userId = userApiVO.getUserId();
        }
        return ServerResponseEntity.success(userAddrService.getUserAddrByUserId(addrId, userId));
    }

    @PostMapping
    @ApiOperation(value = "保存用户地址", notes = "保存用户地址")
    public ServerResponseEntity<Void> save(@Valid @RequestBody UserAddrDTO userAddrDTO) {
        ServerResponseEntity<StaffVO> staffData = staffFeignClient.getStaffById(AuthUserContext.get().getUserId());
        if (!staffData.isSuccess() || null == staffData.getData()){
            throw new LuckException("当前导购不存在");
        }
        Long userId;
        if (userAddrDTO.getUserId() == null) {
            UserApiVO userApiVO = userService.getUserByMobile(staffData.getData().getMobile());
            if (null == userApiVO){
                throw new LuckException("当前导购未注册会员");
            }
            userId = userApiVO.getUserId();
        } else {
            userId = userAddrDTO.getUserId();
        }

        int userAddrCount = userAddrService.countByUserId(userId);
//        if (userAddrCount >= MAX_USER_ADDR) {
//            return ServerResponseEntity.showFailMsg("收货地址已达到上限，无法再新增地址");
//        }
        UserAddr userAddr = mapperFacade.map(userAddrDTO, UserAddr.class);
        if (userAddrCount == 0) {
            userAddr.setIsDefault(UserAddr.DEFAULT_ADDR);
        } else if (!UserAddr.DEFAULT_ADDR.equals(userAddr.getIsDefault())){
            userAddr.setIsDefault(UserAddr.NOT_DEFAULT_ADDR);
        }
        userAddr.setAddrId(null);
        userAddr.setUserId(userId);
        userAddrService.save(userAddr);
        // 清除默认地址缓存
        if (UserAddr.DEFAULT_ADDR.equals(userAddr.getIsDefault())) {
            userAddrService.removeUserDefaultAddrCacheByUserId(userId);
        }
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新用户地址", notes = "更新用户地址")
    public ServerResponseEntity<Void> update(@Valid @RequestBody UserAddrDTO userAddrDTO) {
        ServerResponseEntity<StaffVO> staffData = staffFeignClient.getStaffById(AuthUserContext.get().getUserId());
        if (!staffData.isSuccess() || null == staffData.getData()){
            throw new LuckException("当前导购不存在");
        }
        Long userId;
        if (userAddrDTO.getUserId() == null) {
            UserApiVO userApiVO = userService.getUserByMobile(staffData.getData().getMobile());
            if (null == userApiVO){
                throw new LuckException("当前导购未注册会员");
            }
            userId = userApiVO.getUserId();
        } else {
            userId = userAddrDTO.getUserId();
        }
        UserAddrVO dbUserAddr = userAddrService.getUserAddrByUserId(userAddrDTO.getAddrId(), userId);
        if (dbUserAddr == null) {
            throw new LuckException("该地址已被删除");
        }
        // 默认地址不能修改为普通地址
//        else if (dbUserAddr.getIsDefault().equals(UserAddr.DEFAULT_ADDR) && userAddrDTO.getIsDefault().equals(UserAddr.NOT_DEFAULT_ADDR)) {
//            throw new LuckException(ResponseEnum.DATA_ERROR);
//        }
        UserAddr userAddr = mapperFacade.map(userAddrDTO, UserAddr.class);
        userAddr.setUserId(userId);
        userAddrService.update(userAddr);
        // 清除默认地址缓存
        if (userAddr.getIsDefault().equals(UserAddr.DEFAULT_ADDR)) {
            userAddrService.removeUserDefaultAddrCacheByUserId(userId);
        }
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除用户地址", notes = "根据用户地址id删除用户地址")
    public ServerResponseEntity<Void> delete(@RequestParam Long addrId, Long userId) {
        ServerResponseEntity<StaffVO> staffData = staffFeignClient.getStaffById(AuthUserContext.get().getUserId());
        if (!staffData.isSuccess() || null == staffData.getData()){
            throw new LuckException("当前导购不存在");
        }
        if (userId == null) {
            UserApiVO userApiVO = userService.getUserByMobile(staffData.getData().getMobile());
            if (null == userApiVO){
                throw new LuckException("当前导购未注册会员");
            }
            userId = userApiVO.getUserId();
        }
        UserAddrVO dbUserAddr = userAddrService.getUserAddrByUserId(addrId, userId);
        if (dbUserAddr == null) {
            throw new LuckException("该地址已被删除");
        } else if (dbUserAddr.getIsDefault().equals(UserAddr.DEFAULT_ADDR)) {
            throw new LuckException("默认地址不能删除");
        }
        userAddrService.deleteUserAddrByUserId(addrId, userId);
        return ServerResponseEntity.success();
    }
}
