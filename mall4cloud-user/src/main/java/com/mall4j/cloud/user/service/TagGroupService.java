package com.mall4j.cloud.user.service;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.AddTagGroupDTO;
import com.mall4j.cloud.user.dto.EditParentTagGroupDTO;
import com.mall4j.cloud.user.dto.EditTagGroupDTO;
import com.mall4j.cloud.user.dto.QueryTagGroupGradeDTO;
import com.mall4j.cloud.user.model.TagGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.user.vo.TagGroupVO;

import java.util.List;

/**
 * <p>
 * 标签组表 服务类
 * </p>
 *
 * @author ben
 * @since 2023-02-08
 */
public interface TagGroupService extends IService<TagGroup> {

    ServerResponseEntity addTagGroup(AddTagGroupDTO addTagGroupDTO);

    ServerResponseEntity updateTagGroup(EditTagGroupDTO editTagGroupDTO);

    ServerResponseEntity getFirstGrade(QueryTagGroupGradeDTO queryTagGroupGradeDTO);

    ServerResponseEntity<List<TagGroupVO>> getSecondGrade(QueryTagGroupGradeDTO queryTagGroupGradeDTO);

    ServerResponseEntity updateParentTagGroup(EditParentTagGroupDTO editTagGroupDTO);
}
