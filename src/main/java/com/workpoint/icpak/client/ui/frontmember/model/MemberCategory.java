package com.workpoint.icpak.client.ui.frontmember.model;

import com.workpoint.icpak.shared.model.Listable;

public class MemberCategory implements Listable {
	private String memberCategory;
	private String displayName;

	public MemberCategory(String memberCategory, String displayName) {
		this.memberCategory = memberCategory;
		this.displayName = displayName;
	}

	@Override
	public String getName() {
		return memberCategory;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}
}
