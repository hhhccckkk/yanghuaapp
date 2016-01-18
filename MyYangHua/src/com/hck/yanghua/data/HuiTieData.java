package com.hck.yanghua.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hck.yanghua.bean.HuiTieBean;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class HuiTieData {
	@JsonProperty("data")
	private List<HuiTieBean> huiTieBeans;

	public List<HuiTieBean> getHuiTieBeans() {
		return huiTieBeans;
	}

	public void setHuiTieBeans(List<HuiTieBean> huiTieBeans) {
		this.huiTieBeans = huiTieBeans;
	}

}
