package com.mall4j.cloud.biz.service.cp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.cp.CpPhoneLibraryDTO;
import com.mall4j.cloud.biz.model.cp.CpPhoneLibrary;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 引流手机号库
 *
 * @author gmq
 * @date 2023-10-30 17:13:11
 */
public interface CpPhoneLibraryService extends IService<CpPhoneLibrary> {

	/**
	 * 分页获取引流手机号库列表
	 * @param pageDTO 分页参数
	 * @return 引流手机号库列表分页数据
	 */
	PageVO<CpPhoneLibrary> page(PageDTO pageDTO,CpPhoneLibraryDTO cpPhoneLibraryDTO);

	/**
	 * 根据引流手机号库id获取引流手机号库
	 *
	 * @param id 引流手机号库id
	 * @return 引流手机号库
	 */
	CpPhoneLibrary getById(Long id);

	CpPhoneLibrary getByPhone(String phone);

	void createAndUpdate(CpPhoneLibraryDTO phoneLibraryDTO);

	/**
	 * 根据引流手机号库id删除引流手机号库
	 * @param id 引流手机号库id
	 */
	void deleteById(Long id);

	void importExcel(MultipartFile file);

	void refeshStatus();
}
