package com.hck.yanghua.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hck.yanghua.bean.FriendBean;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class FriendData {
	@JsonProperty("data")
	private List<FriendBean> friendBeans;

	public List<FriendBean> getFriendBeans() {
		return friendBeans;
	}

	public void setFriendBeans(List<FriendBean> friendBeans) {
		this.friendBeans = friendBeans;
	}

}
