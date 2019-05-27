package com.ftofs.twant.domain.contract;
import java.io.Serializable;

public class ContractLog implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private Integer logId;
	
	/**
	 * 店铺ID
	 */
	private Integer logStoreid;
	
	/**
	 * 店铺名称
	 */
	private String logStorename;
	
	/**
	 * 服务项目ID
	 */
	private Integer logItemid;
	
	/**
	 * 服务项目名称
	 */
	private String logItemname;
	
	/**
	 * 操作描述
	 */
	private String logMsg;
	
	/**
	 * 添加时间
	 */
	private String logAddtime;
	
	/**
	 * 操作者角色 管理员为admin 商家为seller
	 */
	private String logRole;
	
	/**
	 * 操作者ID
	 */
	private Integer logUserid;
	
	/**
	 * 操作者名称
	 */
	private String logUsername;

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Integer getLogStoreid() {
		return logStoreid;
	}

	public void setLogStoreid(Integer logStoreid) {
		this.logStoreid = logStoreid;
	}

	public String getLogStorename() {
		return logStorename;
	}

	public void setLogStorename(String logStorename) {
		this.logStorename = logStorename;
	}

	public Integer getLogItemid() {
		return logItemid;
	}

	public void setLogItemid(Integer logItemid) {
		this.logItemid = logItemid;
	}

	public String getLogItemname() {
		return logItemname;
	}

	public void setLogItemname(String logItemname) {
		this.logItemname = logItemname;
	}

	public String getLogMsg() {
		return logMsg;
	}

	public void setLogMsg(String logMsg) {
		this.logMsg = logMsg;
	}

	public String getLogAddtime() {
		return logAddtime;
	}

	public void setLogAddtime(String logAddtime) {
		this.logAddtime = logAddtime;
	}

	public String getLogRole() {
		return logRole;
	}

	public void setLogRole(String logRole) {
		this.logRole = logRole;
	}

	public Integer getLogUserid() {
		return logUserid;
	}

	public void setLogUserid(Integer logUserid) {
		this.logUserid = logUserid;
	}

	public String getLogUsername() {
		return logUsername;
	}

	public void setLogUsername(String logUsername) {
		this.logUsername = logUsername;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}