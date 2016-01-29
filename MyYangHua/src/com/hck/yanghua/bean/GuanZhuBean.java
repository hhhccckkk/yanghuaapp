package com.hck.yanghua.bean;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class GuanZhuBean {
	@JsonProperty("gid")
private long gid;
	@JsonProperty("uid")
private long uid;
	@JsonProperty("buid")
private long buid;
	public long getGid() {
		return gid;
	}
	public void setGid(long gid) {
		this.gid = gid;
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

}
