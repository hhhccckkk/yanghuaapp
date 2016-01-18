package com.hck.yanghua.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hck.yanghua.bean.ZanBean;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class ZanData {
	@JsonProperty("data")
	private List<ZanBean> zanBean;

	public List<ZanBean> getZanBean() {
		return zanBean;
	}

	public void setZanBean(List<ZanBean> zanBean) {
		this.zanBean = zanBean;
	}



}
