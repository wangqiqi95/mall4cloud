package com.mall4j.cloud.platform.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.dto.OrganizationDTO;
import com.mall4j.cloud.platform.mapper.OrganizationMapper;
import com.mall4j.cloud.platform.mapper.TzStoreMapper;
import com.mall4j.cloud.platform.model.Organization;
import com.mall4j.cloud.platform.service.OrganizationService;
import com.mall4j.cloud.platform.vo.OrganizationVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 组织结构表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 13:44:09
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    TzStoreMapper tzStoreMapper;

    @Override
    public PageVO<Organization> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> organizationMapper.listOrg());
    }

    @Override
    public Organization getByOrgId(Long orgId) {
        return organizationMapper.getByOrgId(orgId);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.PLATFORM_ORG_TOTAL),
            @CacheEvict(cacheNames = CacheNames.PLATFORM_ORG_ALL),
            @CacheEvict(cacheNames = CacheNames.PLATFORM_ORG_CHILD)
    })
    public void saveOrg(Organization organization) {
        if (organization.getParentId() != 0) {
            Organization parent = organizationMapper.getByOrgId(organization.getParentId());
            organization.setPath(parent.getPath() + "," + organization.getParentId());
        } else {
            organization.setPath("0");
        }
        organizationMapper.saveOrg(organization);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.PLATFORM_ORG_TOTAL),
            @CacheEvict(cacheNames = CacheNames.PLATFORM_ORG_ALL),
            @CacheEvict(cacheNames = CacheNames.PLATFORM_ORG_CHILD)
    })
    public void update(Organization organization) {
        organizationMapper.updateOrg(organization);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.PLATFORM_ORG_TOTAL),
            @CacheEvict(cacheNames = CacheNames.PLATFORM_ORG_ALL),
            @CacheEvict(cacheNames = CacheNames.PLATFORM_ORG_CHILD)
    })
    public void deleteById(Long orgId) {
        Organization organization = organizationMapper.getByOrgId(orgId);
        // todo 要删除的节点存在子节点不允许删除。

        //当要删除的节点类型为商铺时，同时逻辑删除商铺信息。
        if (organization.getType() == 4) {
            tzStoreMapper.logicDeleteById(orgId);
        }
        organizationMapper.deleteById(orgId);
    }

    @Override
    public List<Organization> list(Integer type, String keyword) {
        return organizationMapper.listByType(type, keyword);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.PLATFORM_ORG_TOTAL, key = "'org'")
    public List<OrganizationVO> total() {
        List<Organization> list = organizationMapper.listOrg();
        List<OrganizationVO> organizationVOList = mapperFacade.mapAsList(list, OrganizationVO.class);
        //获取根节点
        List<OrganizationVO> resultList
                = organizationVOList.stream().filter(organizationVO -> Objects.equals(1, organizationVO.getType())).collect(Collectors.toList());
        Map<Long, List<OrganizationVO>> collect = organizationVOList.stream().collect(Collectors.groupingBy(OrganizationVO::getParentId));
        resultList.forEach(organizationVO -> {
            forEach(collect, organizationVO);
        });
        // 循环出每一级下级节点
        return resultList;
    }

    @Override
    public List<Long> listByTypeAndParentId(int type, List<Long> orgIdList) {
        List<Long> orgIds = new ArrayList<>();
        List<Organization> list = this.list();
        if (type == 2) {
            //片群id 及 片区下门店
            List<Long> dOrgIdList = list.stream().filter(organization -> organization.getType() == 2 && orgIdList.contains(organization.getOrgId())).map(Organization::getOrgId).collect(Collectors.toList());
            orgIds.addAll(list.stream().filter(organization -> organization.getType() == 4 && dOrgIdList.contains(organization.getParentId())).map(Organization::getOrgId).collect(Collectors.toList()));
            //店群id 及 店群下门店
            List<Long> gOrgIdList = list.stream().filter(organization -> organization.getType() == 3 && dOrgIdList.contains(organization.getParentId())).map(Organization::getOrgId).collect(Collectors.toList());
            orgIds.addAll(list.stream().filter(organization -> organization.getType() == 4 && gOrgIdList.contains(organization.getParentId())).map(Organization::getOrgId).collect(Collectors.toList()));
        } else if (type == 3) {
            //店群id 及 店群下门店
            List<Long> gOrgIdList = list.stream().filter(organization -> organization.getType() == 3 && orgIdList.contains(organization.getOrgId())).map(Organization::getOrgId).collect(Collectors.toList());
            orgIds.addAll(list.stream().filter(organization -> organization.getType() == 4 && gOrgIdList.contains(organization.getParentId())).map(Organization::getOrgId).collect(Collectors.toList()));
        }
        return orgIds;
    }

    @Override
//    @Cacheable(cacheNames = CacheNames.PLATFORM_ORG_TOTAL, key = "'org'")
    public List<OrganizationVO> tree() {
        List<Organization> list = organizationMapper.listOrg();
        List<OrganizationVO> organizationVOList = mapperFacade.mapAsList(list, OrganizationVO.class);
        //获取根节点
//        List<OrganizationVO> resultList
//                = organizationVOList.stream().filter(organizationVO -> Objects.equals(1, organizationVO.getType())).collect(Collectors.toList());
        Map<Long, List<OrganizationVO>> collect = organizationVOList.stream().collect(Collectors.groupingBy(OrganizationVO::getParentId));
        organizationVOList.forEach(organizationVO -> {
            // 循环出每一级下级节点
            forEach(collect, organizationVO);
        });
        organizationVOList.sort(Comparator.comparing(OrganizationVO::getOrgId));
        return organizationVOList;
    }

    @Override
    public List<Organization> listByTypeAndPathLike(int type, String path) {
        return organizationMapper.listByTypeAndPathLike(type, path);
    }

    @Override
    public Organization getOrganizationByParam(OrganizationDTO param) {
        return organizationMapper.getOrganizationByParam(param);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.PLATFORM_ORG_ALL, key = "'all'")
    public List<Organization> all() {
        return this.lambdaQuery().eq(Organization::getIsDeleted, 0).list();
    }

    @Override
    @Cacheable(cacheNames = CacheNames.PLATFORM_ORG_CHILD, key = "#parentId")
    public List<Organization> childList(Long parentId) {
        return this.lambdaQuery().eq(Organization::getParentId, parentId).eq(Organization::getIsDeleted, 0).list();
    }

    @Override
    public List<Long> getStoreOrgId(String path) {
        return organizationMapper.getStoreOrgIdByPath(path);
    }

    @Override
    public List<Organization> getListByIds(List<String> orgIds) {
        List<Organization> orgs=organizationMapper.selectListByOrgIds(orgIds);
        return CollUtil.isNotEmpty(orgs)?orgs: ListUtil.empty();
    }

    private static void forEach(Map<Long, List<OrganizationVO>> collect, OrganizationVO treeMenuNode) {
        List<OrganizationVO> treeMenuNodes = collect.get(treeMenuNode.getOrgId());
        if (collect.get(treeMenuNode.getOrgId()) != null) {
            //排序
            treeMenuNodes.sort(Comparator.comparing(OrganizationVO::getOrgId));
//            treeMenuNodes.sort(Comparator.comparing(OrganizationVO::getType));
            treeMenuNode.setChildList(treeMenuNodes);
            treeMenuNode.getChildList().forEach(t -> {
                forEach(collect, t);
            });
        }
    }


}
