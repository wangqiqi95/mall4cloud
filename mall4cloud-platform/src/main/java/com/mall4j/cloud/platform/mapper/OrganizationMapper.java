package com.mall4j.cloud.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.platform.dto.OrganizationDTO;
import com.mall4j.cloud.platform.model.Organization;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组织结构表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 13:44:09
 */
public interface OrganizationMapper extends BaseMapper<Organization> {

	/**
	 * 获取组织结构表列表
	 * @return 组织结构表列表
	 */
	List<Organization> listOrg();

	/**
	 * 根据组织结构表id获取组织结构表
	 *
	 * @param orgId 组织结构表id
	 * @return 组织结构表
	 */
	Organization getByOrgId(@Param("orgId") Long orgId);

	/**
	 * 保存组织结构表
	 * @param organization 组织结构表
	 */
	void saveOrg(@Param("organization") Organization organization);

	/**
	 * 更新组织结构表
	 * @param organization 组织结构表
	 */
	void updateOrg(@Param("organization") Organization organization);

	/**
	 * 根据组织结构表id删除组织结构表
	 * @param orgId
	 */
	void deleteById(@Param("orgId") Long orgId);

	List<Organization> listByType(@Param("type") Integer type,@Param("keyword") String keyword);

	List<Organization> listByTypeAndPathLike(@Param("type") Integer type, @Param("path") String path);

	Organization getOrganizationByParam(@Param("param") OrganizationDTO param);

    List<Long> getStoreOrgIdByPath(@Param("path") String path);

    List<Organization> getStoreOrgIdByPaths(@Param("paths") List<String> paths);

    List<Organization> selectListByOrgIds(@Param("orgIds") List<String> orgIds);
}
