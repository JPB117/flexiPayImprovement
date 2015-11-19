package com.icpak.servlet.upload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.workpoint.icpak.shared.model.events.DelegateDto;

public class GetDelegatesReport {
	Logger logger = Logger.getLogger(GetDelegatesReport.class);

	private static final String[] titles = { "ICPAK MEMBER", "MEMBER NO",
			"ERN NO", "DELEGATE NAMES", "EMAIL", "BOOKING DATE", "SPONSOR",
			"CONTACT PERSON", "CONTACT EMAIL", "SPONSOR TEL", "ACCOMODATION",
			"PAID", "RECEIPT", "LPO NO", "CREDIT", "CLEARANCE NO", "ATTEND" };

	static Map<String, CellStyle> styles = null;
	static Map<String, Font> fonts = null;

	private Workbook wb = new HSSFWorkbook();

	private String name;

	public GetDelegatesReport() {

	}

	public String getName() {
		return name;
	}

	public GetDelegatesReport(List<DelegateDto> delegateDtos, String docType,
			String eventName) {
		logger.error(" === dto size === " + delegateDtos.size());
		name = eventName + "_Report" + "." + docType;

		fonts = createFonts(wb);
		styles = createStyles(wb);

		if (!delegateDtos.isEmpty()) {
			generate(wb, name, delegateDtos);
		}
	}

	private Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		DataFormat df = wb.createDataFormat();

		CellStyle style;

		Font sheetHeadingFont = wb.createFont();
		sheetHeadingFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		sheetHeadingFont.setFontHeightInPoints((short) 16);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(sheetHeadingFont);
		styles.put("sheet_heading", style);

		Font headerFont = wb.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setFontHeightInPoints((short) 14);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE
				.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(headerFont);
		styles.put("header", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE
				.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(headerFont);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("header_date", style);

		Font font1 = wb.createFont();
		font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(font1);
		style.setWrapText(true);
		styles.put("cell_b", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font1);
		styles.put("cell_b_centered", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setFont(font1);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_b_date", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		style.setDataFormat(df.getFormat("#,##0.0")); //
		styles.put("cell_currency", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setFont(font1);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_g", style);

		Font font2 = wb.createFont();
		font2.setColor(IndexedColors.BLUE.getIndex());
		font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(font2);
		styles.put("cell_bb", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setFont(font1);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_bg", style);

		Font font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 14);
		font3.setColor(IndexedColors.DARK_BLUE.getIndex());
		font3.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(font3);
		style.setWrapText(true);
		styles.put("cell_h", style);

		Font subHeaderFont = wb.createFont();
		subHeaderFont.setItalic(true);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(subHeaderFont);
		style.setWrapText(true);
		styles.put("cell_h_sub", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		style.setWrapText(true);
		styles.put("cell_normal", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setWrapText(true);
		styles.put("cell_normal_centered", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setWrapText(true);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_normal_date", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setIndention((short) 1);
		style.setWrapText(true);
		styles.put("cell_indented", style);

		style = createBorderedStyle(wb);
		style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styles.put("cell_blue", style);

		return styles;
	}

	private CellStyle createBorderedStyle(Workbook wb2) {
		CellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		return style;
	}

	private Map<String, Font> createFonts(Workbook wb2) {
		fonts = new HashMap<>();
		Font headerFont = wb.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fonts.put("header", headerFont);

		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setColor(IndexedColors.GREEN.index);
		fonts.put("font-success", font);

		font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setColor(Font.COLOR_RED);
		fonts.put("font-failure", font);

		font = wb.createFont();
		font.setColor(IndexedColors.VIOLET.index);
		fonts.put("_nodata", font);

		font = wb.createFont();
		font.setColor(IndexedColors.BLACK.index);
		fonts.put("default", font);

		return fonts;
	}

	private void generate(Workbook workbook, String sheetName,
			List<DelegateDto> delegateDtos) {
		Sheet sheet = workbook.createSheet(sheetName);

		// sheet.setDisplayGridlines(false);
		// sheet.setPrintGridlines(false);
		sheet.setFitToPage(true);
		sheet.setHorizontallyCenter(true);
		PrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(true);

		// the following three statements are required only for HSSF
		sheet.setAutobreaks(true);
		printSetup.setFitHeight((short) 1);
		printSetup.setFitWidth((short) 1);

		// the header row: centered text in 48pt font

		// Row headingRow = sheet.createRow(0);
		// headingRow.setHeightInPoints(30f);
		// Cell headingCell = headingRow.createCell(0);
		// headingCell.setCellStyle(styles.get("sheet_heading"));
		// headingCell.setCellValue(sheetName);
		// sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

		Row headerRow = sheet.createRow(0);
		headerRow.setHeightInPoints(30f);
		for (int i = 0; i < titles.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(titles[i]);
			cell.setCellStyle(styles.get("header"));
		}

		CreationHelper helper = wb.getCreationHelper();
		int rownum = 1;
		paintRows(delegateDtos, rownum, helper, sheet);

		sheet.setColumnWidth(0, 256 * 20);
		sheet.setColumnWidth(1, 256 * 40);
		sheet.setColumnWidth(2, 256 * 40);
		sheet.setColumnWidth(3, 256 * 35);
		sheet.setColumnWidth(4, 256 * 35);
		sheet.setColumnWidth(5, 256 * 10);// Funding/ Estimate
		sheet.setColumnWidth(6, 256 * 10);// Actual
		sheet.setColumnWidth(7, 256 * 10);// Status
		sheet.setColumnWidth(8, 256 * 15);// Remarks
		sheet.setColumnWidth(9, 256 * 10);// Monitoring test
		sheet.setZoom(3, 4);
	}

	private static int paintRows(List<DelegateDto> data, int rownum,
			CreationHelper helper, Sheet sheet) {
		if (data == null || data.isEmpty()) {
			return rownum - 1;
		}

		for (Integer i = 0; i < data.size(); i++, rownum++) {

			Row row = sheet.createRow(rownum);
			DelegateDto detail = data.get(i);
			bindData(detail, helper, sheet, row, i);

			// rownum = paintRows(data, rownum + 1, helper, sheet);
		}

		return --rownum;
	}

	private static void bindData(DelegateDto detail, CreationHelper helper,
			Sheet sheet, Row row, Integer i) {
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");

		// {"ICPAK MEMBER", "MEMBER NO", "ERN NO",
		// "DELEGATE NAMES","EMAIL","BOOKING DATE", "SPONSOR",
		// "CONTACT PERSON","CONTACT EMAIL","SPONSOR TEL","BOOKING DATE",
		// "ACCOMODATION"
		// ,"PAID","RECEIPT" ,"LPO NO", "CREDIT","CLEARANCE NO","ATTEND"}

		for (int j = 0; j < titles.length; j++) {
			Cell cell = row.createCell(j);
			String styleName = null;

			if (j == 0) {
				// cell.setCellValue(formater.format(detail.getCreatedDate()));
				cell.setCellValue((detail.getMemberNo() != null ? "1" : "0"));
			}

			if (j == 1) {
				cell.setCellValue(detail.getMemberNo());
			}

			if (j == 2) {
				cell.setCellValue(detail.getErn());
			}

			if (j == 3) {
				cell.setCellValue(detail.getSurname() + " "
						+ detail.getOtherNames());
			}

			if (j == 4) {
				cell.setCellValue(detail.getEmail());
			}

			// "BOOKING DATE", "SPONSOR",
			// "CONTACT PERSON","CONTACT EMAIL","SPONSOR TEL"
			// "ACCOMODATION"

			if (j == 5) {
				cell.setCellValue(formater.format(detail.getCreatedDate()));
			}

			if (j == 6) {
				cell.setCellValue(detail.getCompanyName());
			}

			if (j == 7) {
				cell.setCellValue(detail.getContactName());
			}

			if (j == 8) {
				cell.setCellValue(detail.getContactEmail());
			}

			if (j == 9) {
				cell.setCellValue("");
			}

			if (j == 10) {
				cell.setCellValue((detail.getAccommodation() == null ? "None"
						: detail.getAccommodation().getHotel()));
			}

			// ,"PAID","RECEIPT" ,"LPO NO", "CREDIT","CLEARANCE NO","ATTEND"}
			if (j == 11) {
				cell.setCellValue(detail.getPaymentStatus().getDisplayName());
			}

			if (j == 12) {
				cell.setCellValue(detail.getReceiptNo());
			}

			if (j == 13) {
				cell.setCellValue(detail.getLpoNo());
			}

			if (j == 14) {
				cell.setCellValue(detail.getIsCredit());
			}

			if (j == 15) {
				cell.setCellValue(detail.getClearanceNo());
			}

			if (j == 16) {
				cell.setCellValue(detail.getAttendance().getDisplayName());
			}

			styleName = "cell_normal_centered";

			cell.setCellStyle(styles.get(styleName));
		}

	}

	public void generateDelegateReport(List<DelegateDto> delegateDtos,
			String docType) throws Exception {

		byte[] bites = new GetDelegatesReport(delegateDtos, docType, "My Event")
				.getBytes();

		FileOutputStream os = new FileOutputStream(new File(
				"delegatesReport.xlsx"));
		os.write(bites);
		os.close();
	}

	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream byteo = new ByteArrayOutputStream();
		wb.write(byteo);
		return byteo.toByteArray();
	}
}
