package com.mall4j.cloud.api.docking.jos.dto;

import com.mall4j.cloud.api.docking.jos.service.IJosParam;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @description: 会员以及协议基本信息接口请求参数
 * @date 2021/12/23 22:17
 */
public class MemberAndProtocolInfoDto implements IJosParam {

	private static final long serialVersionUID = 4890070325873141200L;
	@ApiModelProperty(value = "平台编码，益世分配")
	private String platformCode;

	@ApiModelProperty(value = "请求编号，标志每次请求，用于后续异步通知的时候对应，业务自定”,(建议少于28个字节)", required = true)
	private String requestId;

	@ApiModelProperty(value = "姓名，证件上的姓名", required = true)
	private String name;

	@ApiModelProperty(value = "证件号码，身份证的最后一位若为X，建议大写", required = true)
	private String certNo;

	/**
	 * 传代码201
	 * ("201","居民身份证"),
	 * ("202", "军官证"),
	 * ("203", "武警警官证"),
	 * ("204", "士兵证"),
	 * ("208", "外国护照"),
	 * ("209", "港澳同胞回乡证"),
	 * ("211","台胞证"),
	 * ("213","台湾居民来往大陆通行证"),
	 * ("214","大陆居民往来台湾通行证")
	 * ("210","港澳居民来往内地通行证")
	 * ("237","中华人民共和国港澳居民居住证")
	 *
	 * {@link com.mall4j.cloud.api.docking.jos.enums.CertType}
	 */
	@ApiModelProperty(value = "证件类型", required = true)
	private String certType;

	/**
	 * ('156','中华人民共和国')，传代码156
	 */
	@ApiModelProperty(value = "国籍代码", required = true)
	private String nationality;

	@ApiModelProperty(value = "电话号码，11位手机号码", required = true)
	private String telephone;

	@ApiModelProperty(value = "地址", required = true)
	private String address;

	@ApiModelProperty(value = "出生日期，yyyy-MM-dd；certType为201身份证时可以不传", required = true)
	private String birthDate;

	/**
	 * {@link com.mall4j.cloud.api.docking.jos.enums.Sex}
	 */
	@ApiModelProperty(value = "性别，0:男, 1:女；certType为201身份证时可以不传", required = true)
	private Integer sex;

	@ApiModelProperty(value = "注册时间，yyyy-MM-dd HH:mm:ss，在业务侧自然人注册的时间", required = true)
	private String registerTime;

	@ApiModelProperty(value = "协议文件列表", required = true)
	private List<MemberProtocal> memberProtocalList;

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public List<MemberProtocal> getMemberProtocalList() {
		return memberProtocalList;
	}

	public void setMemberProtocalList(List<MemberProtocal> memberProtocalList) {
		this.memberProtocalList = memberProtocalList;
	}

	@Override
	public String toString() {
		return "MemberAndProtocolInfoDto{" + "platformCode='" + platformCode + '\'' + ", requestId='" + requestId + '\'' + ", name='" + name + '\''
				+ ", certNo='" + certNo + '\'' + ", certType='" + certType + '\'' + ", nationality='" + nationality + '\'' + ", telephone='" + telephone + '\''
				+ ", address='" + address + '\'' + ", birthDate='" + birthDate + '\'' + ", sex=" + sex + ", registerTime='" + registerTime + '\''
				+ ", memberProtocalList=" + memberProtocalList + '}';
	}

	public static class MemberProtocal {
		@ApiModelProperty(value = "0:身份证正面 1：身份证反面 2：授权委托书 3：合同 4:其他附件", required = true)
		private Integer fileType;

		/**
		 * 说明：此参数尽量传0，使用url，使用文件流有可能会比较大，会超过jsf接口大小限制（8M）
		 */
		@ApiModelProperty(value = "0：url， 1：文件流", required = true)
		private String fileFormat;

		/**
		 * 说明：此参数尽量传url，使用文件流有可能会比较大，会超过jsf接口大小限制（8M）
		 */
		@ApiModelProperty(value = "文件格式为0时传url，文件格式为1时传文件流base64编码串", required = true)
		private String fileInfo;

		@ApiModelProperty(value = "例如：合同.doc", required = true)
		private String fileName;

		public Integer getFileType() {
			return fileType;
		}

		public void setFileType(Integer fileType) {
			this.fileType = fileType;
		}

		public String getFileFormat() {
			return fileFormat;
		}

		public void setFileFormat(String fileFormat) {
			this.fileFormat = fileFormat;
		}

		public String getFileInfo() {
			return fileInfo;
		}

		public void setFileInfo(String fileInfo) {
			this.fileInfo = fileInfo;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		@Override
		public String toString() {
			return "MemberProtocal{" + "fileType=" + fileType + ", fileFormat='" + fileFormat + '\'' + ", fileInfo='" + fileInfo + '\'' + ", fileName='"
					+ fileName + '\'' + '}';
		}
	}

	/**
	 * 当请求参数为
	 *
	 * @return
	 */
	@Override
	public String asJsonPropertiesKey() {
		return "memberAndProtocolInfoJson";
	}

	@Override
	public void setJosContext(JosIntefaceContext context) {
		this.platformCode = context.getPlatformCode();
		if (StringUtils.isBlank(this.requestId)) {
			this.requestId = context.getRequestId();
		}
	}
}
