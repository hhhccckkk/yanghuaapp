package com.hck.yanghua.bean;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class HuiTieBean implements Serializable {
	@JsonProperty("hid")
	private Long hid;
	@JsonProperty("tid")
	private Long tid;
	@JsonProperty("content")
	private String content;
	@JsonProperty("time")
	private String time;
	@JsonProperty("image1")
	private String image1;
	@JsonProperty("iamge2")
	private String iamge2;
	@JsonProperty("iamge3")
	private String iamge3;
	@JsonProperty("type")
	private Integer type;
	@JsonProperty("address")
	private String address;
	@JsonProperty("name")
	private String name;
	@JsonProperty("uid")
	private long uid;
	@JsonProperty("fensi")
    private int fensi;
	@JsonProperty("nan")
    private boolean xingbie;
	@JsonProperty("touxiang")
    private String touxiang;
	@JsonProperty("yuanTie")
    private String yuantie;
	@JsonProperty("huifuUserName")
    private String huifuUserName;
    
	public String getHuifuUserName() {
		return huifuUserName;
	}

	public void setHuifuUserName(String huifuUserName) {
		this.huifuUserName = huifuUserName;
	}

	public String getYuantie() {
		return yuantie;
	}

	public void setYuantie(String yuantie) {
		this.yuantie = yuantie;
	}

	public String getTouxiang() {
		return touxiang;
	}

	public void setTouxiang(String touxiang) {
		this.touxiang = touxiang;
	}

	public int getFensi() {
		return fensi;
	}

	public void setFensi(int fensi) {
		this.fensi = fensi;
	}

	public boolean isXingbie() {
		return xingbie;
	}

	public void setXingbie(boolean xingbie) {
		this.xingbie = xingbie;
	}

	public Long getHid() {
		return hid;
	}

	public void setHid(Long hid) {
		this.hid = hid;
	}

	public Long getTid() {
		return tid;
	}

	public void setTid(Long tid) {
		this.tid = tid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getIamge2() {
		return iamge2;
	}

	public void setIamge2(String iamge2) {
		this.iamge2 = iamge2;
	}

	public String getIamge3() {
		return iamge3;
	}

	public void setIamge3(String iamge3) {
		this.iamge3 = iamge3;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

}
