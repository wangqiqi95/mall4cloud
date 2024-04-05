package com.mall4j.cloud.group.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.*;

import com.mall4j.cloud.group.vo.SignActivityListVO;
import com.mall4j.cloud.group.vo.SignActivityVO;
import com.mall4j.cloud.group.vo.SignDetailVO;
import com.mall4j.cloud.group.vo.SignGatherVO;
import com.mall4j.cloud.group.vo.app.SignActivityAppVO;

import javax.servlet.http.HttpServletResponse;

public interface SignActivityBizService {
    ServerResponseEntity<Void> saveOrUpdateSignActivity(SignActivityDTO param);

    ServerResponseEntity<SignActivityVO> detail(Integer id);

    ServerResponseEntity<PageVO<SignActivityListVO>> page(SignActivityPageDTO param);

    ServerResponseEntity<Void> enable(Integer id);

    ServerResponseEntity<Void> disable(Integer id);

    ServerResponseEntity<Void> delete(Integer id);

    ServerResponseEntity<SignActivityAppVO> signInfo(Long userId);

    ServerResponseEntity<Void> sign(UserSignDTO param);

    ServerResponseEntity<Void> series(UserSignDTO param);

    ServerResponseEntity<Void> disableUserNotice(UserNoticeDTO param);

    ServerResponseEntity<Void> enableUserNotice(UserNoticeDTO param);

    ServerResponseEntity<SignGatherVO> signGather(SignGatherDTO param);

    ServerResponseEntity<IPage<SignDetailVO>> signDetail(SignDetailDTO param);

    void signNormalDetailExport(HttpServletResponse response, SignDetailDTO param,Long downLoadHisId);
}
