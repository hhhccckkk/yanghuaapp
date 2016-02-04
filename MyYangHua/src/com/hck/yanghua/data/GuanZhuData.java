package com.hck.yanghua.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hck.yanghua.bean.GuanZhuBean;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class GuanZhuData {
	@JsonProperty("data")
	private List<GuanZhuBean> guanZhuBeans;

	public List<GuanZhuBean> getGuanZhuBeans() {
		return guanZhuBeans;
	}

	public void setGuanZhuBeans(List<GuanZhuBean> guanZhuBeans) {
		this.guanZhuBeans = guanZhuBeans;
	}
	
}
