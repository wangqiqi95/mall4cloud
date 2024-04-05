package com.mall4j.cloud.coupon.service;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.dto.ChooseMemberEventExchangeRecordPageDTO;
import com.mall4j.cloud.coupon.dto.ChooseMemberEventMobileRelationPageDTO;
import com.mall4j.cloud.coupon.dto.DeleteChooseMemberEventMobileRelationDTO;
import com.mall4j.cloud.coupon.model.ChooseMemberEventMobileRelation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventMobileRelationVO;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 指定会员活动关系表 服务类
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
public interface ChooseMemberEventMobileRelationService extends IService<ChooseMemberEventMobileRelation> {

    ServerResponseEntity<PageVO<ChooseMemberEventMobileRelationVO>> getTheMobilePage(ChooseMemberEventMobileRelationPageDTO pageDTO);

    ServerResponseEntity removeRelationByIdList(DeleteChooseMemberEventMobileRelationDTO deleteMobileRelationDTO);

    ServerResponseEntity<String> export(ChooseMemberEventMobileRelationPageDTO param, HttpServletResponse response);
}
