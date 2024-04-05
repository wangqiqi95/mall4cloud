package com.mall4j.cloud.platform.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;


/**
 * @Description 门店修改名称配置表
 * @Author axin
 * @Date 2022-11-08 11:35
 **/
@Data
@TableName("tz_store_rename_conf")
public class StoreRenameConf extends BaseModel {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 门店编码
     */
    @TableField("`code`")
    private String code;

    /**
     * 原始门店名
     */
    @TableField("`name`")
    private String name;

    /**
     * 修改后门店名
     */
    @TableField("`rename`")
    private String rename;

}
