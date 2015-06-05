package com.workpoint.icpak.client.ui.registration;

import com.google.gwt.dom.client.DivElement;

public class PageElement {

	private DivElement element;
	private String nextText;
	private String previousText;

	public PageElement(DivElement element, String nextText, String previousText) {
		this.element = element;
		this.nextText = nextText;
		this.previousText = previousText;
	}
	
	public PageElement(DivElement divElement, String nextText) {
		this(divElement,nextText,null);
	}

	public PageElement(DivElement element) {
		this.element = element;
	}

	public DivElement getElement() {
		return element;
	}

	public void setElement(DivElement element) {
		this.element = element;
	}

	public String getNextText() {
		return nextText;
	}

	public void setNextText(String nextText) {
		this.nextText = nextText;
	}

	public String getPreviousText() {
		return previousText;
	}

	public void setPreviousText(String previousText) {
		this.previousText = previousText;
	}

}
