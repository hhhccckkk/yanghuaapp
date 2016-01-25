package com.hck.yanghua.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hck.yanghua.bean.MsgBean;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class MsgData {
	@JsonProperty("data")
	private List<MsgBean> msgBeans;

	public List<MsgBean> getMsgBeans() {
		return msgBeans;
	}

	public void setMsgBeans(List<MsgBean> msgBeans) {
		this.msgBeans = msgBeans;
	}

}
