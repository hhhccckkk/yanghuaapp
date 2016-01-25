package com.hck.yanghua.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_invite_info")
public class MsgInviteBean {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(columnName = "userName")
	private String userName;
	@DatabaseField(columnName = "content")
	private String content;
	@DatabaseField(columnName = "state")
	private int state;
	@DatabaseField(columnName = "touxiang")
	private String touxiang;
	@DatabaseField(columnName = "userMsgId")
	private String userMsgId;
	@DatabaseField(columnName = "userId")
	private long userId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getTouxiang() {
		return touxiang;
	}
	public void setTouxiang(String touxiang) {
		this.touxiang = touxiang;
	}
	public String getUserMsgId() {
		return userMsgId;
	}
	public void setUserMsgId(String userMsgId) {
		this.userMsgId = userMsgId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	

}
