package com.jangmo.web.model.vo;

import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.user.MercenaryStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusVO {
	private MemberStatus memberStatus;
	private MercenaryStatus mercenaryStatus;
}
