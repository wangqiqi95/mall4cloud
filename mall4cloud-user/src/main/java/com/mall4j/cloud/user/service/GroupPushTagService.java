package com.mall4j.cloud.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.user.vo.TagVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.AddTagDTO;
import com.mall4j.cloud.user.dto.EditTagDTO;
import com.mall4j.cloud.user.dto.QueryTagPageDTO;
import com.mall4j.cloud.user.model.GroupPushTag;
import com.mall4j.cloud.user.model.Tag;
import com.mall4j.cloud.user.vo.TagAndGroupRelationVO;


public interface GroupPushTagService extends IService<GroupPushTag> {


}
