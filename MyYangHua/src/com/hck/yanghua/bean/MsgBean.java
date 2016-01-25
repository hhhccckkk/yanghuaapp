package com.hck.yanghua.bean;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class MsgBean implements Serializable{
	@JsonProperty("id")
	private Long id;
	@JsonProperty("content")
	private String content;
	@JsonProperty("type")
	private Integer type;
	@JsonProperty("time")
	private String time;
	@JsonProperty("yuantie")
	private String yuantie;
	@JsonProperty("isRed")
    private int isRed;
	@JsonProperty("tid")
    private long tid;
	@JsonProperty("userName")
    private String userName;
	@JsonProperty("touxiang")
    private String touxiang;
	@JsonProperty("uid")
    private long uid;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getYuantie() {
		return yuantie;
	}
	public void setYuantie(String yuantie) {
		this.yuantie = yuantie;
	}
	public int getIsRed() {
		return isRed;
	}
	public void setIsRed(int isRed) {
		this.isRed = isRed;
	}
	public long getTid() {
		return tid;
	}
	public void setTid(long tid) {
		this.tid = tid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTouxiang() {
		return touxiang;
	}
	public void setTouxiang(String touxiang) {
		this.touxiang = touxiang;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
}
