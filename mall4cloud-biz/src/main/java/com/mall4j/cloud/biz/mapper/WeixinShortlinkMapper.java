package com.mall4j.cloud.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.WeixinShortlinkRecordDTO;
import com.mall4j.cloud.biz.dto.WeixinShortlinkRecordItemDTO;
import com.mall4j.cloud.biz.dto.WeixinShortlinkRecordItemPageDTO;
import com.mall4j.cloud.biz.model.WeixinShortlink;
import com.mall4j.cloud.biz.vo.WeixinShortlinkRecordItemVo;
import com.mall4j.cloud.biz.vo.WeixinShortlinkRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 
 *
 * @author gmq
 * @date 2022-06-09 14:31:51
 */
public interface WeixinShortlinkMapper extends BaseMapper<WeixinShortlink> {

    /**
     * 查询短链列表
     * @param weixinShortlinkRecordDTO     短链数据
     * @return
     */
    List<WeixinShortlinkRecordVo> selectShortLinkRecordList(@Param("weixinShortlinkRecordDTO") WeixinShortlinkRecordDTO weixinShortlinkRecordDTO);

    List<WeixinShortlinkRecordItemVo> selectShortLinkRecordItemList(@Param("weixinShortlinkRecordItemDTO") WeixinShortlinkRecordItemDTO weixinShortlinkRecordItemDTO);

    List<WeixinShortlinkRecordItemVo> selectShortLinkRecordItemPage(@Param("weixinShortlinkRecordItemPageDTO") WeixinShortlinkRecordItemPageDTO weixinShortlinkRecordItemPageDTO);

    int selectShortLinkItemOfUserId(@Param("unionId") String unionId, @Param("shortLinkRecordId") String shortLinkRecordId);

}
