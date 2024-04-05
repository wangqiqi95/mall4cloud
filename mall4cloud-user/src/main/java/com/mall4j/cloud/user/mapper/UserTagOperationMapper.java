package com.mall4j.cloud.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall4j.cloud.user.bo.UserTagOperationBO;
import com.mall4j.cloud.user.dto.QueryUserTagOperationPageDTO;
import com.mall4j.cloud.user.model.UserTagOperation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.vo.UserTagOperationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2023-02-09
 */
public interface UserTagOperationMapper extends BaseMapper<UserTagOperation> {

    void insertBatch(@Param("userTagOperationList") List<UserTagOperationBO> userTagOperationList);
    void insertBatchToImport(@Param("tagId") Long tagId, @Param("doUser") Long createUser, @Param("groupId") Long groupId,
                             @Param("operationState") Integer operationState, @Param("codeList") List<String> codeList);
    IPage<UserTagOperationVO> selectOperationByBeVipCode(@Param("page") IPage page,@Param("param") QueryUserTagOperationPageDTO pageDTO);
}
