package com.mall4j.cloud.product.event;

import com.mall4j.cloud.product.dto.SpuPageSearchDTO;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @Date 2022年4月27日, 0027 14:19
 * @Created by eury
 */
@Data
public class UpdateSkusPricefeeEvent {

    private File file;

    private Long downLoadHisId;
}
