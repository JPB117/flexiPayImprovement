package com.workpoint.icpak.client.ui;

import com.google.gwt.user.client.ui.Anchor;

public interface OnOptionSelected {
	void onSelect(String name, Anchor aLnk);
	void onSelect(String name);
}
