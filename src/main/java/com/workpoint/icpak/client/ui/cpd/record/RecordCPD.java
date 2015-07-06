package com.workpoint.icpak.client.ui.cpd.record;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.OptionControl;
import com.workpoint.icpak.client.ui.component.IssuesPanel;

public class RecordCPD extends Composite {

	private static UnconfirmedCPDUiBinder uiBinder = GWT
			.create(UnconfirmedCPDUiBinder.class);

	interface UnconfirmedCPDUiBinder extends UiBinder<Widget, RecordCPD> {
	}

	@UiField
	HTMLPanel panelForm;

	@UiField
	HTMLPanel panelCategories;

	@UiField
	IssuesPanel issues;
	
	txtTitle;
	
	txtOrganizer;

	// @UiField
	// ActionLink aCategoryA;
	// @UiField
	// ActionLink aCategoryB;
	// @UiField
	// ActionLink aCategoryC;
	// @UiField
	// ActionLink aCategoryD;

	public RecordCPD() {
		initWidget(uiBinder.createAndBindUi(this));
		showForm(false);
	}

	public boolean isValid() {
		boolean isValid = true;
		issues.clear();
		
		if(isNullOrEmpty(txtInstitution.getValue())){
			isValid=false;
			issues.addError("Institution is mandatory");
		}

		return isValid;
	}

	protected void showForm(boolean show) {
		if (show) {
			panelForm.setVisible(true);
			panelCategories.setVisible(false);
		} else {
			panelForm.setVisible(false);
			panelCategories.setVisible(true);
		}

	}

	public OptionControl getOptionSelection() {
		OptionControl selection = new OptionControl() {
			@Override
			public void onSelect(String name, Anchor aLnk) {
				if (name.equals("Next")) {
					showForm(true);
					aLnk.setText("Save");
				}
			}
		};
		return selection;
	}

}
