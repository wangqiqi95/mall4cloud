package com.mall4j.cloud.biz.service.cp;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.cp.CpMaterialMsgImgSelectDTO;
import com.mall4j.cloud.biz.model.cp.CpMaterialMsgImg;
import com.mall4j.cloud.biz.vo.cp.CpMaterialMsgImgVO;

import java.util.List;

/**
 * 
 *
 * @author gmq
 * @date 2023-12-06 16:14:28
 */
public interface CpMaterialMsgImgService extends IService<CpMaterialMsgImg> {

	void formtFileAndSaveTo(Long matId,Long matMsgId,String path,String fileName);

	List<CpMaterialMsgImgVO> filePreview(CpMaterialMsgImgSelectDTO dto);

}
