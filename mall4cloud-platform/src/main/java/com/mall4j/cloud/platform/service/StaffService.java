package com.mall4j.cloud.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.platform.dto.StaffBindQiWeiDTO;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.vo.StaffInfoVO;
import com.mall4j.cloud.api.platform.vo.StaffOrgVO;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.dto.ImportStaffWeChatDto;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.model.Staff;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 员工信息
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
public interface StaffService extends IService<Staff> {

	/**
	 * 分页获取员工信息列表
	 * @param pageDTO 分页参数
	 * @return 员工信息列表分页数据
	 */
	PageVO<StaffVO> page(PageDTO pageDTO, StaffQueryDTO queryDTO);

	/**
	 * 员工下拉选择列表
	 * @param queryDTO
	 * @return
	 */
	List<StaffVO> selectList(StaffQueryDTO queryDTO);

	/**
	 * 根据手机号、工号筛选员工
	 * staffListByMN
	 * @param queryDTO
	 * @return
	 */
	List<StaffVO> staffListByMN(StaffQueryDTO queryDTO);

	/**
	 * 根据员工信息id获取员工信息
	 *
	 * @param id 员工信息id
	 * @return 员工信息
	 */
	StaffVO getById(Long id);

	StaffInfoVO info(Long id);

    /**
     * 根据员工id列表获取员工信息
     *
     * @param id 员工信息id
     * @return 员工信息
     */
    List<StaffVO> getByIds(List<Long> id);

	/**
	 * 通过企微用户id查询员工
	 *
	 * @param qiWeiUserId
	 * @return
	 */
	StaffVO getByQiWeiUserId(String qiWeiUserId);

	/**
	 * 通过手机号查询员工
	 *
	 * @param mobile
	 * @return
	 */
	StaffVO getByMobile(String mobile);

	/**
	 * 通过员工编码查询员工
	 *
	 * @param staffCode
	 * @return
	 */
	StaffVO getByStaffCode(String staffCode);

	/**
	 * 保存员工信息
	 * @param staff 员工信息
	 * @return
	 */
	void add(Staff staff);

	void openPlatform(Long sysUserId);

	/**
	 * 批量保存员工信息
	 * @param staffs
	 */
	void saveBatch(List<Staff> staffs);

	/**
	 * 更新员工信息
	 * @param staff 员工信息
	 */
	void update(Staff staff,boolean pushWx);


	void deleteSysUserId(String sysUserId);

	StaffVO getBySysUserId(String sysUserId);

	/**
	 * 修改会话存档开启状态
	 * @param qiWeiUserId
	 */
	void updateCpMsgAuditByUserId(String qiWeiUserId,Integer cpMsgAudit);


	/**
	 * 更新员工微信号
	 * @param staffs
	 */
	int updateBatchWeChatNoByStaffNo(List<ImportStaffWeChatDto> staffs);
	/**
	 * 通过查询参数查询员工列表
	 *
	 * @param staffQueryDTO
	 * @return
	 */
	List<StaffVO> findByStaffQueryDTO(StaffQueryDTO staffQueryDTO);

    Integer countByStaffNum(StaffQueryDTO staffQueryDTO);

	void bindStaffQiWeiUserId(StaffBindQiWeiDTO staffBindQiWeiDTO);

	List<Staff> getStaffListByMobiles(List<String> mobiles);

	List<Staff> getStaffListByStaffNos(List<String> staffNos);


	/**
	 * 导出员工信息
	 * @param queryDTO
	 * @param response
	 */
	void soldStaffs(StaffQueryDTO queryDTO, HttpServletResponse response);

	String importExcelStaffs(MultipartFile file);


	List<StaffVO> listStaffByStatus(StaffQueryDTO staffQueryDTO);

	/**
	 * 获取员工部门信息
	 * @param staffIds
	 * @return
	 */
	List<StaffOrgVO> getStaffAndOrgs(List<Long> staffIds);

	ServerResponseEntity<List<StaffVO>> getByStaffNOOrNickName(String staff);


	ServerResponseEntity<List<StaffVO>> getStoreManagerByStoreIdList(List<Long> serviceStoreIdList);

}
