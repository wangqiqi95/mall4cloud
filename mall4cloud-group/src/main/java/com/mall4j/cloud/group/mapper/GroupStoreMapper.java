package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.group.model.GroupStore;
import com.mall4j.cloud.group.vo.TimeLimitedDiscountShopVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupStoreMapper extends BaseMapper<GroupStore> {

    void saveBatch(@Param("groupStoreList") List<GroupStore> groupStoreList);

    void deleteByGroupActivityId(@Param("groupActivityId") Long groupActivityId);

    List<GroupStore> selectByActivityId(@Param("groupActivityId") Long groupActivityId);


}
