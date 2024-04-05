package com.mall4j.cloud.transfer.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-04-26 17:08:58
 */
public class EtoMember extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String unionid;

    /**
     * 
     */
    private String memberid;

    /**
     * 
     */
    private String membercard;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getMemberid() {
		return memberid;
	}

	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}

	public String getMembercard() {
		return membercard;
	}

	public void setMembercard(String membercard) {
		this.membercard = membercard;
	}

	@Override
	public String toString() {
		return "EtoMember{" +
				"id=" + id +
				",unionid=" + unionid +
				",memberid=" + memberid +
				",membercard=" + membercard +
				'}';
	}
}
