package com.mall4j.cloud.biz.service.cp.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.biz.constant.cp.CodeChannelEnum;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.biz.dto.cp.analyze.AnalyzeGroupCodeDTO;
import com.mall4j.cloud.biz.dto.cp.analyze.CpAutoGroupCodeAnalyzeDTO;
import com.mall4j.cloud.biz.dto.cp.analyze.CpTagGroupCodeAnalyzeDTO;
import com.mall4j.cloud.biz.dto.cp.analyze.CpTagGroupCodeAnalyzeStaffDTO;
import com.mall4j.cloud.biz.mapper.cp.CpAutoGroupCodeUserMapper;
import com.mall4j.cloud.biz.mapper.cp.CpTaskUserRefMapper;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.vo.cp.analyze.AnalyzeGroupCodeVO;
import com.mall4j.cloud.biz.vo.cp.analyze.CpAutoGroupCodeSendUserVO;
import com.mall4j.cloud.biz.vo.cp.analyze.CpTagGroupCodeSendUserVO;
import com.mall4j.cloud.biz.vo.cp.analyze.CpTagGroupCodeStaffVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.LambdaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 群活码数据分析
 *
 * @author gmq
 * @date 2023-10-27 16:59:44
 */
@Service
public class CpDetailGroupCodeAnalyzeServiceImpl implements CpDetailGroupCodeAnalyzeService {

	@Autowired
	private GroupCodeRefService groupCodeRefService;
	@Autowired
	private GroupCodeRefService refService;
	@Autowired
	private StaffFeignClient staffFeignClient;
	@Autowired
	private CpTaskUserRefMapper cpTaskUserRefMapper;
	@Autowired
	private UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;
	@Autowired
	private CpAutoGroupCodeUserMapper autoGroupCodeUserMapper;
	@Autowired
	private CpAutoGroupCodeService autoGroupCodeService;
	@Autowired
	private GroupCreateTaskService groupCreateTaskService;

	private List<UserStaffCpRelationListVO> getStaffRel(String userName){
		UserStaffCpRelationSearchDTO staffCpRelationSearchDTO=new UserStaffCpRelationSearchDTO();
		staffCpRelationSearchDTO.setUserName(userName);
		staffCpRelationSearchDTO.initPage();
		ServerResponseEntity<List<UserStaffCpRelationListVO>>  responseEntity=userStaffCpRelationFeignClient.getUserStaffRelBy(staffCpRelationSearchDTO);
		ServerResponseEntity.checkResponse(responseEntity);
		return responseEntity.getData();
	}

	private List<String> getUserQiWeiUserId(String userName){
		return getStaffRel(userName).stream().map(item->item.getQiWeiUserId()).collect(Collectors.toList());
	}

	/**
	 * 自动拉群-发送好友列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	@Override
	public PageVO<CpAutoGroupCodeSendUserVO> pageAutoCodeRelUser(PageDTO pageDTO, CpAutoGroupCodeAnalyzeDTO dto) {

		//需要匹配好友名称列表
		if(StrUtil.isNotEmpty(dto.getNickName())){
			List<String> userQiWeiUserIds=getUserQiWeiUserId(dto.getNickName());
			if(CollUtil.isEmpty(userQiWeiUserIds)){
				PageVO pageVO=new PageVO();
				pageVO.inint();
				return pageVO;
			}
			dto.setUserQiWeiUserIds(userQiWeiUserIds);
		}
		if(!initSelect(dto.getStaffName(),dto.getStaffs())){
			PageVO<CpAutoGroupCodeSendUserVO> pageVO=new PageVO<>();
			pageVO.inint();
			return pageVO;
		}

		PageVO<CpAutoGroupCodeSendUserVO> page=PageUtil.doPage(pageDTO, () -> autoGroupCodeUserMapper.listAutoCodeRelUser(dto));

		//获取员工信息
		if(CollUtil.isNotEmpty(page.getList())){
			List<Long> staffIds=page.getList().stream().map(item->item.getStaffId()).collect(Collectors.toList());
			ServerResponseEntity<List<StaffVO>> responseEntity= staffFeignClient.getStaffByIds(staffIds);
			ServerResponseEntity.checkResponse(responseEntity);

			Map<Long,StaffVO> staffVOMap= LambdaUtils.toMap(responseEntity.getData(),StaffVO::getId);

			for (CpAutoGroupCodeSendUserVO sendUserVO : page.getList()) {
				if(staffVOMap.containsKey(sendUserVO.getStaffId())){
					sendUserVO.setStaffName(staffVOMap.get(sendUserVO.getStaffId()).getStaffName());
				}
			}
		}

		return page;
	}

	/**
	 * 自动拉群-关联群聊列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	@Override
	public PageVO<AnalyzeGroupCodeVO> pageAutoCodeRelGroup(PageDTO pageDTO, CpAutoGroupCodeAnalyzeDTO dto) {
		//获取自动拉群&群活码
		CpAutoGroupCode cpAutoGroupCode=autoGroupCodeService.getById(dto.getCodeId());
		if(Objects.isNull(cpAutoGroupCode)){
			throw new LuckException("未获取到自动拉群信息");
		}

		AnalyzeGroupCodeDTO analyzeGroupCodeDTO=new AnalyzeGroupCodeDTO();
		analyzeGroupCodeDTO.setCodeId(cpAutoGroupCode.getId());
		analyzeGroupCodeDTO.setSourceFrom(CodeChannelEnum.AUTO_GROUP_CODE.getValue());
		PageVO<AnalyzeGroupCodeVO> page=PageUtil.doPage(pageDTO, () -> refService.selectListBy(analyzeGroupCodeDTO));

		for (AnalyzeGroupCodeVO analyzeGroupCodeVO : page.getList()) {
			//群聊状态
			analyzeGroupCodeVO.setStatus(groupCodeRefService.getGroupCodeRelStatus(analyzeGroupCodeVO.getScanCount(),
					analyzeGroupCodeVO.getUpperTotal(),
					analyzeGroupCodeVO.getStatus(),
					analyzeGroupCodeVO.getExpireEnd(),
					analyzeGroupCodeVO.getChatUserTotal()));
		}

		return page;
	}

	/**
	 * 标签建群-发送好友列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	@Override
	public PageVO<CpTagGroupCodeSendUserVO> pageTagCodeRelUser(PageDTO pageDTO, CpTagGroupCodeAnalyzeDTO dto) {
		//需要匹配好友名称列表
		if(StrUtil.isNotEmpty(dto.getNickName())){
			List<String> ids=getUserQiWeiUserId(dto.getNickName());
			if(CollUtil.isEmpty(ids)){
				PageVO pageVO=new PageVO();
				pageVO.setPages(0);
				pageVO.setTotal(0L);
				return pageVO;
			}
			dto.setUserQiWeiUserIds(ids);
		}
		if(!initSelect(dto.getStaffName(),dto.getStaffs())){
			PageVO<CpTagGroupCodeSendUserVO> pageVO=new PageVO<>();
			pageVO.inint();
			return pageVO;
		}

		PageVO<CpTagGroupCodeSendUserVO> page=PageUtil.doPage(pageDTO, () -> cpTaskUserRefMapper.list(dto));

		//获取员工信息
		if(CollUtil.isNotEmpty(page.getList())){
			List<Long> staffIds=page.getList().stream().map(item->item.getStaffId()).collect(Collectors.toList());
			ServerResponseEntity<List<StaffVO>> responseEntity= staffFeignClient.getStaffByIds(staffIds);
			ServerResponseEntity.checkResponse(responseEntity);

			Map<Long,StaffVO> staffVOMap= LambdaUtils.toMap(responseEntity.getData(),StaffVO::getId);

			for (CpTagGroupCodeSendUserVO sendUserVO : page.getList()) {
				if(staffVOMap.containsKey(sendUserVO.getStaffId())){
					sendUserVO.setStaffName(staffVOMap.get(sendUserVO.getStaffId()).getStaffName());
				}
			}
		}
		return page;
	}

	/**
	 * 标签建群-关联群聊列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	@Override
	public PageVO<AnalyzeGroupCodeVO> pageTagGroupRelGroup(PageDTO pageDTO, CpTagGroupCodeAnalyzeDTO dto) {
		//获取自动拉群&群活码
		CpGroupCreateTask cpGroupCreateTask=groupCreateTaskService.getById(dto.getTaskId());
		if(Objects.isNull(cpGroupCreateTask)){
			throw new LuckException("未获取到标签建群信息");
		}
		AnalyzeGroupCodeDTO analyzeGroupCodeDTO=new AnalyzeGroupCodeDTO();
		analyzeGroupCodeDTO.setCodeId(cpGroupCreateTask.getId());
		analyzeGroupCodeDTO.setSourceFrom(CodeChannelEnum.TAG_GROUP_CODE.getValue());
		refService.selectListBy(analyzeGroupCodeDTO);
		PageVO<AnalyzeGroupCodeVO> page=PageUtil.doPage(pageDTO, () -> refService.selectListBy(analyzeGroupCodeDTO));

		for (AnalyzeGroupCodeVO analyzeGroupCodeVO : page.getList()) {
			//群聊状态
			analyzeGroupCodeVO.setStatus(groupCodeRefService.getGroupCodeRelStatus(analyzeGroupCodeVO.getScanCount(),
					analyzeGroupCodeVO.getUpperTotal(),
					analyzeGroupCodeVO.getStatus(),
					analyzeGroupCodeVO.getExpireEnd(),
					analyzeGroupCodeVO.getChatUserTotal()));
		}

		return page;
	}

	/**
	 * 标签建群-关联员工
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	@Override
	public PageVO<CpTagGroupCodeStaffVO> pageTagGroupRelStaff(PageDTO pageDTO, CpTagGroupCodeAnalyzeStaffDTO dto) {
		if(!initSelect(dto.getStaffName(),dto.getStaffs())){
			PageVO<CpTagGroupCodeStaffVO> pageVO=new PageVO<>();
			pageVO.inint();
			return pageVO;
		}
		PageVO<CpTagGroupCodeStaffVO> page=PageUtil.doPage(pageDTO, () -> cpTaskUserRefMapper.countByStaff(dto));
		//获取员工信息
		if(CollUtil.isNotEmpty(page.getList())){
			List<Long> staffIds=page.getList().stream().map(item->item.getStaffId()).collect(Collectors.toList());
			ServerResponseEntity<List<StaffVO>> responseEntity= staffFeignClient.getStaffByIds(staffIds);
			ServerResponseEntity.checkResponse(responseEntity);

			Map<Long,StaffVO> staffVOMap= LambdaUtils.toMap(responseEntity.getData(),StaffVO::getId);

			for (CpTagGroupCodeStaffVO sendUserVO : page.getList()) {
				if(staffVOMap.containsKey(sendUserVO.getStaffId())){
					sendUserVO.setStaffName(staffVOMap.get(sendUserVO.getStaffId()).getStaffName());
				}
			}
		}
		return page;
	}

	private boolean initSelect(String staffName,List<Long> staffIds){
		if(StrUtil.isNotEmpty(staffName)){
			StaffQueryDTO staffQueryDTO=new StaffQueryDTO();
			staffQueryDTO.setStaffName(staffName);
			ServerResponseEntity<List<StaffVO>> responseEntity=staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
			ServerResponseEntity.checkResponse(responseEntity);
			if(CollUtil.isEmpty(responseEntity.getData())){
				return false;
			}
			staffIds.addAll(responseEntity.getData().stream().map(item->item.getId()).collect(Collectors.toList()));
			return true;
		}
		return true;
	}
}
