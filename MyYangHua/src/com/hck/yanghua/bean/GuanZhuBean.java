package com.hck.yanghua.bean;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class GuanZhuBean {
	@JsonProperty("id")
	private long id;
	@JsonProperty("uid")
	private long uid;
	@JsonProperty("buid")
	private long buid;
	@JsonProperty("touxiang")
	private String touxiang;
	@JsonProperty("time")
	private String time;
	@JsonProperty("name")
	private String name;
	@JsonProperty("stringUid")
	private String stringUid;
	@JsonProperty("xingbie")
	private int xingbie;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getBuid() {
		return buid;
	}

	public void setBuid(long buid) {
		this.buid = buid;
	}

	public String getTouxiang() {
		return touxiang;
	}

	public void setTouxiang(String touxiang) {
		this.touxiang = touxiang;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStringUid() {
		return stringUid;
	}

	public void setStringUid(String stringUid) {
		this.stringUid = stringUid;
	}

	public int getXingbie() {
		return xingbie;
	}

	public void setXingbie(int xingbie) {
		this.xingbie = xingbie;
	}
}
