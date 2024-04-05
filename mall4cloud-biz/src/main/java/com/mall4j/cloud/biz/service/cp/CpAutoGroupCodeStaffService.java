package com.mall4j.cloud.biz.service.cp;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.cp.CpAutoGroupCodeStaffSelectDTO;
import com.mall4j.cloud.biz.model.cp.CpAutoGroupCodeStaff;
import com.mall4j.cloud.biz.vo.cp.CpAutoGroupCodeStaffVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

/**
 * 
 *
 * @author gmq
 * @date 2023-10-27 16:59:44
 */
public interface CpAutoGroupCodeStaffService extends IService<CpAutoGroupCodeStaff> {

	/**
	 * 分页获取列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	PageVO<CpAutoGroupCodeStaffVO> page(PageDTO pageDTO,CpAutoGroupCodeStaffSelectDTO dto);

	/**
	 * 根据id获取
	 *
	 * @param id id
	 * @return 
	 */
	CpAutoGroupCodeStaff getById(Long id);

	/**
	 * 保存
	 * @param cpAutoGroupCodeStaff 
	 */
	boolean save(CpAutoGroupCodeStaff cpAutoGroupCodeStaff);

	/**
	 * 更新
	 * @param cpAutoGroupCodeStaff 
	 */
	void update(CpAutoGroupCodeStaff cpAutoGroupCodeStaff);

	/**
	 * 根据id删除
	 * @param id id
	 */
	void deleteById(Long id);
}
