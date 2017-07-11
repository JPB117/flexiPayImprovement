package com.icpak.rest.models.erpModels;

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;

public class ErpReceipt {
	private ErpReceiptHeader header;
	private List<ErpReceiptLine> lines;

	public ErpReceiptHeader getHeader() {
		return header;
	}

	public void setHeader(ErpReceiptHeader header) {
		this.header = header;
	}

	public List<ErpReceiptLine> getLines() {
		return lines;
	}

	public void setLines(List<ErpReceiptLine> lines) {
		this.lines = lines;
	}

	public static void main(String[] args) {
		ErpReceipt newReceipt = new ErpReceipt();
		newReceipt.setHeader(new ErpReceiptHeader());
		newReceipt.setLines(Arrays.asList(new ErpReceiptLine()));
		JSONArray json;
		json = new JSONArray();
		System.err.println(("Payload>>>" + json));
	}
}
