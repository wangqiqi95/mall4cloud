package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.dto.AttachFileDTO;
import com.mall4j.cloud.biz.model.AttachFile;
import com.mall4j.cloud.biz.vo.AttachFileVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 上传文件记录表
 *
 * @author YXF
 * @date 2020-11-21 10:21:40
 */
public interface AttachFileService {

	/**
	 * 分页获取上传文件记录表列表
	 * @param pageDTO 分页参数
	 * @param attachFileDTO 文件名
	 * @return 上传文件记录表列表分页数据
	 */
	PageVO<AttachFileVO> page(PageDTO pageDTO, AttachFileDTO attachFileDTO);

	/**
	 * 根据文件id查询文件信息
	 * @param fileId
	 * @return
	 */
	AttachFileVO getById(Long fileId);

	/**
	 * 保存多个上传文件记录表
	 * @param attachFiles 上传多个文件记录表
	 */
	void save(List<AttachFile> attachFiles);

	/**
	 * 更新上传文件记录表
	 * @param attachFile 上传文件记录表
	 */
	void update(AttachFile attachFile);

	/**
	 * 根据上传文件记录表id删除上传文件记录表
	 * @param fileId
	 */
	void deleteById(Long fileId);

	/**
	 * 更新文件名称
	 * @param attachFile
	 * @return
	 */
	Boolean updateFileName(AttachFile attachFile);

	/**
	 * 根据文件Id列表与店铺id批量删除文件记录
	 * @param ids
	 * @param shopId
	 */
    void deleteByIdsAndShopId(List<Long> ids, Long shopId);

	/**
	 * 根据店铺id与文件id列表与分组id批量移动文件
	 * @param shopId
	 * @param ids
	 * @param groupId
	 */
	void batchMoveByShopIdAndIdsAndGroupId(Long shopId, List<Long> ids, Long groupId);

	/**
	 * 根据uid更新店铺id
	 * @param shopId
	 * @param uid
	 */
    void updateShopIdByUid(Long shopId, Long uid);

	/**
	 * 保存上传文件记录表
	 * @param attachFile 上传文件记录表
	 */
	void saveFile(AttachFile attachFile);
}
