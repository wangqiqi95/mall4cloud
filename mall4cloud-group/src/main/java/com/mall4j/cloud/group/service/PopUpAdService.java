package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.AddPopUpAdDTO;
import com.mall4j.cloud.group.dto.QueryPopUpAdPageDTO;
import com.mall4j.cloud.group.dto.UpdatePopUpAdDTO;
import com.mall4j.cloud.group.model.PopUpAd;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.group.vo.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ben
 * @since 2023-04-21
 */
public interface PopUpAdService extends IService<PopUpAd> {
    /**
     *新建开屏广告
     * */
    ServerResponseEntity createPopUpAd(AddPopUpAdDTO addPopUpAdDTO);


    /**
     *新建开屏广告
     * */
    ServerResponseEntity updatePopUpAd(UpdatePopUpAdDTO updatePopUpAdDTO);


    /**
     *删除开屏广告
     * */
    ServerResponseEntity remove(Long adId);

    /**
     *启用或禁用开屏广告
     * */
    ServerResponseEntity<Boolean> enableOrDisableAd(Long adId);

    /**
     *弹出广告内容
     * */
    ServerResponseEntity<PopUpAdContainerVO> popUpByAdIdList(List<Long> adIdList,String tempUid,Long storeId, String entrance);


    /**
     *获取用户对应页面所需弹出广告数据
     * */
    ServerResponseEntity<Map<String, List<Long>>> getThePopUpAdTreeByUser(Long storeId);

    /**
     * 管理后台分页查询功能
     * */
    ServerResponseEntity<PageVO<PopUpAdDataPageVO>> getPage(QueryPopUpAdPageDTO pageDTO);


    ServerResponseEntity<PopUpAdVO> getPopUpAdById(Long adId);
}
