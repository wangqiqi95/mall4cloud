package com.mall4j.cloud.user.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.UserTagDTO;
import com.mall4j.cloud.user.model.UserTag;
import com.mall4j.cloud.user.service.UserTagService;
import com.mall4j.cloud.user.vo.UserTagVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 客户标签
 *
 * @author cl
 * @date 2020-05-17 16:16:53
 */
@RestController("platformUserTagController")
@RequestMapping("/p/user_tag")
@Api(tags = "platform-客户标签")
public class UserTagController {

    @Autowired
    private UserTagService userTagService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取客户标签列表", notes = "分页获取客户标签列表")
	public ServerResponseEntity<PageVO<UserTagVO>> page(@Valid PageDTO pageDTO) {
		PageVO<UserTagVO> userTagPage = userTagService.page(pageDTO);
		return ServerResponseEntity.success(userTagPage);
	}

	@GetMapping
    @ApiOperation(value = "获取客户标签", notes = "根据userTagId获取客户标签")
    public ServerResponseEntity<UserTagVO> getByUserTagId(@RequestParam Long userTagId) {
        UserTagVO userTagVO = mapperFacade.map(userTagService.getByUserTagId(userTagId), UserTagVO.class);
        return ServerResponseEntity.success(userTagVO);
    }

    @PostMapping
    @ApiOperation(value = "保存客户标签", notes = "保存客户标签")
    public ServerResponseEntity<Void> save(@RequestBody @Validated(UserTagDTO.AddUserTag.class) UserTagDTO userTagDTO) {
        UserTag query = new UserTag();
        // 条件标签和手动标签的标签名称都不可以想等
        // query.setTagType(TagTypeEnum.CONDITION.value())
        /*int count = userTagService.count(query);
        if(Objects.equals(userTagDTO.getTagType(),TagTypeEnum.CONDITION.value()) && count + 1 > Constant.TAG_LIMIT_NUM){
            // 数量超过上限
            throw new LuckException("数量超过上限");
        }*/
        query.setTagName(userTagDTO.getTagName());
        int nameCount = userTagService.count(query);
        if(nameCount > 0){
            // 标签名称已存在！
            throw new LuckException("标签名称已存在！");
        }
        userTagService.addUserTag(userTagDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新客户标签", notes = "更新客户标签")
    public ServerResponseEntity<Void> update(@RequestBody @Validated(UserTagDTO.UpdateUserTag.class) UserTagDTO userTagDTO) {

        UserTag query = new UserTag();
//        query.setTagType(TagTypeEnum.CONDITION.value())
        query.setTagName(userTagDTO.getTagName());
        query.setUserTagId(userTagDTO.getUserTagId());
        int nameCount = userTagService.count(query);
        if(nameCount > 0){
            // 标签名称已存在！
            throw new LuckException("标签名称已存在！");
        }
        userTagService.updateUserTag(userTagDTO);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除客户标签", notes = "根据客户标签id删除客户标签")
    public ServerResponseEntity<Void> delete(@RequestParam Long userTagId) {
        userTagService.deleteById(userTagId);
        return ServerResponseEntity.success();
    }

    @GetMapping("/refresh")
    @ApiOperation(value = "刷新客户标签统计数据", notes = "通过id刷新客户标签统计数据")
    public ServerResponseEntity<UserTagVO> refreshUserTag(@RequestParam Long userTagId) {
        return ServerResponseEntity.success(userTagService.refreshConditionTag(userTagId));
    }

    @GetMapping("/tag_list" )
    @ApiOperation(value = "可以添加的标签列表", notes = "可以添加的标签列表")
    public ServerResponseEntity<List<UserTagVO>> getTagList() {
        return ServerResponseEntity.success(userTagService.list());
    }


    @GetMapping("/tag_page")
    @ApiOperation(value = "获取客户标签列表", notes = "分页获取客户标签列表")
    public ServerResponseEntity<PageVO<UserTagVO>> getTagPage(@Valid PageDTO pageDTO, UserTagDTO userTagDTO) {
        PageVO<UserTagVO> resPage = userTagService.getPage(pageDTO, userTagDTO);
        return ServerResponseEntity.success(resPage);
    }

}
