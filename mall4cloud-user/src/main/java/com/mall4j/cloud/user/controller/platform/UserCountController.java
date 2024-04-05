package com.mall4j.cloud.user.controller.platform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.mall4j.cloud.api.user.vo.ExportUserCountVO;
import com.mall4j.cloud.api.user.vo.UserCountVO;
import com.mall4j.cloud.api.user.vo.UserRelactionAddWayVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.UserCountDTO;
import com.mall4j.cloud.user.service.UserCountService;
import com.mall4j.cloud.user.vo.UserRelCountDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * 好友统计
 *
 */
@RestController("userCountController")
@RequestMapping("/p/user_count")
@Api(tags = "好友统计")
public class UserCountController {

    @Autowired
    private UserCountService userCountService;

    @Autowired
	private MapperFacade mapperFacade;

    @GetMapping("/getTopData")
    @ApiOperation(value = "好友统计头部数据", notes = "好友统计头部数据")
    public ServerResponseEntity<UserRelCountDataVO> getTopData(UserCountDTO dto) {
        return ServerResponseEntity.success(userCountService.getUserRelCountDataVO(dto));
    }

    @GetMapping("/exportTopData")
    @ApiOperation(value = "导出好友统计头部数据", notes = "导出好友统计头部数据")
    public void exportTopData(UserCountDTO dto, HttpServletResponse response)throws IOException {
        UserRelCountDataVO userRelCountDataVO=userCountService.getUserRelCountDataVO(dto);

        List<UserRelCountDataVO> exportList= ListUtil.toList(userRelCountDataVO);

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        String fileName="exportTopData"+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        ExcelWriterBuilder writeWork = EasyExcelFactory.write(response.getOutputStream(), UserRelCountDataVO.class);
        ExcelWriterSheetBuilder sheet = writeWork.sheet();
        sheet.doWrite(exportList);
    }

	@GetMapping("/list")
	@ApiOperation(value = "好友统计排行榜", notes = "分页获取好友统计排行榜")
	public ServerResponseEntity<PageVO<UserCountVO>> page(UserCountDTO dto) {
		PageVO<UserCountVO> page = userCountService.page(dto);
		return ServerResponseEntity.success(page);
	}

    @GetMapping("/exportList")
    @ApiOperation(value = "导出好友统计排行榜", notes = "导出好友统计排行榜")
    public void exportList(UserCountDTO dto, HttpServletResponse response)throws IOException {
        List<UserCountVO> list=userCountService.pageList(dto);
        if(CollUtil.isEmpty(list)){
            throw new LuckException("无数据导出");
        }
        List<ExportUserCountVO> exportList=mapperFacade.mapAsList(list,ExportUserCountVO.class);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        String fileName="exportList"+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        ExcelWriterBuilder writeWork = EasyExcelFactory.write(response.getOutputStream(), ExportUserCountVO.class);
        ExcelWriterSheetBuilder sheet = writeWork.sheet();
        sheet.doWrite(exportList);
    }

	@GetMapping("/sex_analysis")
    @ApiOperation(value = "性别分析", notes = "性别分析")
    public ServerResponseEntity<List<UserCountVO>> getSexCount(UserCountDTO dto) {
        List<UserCountVO> countVOList = userCountService.getSexCount(dto);
        return ServerResponseEntity.success(countVOList);
    }

    @GetMapping("/chart_count")
    @ApiOperation(value = "好友图表统计", notes = "好友图表统计")
    public ServerResponseEntity<List<UserCountVO>> chartCount(UserCountDTO dto) {
        List<UserCountVO> countVOList = userCountService.getChartCount(dto);
        return ServerResponseEntity.success(countVOList);
    }

    @GetMapping("/addway_analysis")
    @ApiOperation(value = "来源分析", notes = "来源分析")
    public ServerResponseEntity<List<UserRelactionAddWayVO>> getUserRelactionAddWays(UserCountDTO dto) {
        return ServerResponseEntity.success(userCountService.getUserRelactionAddWays(dto));
    }

}
