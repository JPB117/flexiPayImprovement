package com.workpoint.icpak.client.ui.component;

import com.google.gwt.user.client.ui.TextBox;

public class TextField extends TextBox {

	public void setPlaceholder(String placeHolderValue) {
		getElement().setAttribute("placeHolder", placeHolderValue);
	}

	public void setClass(String className) {
		setStyleName(className);
	}

	public void setType(String type) {
		getElement().setAttribute("type", type);
	}

	public void setDataToggle(String dataToggle) {
		getElement().setAttribute("data-toggle", dataToggle);
	}

	public void setAriaHaspopup(String ariaHasPopup) {
		getElement().setAttribute("aria-haspopup", ariaHasPopup);
	}

	public void setAriaExpanded(String ariaExpanded) {
		getElement().setAttribute("aria-expanded", ariaExpanded);
	}
	public void setDisabled(Boolean isDisabled) {
		if (isDisabled) {
			getElement().setAttribute("disabled", "disabled");
		}
	}

	public void setRequired(Boolean isRequired) {
		if (isRequired) {
			getElement().setAttribute("required", "required");
		}
	}

	public void setMaxLength(String maxLength) {
		getElement().setAttribute("max-length", maxLength);
	}

	public void setSize(String size) {
		getElement().setAttribute("size", size);
	}

	public void setAutoComplete(boolean autocomplete) {
		if (autocomplete) {
			getElement().setAttribute("autocomplete", "on");
		} else {
			getElement().setAttribute("autocomplete", "off");
		}
	}
}
