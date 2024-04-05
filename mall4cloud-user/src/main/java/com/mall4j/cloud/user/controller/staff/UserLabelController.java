package com.mall4j.cloud.user.controller.staff;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.UserLabelDTO;
import com.mall4j.cloud.user.model.UserLabel;
import com.mall4j.cloud.user.service.UserLabelService;
import com.mall4j.cloud.user.vo.UserLabelVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 导购标签信息
 *
 * @author ZengFanChang
 * @date 2022-01-08 23:27:34
 */
@RestController("staffUserLabelController")
@RequestMapping("/s/user_label")
@Api(tags = "导购标签信息")
public class UserLabelController {

    @Autowired
    private UserLabelService userLabelService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取导购标签信息列表", notes = "分页获取导购标签信息列表")
	public ServerResponseEntity<PageVO<UserLabelVO>> page(@Valid PageDTO pageDTO, UserLabelDTO userLabelDTO) {
		PageVO<UserLabelVO> userLabelPage = userLabelService.page(pageDTO, userLabelDTO);
		return ServerResponseEntity.success(userLabelPage);
	}

    @PostMapping
    @ApiOperation(value = "保存导购标签信息", notes = "保存导购标签信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody UserLabelDTO userLabelDTO) {
        UserLabel userLabel = mapperFacade.map(userLabelDTO, UserLabel.class);
        userLabelService.save(userLabel);
        return ServerResponseEntity.success();
    }


    @PostMapping("/addUser")
    @ApiOperation(value = "导购标签用户添加", notes = "保存导购标签信息")
    public ServerResponseEntity<Void> addUser(@Valid @RequestBody UserLabelDTO userLabelDTO) {
        userLabelService.addUser(userLabelDTO);
        return ServerResponseEntity.success();
    }

//    @PutMapping
//    @ApiOperation(value = "更新导购标签信息", notes = "更新导购标签信息")
//    public ServerResponseEntity<Void> update(@Valid @RequestBody UserLabelDTO userLabelDTO) {
//        UserLabel userLabel = mapperFacade.map(userLabelDTO, UserLabel.class);
//        userLabelService.update(userLabel);
//        return ServerResponseEntity.success();
//    }

    @DeleteMapping
    @ApiOperation(value = "删除导购标签信息", notes = "根据导购标签信息id删除导购标签信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        userLabelService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
