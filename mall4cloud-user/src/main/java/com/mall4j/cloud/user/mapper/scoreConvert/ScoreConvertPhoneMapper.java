package com.mall4j.cloud.user.mapper.scoreConvert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.model.scoreConvert.ScoreConvertPhone;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 积分兑换
 *
 * @author shijing
 * @date 2021-12-10 18:02:09
 */

public interface ScoreConvertPhoneMapper extends BaseMapper<ScoreConvertPhone> {

    List<String> list(@Param("convertId") Long convertId, @Param("phoneNum") String phoneNum);

    void insertBatch(List<ScoreConvertPhone> list);

    void deleteBatch(@Param("id") Long id,@Param("phoneNums")List<String> phoneNums);

}
