package com.mall4j.cloud.platform.controller.platform;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.PhoneUtil;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.biz.feign.WxMaApiFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.api.platform.vo.SysUserVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.platform.dto.ChangeAccountDTO;
import com.mall4j.cloud.platform.dto.SysUserDTO;
import com.mall4j.cloud.platform.dto.SysUserPageDTO;
import com.mall4j.cloud.platform.manager.WxCpStaffManager;
import com.mall4j.cloud.platform.model.Staff;
import com.mall4j.cloud.platform.model.SysUser;
import com.mall4j.cloud.platform.service.StaffService;
import com.mall4j.cloud.platform.service.SysUserAccountService;
import com.mall4j.cloud.platform.service.SysUserService;
import com.mall4j.cloud.platform.vo.SysUserSimpleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author lhd
 * @date 2020/12/21
 */
@RequestMapping(value = "/p/sys_user")
@RestController
@Api(tags = "平台用户信息")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    WxMaApiFeignClient wxMaApiFeignClient;
    @Autowired
    private SysUserAccountService SysUserAccountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StaffService staffService;

    @GetMapping("/info")
    @ApiOperation(value = "登陆平台用户信息", notes = "获取当前登陆平台用户的用户信息")
    public ServerResponseEntity<SysUserSimpleVO> info() {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        SysUserSimpleVO sysUserSimple = sysUserService.getSimpleByUserId(userInfoInTokenBO.getUserId());
        sysUserSimple.setIsAdmin(userInfoInTokenBO.getIsAdmin());
        return ServerResponseEntity.success(sysUserSimple);
    }

    @PostMapping("/page")
    @ApiOperation(value = "平台用户列表", notes = "获取平台用户列表")
    public ServerResponseEntity<PageVO<SysUserVO>> page(@Valid @RequestBody  SysUserPageDTO sysUserDTO) {
        PageDTO pageDTO=new PageDTO();
        pageDTO.setPageNum(sysUserDTO.getPageNum());
        pageDTO.setPageSize(sysUserDTO.getPageSize());
        return ServerResponseEntity.success(sysUserService.pageByShopId(pageDTO, sysUserDTO));
    }

    @GetMapping
    @ApiOperation(value = "获取平台用户信息", notes = "根据用户id获取平台用户信息")
    public ServerResponseEntity<SysUserVO> detail(@RequestParam Long sysUserId) {
        SysUserVO sysUserVO = sysUserService.getByUserId(sysUserId);
        return ServerResponseEntity.success(sysUserVO);
    }

    @PostMapping
    @ApiOperation(value = "保存平台用户信息", notes = "保存平台用户信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody SysUserDTO sysUserDTO) {
        //校验手机号重复
        if(Objects.nonNull(sysUserService.getByPhoneNum(sysUserDTO.getPhoneNum()))){
            throw new LuckException("手机号已存在");
        }
        if(Objects.nonNull(staffService.getByMobile(sysUserDTO.getPhoneNum()))){
            throw new LuckException("手机号已存在");
        }
        //创建系统账号
        SysUser sysUser = mapperFacade.map(sysUserDTO, SysUser.class);
        sysUser.setOpenPlatform(0);
        sysUser.setSysUserId(null);
        sysUser.setHasAccount(0);
        List<Long> roleIds = sysUserDTO.getRoleIds();
        sysUserService.save(sysUser, roleIds);

        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新平台用户信息", notes = "更新平台用户信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody SysUserDTO sysUserDTO) {
        SysUser sysUser = mapperFacade.map(sysUserDTO, SysUser.class);
        sysUser.setUpdateBy(AuthUserContext.get().getUsername());
        sysUser.setUpdateTime(new Date());
        sysUserService.update(sysUser, sysUserDTO.getRoleIds());
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除平台用户信息", notes = "根据平台用户id删除平台用户信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long sysUserId) {
        if (Objects.equals(AuthUserContext.get().getUserId(), sysUserId)) {
            throw new LuckException("您正在使用当前账号，无法进行删除操作");
        }
        sysUserService.deleteById(sysUserId);
        return ServerResponseEntity.success();
    }

    @GetMapping("/openPlatform")
    @ApiOperation(value = "开通导购", notes = "开通导购")
    public ServerResponseEntity<Void> openPlatform(@RequestParam Long sysUserId) {
        sysUserService.openPlatform(sysUserId);
        return ServerResponseEntity.success();
    }

    @GetMapping("/encodePwd")
    @ApiOperation(value = "encodePwd", notes = "encodePwd")
    public ServerResponseEntity<String> encodePwd(@RequestParam(name = "pwd",required = true) String pwd) {
        return ServerResponseEntity.success(passwordEncoder.encode(pwd));
    }
}
