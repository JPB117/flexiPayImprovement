package com.workpoint.icpak.client.ui.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.TabData;
import com.workpoint.icpak.client.ui.admin.AbstractTabItem;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.component.BulletPanel;

public class TabItem extends AbstractTabItem {

	private static TabItemUiBinder uiBinder = GWT.create(TabItemUiBinder.class);

	interface TabItemUiBinder extends UiBinder<Widget, TabItem> {
	}

	@UiField BulletPanel liContainter;
	@UiField Anchor aLink;
	@UiField Element eIcon;
	@UiField SpanElement spnText;
	
	public TabItem(TabData tabData) {
		super(tabData);
		initWidget(uiBinder.createAndBindUi(this));
		setText(data.getLabel());
		liContainter.getElement().setAttribute("role","presentation");
		
		TabDataExt data = (TabDataExt)tabData;
		if(data.getIconStyle()!=null){
			eIcon.setClassName(data.getIconStyle());
		}
	}

	@Override
	public Widget getLiContainer() {
		return liContainter;
	}

	@Override
	public Anchor getLink() {
		return aLink;
	}

	@Override
	public Element getNameEl() {
		return spnText;
	}
	
//	public boolean isFor(TaskType type){
//		if(!(data instanceof HomeTabData)){
//			return false;
//		}
//		
//		return type.name().equals(((HomeTabData)data).getKey());
//	}

}
