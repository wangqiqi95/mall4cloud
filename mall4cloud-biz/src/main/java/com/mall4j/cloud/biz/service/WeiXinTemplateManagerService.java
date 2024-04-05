package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.dto.WeixinTemplateManagerDTO;
import com.mall4j.cloud.biz.model.WeixinImgTemplate;
import com.mall4j.cloud.biz.vo.WeixinTemplageManagerVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 微信图片模板公共使用
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:53:22
 */
public interface WeiXinTemplateManagerService {

    WeixinTemplageManagerVO getTemplate(String templateId,String msgType);

    WeixinTemplageManagerVO saveTemplate(WeixinTemplateManagerDTO templateManagerDTO);

    List<WeixinTemplageManagerVO> saveTemplatesAndAutoResponses(WeixinTemplateManagerDTO templateManagerDTO);

}
