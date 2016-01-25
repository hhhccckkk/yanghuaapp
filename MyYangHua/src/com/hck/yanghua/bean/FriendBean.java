package com.hck.yanghua.bean;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class FriendBean implements Serializable {
	@JsonProperty("id")
	private long id;
	@JsonProperty("time")
	private String time;
	@JsonProperty("uid")
	private long uid;
	@JsonProperty("userName")
	private String userName;
	@JsonProperty("userImg")
	private String userImg;
	@JsonProperty("userMsgId")
	private String userMsgId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public String getUserMsgId() {
		return userMsgId;
	}

	public void setUserMsgId(String userMsgId) {
		this.userMsgId = userMsgId;
	}

}
