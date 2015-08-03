package com.workpoint.icpak.client.ui.events.registration.proforma;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.component.TableView;
import com.workpoint.icpak.client.ui.events.registration.proforma.details.ProformaTableRow;
import com.workpoint.icpak.client.ui.events.registration.proforma.last.ProformaLastRow;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceLineDto;
import com.workpoint.icpak.shared.model.events.AccommodationDto;

public class ProformaInvoice extends Composite {

	private static ProformaInvoiceUiBinder uiBinder = GWT
			.create(ProformaInvoiceUiBinder.class);

	interface ProformaInvoiceUiBinder extends UiBinder<Widget, ProformaInvoice> {
	}

	@UiField TableView tblProforma;
	@UiField InlineLabel lblCompany;
	@UiField InlineLabel lblAddress;
	@UiField InlineLabel lblQuoteNo;
	@UiField InlineLabel lblInvoiceDate;

	public ProformaInvoice() {
		initWidget(uiBinder.createAndBindUi(this));

		createHeader();
		createRow(new ProformaTableRow());
		createLastRow(new ProformaLastRow());
	}

	public void createHeader() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("Description"));
		th.add(new TableHeader("Accommodation"));
		th.add(new TableHeader("Unit Price"));
		th.add(new TableHeader("Amount"));
		tblProforma.setTableHeaders(th);
	}

	public void createRow(ProformaTableRow row) {
		tblProforma.addRow(row);
	}

	private void createLastRow(ProformaLastRow row) {
		tblProforma.addRow(row);
	}

	public void clearRows() {
		tblProforma.clearRows();
	}

	public void setInvoice(InvoiceDto invoice) {
		lblCompany.setText(invoice.getCompanyName());
		lblAddress.setText(invoice.getCompanyAddress());
		lblInvoiceDate.setText(DateUtils.DATEFORMAT.format(new Date(invoice.getDate())));
		lblQuoteNo.setText(invoice.getDocumentNo());
		
		for(InvoiceLineDto line: invoice.getLines()){
			AccommodationDto accommodation = line.getAccommodation();
			tblProforma.addRow(new InlineLabel(line.getDescription()+" ("
		+(line.getMemberId()==null?"Non Member" : "Member "+line.getMemberId())+ 
		" ERN: "+(line.getDelegateERN()==null? "" : line.getDelegateERN())+")"),
					new InlineLabel(accommodation==null?"NONE" :
						(accommodation.getHotel()+" "+accommodation.getNights()+" nights "+ accommodation.getType())),
					new InlineLabel(line.getUnitPrice()+""),
					new InlineLabel(line.getTotalAmount()+""));
		}
		tblProforma.addRow(new InlineLabel(),
				new InlineLabel(),	
				new InlineLabel("Grand Total"),
				new InlineLabel(invoice.getAmount()+""));
	}

}
