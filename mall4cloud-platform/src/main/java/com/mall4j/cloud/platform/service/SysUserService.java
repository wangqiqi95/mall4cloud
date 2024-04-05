package com.mall4j.cloud.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.dto.SysUserDTO;
import com.mall4j.cloud.platform.dto.SysUserPageDTO;
import com.mall4j.cloud.platform.model.SysUser;
import com.mall4j.cloud.platform.vo.SysUserSimpleVO;
import com.mall4j.cloud.api.platform.vo.SysUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lhd
 * @date 2020/12/22
 */
public interface SysUserService extends IService<SysUser> {

	/**
	 * 根据用户id获取当前登陆的商家用户信息
	 * @param userId 用户id
	 * @return 商家用户信息
	 */
	SysUserSimpleVO getSimpleByUserId(Long userId);

	/**
	 * 分页获取平台用户列表
	 * @param pageDTO 分页参数
	 * @param sysUserDTO 搜索参数
	 * @return 平台用户列表
	 */
	PageVO<SysUserVO> pageByShopId(PageDTO pageDTO, SysUserPageDTO sysUserDTO);

	/**
	 * 根据用户id获取商家用户信息
	 *
	 * @param userId 用户id
	 * @return 商家用户信息
	 */
	SysUserVO getByUserId(Long userId);

	SysUserVO getByUserInfoId(Long userId);

	/**
	 * 保存平台用户信息
	 * @param sysUser 平台用户id
	 * @param roleIds 角色id列表
	 */
	void save(SysUser sysUser, List<Long> roleIds);

	/**
	 * 更新平台用户信息
	 * @param sysUser 平台用户id
	 * @param roleIds 角色id列表
	 */
	void update(SysUser sysUser,List<Long> roleIds);

	/**
	 * 根据平台用户id删除平台用户信息
	 * @param sysUserId 平台用户id
	 */
	void deleteById(Long sysUserId);

	void openPlatform(Long sysUserId);

	/**
	 * 根据用户id列表查名称
	 * @param userIds 用户id列表
	 * @return
	 */
	List<SysUserVO> getUserNameMap(List<Long> userIds);

	SysUserVO getByPhoneNum(String phone);

}
