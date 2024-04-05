package com.mall4j.cloud.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.platform.dto.DownloadCenterQueryDTO;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.platform.model.DownloadCenter;
import com.mall4j.cloud.platform.model.OfflineHandleEventItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DownloadCenterMapper extends BaseMapper<DownloadCenter> {

    List<DownloadCenter> queryByPrams(@Param("dto") DownloadCenterQueryDTO downloadCenterQueryDTO);

}
