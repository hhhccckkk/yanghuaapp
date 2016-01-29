package com.hck.yanghua.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hck.yanghua.bean.HuoDongBean;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class HuoDongdata {
	@JsonProperty("data")
	private List<HuoDongBean> huoDongBeans;

	public List<HuoDongBean> getHuoDongBeans() {
		return huoDongBeans;
	}

	public void setHuoDongBeans(List<HuoDongBean> huoDongBeans) {
		this.huoDongBeans = huoDongBeans;
	}
	

}
