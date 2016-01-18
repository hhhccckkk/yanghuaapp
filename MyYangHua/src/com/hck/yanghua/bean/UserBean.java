package com.hck.yanghua.bean;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class UserBean implements Serializable{
	 @JsonProperty("uid")
	private Long uid;
	 @JsonProperty("name")
	private String name;
	 @JsonProperty("password")
	private String password;
	 @JsonProperty("touxiang")
	private String touxiang;
	 @JsonProperty("type")
	private Integer type;
	 @JsonProperty("xingbie")
	private Integer xingbie;
	 @JsonProperty("jifeng")
	private Long jifeng;
	 @JsonProperty("jinbi")
	private Integer jinbi;
	 @JsonProperty("fensi")
	private Integer fensi;
	 @JsonProperty("guanzhu")
	private Integer guanzhu;
	 @JsonProperty("dongtai")
	private Integer dongtai;
	 @JsonProperty("jingdu")
	private Double jingdu;
	 @JsonProperty("weidu")
	private Double weidu;
	 @JsonProperty("address")
	private String address;
	 @JsonProperty("aihao")
	private String aihao;
	 @JsonProperty("time")
	private String time;
	 @JsonProperty("logintime")
	private String logintime;
	 @JsonProperty("userId")
	private String userId;
	 @JsonProperty("imei")
	private String imei;
	 @JsonProperty("city")
	private String city;
	 @JsonProperty("pushid")
	private String pushid;
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTouxiang() {
		return touxiang;
	}
	public void setTouxiang(String touxiang) {
		this.touxiang = touxiang;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getXingbie() {
		return xingbie;
	}
	public void setXingbie(Integer xingbie) {
		this.xingbie = xingbie;
	}
	public Long getJifeng() {
		return jifeng;
	}
	public void setJifeng(Long jifeng) {
		this.jifeng = jifeng;
	}
	public Integer getJinbi() {
		return jinbi;
	}
	public void setJinbi(Integer jinbi) {
		this.jinbi = jinbi;
	}
	public Integer getFensi() {
		return fensi;
	}
	public void setFensi(Integer fensi) {
		this.fensi = fensi;
	}
	public Integer getGuanzhu() {
		return guanzhu;
	}
	public void setGuanzhu(Integer guanzhu) {
		this.guanzhu = guanzhu;
	}
	public Integer getDongtai() {
		return dongtai;
	}
	public void setDongtai(Integer dongtai) {
		this.dongtai = dongtai;
	}
	public Double getJingdu() {
		return jingdu;
	}
	public void setJingdu(Double jingdu) {
		this.jingdu = jingdu;
	}
	public Double getWeidu() {
		return weidu;
	}
	public void setWeidu(Double weidu) {
		this.weidu = weidu;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAihao() {
		return aihao;
	}
	public void setAihao(String aihao) {
		this.aihao = aihao;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLogintime() {
		return logintime;
	}
	public void setLogintime(String logintime) {
		this.logintime = logintime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPushid() {
		return pushid;
	}
	public void setPushid(String pushid) {
		this.pushid = pushid;
	}
	
	
}
