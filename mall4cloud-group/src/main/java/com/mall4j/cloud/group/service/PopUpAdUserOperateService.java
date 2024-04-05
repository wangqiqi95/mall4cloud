package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.PopUpAdClickDTO;
import com.mall4j.cloud.group.dto.PopUpAdCouponReceiveDTO;
import com.mall4j.cloud.group.dto.PopUpAdUserOperatePageDTO;
import com.mall4j.cloud.group.model.PopUpAdUserOperate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.group.vo.OperateStatisticsVO;
import com.mall4j.cloud.group.vo.PopUpAdUserOperateVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ben
 * @since 2023-04-21
 */
public interface PopUpAdUserOperateService extends IService<PopUpAdUserOperate> {


    /**
     * 广告用户操作统计
     * */
    ServerResponseEntity<OperateStatisticsVO> operateStatisticsByAdId(Long adId);



    ServerResponseEntity<PageVO<PopUpAdUserOperateVO>> operateRecordByAdId(PopUpAdUserOperatePageDTO pageDTO);

    ServerResponseEntity click(PopUpAdClickDTO popUpAdClickDTO);

    ServerResponseEntity downloadData(Long adId, HttpServletResponse response);

    ServerResponseEntity couponReceive(PopUpAdCouponReceiveDTO popUpAdCouponReceiveDTO);
}
