package com.mall4j.cloud.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.WeixinQrcodeTentacleStoreItemDTO;
import com.mall4j.cloud.biz.dto.WeixinShortlinkRecordDTO;
import com.mall4j.cloud.biz.model.WeixinQrcodeTentacleStoreItem;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleStoreItemVO;
import com.mall4j.cloud.biz.vo.WeixinShortlinkRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 触点记录详情表
 * @author ty
 * @date 2022-10-19 14:31:51
 */
public interface WeixinQrcodeTentacleStoreItemMapper extends BaseMapper<WeixinQrcodeTentacleStoreItem> {

    List<WeixinQrcodeTentacleStoreItemVO> selectTentacleStoreItemList(@Param("weixinQrcodeTentacleStoreItemDTO") WeixinQrcodeTentacleStoreItemDTO weixinQrcodeTentacleStoreItemDTO);

}
