package com.mall4j.cloud.biz.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.MicroPageBurialPointRecordPageDTO;
import com.mall4j.cloud.biz.model.MicroPageBurialPointRecord;
import com.mall4j.cloud.biz.model.MicroPageBurialPointStatistics;
import com.mall4j.cloud.biz.vo.MicroPageBurialPointRecordVO;
import com.mall4j.cloud.common.database.dto.TimeBetweenDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微页面埋点数据表
 */
@Mapper
public interface MicroPageBurialPointRecordMapper extends BaseMapper<MicroPageBurialPointRecord> {

    /*
    * 根据微页面ID查询对应埋点的访问数据（分页）
    * */
    List<MicroPageBurialPointRecordVO> selectBurialPointRecordPage(@Param("params") MicroPageBurialPointRecordPageDTO pageDTO);

    /*
    * 根据微页面ID查询对应的埋点访问数据
    * */
    List<MicroPageBurialPointStatistics> selectBurialPointRecords(@Param("renovationId") Long renovationId, @Param("params") TimeBetweenDTO dto);

    /*
    * 根据UnionID查询此客户是否已经浏览过微页面
    * */
    Integer selectCountByUnionId(@Param("renovationId") Long renovationId, @Param("unionId") String unionId);
}
