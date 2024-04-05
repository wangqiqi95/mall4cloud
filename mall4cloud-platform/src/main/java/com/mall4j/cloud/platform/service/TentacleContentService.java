package com.mall4j.cloud.platform.service;

import com.mall4j.cloud.api.platform.dto.TentacleContentDTO;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.model.TentacleContent;

/**
 * 触点内容信息
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
public interface TentacleContentService {

    /**
     * 分页获取触点内容信息列表
     *
     * @param pageDTO 分页参数
     * @return 触点内容信息列表分页数据
     */
    PageVO<TentacleContent> page(PageDTO pageDTO);

    /**
     * 根据触点内容信息id获取触点内容信息
     *
     * @param id 触点内容信息id
     * @return 触点内容信息
     */
    TentacleContent getById(Long id);

    /**
     * 保存触点内容信息
     *
     * @param tentacleContent 触点内容信息
     */
    void save(TentacleContent tentacleContent);

    /**
     * 更新触点内容信息
     *
     * @param tentacleContent 触点内容信息
     */
    void update(TentacleContent tentacleContent);

    /**
     * 根据触点内容信息id删除触点内容信息
     *
     * @param id 触点内容信息id
     */
    void deleteById(Long id);


    /**
     * 创建或者查询触点对象
     * C端  分享之前调用生成触点对象
     * C端  A分享给B , B访问时  生成触点对象
     * @return
     */
    TentacleContentVO findOrCreateByContent(TentacleContentDTO tentacleContent);

    TentacleContentVO findByTentacleNo(String tentacleNo);

}
