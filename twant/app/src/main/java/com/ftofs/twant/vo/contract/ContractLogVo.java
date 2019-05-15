package com.ftofs.twant.vo.contract;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 保障日志
 *
 * @author sjz
 * Created 2017/4/17 11:50
 */
public class ContractLogVo {
	private int logId;
	private int logStoreid;
	private String logStorename;
	private int logItemid;
	private String logItemname;
	private String logMsg;
	private String logAddtime;
	private String logRole;
	private int logUserid;
	private String logUsername;
	private String operater;
	
	public String getOperater() {
		return operater;
	}
	public void setOperater(String operater) {
		this.operater = operater;
	}
	public int getLogId() {
		return logId;
	}
	public void setLogId(int logId) {
		this.logId = logId;
	}
	public int getLogStoreid() {
		return logStoreid;
	}
	public void setLogStoreid(int logStoreid) {
		this.logStoreid = logStoreid;
	}
	public String getLogStorename() {
		return logStorename;
	}
	public void setLogStorename(String logStorename) {
		this.logStorename = logStorename;
	}
	public int getLogItemid() {
		return logItemid;
	}
	public void setLogItemid(int logItemid) {
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
	public int getLogUserid() {
		return logUserid;
	}
	public void setLogUserid(int logUserid) {
		this.logUserid = logUserid;
	}
	public String getLogUsername() {
		return logUsername;
	}
	public void setLogUsername(String logUsername) {
		this.logUsername = logUsername;
	}
}