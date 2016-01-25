package com.hck.yanghua.bean;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class MsgBean implements Serializable {

	public String getFatieUserTX() {
		return fatieUserTX;
	}

	public void setFatieUserTX(String fatieUserTX) {
		this.fatieUserTX = fatieUserTX;
	}

	@JsonProperty("fatieUserTX")
	private String fatieUserTX;

	@JsonProperty("fensi")
	private int fensi;
	@JsonProperty("fatieTime")
	private String fatieTime;
	@JsonProperty("saleOrNorm")
	private int saleOrNorm;
	@JsonProperty("xingbie")
	private int xingbie;
	@JsonProperty("address")
	private String address;
	@JsonProperty("image1")
	private String image1;
	@JsonProperty("image2")
	private String image2;
	@JsonProperty("image3")
	private String image3;
	@JsonProperty("image4")
	private String image4;
	@JsonProperty("image5")
	private String image5;
	@JsonProperty("tieziUserName")
	private String tieziUserName;

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
	@JsonProperty("userMsgId")
	private String userMsgId;

	public int getFensi() {
		return fensi;
	}

	public void setFensi(int fensi) {
		this.fensi = fensi;
	}

	public String getFatieTime() {
		return fatieTime;
	}

	public void setFatieTime(String fatieTime) {
		this.fatieTime = fatieTime;
	}

	public int getSaleOrNorm() {
		return saleOrNorm;
	}

	public void setSaleOrNorm(int saleOrNorm) {
		this.saleOrNorm = saleOrNorm;
	}

	public int getXingbie() {
		return xingbie;
	}

	public void setXingbie(int xingbie) {
		this.xingbie = xingbie;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getImage3() {
		return image3;
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}

	public String getImage4() {
		return image4;
	}

	public void setImage4(String image4) {
		this.image4 = image4;
	}

	public String getImage5() {
		return image5;
	}

	public void setImage5(String image5) {
		this.image5 = image5;
	}

	public String getTieziUserName() {
		return tieziUserName;
	}

	public void setTieziUserName(String tieziUserName) {
		this.tieziUserName = tieziUserName;
	}

	public String getUserMsgId() {
		return userMsgId;
	}

	public void setUserMsgId(String userMsgId) {
		this.userMsgId = userMsgId;
	}

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
