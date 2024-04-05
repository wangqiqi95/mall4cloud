package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.File;
import java.util.Date;

/**
 * VO
 *
 * @author gmq
 * @date 2022-03-09
 */
@Data
public class WxQrCodeFileVO extends BaseVO {

    private String storeName;
    private String storeId;
    private String storeCode;
    private File file;
    private String filePath;
}
