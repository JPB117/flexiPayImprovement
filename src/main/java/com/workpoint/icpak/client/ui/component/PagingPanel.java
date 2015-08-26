package com.workpoint.icpak.client.ui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class PagingPanel extends Composite {

	private static PagingPanelUiBinder uiBinder = GWT
			.create(PagingPanelUiBinder.class);

	interface PagingPanelUiBinder extends UiBinder<Widget, PagingPanel> {
	}

	@UiField
	BulletListPanel ankaPanel;
	@UiField
	SpanElement spnOffset;
	@UiField
	SpanElement spnPageEnd;
	@UiField
	SpanElement spnTotal;

	@UiField
	SpanElement spnTotalPages;

	@UiField
	IntegerField txtPageNo;

	@UiField
	ActionLink aNext;
	@UiField
	ActionLink aPrevous;
	@UiField
	ActionLink aLastPage;
	@UiField
	ActionLink aFirstPage;

	PagingLoader loader;

	PagingConfig config = new PagingConfig(0, 0);

	public PagingPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		txtPageNo.setValue(0);
		txtPageNo.addValueChangeHandler(new ValueChangeHandler<Integer>() {

			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				int count = event.getValue();
				if (count > config.getPages()) {
					count = config.getPages();
				}

				if (count < 0) {
					count = 0;
				}
				txtPageNo.setValue(count);
				move(count);
			}
		});

		aNext.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				move(-1);
			}
		});

		aPrevous.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				move(-2);
			}
		});

		aFirstPage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				move(1);
			}
		});

		aLastPage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				move(config.getPages());
			}
		});

	}

	@Override
	public Widget asWidget() {
		return super.asWidget();
	}

	public void setTotal(int total) {
		config = new PagingConfig(0, total);
		init();
	}

	private void init() {
		setPageDetails();
		ankaPanel.clear();

		if (config.getPages() > 0) {
			BulletPanel previous = getPanel(-2, "Previous");
			Anchor prevLink = (Anchor) previous.getWidget(0);
			prevLink.getElement().setAttribute("aria-label", "Previous");
			prevLink.getElement().setInnerHTML(
					"<span aria-hidden=\"true\">&laquo;</span>");
			ankaPanel.add(previous);
		}

		for (int i = 0; i < config.getPages(); i++) {
			ankaPanel.add(getPanel(i, (i + 1) + "", i == 0));
		}

		if (config.getPages() > 0) {
			BulletPanel next = getPanel(-1, "Next");
			Anchor nextLink = (Anchor) next.getWidget(0);
			nextLink.getElement().setAttribute("aria-label", "Next");
			nextLink.getElement().setInnerHTML(
					"<span aria-hidden=\"true\">&raquo;</span>");
			ankaPanel.add(next);
		}

	}

	private void setPageDetails() {
		spnOffset.setInnerText(config.getOffset() + "");
		spnPageEnd.setInnerText(config.getPageEnd() + "");
		spnTotal.setInnerText(config.getTotal() + "");
		spnTotalPages.setInnerText(config.getPages() + "");
	}

	public BulletPanel getPanel(int idx, String text) {
		return getPanel(idx, text, false);
	}

	public BulletPanel getPanel(int idx, String text, boolean isActive) {
		BulletPanel panel = new BulletPanel();
		ActionLink a = new ActionLink(idx, text);
		a.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int idx = (Integer) ((ActionLink) event.getSource()).getModel();
				move(idx);
			}
		});

		panel.add(a);

		if (isActive)
			panel.addStyleName("active");
		return panel;
	}

	protected void move(int idx) {
		int current = config.getCurrentPage();

		if (idx == -2) {
			// previous
			if (config.hasPrevious()) {
				config.previous();
//				ankaPanel.getWidget(widgetIdx).removeStyleName("active");
//				ankaPanel.getWidget(widgetIdx - 1).addStyleName("active");
				load(config.getOffset());
			}
		} else if (idx == -1) {
			// Next
			if (config.hasNext()) {
				config.next();
//				ankaPanel.getWidget(widgetIdx).removeStyleName("active");
//				ankaPanel.getWidget(widgetIdx + 1).addStyleName("active");
				load(config.getOffset());
			}
		} else {
			if(idx==config.getPages()){
				//move to last
				load(config.last());
			}else{
				config.setPage(idx);
				load(config.getOffset());
			}
//			ankaPanel.getWidget(widgetIdx).removeStyleName("active");
//			ankaPanel.getWidget(idx + 1).addStyleName("active");
			
		}

		txtPageNo.setValue(config.getCurrentPage());
	}

	private void load(int offset) {
		setPageDetails();
		if (loader != null) {
			loader.load(offset, config.getLimit());
		}
	}

	public PagingConfig getConfig() {
		return config;
	}

	public void setLoader(PagingLoader loader) {
		this.loader = loader;
	}

}
