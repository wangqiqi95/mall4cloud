package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 *
 * @author hwy
 * @date 2022-02-16 12:01:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GroupTagRef extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;
	public GroupTagRef(){}
	public GroupTagRef(String groupId,String tagId){
		this.groupId = groupId;
		this.tagId = tagId;
	}
    /**
     * 
     */
    private String groupId;

    /**
     * 
     */
    private String tagId;
}
