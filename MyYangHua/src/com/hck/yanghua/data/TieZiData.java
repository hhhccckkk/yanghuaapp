package com.hck.yanghua.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hck.yanghua.bean.TieZiBean;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class TieZiData {
	@JsonProperty("data")
	private List<TieZiBean> tieZiBeans;

	public List<TieZiBean> getTieZiBeans() {
		return tieZiBeans;
	}

	public void setTieZiBeans(List<TieZiBean> tieZiBeans) {
		this.tieZiBeans = tieZiBeans;
	}

}
