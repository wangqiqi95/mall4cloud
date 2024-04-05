package com.mall4j.cloud.user.service;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.AddTagDTO;
import com.mall4j.cloud.user.dto.EditTagDTO;
import com.mall4j.cloud.user.dto.QueryTagPageDTO;
import com.mall4j.cloud.user.model.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.user.vo.TagAndGroupRelationVO;
import com.mall4j.cloud.api.user.vo.TagVO;


/**
 * <p>
 * 标签表 服务类
 * </p>
 *
 * @author ben
 * @since 2023-02-08
 */
public interface TagService extends IService<Tag> {

    ServerResponseEntity<TagAndGroupRelationVO> addTag(AddTagDTO addTagDTO);

    ServerResponseEntity<PageVO<TagVO>> getTagPageVO(QueryTagPageDTO queryTagPageDTO);

    ServerResponseEntity editTag(EditTagDTO editTagDTO);

    ServerResponseEntity<TagVO> getTag(Long tagId);

    ServerResponseEntity removeTag(Long tagId, Long groupId);


    ServerResponseEntity getTheTagListByVipCode(String vipCode);

}
