package com.mall4j.cloud.api.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * @Date 2022年3月22日, 0022 15:11
 * @Created by eury
 */
@Data
public class ExcelUploadDTO implements Serializable {

    private static final long serialVersionUID = 8692554187484866885L;

    @ApiModelProperty(value = "下载中心记录id",required = true)
    private Long downLoadHisId;

    @ApiModelProperty(value = "导出的数据",required = true)
    private List list;

    @ApiModelProperty(value = "表格名称",required = true)
    private String excelName;

    @ApiModelProperty(value = "导出的数据",required = true)
    private int mergeRowIndex;

    @ApiModelProperty(value = "哪一行开始导出",required = true)
    private int[] mergeColumnIndex;

    @ApiModelProperty(value = "导出泛型类",required = true)
    private Class clazz;

    @ApiModelProperty(value = "上传文件目录",required = true)
    private String excelMimoPath;

    @ApiModelProperty(value = "文件后缀类型(.xls/.csv)",required = false)
    private String excelType;

    @ApiModelProperty(value = "文件类型(application/vnd.ms-excel)",required = false)
    private String contentType;

    @ApiModelProperty(value = "是否导出文件：0否 1是",required = false)
    private Integer isFile=0;

    @ApiModelProperty(value = "文件存放目录",required = false)
    private String filePath;

    @ApiModelProperty(value = "文件名称",required = false)
    private String fileName;

    @ApiModelProperty("计算量")
    private Integer calCount;

    public ExcelUploadDTO(){}

    public ExcelUploadDTO(Long downLoadHisId,List list,String excelName,int mergeRowIndex,int[] mergeColumnIndex,Class clazz){
        this.downLoadHisId=downLoadHisId;
        this.list=list;
        this.excelName=excelName;
        this.mergeRowIndex=mergeRowIndex;
        this.mergeColumnIndex=mergeColumnIndex;
        this.clazz=clazz;

    }
}
