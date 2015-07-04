package com.workpoint.icpak.client.ui.component.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.MyHTMLPanel;

public class TabContent extends Composite {
	
	@UiField MyHTMLPanel divItem;
	private static TabContentUiBinder uiBinder = GWT
			.create(TabContentUiBinder.class);
	private Boolean isActive;

	interface TabContentUiBinder extends UiBinder<Widget, TabContent> {
	}

	public TabContent(Widget content, String id, Boolean isActive) {
		initWidget(uiBinder.createAndBindUi(this));
		divItem.add(content);
		setId(id);
		setisActive(isActive);
	}
	
	public void setisActive(Boolean isActive) {
		
		this.isActive = isActive;
		if(isActive){
			divItem.addStyleName("active");
		}else{
			divItem.removeStyleName("active");
		}
	}

	public void setId(String id) {
		divItem.setCssId(id);
	}

	public boolean isActive() {
		return isActive;
	}
	
	
}
