package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.api.biz.vo.MsgAttachment;
import com.mall4j.cloud.biz.dto.cp.*;
import com.mall4j.cloud.biz.model.cp.Material;
import com.mall4j.cloud.biz.vo.cp.CpMaterialBrowseRecordVO;
import com.mall4j.cloud.biz.vo.cp.MaterialVO;
import com.mall4j.cloud.biz.vo.cp.MiniMaterialVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 素材表
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
public interface MaterialService {

	/**
	 * 分页获取素材表列表
	 * @param pageDTO 分页参数
	 * @return 素材表列表分页数据
	 */
	PageVO<MaterialVO> page(PageDTO pageDTO, MaterialPageDTO request);

	/**
	 * 小程序端素材查询列表
	 * @return 素材表列表
	 */
	PageVO<MiniMaterialVO> miniPage(PageDTO pageDTO,MaterialPageDTO request);

	List<Material> getByIds(MaterialPageDTO dto);

	/**
	 * 根据素材表id获取素材表
	 *
	 * @param id 素材表id
	 * @return 素材表
	 */
	Material getById(Long id);

	/**
	 * 保存素材表
	 * @param material 素材表
	 * @param  msgList 消息列表
	 * @param storeIds 店列表
	 */
	void save(Material material, List<MaterialMsgDTO> msgList, List<Long> storeIds,List<CpMaterialLableDTO> lableList);

	/**
	 * 更新素材表
	 * @param material 素材表
	 */
	void update(Material material);

	/**
	 * 根据素材表id删除素材表
	 * @param id 素材表id
	 */
	void deleteById(Long id);

	void deleteByIds(List<Long> ids);

	/**
	 * 同步素材中心素材状态，将超过有效期的素材状态更改为【禁用】
	 */
	void syncCpMaterialStatus();

    PageVO<CpMaterialBrowseRecordVO> browsePage(PageDTO pageDTO, MaterialBrowsePageDTO request);

    List<CpMaterialBrowseRecordVO> soldBrowsePage(MaterialBrowsePageDTO request);

	void changeMenu(MaterialChangeMenuDTO request);

	/**
	 * 素材使用次数 +1
	 * 根据素材id返回素材内容，如果开启素材雷达时，不管素材原类型，这里统一返回类型为链接
	 * @param id
	 * @param staffId
	 * @return
	 */
	MsgAttachment useAndFindAttachmentById(Long id, Long staffId);

	MsgAttachment taskUseAndFindAttachmentById(Long id, Long staffId);

	void used(Long id);
}
