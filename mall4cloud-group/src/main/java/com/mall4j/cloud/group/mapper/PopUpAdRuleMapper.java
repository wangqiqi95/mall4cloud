package com.mall4j.cloud.group.mapper;

import com.mall4j.cloud.group.bo.AddPopUpAdPushRuleBO;
import com.mall4j.cloud.group.dto.AddPopUpAdPushRuleDTO;
import com.mall4j.cloud.group.model.PopUpAdRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 开屏广告规则表 Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2023-04-21
 */
public interface PopUpAdRuleMapper extends BaseMapper<PopUpAdRule> {

    void insertBatch(@Param("popUpAdId") Long popUpAdRuleId, @Param("ruleList") List<AddPopUpAdPushRuleBO> ruleList,
                     @Param("createUser") Long createUser);

}
