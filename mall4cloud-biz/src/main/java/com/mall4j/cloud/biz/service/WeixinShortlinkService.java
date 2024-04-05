package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.biz.dto.WeixinQrcodeTentacleStoreItemDTO;
import com.mall4j.cloud.biz.dto.WeixinShortlinkRecordDTO;
import com.mall4j.cloud.biz.dto.WeixinShortlinkRecordItemDTO;
import com.mall4j.cloud.biz.dto.WeixinShortlinkRecordItemPageDTO;
import com.mall4j.cloud.biz.vo.WeixinShortlinkRecordItemVo;
import com.mall4j.cloud.biz.vo.WeixinShortlinkRecordVo;
import com.mall4j.cloud.biz.vo.WeixinShortlinkVO;
import com.mall4j.cloud.biz.model.WeixinShortlink;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author gmq
 * @date 2022-06-09 14:31:51
 */
public interface WeixinShortlinkService extends IService<WeixinShortlink> {


    ServerResponseEntity<String> saveTo(WeixinShortlink weixinShortlink);

    WeixinShortlinkVO getShortkey(String shortkey);

    /**
     * 查询短链列表
     * @param weixinShortlinkRecordDTO     查询短链列表传参
     * @return
     */
    PageVO<WeixinShortlinkRecordVo> selectShortLinkRecordList(WeixinShortlinkRecordDTO weixinShortlinkRecordDTO);

    /**
     * 查询短链列表详情
     * @param weixinShortlinkRecordItemDTO 查询短链列表详情参数
     * @return
     */
    PageVO<WeixinShortlinkRecordItemVo> selectShortLinkRecordItemList(WeixinShortlinkRecordItemDTO weixinShortlinkRecordItemDTO);

    /**
     * 查询短链列表详情分页
     * @param weixinShortlinkRecordItemPageDTO 查询短链列表详情参数
     * @return
     */
    PageVO<WeixinShortlinkRecordItemVo> selectShortLinkRecordItemPage(WeixinShortlinkRecordItemPageDTO weixinShortlinkRecordItemPageDTO);

    /**
     * 短链列表详情导出
     * @param param 短链列表详情导出参数
     * @return
     */
    String shortLinkRecordItemExcel(WeixinShortlinkRecordItemPageDTO param);

    /**
     * 新增短链详情记录
     * @param shortLinkRecordId    短链记录表ID
     * @param code    微信Code
     * @return
     */
    ServerResponseEntity<String> saveShortLinkRecordItem(String shortLinkRecordId, String code) throws WxErrorException;

}
