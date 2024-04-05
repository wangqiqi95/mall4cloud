package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 素材 会员浏览记录
 *
 * @author FrozenWatermelon
 * @date 2023-10-24 18:42:12
 */
@Data
public class CpMaterialBrowseRecord extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 素材id
     */
    private Long matId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 备注
     */
    private String remark;

    /**
     * userid
     */
    private Long userId;

    /**
     * union_id
     */
    private String unionId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 浏览时长
     */
    private Integer browseDuration;

    /**
     * 浏览次数
     */
    private Integer browseCount;

    /**
     * 标签id
     */
    private String labalId;

    /**
     * 标签name
     */
    private String labalName;

    /**
     * 唯一id，由前端传值。同一id则为同一次浏览记录
     */
    private String browseId;

    /**
     * 是否打标 0否1是
     */
    private Integer status;

    /**
     * 员工id
     */
    private Long staffId;

}
