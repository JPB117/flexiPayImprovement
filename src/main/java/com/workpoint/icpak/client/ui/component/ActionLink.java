package com.workpoint.icpak.client.ui.component;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;

public class ActionLink extends Anchor {

	private Object model;

	public ActionLink() {
		addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// setStyleName("hidden");
			}
		});
	}

	public ActionLink(Object model) {
		this.model = model;
	}

	public ActionLink(String text) {
		super();
		setText(text);
	}

	public ActionLink(Object model, String text) {
		super();
		this.model = model;
		setText(text);
	}

	public void setDataToggle(String data) {
		getElement().setAttribute("data-toggle", data);
	}

	public void setDataTarget(String data) {
		getElement().setAttribute("data-target", data);
	}

	@Override
	public void setTitle(String title) {
		super.setTitle(title);
		getElement().setAttribute("data-original-title", title);
	}

	public void setDataOriginalTitle(String data) {
		getElement().setAttribute("data-original-title", data);
	}

	public void setDataPlacement(String data) {
		getElement().setAttribute("data-placement", data);
	}

	public void setAriaExpanded(String data) {
		getElement().setAttribute("aria-expanded", data);
	}

	public void setHtmlId(String data) {
		getElement().setAttribute("id", data);
	}

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}

	public void setLoadingState(Anchor anchor, boolean isLoading) {
		String previousText = anchor.getText();
		if (isLoading) {
			anchor.getElement().setAttribute("disabled", "disabled");
			anchor.getElement()
					.setInnerHTML("<span class='fa fa-spinner fa-spin' ui:field='spnSpinner'></span>Submitting...");
		} else {
			anchor.getElement().removeAttribute("disabled");
			anchor.setText(previousText);
		}
	}

	public void setLoadingState(boolean isLoading) {
		String previousText = this.getText();
		if (isLoading) {
			getElement().setAttribute("disabled", "disabled");
			getElement().setInnerHTML("<span class='fa fa-spinner fa-spin' ui:field='spnSpinner'></span>"+previousText);
		} else {
			getElement().removeAttribute("disabled");
			getElement().setInnerHTML(previousText);
		}
	}

}
