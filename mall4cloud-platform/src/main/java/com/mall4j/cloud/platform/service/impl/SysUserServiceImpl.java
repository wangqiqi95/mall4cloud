package com.mall4j.cloud.platform.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.auth.feign.AccountFeignClient;
import com.mall4j.cloud.api.auth.vo.AuthAccountVO;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.api.rbac.dto.UserRoleDTO;
import com.mall4j.cloud.api.rbac.feign.UserRoleFeignClient;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.platform.dto.ChangeAccountDTO;
import com.mall4j.cloud.platform.dto.SysUserDTO;
import com.mall4j.cloud.platform.dto.SysUserPageDTO;
import com.mall4j.cloud.platform.mapper.OrganizationMapper;
import com.mall4j.cloud.platform.mapper.SysUserMapper;
import com.mall4j.cloud.platform.model.Organization;
import com.mall4j.cloud.platform.model.Staff;
import com.mall4j.cloud.platform.model.SysUser;
import com.mall4j.cloud.platform.service.OrganizationService;
import com.mall4j.cloud.platform.service.StaffService;
import com.mall4j.cloud.platform.service.SysUserAccountService;
import com.mall4j.cloud.platform.service.SysUserService;
import com.mall4j.cloud.platform.vo.SysUserSimpleVO;
import com.mall4j.cloud.api.platform.vo.SysUserVO;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lhd
 * @date 2020/12/22
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper,SysUser> implements SysUserService {

	@Resource
	private SysUserMapper sysUserMapper;
	@Autowired
	private AccountFeignClient accountFeignClient;
	@Autowired
	private UserRoleFeignClient userRoleFeignClient;
	@Autowired
	private SysUserAccountService SysUserAccountService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private SegmentFeignClient segmentFeignClient;

	@Autowired
	private OrganizationMapper organizationMapper;


	@Override
	@Cacheable(cacheNames = CacheNames.PLATFORM_SIMPLE_INFO_KEY, key = "#userId")
	public SysUserSimpleVO getSimpleByUserId(Long userId) {
		return sysUserMapper.getSimpleByUserId(userId);
	}

	@Override
	public PageVO<SysUserVO> pageByShopId(PageDTO pageDTO, SysUserPageDTO sysUserDTO) {
		if(StrUtil.isNotBlank(sysUserDTO.getPath())){
			List<Long>  orgIds=organizationMapper.getStoreOrgIdByPath(sysUserDTO.getPath());
			sysUserDTO.setOrgIds(orgIds);
		}
		PageVO<SysUserVO> pageVO=PageUtil.doPage(pageDTO, () -> sysUserMapper.listByShopId(sysUserDTO));

		for (SysUserVO sysUserVO : pageVO.getList()) {
//			sysUserVO.setPhoneNum(Objects.isNull(sysUserVO.getPhoneNum()) ? null : PhoneUtil.hideBetween(sysUserVO.getPhoneNum()).toString());
			//部门
			if(CharSequenceUtil.isNotEmpty(sysUserVO.getOrgId())){
				List<Organization>  orgList=organizationMapper.selectListByOrgIds(Arrays.asList(sysUserVO.getOrgId().split(",")));
				if(CollUtil.isNotEmpty(orgList)){
					sysUserVO.setOrgs(orgList.stream().map(item->item.getOrgName()).collect(Collectors.toList()));
				}
			}
//			//角色
//			ServerResponseEntity<List<RoleVO>> responseEntity=userRoleFeignClient.getUserRoles(sysUserVO.getSysUserId());
//			if(responseEntity.isSuccess()){
//				sysUserVO.setRoles(responseEntity.getData().stream().map(item->item.getRoleName()).collect(Collectors.toList()));
//			}
		}
		return pageVO;
	}

	@Override
	public SysUserVO getByUserId(Long userId) {
		SysUserVO sysUser = sysUserMapper.getByUserId(userId);
		ServerResponseEntity<List<Long>> roleIds = userRoleFeignClient.getRoleIds(sysUser.getSysUserId());
		sysUser.setRoleIds(roleIds.getData());

		//登录账户信息
		ServerResponseEntity<AuthAccountVO> responseEntity=SysUserAccountService.getByUserIdAndSysType(sysUser.getSysUserId(),1);
		ServerResponseEntity.checkResponse(responseEntity);
		sysUser.setPassword(responseEntity.getData().getPassword());
		sysUser.setUsername(responseEntity.getData().getUsername());

		return sysUser;
	}

	@Override
	public SysUserVO getByUserInfoId(Long userId) {
		return sysUserMapper.getByUserId(userId);
	}

	@Override
	@GlobalTransactional(rollbackFor = Exception.class)
//	@Transactional(rollbackFor = Exception.class)
    public void save(SysUser sysUser, List<Long> roleIds) {
//		ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.SCRM_PLATFORM_USER);
//		if (!segmentIdResponse.isSuccess()) {
//			throw new LuckException(segmentIdResponse.getMsg());
//		}
		if (CollUtil.isEmpty(roleIds)) {
			throw new LuckException("请配置角色");
		}
//		Long sysUserId = segmentIdResponse.getData()+ RandomUtil.randomInt(2);
		checkSysUser(sysUser);
//		sysUser.setSysUserId(sysUserId);
		UserRoleDTO userRoleDTO = new UserRoleDTO();
		userRoleDTO.setRoleIds(roleIds);
		sysUser.setCreateBy(AuthUserContext.get().getUsername());
		sysUser.setCreateTime(new Date());
		this.save(sysUser);

		//权限角色
		userRoleDTO.setUserId(sysUser.getSysUserId());
		userRoleFeignClient.saveByUserIdAndSysType(userRoleDTO);

		//同时创建登录账户
		ChangeAccountDTO changeAccountDTO=new ChangeAccountDTO();
		changeAccountDTO.setEmail(sysUser.getEmail());
		changeAccountDTO.setPassword(sysUser.getPassword());
		changeAccountDTO.setPhone(sysUser.getPhoneNum());
		changeAccountDTO.setStatus(sysUser.getStatus());
		changeAccountDTO.setUsername(sysUser.getUsername());
		changeAccountDTO.setUserId(sysUser.getSysUserId());
		ServerResponseEntity<Void>  responseEntity=SysUserAccountService.save(changeAccountDTO);
		ServerResponseEntity.checkResponse(responseEntity);

		//同时创建员工账户
//		Staff staff=new Staff();
//		staff.setOpenPlatform(sysUser.getOpenPlatform());
//		staff.setSysUserId(sysUser.getSysUserId().toString());
//		staff.setStaffCode(sysUser.getSysUserId().toString());
//		staff.setStaffNo(sysUser.getSysUserId().toString());
//		staff.setOrgId(""+sysUser.getOrgId());
//		staff.setMobile(sysUser.getPhoneNum());
//		staff.setStaffName(sysUser.getNickName());
//		staff.setStatus(sysUser.getStatus());
//		staff.setCreateBy(AuthUserContext.get().getUsername());
//		staff.setCreateTime(new Date());
//		staffService.add(staff);
	}

	private void checkSysUser(SysUser sysUser) {
		// 修改，检查昵称是否重复，sys_user_id是不等于sysUserId
//		if (sysUserMapper.countNickName(sysUser.getNickName(), sysUser.getSysUserId()) > 0) {
//			throw new LuckException("该昵称已存在");
//		}
	}

	@Override
	@GlobalTransactional(rollbackFor = Exception.class)
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(cacheNames = CacheNames.PLATFORM_SIMPLE_INFO_KEY, key = "#sysUser.sysUserId")
	public void update(SysUser sysUser, List<Long> roleIds) {
		checkSysUser(sysUser);
		UserRoleDTO userRoleDTO = new UserRoleDTO();
		userRoleDTO.setRoleIds(roleIds);
		userRoleDTO.setUserId(sysUser.getSysUserId());
		sysUser.setUpdateTime(new Date());
		sysUser.setUpdateBy(AuthUserContext.get().getUsername());
		this.updateById(sysUser);

		//同时更新员工账户
//		Staff staff=new Staff();
//		staff.setOpenPlatform(sysUser.getOpenPlatform());
//		staff.setSysUserId(sysUser.getSysUserId().toString());
//		staff.setOrgId(""+sysUser.getOrgId());
//		staff.setMobile(sysUser.getPhoneNum());
//		staff.setStaffName(sysUser.getNickName());
//		staff.setStatus(sysUser.getStatus());
//		staff.setUpdateBy(AuthUserContext.get().getUsername());
//		staff.setUpdateTime(new Date());
//		staffService.update(staff,true);

		//权限角色
		userRoleFeignClient.updateByUserIdAndSysType(userRoleDTO);

		//同时更新登录账户信息
		ChangeAccountDTO changeAccountDTO=new ChangeAccountDTO();
		changeAccountDTO.setEmail(sysUser.getEmail());
		changeAccountDTO.setPassword(sysUser.getPassword());
		changeAccountDTO.setPhone(sysUser.getPhoneNum());
		changeAccountDTO.setStatus(sysUser.getStatus());
		changeAccountDTO.setUsername(sysUser.getUsername());
		changeAccountDTO.setUserId(sysUser.getSysUserId());
		ServerResponseEntity<Void>  responseEntity=SysUserAccountService.update(changeAccountDTO);
		ServerResponseEntity.checkResponse(responseEntity);
	}

	@Override
	@GlobalTransactional(rollbackFor = Exception.class)
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(cacheNames = CacheNames.PLATFORM_SIMPLE_INFO_KEY, key = "#sysUserId")
	public void deleteById(Long sysUserId) {
//		SysUserVO sysUserVO = this.getByUserId(sysUserId);
		SysUser sysUser=this.getById(sysUserId);
		if(Objects.isNull(sysUser)){
			throw new LuckException("账户未找到");
		}
		ServerResponseEntity<Void> responseEntity = accountFeignClient.deleteByUserIdAndSysType(sysUserId);
		if (!responseEntity.isSuccess()) {
			throw new LuckException(responseEntity.getMsg());
		}
		userRoleFeignClient.deleteByUserIdAndSysType(sysUserId);
		 sysUser=new SysUser();
		sysUser.setSysUserId(sysUserId);
		sysUser.setIsDelete(1);
		sysUser.setUpdateTime(new Date());
		sysUser.setUpdateBy(AuthUserContext.get().getUsername());
		this.updateById(sysUser);
		//删除员工
		staffService.deleteSysUserId(sysUserId.toString());
	}

	@Override
	public void openPlatform(Long sysUserId) {
		staffService.openPlatform(sysUserId);
	}

	@Override
	public List<SysUserVO> getUserNameMap(List<Long> userIds) {
		return sysUserMapper.getUserName(userIds);
	}

	@Override
	public SysUserVO getByPhoneNum(String phone) {
		return sysUserMapper.getByPhoneNum(phone);
	}

}
