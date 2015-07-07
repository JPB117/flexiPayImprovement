package com.workpoint.icpak.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.popup.GenericPopupPresenter;

public class AppManager {

	@Inject
	static MainPagePresenter mainPagePresenter;

	@Inject
	static GenericPopupPresenter popupPresenter;

	public static void showPopUp(String header, String content,
			final OnOptionSelected onOptionSelected, String... buttons) {
		showPopUp(header, new InlineLabel(content), onOptionSelected, buttons);
	}

	public static void showPopUp(String header, Widget widget,
			final OnOptionSelected onOptionSelected, String... buttons) {
		showPopUp(header, widget, null, onOptionSelected, buttons);
	}

	public static void showPopUp(String header, Widget widget,
			final String customPopupStyle,
			final OnOptionSelected onOptionSelected, String... buttons) {
		mainPagePresenter.removeFromPopupSlot(popupPresenter);
		popupPresenter.setHeader(header);
		popupPresenter.setInSlot(GenericPopupPresenter.BODY_SLOT, null);
		popupPresenter.setInSlot(GenericPopupPresenter.BUTTON_SLOT, null);

		popupPresenter.getView().setInSlot(GenericPopupPresenter.BODY_SLOT,
				widget);

		if (customPopupStyle != null) {
			popupPresenter.getView().addStyleName(customPopupStyle);
			// popupPresenter.getView().removeStyleName("modal");
		}

		for (final String text : buttons) {
			final Anchor aLnk = new Anchor();
			
			//TODO:-TOM  REMOVE THESE CUSTOM BUTTON STYLES. - MAKES IT IMPOSSIBLE TO CREATE OTHER BUTTONS
			//WORK ON A GENERIC PLACEMENT MECHANISM e.g Centered Buttons, 
			//AS WELL AS DYNAMIC STYLING BY PASSING BUTTON STYLES TO THIS MECHANISM
			
			if (text.equals("Cancel")) {
				aLnk.setHTML("&nbsp;<i class=\"icon-remove\"></i>" + text);
				aLnk.setStyleName("btn btn-default btn-fill pull-right");
			} else if (text.equals("Save")) {
				aLnk.setHTML(text
						+ "&nbsp;<i class=\"icon-double-angle-right\"></i>");
				//aLnk.setStyleName("btn btn-primary btn-fill pull-left");
				aLnk.setStyleName("btn btn-primary btn-fill pull-right");
			} else if (text.equals("Next")) {
				aLnk.setHTML(text
						+ "&nbsp;<i class=\"icon-double-angle-right\"></i>");
				aLnk.setStyleName("btn btn-primary btn-fill pull-right");
			}else if (text.equals("Previous")) {
				aLnk.setHTML(text
						+ "&nbsp;<i class=\"icon-double-angle-right\"></i>");
				aLnk.setStyleName("btn btn-primary btn-fill pull-left");
			}
			

			aLnk.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (onOptionSelected instanceof OptionControl) {
						((OptionControl) onOptionSelected)
								.setPopupView((PopupView) (popupPresenter
										.getView()));
						onOptionSelected.onSelect(text);
					} else {
						popupPresenter.getView().hide();
						onOptionSelected.onSelect(text);
					}

					if (!popupPresenter.isVisible() && customPopupStyle != null) {
						popupPresenter.getView().removeStyleName(
								customPopupStyle);
					}
				}
				
			});
			popupPresenter.getView().addToSlot(
					GenericPopupPresenter.BUTTON_SLOT, aLnk);
		}
		mainPagePresenter.addToPopupSlot(popupPresenter, true);
	}

	public static void showPopUp(String header,
			PresenterWidget<ViewImpl> presenter,
			final OnOptionSelected onOptionSelected, String... buttons) {
		showPopUp(header, presenter.asWidget(), onOptionSelected, buttons);
	}

	/**
	 * Returns positions of the modal/popover in Relative to the browser size
	 * 
	 * TODO: LET POSITIONING BE DONE THROUGH CSS AS OPPOSED TO THIS... CREATES MORE CHALLENGES AS THE APP GROWS
	 * 
	 * @param %top, %left
	 * @return top(px),left(px)
	 */
	public static int[] calculatePosition(int top, int left) {

		int[] positions = new int[2];
		// ----Calculate the Size of Screen;
		int height = Window.getClientHeight();
		int width = Window.getClientWidth();

		/* Percentage to the Height and Width */
		double percentTop = (top / 100.0) * height;
		double percentLeft = (left / 100.0) * width;

		positions[0] = (int) percentTop;
		positions[1] = (int) percentLeft;

		return positions;
	}
}
