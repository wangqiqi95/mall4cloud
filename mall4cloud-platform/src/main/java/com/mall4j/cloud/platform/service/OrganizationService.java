package com.mall4j.cloud.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.dto.OrganizationDTO;
import com.mall4j.cloud.platform.model.Organization;
import com.mall4j.cloud.platform.vo.OrganizationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组织结构表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 13:44:09
 */
public interface OrganizationService extends IService<Organization> {

	/**
	 * 分页获取组织结构表列表
	 * @param pageDTO 分页参数
	 * @return 组织结构表列表分页数据
	 */
	PageVO<Organization> page(PageDTO pageDTO);

	/**
	 * 根据组织结构表id获取组织结构表
	 *
	 * @param orgId 组织结构表id
	 * @return 组织结构表
	 */
	Organization getByOrgId(Long orgId);

	/**
	 * 保存组织结构表
	 * @param organization 组织结构表
	 */
	void saveOrg(Organization organization);

	/**
	 * 更新组织结构表
	 * @param organization 组织结构表
	 */
	void update(Organization organization);

	/**
	 * 根据组织结构表id删除组织结构表
	 * @param orgId 组织结构表id
	 */
	void deleteById(Long orgId);

	List<Organization> list(Integer type, String keyword);

    List<Long> listByTypeAndParentId(int type, List<Long> groupOrgIdList);

    List<OrganizationVO> tree();

	List<Organization> listByTypeAndPathLike(int type, String path);

	Organization getOrganizationByParam(OrganizationDTO param);

	List<Organization> all();

	List<Organization> childList(Long parentId);

	List<Long> getStoreOrgId(String path);

	List<Organization> getListByIds(List<String> orgIds);

	List<OrganizationVO> total();
}
