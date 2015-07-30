package com.workpoint.icpak.client.ui.membership;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.LIElement;

public class PageElement {

	private DivElement element;
	private String nextText;
	private String previousText;
	private boolean complete;
	private LIElement liElement;

	public PageElement(DivElement divPackage, String nextText, String prevText,
			LIElement tabElement) {
		this.setLiElement(tabElement);
		this.element = divPackage;
		this.nextText = nextText;
		this.previousText = prevText;
	}

	public PageElement(DivElement divPackage, String nextText,
			LIElement liElement) {
		this(divPackage, nextText, null, liElement);
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

	public boolean isComplete() {
		return complete;
	}

	public void setCompletion(boolean completion) {
		this.complete = completion;
	}

	public LIElement getLiElement() {
		return liElement;
	}

	public void setLiElement(LIElement liElement) {
		this.liElement = liElement;
	}

}
