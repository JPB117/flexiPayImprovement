package com.workpoint.icpak.client.ui.members;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.Checkbox;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.events.CheckboxSelectionEvent;
import com.workpoint.icpak.client.ui.members.header.MembersHeader;
import com.workpoint.icpak.client.ui.members.row.MembersTableRow;
import com.workpoint.icpak.client.ui.members.table.MembersTable;
import com.workpoint.icpak.client.ui.profile.widget.ProfileWidget;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.ApplicationCategoryDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationSummaryDto;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.auth.ApplicationStatus;

public class ApplicationsView extends ViewImpl implements ApplicationsPresenter.IApplicationsView {

	private final Widget widget;
	@UiField
	HTMLPanel container;

	@UiField
	DivElement divSingleApplication;
	@UiField
	DivElement divAllApplication;
	@UiField
	MembersTable tblView;
	@UiField
	MembersHeader headerContainer;
	@UiField
	ProfileWidget panelProfile;
	@UiField
	FlexTable tblApplicationCategory;

	@UiField
	Anchor aAllApplications;
	@UiField
	Anchor aApplicationCategories;
	@UiField
	DivElement divAll;
	@UiField
	DivElement divApplicationCategories;
	@UiField
	Element liAllApplications;
	@UiField
	Element liApplicationCategories;
	@UiField
	Anchor aEditCategory;
	@UiField
	Anchor aDeleteCategory;
	@UiField
	Anchor aAddCategory;

	@UiField
	Anchor aRQASettings;
	@UiField
	Anchor aSyncApprovedMembers;

	public interface Binder extends UiBinder<Widget, ApplicationsView> {
	}

	@Inject
	public ApplicationsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void bindApplications(List<ApplicationFormHeaderDto> list) {
		tblView.clearRows();
		int counter = 0;
		for (ApplicationFormHeaderDto dto : list) {
			MembersTableRow row = new MembersTableRow(dto, counter);
			tblView.createRow(row);
			counter++;
		}
	}

	@Override
	public void setCount(Integer aCount) {
		tblView.getPagingPanel().setTotal(aCount);
	}

	@Override
	public PagingPanel getPagingPanel() {
		return tblView.getPagingPanel();
	}

	@Override
	public void bindSummary(ApplicationSummaryDto summary) {
		headerContainer.setValues(summary.getPendingCount() + summary.getProcessedCount(), summary.getProcessedCount(),
				summary.getPendingCount());

	}

	public ProfileWidget getPanelProfile() {
		return panelProfile;
	}

	@Override
	public void showSingleApplication(boolean show) {
		showSingleApplication(show, "0", "0", 0);
	}

	@Override
	public String getSearchText() {
		return tblView.getSearchValue();
	}

	@Override
	public HasKeyDownHandlers getTxtSearch() {
		return tblView.getSearchKeyDownHandler();
	}

	@Override
	public void showSingleApplication(boolean show, String previousRefId, String nextRefId, int maxSize) {
		panelProfile.setNavigationLinks(previousRefId, nextRefId, maxSize);

		if (show) {
			divAllApplication.addClassName("hide");
			divSingleApplication.removeClassName("hide");
		} else {
			divAllApplication.removeClassName("hide");
			divSingleApplication.addClassName("hide");
		}
	}

	public DropDownList<ApplicationStatus> getLstApplicationStatus() {
		return tblView.getLstApplicationStatus();
	}

	public DropDownList<PaymentStatus> getLstPaymentStatus() {
		return tblView.getLstPaymentStatus();
	}

	public ActionLink getaSearch() {
		return tblView.getaSearch();
	}

	@Override
	public void bindApplicationCategories(ArrayList<ApplicationCategoryDto> categories) {
		tblApplicationCategory.removeAllRows();
		setCategoriesHeaders(tblApplicationCategory);

		int i = 1;
		for (ApplicationCategoryDto category : categories) {
			int j = 0;
			Checkbox box = new Checkbox(category);
			box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					Object model = ((Checkbox) (event.getSource())).getModel();
					AppContext.fireEvent(new CheckboxSelectionEvent(model, event.getValue()));
				}
			});

			tblApplicationCategory.setWidget(i, j++, box);
			tblApplicationCategory.setWidget(i, j++,
					new HTMLPanel(category.getType() == null ? "" : category.getType().getDisplayName()));
			tblApplicationCategory.setWidget(i, j++,
					new HTMLPanel(category.getDescription() == null ? "" : category.getDescription()));
			tblApplicationCategory.setWidget(i, j++, new HTMLPanel(Double.toString(category.getApplicationAmount())));
			tblApplicationCategory.setWidget(i, j++, new HTMLPanel(Double.toString(category.getRenewalAmount())));
			tblApplicationCategory.setWidget(i, j++, new HTMLPanel(""));
			++i;
		}
	}

	private void setCategoriesHeaders(FlexTable table) {
		int j = 0;
		table.setWidget(0, j++, new HTMLPanel("<strong>#</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "20px");

		table.setWidget(0, j++, new HTMLPanel("<strong>Name</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Description</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "110px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Application Amount</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Renewal Amount</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Renewal Due Date</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "50px");

		for (int i = 0; i < table.getCellCount(0); i++) {
			table.getFlexCellFormatter().setStyleName(0, i, "th");
		}
	}

	@Override
	public void setActiveTab(String page) {
		if (page.equals("allApplications")) {
			divAll.addClassName("active");
			divApplicationCategories.removeClassName("active");
			liApplicationCategories.removeClassName("active");
			liAllApplications.addClassName("active");
		} else if (page.equals("applicationCategories")) {
			divAll.removeClassName("active");
			divApplicationCategories.addClassName("active");
			liApplicationCategories.addClassName("active");
			liAllApplications.removeClassName("active");
		}
	}

	@Override
	public void setApplicationCategoryEdit(boolean value) {
		if (value) {
			aEditCategory.removeStyleName("hide");
			aDeleteCategory.removeStyleName("hide");
		} else {
			aEditCategory.addStyleName("hide");
			aDeleteCategory.addStyleName("hide");
		}
	}

	public HasClickHandlers getCategoryAddButton() {
		return aAddCategory;
	}

	public HasClickHandlers getCategoryEditButton() {
		return aEditCategory;
	}

	public HasClickHandlers getCategoryDeleteButton() {
		return aDeleteCategory;
	}

	public HasClickHandlers getRQAButton() {
		return aRQASettings;
	}

	public HasClickHandlers getSyncApprovedButton() {
		return aSyncApprovedMembers;
	}

}
