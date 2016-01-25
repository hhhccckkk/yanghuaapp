package com.hck.yanghua.bean;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class TieZiBean extends UserBean implements Serializable{
	@JsonProperty("tid")
	private Long tid;
	@JsonProperty("content")
	private String content;
	@JsonProperty("tupian1")
	private String tupian1;
	@JsonProperty("tupian2")
	private String tupian2;
	@JsonProperty("tupian3")
	private String tupian3;
	@JsonProperty("tupian4")
	private String tupian4;
	@JsonProperty("tupian5")
	private String tupian5;
	@JsonProperty("time")
	private String time;
	@JsonProperty("pinglunsize")
	private Integer pinglunsize;
	@JsonProperty("dingsize")
	private Integer dingsize;
	@JsonProperty("isjinghua")
	private Integer isjinghua;
	@JsonProperty("iszhiding")
	private Integer iszhiding;
	@JsonProperty("istuijian")
	private Integer istuijian;
	@JsonProperty("tupian6")
	private String tupian6;
	@JsonProperty("tupian7")
	private String tupian7;
	@JsonProperty("type")
	private int tieZieType;
	@JsonProperty("huiFuTime")
    private String huifuTiem;
	@JsonProperty("name")
	private String name;
	@JsonProperty("isNan")
	private boolean isNan;
	@JsonProperty("address")
	private String address;
	
	
    
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isNan() {
		return isNan;
	}

	public void setNan(boolean isNan) {
		this.isNan = isNan;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHuifuTiem() {
		return huifuTiem;
	}

	public void setHuifuTiem(String huifuTiem) {
		this.huifuTiem = huifuTiem;
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

	public String getTupian1() {
		return tupian1;
	}

	public void setTupian1(String tupian1) {
		this.tupian1 = tupian1;
	}

	public String getTupian2() {
		return tupian2;
	}

	public void setTupian2(String tupian2) {
		this.tupian2 = tupian2;
	}

	public String getTupian3() {
		return tupian3;
	}

	public void setTupian3(String tupian3) {
		this.tupian3 = tupian3;
	}

	public String getTupian4() {
		return tupian4;
	}

	public void setTupian4(String tupian4) {
		this.tupian4 = tupian4;
	}

	public String getTupian5() {
		return tupian5;
	}

	public void setTupian5(String tupian5) {
		this.tupian5 = tupian5;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getTieZieType() {
		return tieZieType;
	}

	public void setTieZieType(int tieZieType) {
		this.tieZieType = tieZieType;
	}

	public Integer getPinglunsize() {
		return pinglunsize;
	}

	public void setPinglunsize(Integer pinglunsize) {
		this.pinglunsize = pinglunsize;
	}

	public Integer getDingsize() {
		return dingsize;
	}

	public void setDingsize(Integer dingsize) {
		this.dingsize = dingsize;
	}

	public Integer getIsjinghua() {
		return isjinghua;
	}

	public void setIsjinghua(Integer isjinghua) {
		this.isjinghua = isjinghua;
	}

	public Integer getIszhiding() {
		return iszhiding;
	}

	public void setIszhiding(Integer iszhiding) {
		this.iszhiding = iszhiding;
	}

	public Integer getIstuijian() {
		return istuijian;
	}

	public void setIstuijian(Integer istuijian) {
		this.istuijian = istuijian;
	}

	public String getTupian6() {
		return tupian6;
	}

	public void setTupian6(String tupian6) {
		this.tupian6 = tupian6;
	}

	public String getTupian7() {
		return tupian7;
	}

	public void setTupian7(String tupian7) {
		this.tupian7 = tupian7;
	}

}
