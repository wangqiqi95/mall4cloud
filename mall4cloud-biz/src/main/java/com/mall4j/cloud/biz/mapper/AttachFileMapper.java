package com.mall4j.cloud.biz.mapper;


import com.mall4j.cloud.biz.dto.AttachFileDTO;
import com.mall4j.cloud.biz.model.AttachFile;
import com.mall4j.cloud.biz.vo.AttachFileVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 上传文件记录表
 *
 * @author YXF
 * @date 2020-11-21 10:21:40
 */
public interface AttachFileMapper {

	/**
	 * 获取上传文件记录表列表
	 *
	 * @param attachFile
	 * @return 文件记录表列表
	 */
	List<AttachFileVO> list(@Param("attachFile") AttachFileDTO attachFile);

	/**
	 * 根据店铺id保存文件记录
	 *
	 * @param attachFiles 上传文件记录表
	 * @param shopId      店铺id
	 * @param uid 		  uid
	 */
	void save(@Param("attachFiles") List<AttachFile> attachFiles, @Param("shopId") Long shopId, @Param("uid") Long uid);

	/**
	 * 更新上传文件记录表
	 *
	 * @param attachFile
	 * @param attachFile 上传文件记录表
	 */
	void update(@Param("attachFile") AttachFile attachFile);

	/**
	 * 根据上传文件记录表id删除上传文件记录表
	 *
	 * @param fileId
	 */
	void deleteById(@Param("fileId") Long fileId);

	/**
	 * 根据id获取文件信息
	 *
	 * @param fileId
	 * @return
	 */
    AttachFile getById(@Param("fileId") Long fileId);

	/**
	 * 批量更新文件的分组
	 * @param attachFileGroupId
	 */
	void updateBatchByAttachFileGroupId(@Param("attachFileGroupId") Long attachFileGroupId);

	/**
	 * 根据id列表获取文件信息列表
	 * @param ids
	 * @return
	 */
    List<AttachFileVO> getByIds(@Param("ids") List<Long> ids);

	/**
	 * 根据id列表批量删除文件记录
	 * @param ids
	 */
	void batchDeleteByIds(@Param("ids") List<Long> ids);

	/**
	 * 根据店铺id与文件id列表与分组id批量移动文件
	 * @param shopId
	 * @param ids
	 * @param groupId
	 */
    void batchMoveByShopIdAndIdsAndGroupId(@Param("shopId") Long shopId, @Param("ids") List<Long> ids, @Param("groupId") Long groupId);

	/**
	 * 根据uid更新店铺id
	 * @param shopId
	 * @param uid
	 */
	void updateShopIdByUid(@Param("shopId") Long shopId, @Param("uid") Long uid);

	/**
	 * 保存上传文件记录表
	 * @param attachFile 上传文件记录表
	 * @param uid
	 */
	void saveFile(@Param("attachFile")AttachFile attachFile,@Param("uid")Long uid);
}
